import { useSettingStore } from '@/store/modules/setting'
import { SystemThemeEnum } from '@/enums/appEnum'

export const useTheme = () => {
  const store = useSettingStore()

  const switchTheme = (theme: SystemThemeEnum) => {
    store.setSystemThemeType(theme)
    document.documentElement.setAttribute('data-theme', theme)
  }

  return {
    switchTheme
  }
} 