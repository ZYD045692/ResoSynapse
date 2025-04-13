import 'vue-i18n'

declare module 'vue-i18n' {
  interface DefineLocaleMessage {
    login: {
      title: string
      subTitle: string
      placeholder: string[]
      sliderText: string
      sliderSuccessText: string
      rememberPwd: string
      forgetPwd: string
      btnText: string
      noAccount: string
      register: string
      success: {
        title: string
        message: string
      }
    }
  }
} 