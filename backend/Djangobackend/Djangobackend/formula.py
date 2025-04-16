import os
from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt
from django.views.decorators.http import require_http_methods
from Djangobackend.pdf import get_unique_filename
from latex2mathml.converter import convert
import base64
from openai import OpenAI
import re
from latex2mathml.exceptions import NoAvailableTokensError
from utils.config import KIMI_API_KEY
client = OpenAI(
    api_key = KIMI_API_KEY,
    base_url = "https://api.moonshot.cn/v1",
)

output_folder = "tempPng"
os.makedirs(output_folder, exist_ok=True)

def extract_formulas(text):
    pattern = r'\\\((.*?)\\\)|\\\[(.*?)\\\]'
    formulas = re.findall(pattern, text, re.DOTALL)
    # 合并匹配结果
    result = []
    for group in formulas:
        result.extend([g for g in group if g.strip()])
    
    return result

@csrf_exempt
@require_http_methods(["POST"])
def ocrFormula(request):
    try :
        uploaded_file = request.FILES.get('image')
        image_path = os.path.join(output_folder, get_unique_filename(uploaded_file.name))
        with open(image_path, 'wb+') as destination:
            for chunk in uploaded_file.chunks():
                destination.write(chunk)
        # 对图片进行base64编码
        with open(image_path, 'rb') as f:
            img_base = base64.b64encode(f.read()).decode('utf-8')
        os.remove(image_path)
        
        response = client.chat.completions.create(
        model="moonshot-v1-8k-vision-preview", 
        messages=[
            {
                "role": "user",
                "content": [
                    {
                        "type": "image_url",
                        "image_url": {
                            "url": f"data:image/jpeg;base64,{img_base}"
                        }
                    },
                    {
                        "type": "text",
                        "text": "请忽略历史信息，专注于当前对话，提取图片中的公式,回答中只用给出公式内容"
                    }
                ]
            }
        ])
        texts = response.choices[0].message.content
        formulas = extract_formulas(texts)
        if not formulas:
            return JsonResponse({"stateCode":0})
            
        result = {}
        for index, formula in enumerate(formulas):
            try:
                mathml_text = convert(formula)
                result[str(index)] = {
                        "latex": formula,
                        "mathml": mathml_text
                    }
            except:
                pass
        return JsonResponse({"stateCode":1,"result":result})
    except:
        return JsonResponse({"stateCode":0})

