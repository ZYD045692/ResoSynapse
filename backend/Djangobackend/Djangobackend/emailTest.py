# views.py
from django.http import JsonResponse, HttpResponse
from django.views.decorators.csrf import csrf_exempt
import random
import smtplib
from datetime import datetime, timedelta
from email.mime.text import MIMEText
from django.utils import timezone
from datetime import timedelta
import json
from utils.sent import send_email

@csrf_exempt
def getEmailCaptcha(request):
    # SMTP服务器配置常量
    SMTP_SERVER = "smtp.qq.com"
    SMTP_PORT = 587
    SENDER_EMAIL = "2214845593@qq.com"
    SENDER_PASSWORD = "lcwldryyxddqdhgf"
    EMAIL_SUBJECT = "验证码邮件"
    print(dict(request.session))
    try:
        email = request.GET.get('email')
        print('request', request)
        print('email', email)

        # 生成6位随机验证码
        formatted_number = f"{random.randint(0, int(1e6)):06d}"

        # 检查发送间隔
        if request.session.get(email):
            email_session = request.session[email]
            last_time = datetime.strptime(email_session['lasttime'], '%Y-%m-%d %H:%M:%S')
            current_time = datetime.strptime(timezone.now().strftime('%Y-%m-%d %H:%M:%S'), '%Y-%m-%d %H:%M:%S')
            
            if (current_time - last_time) <= timedelta(minutes=0):
                return JsonResponse({'stateCode': 0})

        # 发送邮件
        send_success = send_email(
            SMTP_SERVER,
            SMTP_PORT,
            SENDER_EMAIL,
            SENDER_PASSWORD,
            email,  # 直接使用email变量作为接收者
            EMAIL_SUBJECT,
            formatted_number
        )
        if send_success == 1:
            # 更新session信息
            request.session[email] = {
                'captcha': formatted_number,
                'lasttime': timezone.now().strftime('%Y-%m-%d %H:%M:%S'),
                'expiration': (timezone.now() + timedelta(minutes=10)).strftime('%Y-%m-%d %H:%M:%S'),
                'used': False
            }
            request.session.save()
            print(dict(request.session))
            return JsonResponse({'stateCode': 1})
        
        return JsonResponse({'stateCode': 2})

    except Exception as e:
        print(f"Error occurred: {str(e)}")
        return JsonResponse({'stateCode': 3})
@csrf_exempt
def checkEmailCaptcha(request):
    try:
        data = json.loads(request.body.decode('utf8'))
    except json.JSONDecodeError:
        return JsonResponse({'stateCode': 4, 'message': 'Invalid JSON data'})

    email = data.get('email')
    captcha = data.get('captcha')
    print(dict(request.session))
    if not email or not captcha:
        return JsonResponse({'stateCode': 4, 'message': '缺少必要参数'})

    if email not in request.session:
        return JsonResponse({'stateCode': 2, 'message': '验证码不存在'})

    captcha_data = request.session[email]

    if captcha_data.get('used'):
        return JsonResponse({'stateCode': 5, 'message': '验证码已使用'})

    if 'captcha' not in captcha_data or 'expiration' not in captcha_data:
        return JsonResponse({'stateCode': 2, 'message': '验证码数据不完整'})

    current_time = timezone.now()  # 这是一个 offset-aware 的 datetime 对象
    expiration_time = datetime.strptime(captcha_data['expiration'], '%Y-%m-%d %H:%M:%S')

    time_diff = expiration_time - current_time
    print('time_diff',time_diff)
    print('time_diff',time_diff < timedelta(minutes=0))

    if time_diff < timedelta(minutes=0):
        return JsonResponse({'stateCode': 3, 'message': '验证码过期'})

    if captcha_data['captcha'] == captcha:
        request.session[email]['used'] = True
        request.session.save()
        return JsonResponse({'stateCode': 1, 'message': '验证码正确'})
    else:
        return JsonResponse({'stateCode': 0, 'message': '验证码错误'})