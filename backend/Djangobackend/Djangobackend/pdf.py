from django.http import JsonResponse
from .models import Service, File
from django.views.decorators.http import require_http_methods
from django.views.decorators.csrf import csrf_exempt
import json

import os
import fitz  # PyMuPDF
import zipfile
from django.core.files.storage import FileSystemStorage
from django.http import FileResponse
from django.db import transaction
from django.http import HttpResponse
from utils.get_unique_filename import get_unique_filename
import base64
from utils.config import PNG_DIR,PDF_DIR,ZIP_DIR
output_folder = PNG_DIR
outputpdf_folder = PDF_DIR
zip_folder = ZIP_DIR
# 确保输出文件夹存在
os.makedirs(output_folder, exist_ok=True)
os.makedirs(outputpdf_folder, exist_ok=True)
os.makedirs(zip_folder, exist_ok=True)




# 将 PDF 转换为图片
def pdf_to_images(pdf_path):
    pathlist = []
    doc = fitz.open(pdf_path)
    for page_num in range(len(doc)):
        page = doc.load_page(page_num)
        pix = page.get_pixmap()
        savePath = os.path.join(output_folder,get_unique_filename(f"{page_num}.png"))
        pathlist.append(savePath)
        pix.save(savePath)
    doc.close()
    return pathlist

# 根据索引生成新的 PDF
def modifyPdf(pdf_name,pngPathList):
    # 创建一个新的 PDF 文档
    pdf_doc = fitz.open()
    # 遍历图片文件并将其添加到 PDF 中
    for path in pngPathList:
        img = fitz.open(path)
        img_rect = img[0].rect
        pdf_page = pdf_doc.new_page(width=img_rect.width, height=img_rect.height)
        pdf_page.insert_image(img_rect, filename=path)

    pdfsavePath = os.path.join(outputpdf_folder, get_unique_filename(pdf_name))
    pdf_doc.save(pdfsavePath)
    pdf_doc.close()
    return pdfsavePath

@csrf_exempt
@require_http_methods(["POST"])
def addPage(request):
    uploaded_file = request.FILES.get('file')
    service_id = request.POST.get('serviceId')
    insert_index = int(request.POST.get('insertIndex'))

    try:
        fs = FileSystemStorage()
        file_name = uploaded_file.name
        if uploaded_file.name.endswith(".pdf"):
            file_path = os.path.join(outputpdf_folder,get_unique_filename(file_name))
            fs.save(file_path, uploaded_file)
            pnglist = pdf_to_images(file_path)
            doc = fitz.open(file_path)
            num = len(doc)
            doc.close()
        else:
            file_path = os.path.join(output_folder,get_unique_filename(file_name))
            fs.save(file_path, uploaded_file)
            pnglist = [file_path]
            num = 1

        for i in range(num):
            # 创建新的 File 记录
            new_file = File(
                service_id=service_id,
                file_url = pnglist[i],
                position_index=0-i
            )
            new_file.save()  # 保存新记录
        add_index = list(File.objects.filter(service_id=service_id, position_index__lte=0).values_list('position_index', flat=True))
        before_index = list(File.objects.filter(service_id=service_id, position_index__gt=0).values_list('position_index', flat=True))
        add_index.sort(reverse=True)
        before_index.sort()
        if insert_index == 1:
            new_index = add_index + before_index
        else:
            new_index = add_index + before_index[insert_index-1:]
        for i in range(len(new_index)-1,-1,-1):
            File.objects.filter(position_index=new_index[i]).update(position_index=i+insert_index)
        
        files = File.objects.filter(service_id=service_id)
        image_list = {}
        for file in files:
            with open(file.file_url, 'rb') as f:
                file_data = f.read()
            file_base64 = base64.b64encode(file_data).decode('utf-8')
            image_list = [{
                'data': file_base64, 
                'id': file.id,
                'positionIndex': file.position_index,
            }]
        service = Service.objects.get(service_id= service_id)
        service.save()
        return JsonResponse({'stateCode': 1, 'imagelist': image_list})
    except Service.DoesNotExist:
        return JsonResponse({'code': 200, 'stateCode': 0})
    except Exception as e:
        return JsonResponse({'code': 200, 'stateCode': 0})
    
@csrf_exempt
@require_http_methods(["POST"])
def movePage(request):
    data = json.loads(request.body)
    # 检查 user_id 和 service_name 是否提供
    if not data:
        return JsonResponse({'code': 200, 'stateCode': 0}) 
    try:
        # max_index = max(page_num,after_page)
        # min_index = min(page_num,after_page)
        # 使用 user_id 和 service_name 查询 Service 表以获取 service_id
        # service = Service.objects.get(service_id = service_id)
        # original_files = list(File.objects.filter(service_id=service_id, position_index__gte=min_index, position_index__lte=max_index).order_by('position_index'))
        # 获取原始 position_index 列表
        # original_list = [file.position_index for file in original_files]
        # if page_num > after_page:
        #     modified_list = original_list[1:] + [original_list[0]]
        # else:
        #     modified_list = [original_list[-1]] + original_list[:-1]

        # 使用事务确保操作的原子性
        with transaction.atomic():
            for id,new_index in data.items():
                file = File.objects.filter(id=id).update(position_index=new_index)
            # for id, index in updates.items():
            #     File.objects.filter(id=id).update(index=index)
            # for file, new_index in zip(original_files, modified_list):
            #     file.position_index = new_index  # 更新 position_index
                # file.save()  # 保存更新后的记录
       # 获取更新后的 File 实例
            file = File.objects.get(id=id)
            service = Service.objects.get(service_id=file.service_id)
            service.save()  # 保存 Service 对象
            return JsonResponse({'code': 200, 'stateCode': 1})
    except Service.DoesNotExist:
        return JsonResponse({'code': 200, 'stateCode': 0})
    except Exception as e:
        return JsonResponse({'code': 200, 'stateCode': 0})

@csrf_exempt
@require_http_methods(["GET"])
def deletePage(request):
    service_id = request.GET.get('serviceId')
    position_index = request.GET.get('positionIndex')

    # 检查 user_id 和 service_name 是否提供
    if not service_id :
        return JsonResponse({'code': 200, 'stateCode': 0})

    try:
        # 使用 user_id 和 service_name 查询 Service 表以获取 service_id
        service = Service.objects.get(service_id = service_id)
        File.objects.filter(position_index=position_index, service_id=service_id).delete()
        original_index = File.objects.filter(service_id=service_id, position_index__gt=position_index)
        # 使用事务确保操作的原子性
        with transaction.atomic():
            for file in original_index:
                file.position_index -= 1  # 对 position_index 减一
                file.save()  # 保存更新后的记录
        service.save()
        return JsonResponse({'stateCode': 1})
    except Service.DoesNotExist:
        return JsonResponse({'stateCode': 0})
    except Exception as e:
        return JsonResponse({'stateCode': 0})

@csrf_exempt
@require_http_methods(["POST"])
def downloadPdf(request):
    data = json.loads(request.body)
    service_id = data.get('serviceId')

    if not service_id:
        return JsonResponse({'stateCode': 0})
    try:
        service = Service.objects.get(service_id= service_id)
        user_id = service.user_id
        service_name = service.service_name
        # 使用 service_id 查询 File 表以获取所有相关记录
        files_data = File.objects.filter(service_id=service_id).values('file_url', 'position_index')

        # 将查询结果转换为列表
        files_list = list(files_data)
        sorted_data = sorted(files_list, key=lambda x: x['position_index'])
        pngpathlist = [item['file_url'] for item in sorted_data]
        pdf_name = f"{user_id}_{service_name}.pdf"
        pdf_url = modifyPdf(pdf_name,pngpathlist)

        response = FileResponse(open(pdf_url, 'rb'), content_type='application/pdf')
        response['Content-Disposition'] = f'attachment; filename="{pdf_name}"'
        service.save()
        return response

    except Service.DoesNotExist:
        return JsonResponse({'code': 200, 'stateCode': 0})
    except Exception as e:
        return JsonResponse({'code': 200, 'stateCode': 0})
    

@csrf_exempt
@require_http_methods(["POST"])
def downloadImages(request):
    data = json.loads(request.body)
    service_id = data.get('serviceId')

    # 检查 user_id 和 service_name 是否提供
    if not service_id:
        return JsonResponse({'code': 200, 'stateCode': 0})

    try:
        service = Service.objects.get(service_id = service_id)
        user_id = service.user_id
        service_name = service.service_name

        # 查询所有图片 URL
        files_data = File.objects.filter(service_id=service_id).values('file_url')
        files_list = list(files_data)

        # 创建一个临时的 ZIP 文件
        zip_name = f"{user_id}_{service_name}.zip"
        zip_file_path = os.path.join(zip_folder, get_unique_filename(zip_folder,zip_name))# 保存在服务器的临时目录
        with zipfile.ZipFile(zip_file_path, 'w') as zip_file:
            for file_data in files_list:
                file_path = file_data['file_url']  # 假设 file_url 是本地路径
                if os.path.exists(file_path):
                    file_name = os.path.basename(file_path)
                    zip_file.write(file_path, arcname=file_name)
                else:
                    return JsonResponse({'code': 200, 'stateCode': 0})

        # 提供 ZIP 文件下载
        with open(zip_file_path, 'rb') as zip_file:
            response = HttpResponse(zip_file, content_type='application/zip')
            response['Content-Disposition'] = f'attachment; filename="{zip_name}"'
            service.save()
            return response

    except Service.DoesNotExist:
        return JsonResponse({'code': 200, 'stateCode': 0})
    except Exception as e:
        return JsonResponse({'code': 200, 'stateCode': 0})