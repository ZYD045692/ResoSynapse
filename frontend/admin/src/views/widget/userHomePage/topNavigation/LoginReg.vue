<template>
  <div class="modal-container" v-if="props.isLoginandRegVisible">
    <!-- 登录表单 -->
    <transition :name="transitionName" mode="out-in">
      <div v-if="isLogin" key="login" class="form-container">
        <h1>登录</h1>
        <form @submit.prevent="handleLogin">
          <div class="input-group">
            <label>用户名</label>
            <input type="text" v-model="loginUser" required />
          </div>
          <div class="input-group">
            <label>密码</label>
            <input type="password" v-model="loginPwd" required />
          </div>
          <button type="submit">立即登录</button>
          <div class="toggle-text">
            <span @click="toggleForm(false)">没有账号？立即注册</span>
          </div>
        </form>
      </div>

      <!-- 注册表单 -->
      <div v-else key="register" class="form-container">
        <h1>注册</h1>
        <form @submit.prevent="handleRegister">
          <div class="input-group">
            <label>用户名</label>
            <input type="text" v-model="regUser" required />
          </div>
          <div class="input-group">
            <label>密码</label>
            <input type="password" v-model="regPwd" required />
          </div>
          <button type="submit">立即注册</button>
          <div class="toggle-text">
            <span @click="toggleForm(true)">已有账号？立即登录</span>
          </div>
        </form>
      </div>
    </transition>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { defineProps } from 'vue';

const props = defineProps({
  isLoginandRegVisible: {
    type: Boolean,
    required: true
  }
});


// 响应式状态
const isLogin = ref(true);
const transitionName = ref('slide-left');
const loginUser = ref('');
const loginPwd = ref('');
const regUser = ref('');
const regPwd = ref('');

// 方法
const toggleForm = (isLoginValue: boolean) => {
  transitionName.value = isLoginValue ? 'slide-right' : 'slide-left';
  isLogin.value = isLoginValue;
};

const handleLogin = () => {
  // 登录逻辑
};

const handleRegister = () => {
  // 注册逻辑
};
</script>

<!-- <style> -->
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
}
</style>