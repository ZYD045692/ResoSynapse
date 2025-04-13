<template>
  <div class="modal-container" v-if="isLoginandRegVisible">
    <transition :name="transitionName" mode="out-in">
      <div v-if="currentForm === 'login'" key="login" class="form-container">
        <h1>登录</h1>
        <form @submit.prevent="handleLogin">
          <div class="input-group">
            <label>用户名 <span class="required">*</span></label>
            <input type="text" v-model="loginUser" required autocomplete="username" />
          </div>
          <div class="input-group">
            <label>密码 <span class="required">*</span></label>
            <input type="password" v-model="loginPwd" required autocomplete="current-password" />
          </div>
          <div class="combination">
            <button class="back-button" @click="back">返回</button>
            <button type="submit">立即登录</button>
          </div>
          <div class="toggle-text">
            <span @click="toggleForm('register')">没有账号？立即注册</span>
            <span @click="toggleForm('forgot')">忘记密码？</span>
          </div>
        </form>
      </div>

      <!-- 注册表单 -->
      <div v-else-if="currentForm === 'register'" key="register" class="form-container">
        <h1>注册</h1>
        <form @submit.prevent="handleRegister">
          <div class="input-group">
            <label>用户名 <span class="required">*</span></label>
            <input type="text" v-model="regUser" required autocomplete="username" />
          </div>
          <div class="input-group">
            <label>密码 <span class="required">*</span></label>
            <input type="password" v-model="regPwd" required autocomplete="new-password" />
          </div>
          <div class="input-group">
            <label>邮箱 <span class="required">*</span></label>
            <div style="display: flex; align-items: center;">
              <input type="email" v-model="regEmail" required autocomplete="email" />
              <button type="button" class="verification-btn" @click="sendVerificationCode"
                :disabled="verificationDisabled">
                {{ verificationText }}
              </button>
            </div>
          </div>
          <div class="input-group">
            <label>验证码 <span class="required">*</span></label>
            <input type="text" v-model="regCode" required />
          </div>
          <div class="combination">
            <button class="back-button" @click="back">返回</button>
            <button type="submit">立即注册</button>
          </div>
          <div class="toggle-text">
            <span @click="toggleForm('login')">已有账号？立即登录</span>
          </div>
        </form>
      </div>

      <!-- 忘记密码表单 -->
      <div v-else-if="currentForm === 'forgot'" key="forgot" class="form-container">
        <h1>忘记密码</h1>
        <form @submit.prevent="handleForgotPassword">
          <div class="input-group">
            <label>用户名 <span class="required">*</span></label>
            <input type="text" v-model="forgotUser" required autocomplete="username" />
          </div>
          <div class="input-group">
            <label>邮箱 <span class="required">*</span></label>
            <div style="display: flex; align-items: center;">
              <input type="email" v-model="forgotEmail" required autocomplete="email" />
              <button type="button" class="verification-btn" @click="sendVerificationCode"
                :disabled="verificationDisabled">
                {{ verificationText }}
              </button>
            </div>
          </div>
          <div class="input-group">
            <label>验证码 <span class="required">*</span></label>
            <input type="text" v-model="forgotCode" required />
          </div>
          <div class="input-group">
            <label>新密码 <span class="required">*</span></label>
            <input type="password" v-model="forgotPwd" required autocomplete="new-password" />
          </div>
          <div class="combination">
            <button class="back-button" @click="back">返回</button>
            <button type="submit">更新密码</button>
          </div>
          <div class="toggle-text">
            <span @click="toggleForm('login')">返回登录</span>
          </div>
        </form>
      </div>
    </transition>
  </div>
</template>


<script setup lang="ts">
import { ref, computed } from 'vue';
// import { defineProps, defineEmits } from 'vue';
// import EmojiText from '@/utils/emojo'
import { apiUrls } from '@/api/api';
import stringToMD5 from '@/utils/md5';
import axios from 'axios';
import { useUserStore } from '@/store/User';
import { ElMessage } from 'element-plus'
const userStore = useUserStore();
const isLoginandRegVisible = computed(() => userStore.isLoginandRegVisible);
// 响应式状态
const currentForm = ref('login'); // 当前显示的表单：login、register、forgot
const transitionName = ref('slide-left');
const loginUser = ref('');
const loginPwd = ref('');
const regUser = ref('');
const regPwd = ref('');
const regEmail = ref(''); // 注册邮箱
const regCode = ref('');  // 注册验证码
const forgotUser = ref('');
const forgotEmail = ref('');
const forgotCode = ref('');
const forgotPwd = ref('');
const verificationText = ref('获取验证码'); // 获取验证码按钮文本
const verificationDisabled = ref(false); // 获取验证码按钮是否禁用
const verificationCountdown = ref(60); // 验证码倒计时时间（秒）

// 发送验证码
const sendVerificationCode = async () => {
  // 验证邮箱格式
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!emailRegex.test(regEmail.value)) {
    ElMessage.warning('请输入有效的邮箱地址');
    return;
  }

  const postData = {
    email: regEmail.value,
  };

  const GetEmailCaptcharegResponse = await axios.get(apiUrls.GetEmailCaptcha_API_URL, { params: postData });

  switch (GetEmailCaptcharegResponse.data.stateCode) {
    case 0:
      // 时间差不超过一分钟
      ElMessage.warning("获取时间差不足一分钟");
      verificationCountdown.value = 5
      // 可以在这里添加相关的处理逻辑，比如提示用户
      break;
    case 1:
      ElMessage.success("获取成功");
      break;
    case 2:
      // 验证码获取失败
      ElMessage.error("验证码获取失败");
      verificationCountdown.value = 5
      // 可以在这里添加提示用户重新获取验证码的逻辑
      break;
    case 3:
      // 服务器错误
      ElMessage.error("服务器错误");
      verificationCountdown.value = 5
      // 可以在这里添加错误处理逻辑，比如提示用户稍后重试
      break;
    default:
      // 其他未知状态码
      ElMessage.error("未知状态码：" + GetEmailCaptcharegResponse.status);
      verificationCountdown.value = 5
      // 可以在这里添加默认的错误处理逻辑
      break;
  }


  // 设置按钮状态
  verificationDisabled.value = true;
  let countdown = verificationCountdown.value;
  const timer = setInterval(() => {
    countdown--;
    verificationText.value = `${countdown}秒后重试`;
    if (countdown <= 0) {
      clearInterval(timer);
      verificationText.value = '获取验证码';
      verificationDisabled.value = false;
    }
  }, 1000);
  verificationCountdown.value = 60
};

// 清空所有数据
const resetAllData = () => {
  loginUser.value = '';
  loginPwd.value = '';
  regUser.value = '';
  regPwd.value = '';
  regEmail.value = '';
  regCode.value = '';
  forgotUser.value = '';
  forgotEmail.value = '';
  forgotCode.value = '';
  forgotPwd.value = '';
  verificationText.value = '获取验证码';
  verificationDisabled.value = false;
};

// 方法
const toggleForm = (formType: string) => {
  // 切换表单时设置过渡动画方向
  if (formType === 'login') {
    transitionName.value = currentForm.value === 'register' ? 'slide-right' : 'slide-left';
  } else if (formType === 'register') {
    transitionName.value = 'slide-left';
  } else if (formType === 'forgot') {
    transitionName.value = 'slide-left';
  } else if (formType === 'login-from-forgot') {
    transitionName.value = 'slide-right';
  }
  // 切换表单前清空所有数据
  resetAllData();
  currentForm.value = formType;
};

const back = () => {
  resetAllData();
  currentForm.value = "login";
  userStore.isLoginandRegVisible = false;
};

const handleLogin = async () => {
  // 登录逻辑
  if (loginUser.value.trim() === '' || loginPwd.value.trim() === '') {
    // 如果用户名或密码为空，提示用户
    ElMessage.warning('用户名和密码不能为空！');
    return;
  }

  // 将用户名和密码转换为字符串，并计算密码的 MD5 哈希值
  const postData = {
    userName: String(loginUser.value),
    passwordHash: stringToMD5(String(loginPwd.value)),
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
        userStore.setUserId(999)
        const cookies = document.cookie.split(';'); // 将Cookie字符串按分号分割成数组
        const cookieMap = new Map<string, string>();
        cookies.forEach(cookie => {
          const [name, value] = cookie.trim().split('='); // 分割 Cookie 的名称和值
          cookieMap.set(name, value);
        });
        if (cookieMap.get('userId')){
          userStore.setUserId(Number(cookieMap.get('userId'))||999);
        }
        if (cookieMap.get('userName')){
          userStore.setUserName(cookieMap.get('userName')||'');
        }
        if (cookieMap.get('profilePhotoUrl')){
          userStore.setprofilePhotoUrl(cookieMap.get('profilePhotoUrl')||'https://www.keaitupian.cn/cjpic/frombd/2/253/1639036208/3382934126.jpg');
        }
        back()
        break;
      default:
        // 未知状态码
        ElMessage.error(`登录失败：未知错误，状态码 ${stateCode}`);
        break;
    }
  }
};

// 主函数，处理整个注册流程
const handleRegister = async () => {
  try {
    // 第一阶段：验证邮箱验证码
    await validateEmailCaptcha();

    // 第二阶段：提交注册信息
    await submitRegistration();
  } catch (error: any) {
    console.error("注册错误:", error);
    if (error.response) {
      // 处理HTTP状态码错误（4xx/5xx）
      ElMessage.error(`请求失败：${error.response.status} ${error.response.statusText}`);
    } else {
      ElMessage.error("网络错误或请求被阻止");
    }
  }
};

// 验证邮箱验证码的函数
const validateEmailCaptcha = async () => {
  try {
    const checkCaptcha = await axios.post(
      apiUrls.CheckEmailCaptcha,
      {
        email: regEmail.value,
        captcha: String(regCode.value),
      },
      {
        withCredentials: true,
        headers: {
          'Content-Type': 'application/json', // 默认的 JSON 内容类型
        },
      },
    );

    switch (checkCaptcha.data.stateCode) {
      case 1:  // 验证成功
        break;
      case 0:
        throw new Error('验证码错误');
      case 2:
        throw new Error('验证码或邮箱数据不存在');
      case 3:
        throw new Error('验证码已过期');
      case 4:
        throw new Error('缺少必要参数');
      case 5:
        throw new Error('验证码已被使用');
      default:
        throw new Error(`验证失败：未知状态码 ${checkCaptcha.data.stateCode}`);
    }
  } catch (error: any) {
    ElMessage.error(error.message);
    throw error; // 重新抛出错误以便主函数捕获
  }
};

// 提交注册信息的函数
const submitRegistration = async () => {
  try {
    const register = await axios.post(
      apiUrls.UserRegister_API_URL,
      {
        userName: String(regUser.value),
        passwordHash: stringToMD5(regPwd.value),
        email: regEmail.value
      },
    );

    switch (register.data.stateCode) {
      case 1:
        ElMessage.success('注册成功');
        toggleForm('login');
        break;
      case 0:
        ElMessage.warning('用户名重复');
        break;
      case 2:
        ElMessage.warning('邮箱已被使用');
        break;
      default:
        ElMessage.error(`注册失败：未知状态码 ${register.data.stateCode}`);
    }
  } catch (error: any) {
    // 这里假设submitRegistration内部的错误已经在validateEmailCaptcha或主函数中处理
    // 如果需要，可以在这里添加额外的错误处理逻辑
    throw error;
  }
};

const handleForgotPassword = () => {
  // 忘记密码逻辑
};


</script>

<style scoped>
/* 容器样式 */
.modal-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: rgba(0, 0, 0, 0.2);
  z-index: 9999;
}

.required {
  color: red;
  margin-left: 2px;
}

/* 表单容器 */
.form-container {
  position: relative;
  width: 400px;
  padding: 40px;
  background: white;
  border-radius: 10px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
}

/* 使用深度选择器处理transition动画 */
:deep(.slide-left-enter-active),
:deep(.slide-left-leave-active),
:deep(.slide-right-enter-active),
:deep(.slide-right-leave-active) {
  transition: all 0.5s cubic-bezier(0.55, 0, 0.1, 1);
}

:deep(.slide-left-enter-from) {
  opacity: 0;
  transform: translateX(100px);
}

:deep(.slide-left-leave-to) {
  opacity: 0;
  transform: translateX(-100px);
}

:deep(.slide-right-enter-from) {
  opacity: 0;
  transform: translateX(-100px);
}

:deep(.slide-right-leave-to) {
  opacity: 0;
  transform: translateX(100px);
}

/* 组件内样式 */
.input-group {
  margin-bottom: 25px;
}

.combination {
  display: flex;
  gap: 10px;
  /* 可选：设置子元素之间的间距 */
}

.input-group label {
  display: block;
  margin-bottom: 8px;
  color: #666;
  font-size: 14px;
}

.input-group input {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 16px;
}

button {
  width: 100%;
  padding: 14px;
  background: #007bff;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 16px;
  cursor: pointer;
  transition: background 0.3s;
}

button:hover {
  background: #0056b3;
}

.toggle-text {
  margin-top: 20px;
  text-align: center;
  color: #666;
}

.toggle-text span {
  color: #007bff;
  cursor: pointer;
  text-decoration: underline;
  margin: 0 10px;
}
</style>