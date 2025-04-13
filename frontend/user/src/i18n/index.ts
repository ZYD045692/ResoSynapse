import { LanguageEnum } from '@/enums/appEnum'

const messages = {
  [LanguageEnum.ZH]: {
    login: {
      title: '欢迎登录',
      subTitle: '智汇研析系统',
      placeholder: ['请输入用户名', '请输入密码', '请完成滑块验证'],
      sliderText: '向右滑动完成验证',
      sliderSuccessText: '验证通过',
      rememberPwd: '记住密码',
      forgetPwd: '忘记密码？',
      btnText: '登录',
      noAccount: '还没有账号？',
      register: '立即注册',
      success: {
        title: '登录成功',
        message: '欢迎回来'
      }
    }
  },
  [LanguageEnum.EN]: {
    login: {
      title: 'Welcome',
      subTitle: 'AI Analysis System',
      placeholder: ['Please enter username', 'Please enter password', 'Please complete the slider verification'],
      sliderText: 'Slide right to verify',
      sliderSuccessText: 'Verification passed',
      rememberPwd: 'Remember password',
      forgetPwd: 'Forgot password?',
      btnText: 'Login',
      noAccount: 'No account?',
      register: 'Register now',
      success: {
        title: 'Login successful',
        message: 'Welcome back'
      }
    }
  }
}

export const i18n = createI18n({
  legacy: false,
  locale: LanguageEnum.ZH,
  fallbackLocale: LanguageEnum.ZH,
  messages
}) 