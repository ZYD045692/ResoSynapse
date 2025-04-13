<template>
  <div class="login-container">
    <!-- Left side - Image placeholder -->
    <div class="login-left">
      <div class="image-content">
        <h1>智汇研析</h1>
        <p class="subtitle">智能文档处理专家</p>
        <div class="feature-list">
          <div class="feature-item">
            <el-icon>
              <Document />
            </el-icon>
            <span>PDF智能编辑</span>
          </div>
          <div class="feature-item">
            <el-icon>
              <Operation />
            </el-icon>
            <span>公式快速识别</span>
          </div>
          <div class="feature-item">
            <el-icon>
              <ChatDotRound />
            </el-icon>
            <span>智能文本处理</span>
          </div>
        </div>
      </div>
    </div>

    <!-- Right side - Login form -->
    <div class="login-right">
      <div class="login-form">
        <div class="form-header">
          <h2>欢迎登录</h2>
          <p class="welcome-text">请使用您的账号密码登录系统</p>
        </div>
        <el-form :model="loginForm" :rules="rules" ref="loginFormRef">
          <el-form-item prop="username">
            <el-input v-model="loginForm.username" placeholder="请输入用户名" prefix-icon="User" />
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" prefix-icon="Lock"
              show-password />
          </el-form-item>

          <!-- Slider verification -->
          <el-form-item prop="slider">
            <div class="slider-container">

              <div class="slider-header">
                <span class="slider-title">{{ getSliderText }}</span>
              </div>
              <div class="slider-wrapper">
                <el-slider v-model="sliderValue" :min="0" :max="100" :step="1" :show-stops="false" :show-tooltip="false"
                  @change="handleSliderChange" :disabled="!canSlide || isVerified" class="custom-slider">
                  <template #button>
                    <div class="custom-button">
                      <span v-if="!isVerified">→</span>
                      <el-icon v-else>
                        <CircleCheckFilled />
                      </el-icon>
                    </div>
                  </template>
                </el-slider>
              </div>

            </div>
          </el-form-item>

          <el-form-item>
            <div class="button-container">
              <el-button type="primary" class="login-button" @click="handleLogin" :disabled="!canLogin">
                登录
              </el-button>
            </div>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { User, Lock, Document, Operation, ChatDotRound, CircleCheckFilled } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/User'
import stringToMD5 from '@/utils/md5';
import axios from 'axios';
import { apiUrls } from "../api/api";

const router = useRouter()
const userStore = useUserStore()
const loginFormRef = ref(null)
const sliderValue = ref(0)
const isVerified = ref(false)

const loginForm = reactive({
  username: '',
  password: ''
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
}

const canLogin = computed(() => {
  return loginForm.username.trim() !== '' &&
    loginForm.password.trim() !== '' &&
    isVerified.value
})

const canSlide = computed(() => {
  return loginForm.username.trim() !== '' && loginForm.password.trim() !== ''
})

const getSliderText = computed(() => {
  if (!canSlide.value) {
    return '请先输入用户名和密码'
  }
  return isVerified.value ? '验证通过' : '请向右滑动完成验证'
})

const handleSliderChange = (value) => {
  if (!canSlide.value) {
    ElMessage.warning('请先输入用户名和密码')
    sliderValue.value = 0
    return
  }

  if (value === 100 && !isVerified.value) {
    isVerified.value = true
    ElMessage.success('验证通过')
  }
}

const handleLogin = async () => {
  if (!isVerified.value) {
    ElMessage.warning('请先完成滑动验证')
    return
  }

  loginFormRef.value.validate(async (valid) => {
    if (valid) {
      // 将用户名和密码转换为字符串，并计算密码的 MD5 哈希值
      const postData = {
        userName: String(loginForm.username),
        passwordHash: stringToMD5(String(loginForm.password)),
      };

      // 发送 POST 请求到后端登录接口
      const response = await axios.post(apiUrls.UserLogin_API_URL, postData);

      // 检查响应状态码和 stateCode
      if (response.status === 200) {

        const stateCode = response.data.stateCode;
        switch (stateCode) {
          case 0:
            // 用户不存在
            ElMessage.error('登录失败：用户不存在，请检查用户名是否正确。');
            break;
          case 1:
            // 密码错误
            ElMessage.error('登录失败：密码错误，请检查密码是否正确。');
            break;
          case 2:
            // 登录成功
            ElMessage.success('登录成功！');
            userStore.setUserId(response.data.userId);
            userStore.setUserName(response.data.userName);
            router.push('/')
            localStorage.setItem('isAuthenticated', 'true')
            break;
          default:
            // 未知状态码
            ElMessage.error(`登录失败：未知错误，状态码 ${stateCode}`);
            break;
        }
      }
    }
  })
}
</script>

<style scoped>
.login-container {
  display: flex;
  height: 100vh;
  width: 100vw;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}

.login-left {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, rgba(123, 104, 238, 0.1) 0%, rgba(123, 104, 238, 0.2) 100%);
  position: relative;
  overflow: hidden;
}

.login-left::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: url('@/assets/background.jpg') center/cover;
  opacity: 0.1;
}

.image-content {
  position: relative;
  z-index: 1;
  text-align: center;
  color: #2c3e50;
  padding: 40px;
}

.image-content h1 {
  font-size: 2.5em;
  margin-bottom: 20px;
  color: rgba(123, 104, 238);
  font-weight: bold;
}

.subtitle {
  font-size: 1.2em;
  color: #666;
  margin-bottom: 40px;
}

.feature-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
  align-items: center;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 15px 30px;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 30px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;
}

.feature-item:hover {
  transform: translateY(-3px);
}

.feature-item .el-icon {
  font-size: 24px;
  color: rgba(123, 104, 238);
}

.feature-item span {
  font-size: 16px;
  color: #2c3e50;
}

.login-right {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(10px);
}

.login-form {
  width: 400px;
  padding: 40px;
  border-radius: 20px;
  background: rgba(255, 255, 255, 0.95);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
}

.form-header {
  text-align: center;
  margin-bottom: 40px;
}

.form-header h2 {
  font-size: 28px;
  color: #2c3e50;
  margin-bottom: 10px;
}

.welcome-text {
  color: #666;
  font-size: 16px;
}

.slider-container {
  background: #ffffff;
  padding: 24px;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  width: 100%;
  border: 1px solid rgba(123, 104, 238, 0.1);
}

.slider-header {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 24px;
}

.slider-title {
  font-size: 18px;
  font-weight: 500;
  color: #2c3e50;
  width: 100%;
  text-align: center;
}

.slider-wrapper {
  position: relative;
  margin: 0;
  padding: 0;
}

:deep(.custom-slider) {
  --el-slider-height: 32px;
  --el-slider-button-size: 32px;
  --el-slider-runway-bg-color: #f0f2f5;
  --el-slider-bar-bg-color: rgba(123, 104, 238);
  --el-slider-button-wrapper-offset: 0;
}

:deep(.custom-slider .el-slider__runway) {
  height: 32px;
  border-radius: 16px;
  margin: 0;
}

:deep(.custom-slider .el-slider__bar) {
  height: 32px;
  border-radius: 16px;
  background: linear-gradient(90deg, rgba(123, 104, 238, 0.9) 0%, rgba(123, 104, 238, 0.7) 100%);
}

:deep(.custom-slider .el-slider__button-wrapper) {
  top: 0;
  transform: translateX(-50%);
}

:deep(.custom-slider .el-slider__button) {
  width: 32px;
  height: 32px;
  border: 2px solid rgba(123, 104, 238);
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 2px 8px rgba(123, 104, 238, 0.2);
}

.custom-button {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  color: rgba(123, 104, 238);
  font-weight: 500;
}

.slider-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 12px;
}

.slider-text {
  color: #606266;
  font-size: 14px;
}

:deep(.custom-slider.is-disabled .el-slider__button) {
  border-color: #c0c4cc;
  background: #f5f7fa;
  box-shadow: none;
  cursor: not-allowed;
}

:deep(.custom-slider.is-disabled .el-slider__bar) {
  background: #c0c4cc;
  cursor: not-allowed;
}

:deep(.custom-slider.is-disabled) {
  cursor: not-allowed;
}

.button-container {
  display: flex;
  justify-content: center;
  width: 100%;
  margin-top: 40px;
}

.login-button {
  width: 80%;
  height: 44px;
  font-size: 18px;
  border-radius: 22px;
  background: linear-gradient(135deg, rgba(123, 104, 238) 0%, rgba(123, 104, 238, 0.8) 100%);
  border: none;
  transition: transform 0.3s ease;
}

.login-button:hover {
  transform: translateY(-2px);
}

.login-button:disabled {
  background: #ccc;
  transform: none;
}

:deep(.el-input__wrapper) {
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04) !important;
  border-radius: 8px;
  height: 48px;
  padding: 0 16px;
  outline: none;
  border: none;
  background: none !important;
  --el-input-bg-color: transparent !important;
  --el-fill-color-blank: transparent !important;
}

:deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 2px 12px rgba(123, 104, 238, 0.2) !important;
  outline: none;
  border: none;
  background: none !important;
  --el-input-bg-color: transparent !important;
  --el-fill-color-blank: transparent !important;
}

:deep(.el-input__wrapper.el-input__wrapper.is-focus) {
  background-color: transparent !important;
}

:deep(.el-input__wrapper:not(.is-focus)) {
  background-color: transparent !important;
}

:deep(.el-input__inner) {
  font-size: 18px;
  height: 48px;
  line-height: 48px;
  color: #2c3e50;
  outline: none;
  border: none;
  background: none !important;
  --el-input-bg-color: transparent !important;
  --el-fill-color-blank: transparent !important;
  -webkit-text-fill-color: #2c3e50 !important;
}

:deep(.el-input__inner:focus) {
  outline: none;
  border: none;
  box-shadow: none;
  background: transparent !important;
}

:deep(.el-input) {
  --el-input-bg-color: transparent !important;
  --el-fill-color-blank: transparent !important;
  background: none !important;
}

:deep(.el-input__prefix-inner) {
  font-size: 20px;
  color: rgba(123, 104, 238, 0.8);
  margin-right: 12px;
}

:deep(.el-input__wrapper:hover) {
  box-shadow: 0 2px 12px rgba(123, 104, 238, 0.1);
}

:deep(.el-form-item) {
  margin-bottom: 24px;
}

:deep(.el-form-item__error) {
  font-size: 14px;
  padding-top: 4px;
}

:deep(.el-form-item__content) {
  --el-input-bg-color: transparent !important;
  --el-fill-color-blank: transparent !important;
  background: none !important;
}

.login-form :deep(.el-input__wrapper) {
  background: none !important;
  --el-input-bg-color: transparent !important;
  --el-fill-color-blank: transparent !important;
}

@media (max-width: 768px) {
  .login-container {
    flex-direction: column;
  }

  .login-left {
    display: none;
  }

  .login-form {
    width: 90%;
    padding: 30px 20px;
  }
}
</style>
