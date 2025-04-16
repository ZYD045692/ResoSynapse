import os
import subprocess
from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt
from django.views.decorators.http import require_http_methods
from faster_whisper import WhisperModel
import translators as ts
from pix2text import Pix2Text
from Djangobackend.pdf import get_unique_filename
import json
from langdetect import detect
from utils.config import PNG_DIR,VIDEO_DIR
# 初始化 Pix2Text 对象

png_folder = PNG_DIR
video_folder = VIDEO_DIR
os.makedirs(png_folder, exist_ok=True)
os.makedirs(video_folder, exist_ok=True)


def transText(translated_text,from_language,to_language):
    try:
        lineStringList = translated_text.split('\n')
        dealStringList = []
        length = 200
        for lineString in lineStringList:
            if not len(lineString):
                continue
            splitLineStringList  = []
            if len(lineString) > 300:
                splitLineStringList.extend([lineString[i:i + length] for i in range(0, len(lineString), length)])
            else:
                splitLineStringList.append(lineString)
            transLineString =''
            for LineString in splitLineStringList:
                if not len(LineString):
                    continue
                transLineString+=ts.translate_text(LineString,from_language=from_language,to_language=to_language)
            dealStringList.append(transLineString)
        transString = '\n'.join(dealStringList) if len(dealStringList) else ''
        return transString
    except:
        return ''
    
def convert_mp4_to_mp3(mp4_path, mp3_path):
        # 构造 ffmpeg 命令
    command = [
        r"D:\ffmpeg_full_build\bin\ffmpeg.exe",  # 调用 ffmpeg
        "-i", mp4_path,  # 输入文件路径
        "-vn",  # 不包含视频流
        "-ar", "44100",  # 音频采样率
        "-ac", "2",  # 音频通道数（立体声）
        "-b:a", "192k",  # 音频比特率
        mp3_path  # 输出文件路径
    ]
        
        # 执行命令
    subprocess.run(command, check=True)
    return mp3_path

@csrf_exempt
@require_http_methods(["POST"])
def transformText(request):
    try:
        file = request.FILES["file"]
        # 保存上传的文件
        file_path = os.path.join(video_folder, get_unique_filename(video_folder, file.name))
        with open(file_path, 'wb+') as destination:
            for chunk in file.chunks():
                destination.write(chunk)

        if file.name.endswith(".mp4"):
            # 转换 MP4 到 MP3
            a = file_path
            mp3_path = file_path.replace(".mp4", ".mp3")
            file_path = convert_mp4_to_mp3(file_path, mp3_path)
            os.remove(a)

        texts = ""
        if file_path.endswith(".mp3"):
            model = WhisperModel("base", device="cpu")
            # 使用 WhisperModel 处理音频文件
            segments, info = model.transcribe(file_path)
            for segment in segments:
                texts += (segment.text + "\n")
        else:
            p2t = Pix2Text()
            # 使用 Pix2Text 处理图片文件
            output = p2t(file_path)
            for element in output.elements:
                texts += (element.text + "\n")

        detected_language = detect(texts)
        to_language = "en"
        if detected_language == "en":
            to_language = "zh"

        translated_texts = transText(texts,detected_language,to_language)
        # 删除已处理的文件
        os.remove(file_path)
        if not len(translated_texts):
            return JsonResponse({"stateCode": 0})
        else:
            return JsonResponse({'stateCode': 1, "texts": texts, "translated": translated_texts})
    except Exception as e:
        print(e)
        # 捕获异常并返回指定的响应
        return JsonResponse({"stateCode": 0})
@csrf_exempt
@require_http_methods(["POST"])
def transformLanguage(request):
    try:
        data = json.loads(request.body)
        to_language = data.get('toLanguage')
        texts = data.get('texts')
        detected_language = detect(texts)
        if to_language == detected_language:
            return JsonResponse({"stateCode":0,"translated":texts})
        translated_texts = transText(texts,detected_language,to_language)

        if not len(translated_texts):
            return JsonResponse({"stateCode": 0})
        else:
            return JsonResponse({'stateCode': 1,"translated": translated_texts})
        
    except json.JSONDecodeError:
        return JsonResponse({'stateCode': 0,})
    except Exception as e:
        print(e)
        # 捕获其他异常
        return JsonResponse({'stateCode': 0,})