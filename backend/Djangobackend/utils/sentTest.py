import smtplib
from email.mime.text import MIMEText
import random

def getBody(formatted_number):
    Body  = f"""
<html>
    <head>
        <style>
            @keyframes glow {{
                0% {{ text-shadow: 0 0 10px #ff69b4; }}
                50% {{ text-shadow: 0 0 20px #ff1493, 0 0 30px #ff1493; }}
                100% {{ text-shadow: 0 0 10px #ff69b4; }}
            }}
            @keyframes float {{
                0%, 100% {{ transform: translateY(0); }}
                50% {{ transform: translateY(-10px); }}
            }}
            body {{
                font-family: 'Arial', sans-serif;
                margin: 0;
                padding: 40px 0;
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            }}
            .container {{
                max-width: 600px;
                margin: 0 auto;
                background: rgba(255, 255, 255, 0.95);
                padding: 30px;
                border-radius: 20px;
                box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
                backdrop-filter: blur(10px);
                border: 1px solid rgba(255, 255, 255, 0.3);
                position: relative;
                overflow: hidden;
            }}
            .container:before {{
                content: '';
                position: absolute;
                top: -50%;
                left: -50%;
                width: 200%;
                height: 200%;
                background: linear-gradient(45deg, transparent, rgba(255,255,255,0.2), transparent);
                transform: rotate(45deg);
                animation: shine 5s infinite;
            }}
            .header {{
                text-align: center;
                margin-bottom: 30px;
                position: relative;
                padding-bottom: 20px;
            }}
            .header h1 {{
                color: #2d3748;
                font-size: 36px;
                margin: 0;
                text-transform: uppercase;
                letter-spacing: 2px;
                text-shadow: 2px 2px 4px rgba(0,0,0,0.1);
            }}
            .header:after {{
                content: '';
                width: 60px;
                height: 3px;
                background: linear-gradient(90deg, #667eea, #764ba2);
                position: absolute;
                bottom: 0;
                left: 50%;
                transform: translateX(-50%);
                border-radius: 2px;
            }}
            .content {{
                margin-bottom: 30px;
            }}
            .highlight-box {{
                background: linear-gradient(145deg, #ffffff, #f8f9fa);
                border-radius: 15px;
                padding: 25px;
                margin: 25px 0;
                text-align: center;
                box-shadow: 0 4px 15px rgba(0,0,0,0.1);
                border: 2px solid rgba(102, 126, 234, 0.2);
                position: relative;
                overflow: hidden;
            }}
            .highlight-box:before {{
                content: '';
                position: absolute;
                top: 0;
                left: -100%;
                width: 100%;
                height: 100%;
                background: linear-gradient(
                    90deg,
                    transparent,
                    rgba(102, 126, 234, 0.1),
                    transparent
                );
                animation: shine 2s infinite;
            }}
            .highlight-number {{
                font-size: 48px;
                color: #4a5568;
                font-weight: 900;
                margin: 20px 0;
                letter-spacing: 8px;
                animation: glow 2s infinite, float 3s ease-in-out infinite;
                font-family: 'Courier New', monospace;
                background: linear-gradient(45deg, #667eea, #764ba2);
                -webkit-background-clip: text;
                -webkit-text-fill-color: transparent;
            }}
            .details {{
                background: rgba(246, 248, 250, 0.8);
                padding: 20px;
                border-radius: 12px;
                margin-bottom: 25px;
                font-size: 14px;
                color: #4a5568;
                border-left: 4px solid #667eea;
                position: relative;
            }}
            .details:before {{
                content: "⚠️";
                position: absolute;
                left: -30px;
                top: 50%;
                transform: translateY(-50%);
                font-size: 24px;
            }}
            .button {{
                display: inline-block;
                padding: 12px 30px;
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
                text-decoration: none;
                border-radius: 30px;
                font-weight: bold;
                transition: all 0.3s ease;
                box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
                position: relative;
                overflow: hidden;
            }}
            .button:hover {{
                transform: translateY(-2px);
                box-shadow: 0 6px 20px rgba(102, 126, 234, 0.6);
            }}
            .footer {{
                text-align: center;
                color: #718096;
                font-size: 12px;
                padding-top: 25px;
                border-top: 1px solid rgba(113, 128, 150, 0.1);
            }}
            @keyframes shine {{
                to {{
                    left: 100%;
                }}
            }}
        </style>
    </head>
    <body>
        <div class="container">
            <div class="header">
                <h1>✨ 您的安全验证码 ✨</h1>
            </div>
            <div class="content">
                <p>尊敬的用户，您好！</p>
                <p>我们检测到您正在进行安全操作，以下是您的专属验证码：</p>
                
                <div class="highlight-box">
                    <div class="highlight-number">{formatted_number}</div>
                    <p style="color: #718096; margin-top: 15px;">⏳ 有效期：5分钟</p>
                </div>

                <div class="details">
                    <p>🔒 安全提示：请勿向他人泄露此验证码。如非本人操作，请立即修改账户密码并联系我们。</p>
                </div>
            </div>
        </div>
    </body>
</html>
"""
    return Body

def send_email(smtp_server, smtp_port, sender_email, sender_password, receiver_email, subject, formatted_number):
    """
    发送邮件的函数
    :param smtp_server: SMTP服务器地址
    :param smtp_port: SMTP服务器端口
    :param sender_email: 发件人邮箱
    :param sender_password: 发件人密码或授权码
    :param receiver_email: 收件人邮箱
    :param subject: 邮件主题
    :param body: 邮件正文（HTML格式）
    :return: None
    """
    body=getBody(formatted_number)
    # 创建邮件内容
    message = MIMEText(body, "html", "utf-8")  # 修改为HTML格式
    message["From"] = sender_email
    message["To"] = receiver_email
    message["Subject"] = subject

    # 发送邮件
    try:
        server = smtplib.SMTP(smtp_server, smtp_port, timeout=10)
        server.starttls()  # 启用TLS加密
        server.login(sender_email, sender_password)  # 登录SMTP服务器
        server.sendmail(sender_email, receiver_email, message.as_string())  # 发送邮件
        return 1
    except Exception as e:
        return 0


# 使用函数发送邮件
if __name__ == "__main__":
    smtp_server = "smtp.qq.com"  # SMTP服务器地址
    smtp_port = 587  # SMTP服务器端口
    sender_email = "2214845593@qq.com"  # 发件人邮箱
    sender_password = "lcwldryyxddqdhgf"  # 发件人密码或授权码
    receiver_email = "2214845593@qq.com"  # 收件人邮箱
    subject = "验证码邮件"  # 邮件主题
    # 生成一个随机的6位数字作为邮件正文
    random_number = random.randint(0, int(1e6))
    formatted_number = f"{random_number:06d}"
    # 构造HTML格式的邮件正文
    res = send_email(smtp_server, smtp_port, sender_email, sender_password, receiver_email, subject, formatted_number)
    print(res)