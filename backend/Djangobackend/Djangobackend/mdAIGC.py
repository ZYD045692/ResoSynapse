from django.views.decorators.csrf import csrf_exempt
from django.views.decorators.http import require_http_methods
from django.http import JsonResponse
import json
import os
import time
from django import forms
from django.core.exceptions import ValidationError
from django.core.files.storage import default_storage
from django.core.files.base import ContentFile
from utils.get_unique_filename import get_unique_filename
from utils.config import KIMI_API_KEY_LIST,MD_AIGC_DIR
os.makedirs(MD_AIGC_DIR, exist_ok=True)
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
    text = forms.CharField()
    
from typing import *
from pathlib import Path
from openai import OpenAI

clientList = []

for KIMI_API_KEY in KIMI_API_KEY_LIST:
    client = OpenAI(
        base_url="https://api.moonshot.cn/v1",
        # 我们会从环境变量中获取 MOONSHOT_DEMO_API_KEY 的值作为 API Key，
        # 请确保你已经在环境变量中正确设置了 MOONSHOT_DEMO_API_KEY 的值
        api_key = KIMI_API_KEY,
    )
    clientdict={
        "client": client,
        "lastTime": time.time() - 200,
        "KIMI_API_KEY": KIMI_API_KEY,
    }
    clientList.append(clientdict)
def get_client():
    global clientList
    now = time.time()
    num = 5
    for i in range(num):
        if len(clientList) == 0:
            raise None
        for clientdict in clientList:
            if now - clientdict["lastTime"] > 20:
                clientList.remove(clientdict)
                return clientdict
        if i!=num-1:
            time.sleep(1)
    return None

def add_client(clientdict,time):
    global clientList
    clientdict["lastTime"] = time
    clientList.append(clientdict)


def upload_files(files: List[str]) -> List[Dict[str, Any]]:
    """
    upload_files 会将传入的文件（路径）全部通过文件上传接口 '/v1/files' 上传，并获取上传后的
    文件内容生成文件 messages。每个文件会是一个独立的 message，这些 message 的 role 均为
    system，Kimi 大模型会正确识别这些 system messages 中的文件内容。
 
    :param files: 一个包含要上传文件的路径的列表，路径可以是绝对路径也可以是相对路径，请使用字符串
        的形式传递文件路径。
    :return: 一个包含了文件内容的 messages 列表，请将这些 messages 加入到 Context 中，
        即请求 `/v1/chat/completions` 接口时的 messages 参数中。
    """
    messages = []
    client = clientList[0]["client"]
    # 对每个文件路径，我们都会上传文件并抽取文件内容，最后生成一个 role 为 system 的 message，并加入
    # 到最终返回的 messages 列表中。
    for file in files:
        file_object = client.files.create(file=Path(file), purpose="file-extract")
        file_content = client.files.content(file_id=file_object.id).text
        messages.append({
            "role": "system",
            "content": file_content,
        })
 
    return messages


def getMarkdown(client,text):
    messages = [
        {"role": "system", "content": "你是 Kimi，由 Moonshot AI 提供的人工智能助手。请将以下内容整理为Markdown格式，只返回Markdown格式的内容，不要返回其他任何内容，不要添加任何markdown\n这样的解释或说明。如果不能转换请返回不能转换。"},
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
    if "```" in content:
        content = content.replace("```","")
    if "```" in content:
        content = content.replace("```","")
    if "markdown\n" in content:
        content = content.replace("markdown\n","")
    return content
@csrf_exempt
@require_http_methods(["POST"])
def mdchat(request):
    try:
        form = FileUploadForm(request.POST, request.FILES)
        fileList =[]
        if form.is_valid():
            files = request.FILES.getlist('files')
            for file in files:
                # 安全处理文件名
                filename = os.path.basename(file.name)  # 移除路径信息
                unique_name = get_unique_filename(filename)
                
                # 完整保存路径
                save_path = os.path.join(MD_AIGC_DIR, unique_name)
                fileList.append(save_path)
                
                # 分块写入文件（适合大文件）
                with open(save_path, 'wb+') as destination:
                    for chunk in file.chunks(chunk_size=4096):
                        destination.write(chunk)
        file_messages = upload_files(files=fileList)
        for file in fileList:
            os.remove(file)
        text = form.cleaned_data['text']
        messages = [
            # 我们使用 * 语法，来解构 file_messages 消息，使其成为 messages 列表的前 N 条 messages。
            *file_messages,
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

        #chat
        clientdict = get_client()
        if clientdict == None:
            return JsonResponse({"content": "服务器繁忙，请稍后再试。"})
        try:
            client = clientdict["client"]
            completion = client.chat.completions.create(
                model="moonshot-v1-128k",
                messages=messages,
            )
            content = completion.choices[0].message.content
        except Exception as e:
            return JsonResponse({"content": "服务器繁忙，请稍后再试。"})
        finally:
            add_client(clientdict,time.time())

        #获取markdown
        clientdict = get_client()
        markdown =""
        if clientdict != None:
            try:
                client = clientdict["client"]
                markdown = getMarkdown(client,content)
            except Exception as e:
                markdown =""
            finally:
                add_client(clientdict,time.time())
        if "不能转换" in markdown:
            return JsonResponse({"content": content})
        return JsonResponse({"content": content,"markdown": markdown})
    except Exception as e:
        return JsonResponse({"content": "服务器繁忙，请稍后再试。"})

    