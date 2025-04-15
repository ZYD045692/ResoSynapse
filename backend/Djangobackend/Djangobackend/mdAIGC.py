from django.views.decorators.csrf import csrf_exempt
from django.views.decorators.http import require_http_methods
from django.http import JsonResponse
import json
from django import forms
from django.core.exceptions import ValidationError

# 自定义支持多文件上传的Widget
class MultipleFileInput(forms.FileInput):
    allow_multiple_selected = True  # 关键属性，告诉Django接收多个文件

# 自定义支持多文件上传的字段
class MultipleFileField(forms.FileField):
    widget = MultipleFileInput

    def clean(self, data, initial=None):
        # 处理多文件验证
        if not data and initial:
            return initial
        if not isinstance(data, (list, tuple)):
            data = [data]
        
        result = []
        for file in data:
            super().validate(file)  # 使用父类的单文件验证
            super().run_validators(file)
            result.append(file)
        return result
    
class FileUploadForm(forms.Form):
    files = MultipleFileField(
        required=True,
        label="选择文件",
        help_text="支持多选文件上传"
    )
    
from typing import *
from pathlib import Path
from openai import OpenAI
client = OpenAI(
    base_url="https://api.moonshot.cn/v1",
    # 我们会从环境变量中获取 MOONSHOT_DEMO_API_KEY 的值作为 API Key，
    # 请确保你已经在环境变量中正确设置了 MOONSHOT_DEMO_API_KEY 的值
    api_key = "sk-gfSJSPrWD6zHx50yDsSzNho37CrIKvf6GWFY9ug5FH9knw08",
)
@csrf_exempt
@require_http_methods(["POST"])
def mdchat(request):
    form = FileUploadForm(request.POST, request.FILES)
    if form.is_valid():
        files = request.FILES.getlist('files')
        for file in files:
            pass
    body = request.body
    data = json.loads(body)
    text = data.get('text')
    messages = [
        # 我们使用 * 语法，来解构 file_messages 消息，使其成为 messages 列表的前 N 条 messages。
        # *file_messages,
        {
            "role": "system",
            "content": "你是 Kimi，由 Moonshot AI 提供的人工智能助手，你更擅长中文和英文的对话。你会为用户提供安全，有帮助，"
                       "准确的回答。同时，你会拒绝一切涉及恐怖主义，种族歧视，黄色暴力等问题的回答。Moonshot AI 为专有名词，不"
                       "可翻译成其他语言。",
        },
        {
            "role": "user",
            "content": text,
        },
    ]
 
    completion = client.chat.completions.create(
        model="moonshot-v1-128k",
        messages=messages,
    )
 
    content = completion.choices[0].message.content
    return JsonResponse({"content": content})

    