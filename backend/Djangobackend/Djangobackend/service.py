from django.http import JsonResponse
from .models import Service, File
from django.views.decorators.http import require_http_methods
from django.views.decorators.csrf import csrf_exempt
import base64

@csrf_exempt
@require_http_methods(["GET"])
def createService(request):
    user_id = request.GET.get('userId')
    service_name = request.GET.get('serviceName')
    
    # 检查 user_id 和 service_name 是否已存在
    if Service.objects.filter(user_id=user_id, service_name=service_name).exists():
        return JsonResponse({'stateCode':2})

    try:
        # 创建 Service 实例
        service = Service.objects.create(user_id=user_id, service_name=service_name)
        return JsonResponse({'stateCode': 1})
    except Exception as e:
        return JsonResponse({'stateCode': 0})

@csrf_exempt
@require_http_methods(["GET"])
def updateService(request):

    service_id = request.GET.get('serviceId')
    new_name = request.GET.get('newName')
        # 检查是否提供了所有必要的参数
    if not service_id or not new_name:
        return JsonResponse({'stateCode': 0})
    try:
        service = Service.objects.get(service_id = service_id)
        service.service_name = new_name
        service.save() 

        return JsonResponse({ 'stateCode': 1})
    except Service.DoesNotExist:
        return JsonResponse({'stateCode': 0})
    except Service.MultipleObjectsReturned:
        return JsonResponse({'stateCode': 0})
    except Exception as e:
        return JsonResponse({'stateCode': 0})
    
@csrf_exempt
@require_http_methods(["GET"])
def deleteService(request):
    service_id = request.GET.get('serviceId')
    if not service_id:
        return JsonResponse({'stateCode': 0})
    try:
        File.objects.filter(service_id=service_id).delete()
        Service.objects.get(service_id=service_id).delete()
        return JsonResponse({'stateCode': 1})
    except Exception as e:
        return JsonResponse({'stateCode': 0})
    
@csrf_exempt
@require_http_methods(["GET"])
def inquiryService(request):

    user_id = request.GET.get('userId')
    
    if not user_id:
        return JsonResponse({'stateCode': 0})
    
    try:
        # 查询所有匹配的 Service 实例
        services = Service.objects.filter(user_id=user_id)
        
        # 提取 service_name 列表
        service_names = [{
        "serviceId":service.service_id,
        "serviceName": service.service_name,
        "createdTime": service.created_time,
        "lastTime": service.last_time}
         for service in services]
        
        return JsonResponse({'stateCode': 1, 'serviceInfo': service_names})
    except Exception as e:
        return JsonResponse({'stateCode': 0})
    
@csrf_exempt
@require_http_methods(["GET"])
def detailedService(request):

    service_id = request.GET.get('serviceId')

    if not service_id:
        return JsonResponse({'stateCode': 0})
    try:
        # 使用 service_id 查询 File 表以获取所有相关记录
        files = File.objects.filter(service_id=service_id)
        # 构造返回的 JSON 数据结构
        image_list = []
        for file in files:
            with open(file.file_url, 'rb') as f:
                file_data = f.read()
            file_base64 = base64.b64encode(file_data).decode('utf-8')
            image_list.append({
                'data': file_base64, 
                'id': file.id,
                'positionIndex': file.position_index,
            })
        return JsonResponse({'stateCode': 1,'imagelist': image_list})
    except Service.DoesNotExist:
        return JsonResponse({'stateCode': 0})
    except Exception as e:
        return JsonResponse({'stateCode': 0})