# 安装所需库
# pip install langdetect googletrans==4.0.0-rc1

from langdetect import detect
from googletrans import Translator

# 示例文本
text = "你好，世界！"

# 检测语言
detected_language = detect(text)
print(f"检测到的语言: {detected_language}")

# 翻译文本
translator = Translator()
translated_text = translator.translate(text, src=detected_language, dest='en')
print(f"翻译后的文本: {translated_text.text}")