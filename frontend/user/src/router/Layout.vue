<template>
  <el-container class="app-container">
    <el-aside class="sidebar" :class="{ 'is-collapsed': isCollapsed }">
      <el-menu class="custom-nav-top" :router="true" mode="vertical" background-color="transparent"
        :unique-opened="true">
        <el-menu-item class="logo-menu-item">
          <el-link href="" target="_blank" :underline="false" class="logo-link">
            <img :src="logoicon" class="logo-icon" fit="contain" />
            <span class="title" v-show="!isCollapsed">智汇研析</span>
          </el-link>
        </el-menu-item>
      </el-menu>
      <el-menu class="custom-nav-head" :router="true" mode="vertical" background-color="transparent"
        :unique-opened="true">
        <el-menu-item index="/pdfServer" class="button-link" :route="{ path: '/pdfServer' }">
          <img :src="pdficon" class="button-icon">
          <span class="button-title" v-show="!isCollapsed">PDF编辑魔方</span>
        </el-menu-item>

        <el-menu-item index="/formulaServer" class="button-link" :route="{ path: '/formulaServer' }">
          <img :src="formulaicon" class="button-icon">
          <span class="button-title" v-show="!isCollapsed">公式快闪通</span>
        </el-menu-item>

        <el-menu-item index="/mdAIGC" class="button-link" :route="{ path: '/mdAIGC' }">
          <img :src="markdownicon" class="button-icon">
          <span class="button-title" v-show="!isCollapsed">MD显示器</span>
        </el-menu-item>

        <el-menu-item index="/translate" class="button-link" :route="{ path: '/translate' }">
          <img :src="translate" class="button-icon">
          <span class="button-title" v-show="!isCollapsed">文本智提侠</span>
        </el-menu-item>
      </el-menu>

      <el-menu class="custom-nav-bottom" :router="true" mode="vertical" background-color="transparent">
        <el-menu-item index="/app1" class="button-link" :route="{ path: '/app1' }">
          <img :src="apiicon" class="button-icon">
          <span class="button-title" v-show="!isCollapsed">API调用</span>
        </el-menu-item>

        <el-menu-item class="button-link">
          <el-popover
            placement="right"
            :width="300"
            trigger="click"
          >
            <template #reference>
              <div class="user-profile-trigger">
                <img :src="eopleicon" class="button-icon">
                <span class="button-title" v-show="!isCollapsed">个人中心</span>
              </div>
            </template>
            
            <div class="user-profile-content">
              <!-- 用户信息区域 -->
              <div class="user-info">
                <div class="avatar-wrapper">
                  <el-avatar :size="48" :src="avatarImage" />
                  <div class="status-badge"></div>
                </div>
                <div class="user-details">
                  <h3>zyd045692</h3>
                  <p>普通用户</p>
                </div>
              </div>

              <!-- 消息区域 -->
              <div class="message-section">
                <div class="message-header">
                  <h4>消息中心</h4>
                  <el-button text type="primary" class="view-all">查看全部</el-button>
                </div>
                <div class="message-list">
                  <div class="message-item">
                    <div class="message-icon">
                      <el-badge value="3" class="message-badge">
                        <el-icon><Bell /></el-icon>
                      </el-badge>
                    </div>
                    <div class="message-content">
                      <span class="message-title">系统通知</span>
                      <span class="message-desc">您有3条未读消息</span>
                    </div>
                    <el-icon class="message-arrow"><ArrowRight /></el-icon>
                  </div>
                  <div class="message-item">
                    <div class="message-icon">
                      <el-badge value="5" class="message-badge">
                        <el-icon><ChatDotRound /></el-icon>
                      </el-badge>
                    </div>
                    <div class="message-content">
                      <span class="message-title">消息提醒</span>
                      <span class="message-desc">您有5条未读消息</span>
                    </div>
                    <el-icon class="message-arrow"><ArrowRight /></el-icon>
                  </div>
                </div>
              </div>

              <!-- 操作按钮区域 -->
              <div class="action-buttons">
                <el-button class="action-button feedback" @click="handleFeedback">
                  <div class="button-content">
                    <el-icon><Edit /></el-icon>
                    <span>我要反馈</span>
                  </div>
                  <el-icon class="button-arrow"><ArrowRight /></el-icon>
                </el-button>
                <el-button class="action-button logout" @click="handleLogout">
                  <div class="button-content">
                    <el-icon><SwitchButton /></el-icon>
                    <span>退出登录</span>
                  </div>
                  <el-icon class="button-arrow"><ArrowRight /></el-icon>
                </el-button>
              </div>
            </div>
          </el-popover>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-main class="content-area">
      <div class="scrollable-content">
        <router-view />
      </div>
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import logoicon from '@/assets/logo.ico';
import pdficon from '@/assets/pdf.svg';
import markdownicon from '@/assets/markdown.svg';
import translate from '@/assets/translate.svg';
import formulaicon from '@/assets/formula.svg';
import apiicon from '@/assets/api.svg'
import eopleicon from '@/assets/onePeople.svg'
import avatarImage from '@/assets/avatar.jpg'
import { Bell, ChatDotRound, Edit, SwitchButton, ArrowRight } from '@element-plus/icons-vue'

const router = useRouter();
const isCollapsed = ref(false);
const breakpoint = 768; // 折叠断点宽度

const checkWidth = () => {
  isCollapsed.value = window.innerWidth <= breakpoint;
};

const handleFeedback = () => {
  // TODO: 实现反馈功能
  ElMessage.info('反馈功能开发中...');
};

const handleLogout = () => {
  localStorage.removeItem('isAuthenticated');
  ElMessage.success('已退出登录');
  router.push('/login');
};

onMounted(() => {
  checkWidth();
  window.addEventListener('resize', checkWidth);
});

onUnmounted(() => {
  window.removeEventListener('resize', checkWidth);
});
</script>

<style scoped>
.app-container {
  display: flex;
  height: 100vh;
  background-color: rgba(225, 235, 255, 0.5);
  /* background: rgba(255, 255, 255, 0.8); */
  backdrop-filter: blur(10px);
  border-radius: 20px;
  overflow: hidden;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
}

.sidebar {
  position: relative;
  width: auto !important;
  height: 100vh;
  border-right: none;
  overflow-y: auto;
  overflow-x: hidden;
  scrollbar-width: none;
  -ms-overflow-style: none;
  transition: width 0.3s ease-in-out;
}

.sidebar:not(.is-collapsed) {
  width: 200px !important;
}

.sidebar.is-collapsed {
  width: 64px !important;
}

.sidebar-header {
  padding: 30px;
}

.logo-menu-item {
  padding: 0 !important;
  margin-top: 30px !important;
  height: auto !important;
  line-height: normal !important;
  border-radius: 30px !important;
  background-color: transparent !important;
}

.logo-menu-item:hover {
  background-color: transparent !important;
  transform: none !important;
}

.logo-menu-item:deep(.el-menu-item__content) {
  padding: 30px 20px;
  justify-content: center;
}

.logo-link {
  display: flex;
  align-items: center;
  gap: 20px;
  color: #2c3e50;
  width: 100%;
}

.logo-icon {
  width: 24px;
  height: 24px;
  min-width: 24px;
  min-height: 24px;
}

.title {
  font-family: "Microsoft YaHei", "微软雅黑", sans-serif;
  font-size: 16px;
  color: rgba(123,104,238);
  margin-left: 10px;
  font-weight: bold;
}

.custom-nav-head {
  border-right: none;
  margin-top: 20px;
}

.custom-nav-bottom {
  border-right: none;
  position: absolute;
  bottom: 20px;
  left: 0;
  right: 0;
}

:deep(.el-menu-item) {
  height: auto;
  line-height: normal;
  padding: 16px 20px !important;
  margin: 0 20px 12px;
  border-radius: 30px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border: 1px solid transparent;
  background: transparent;
}

:deep(.el-menu-item:hover) {
  background-color: rgba(255, 255, 255, 0.95) !important;
  transform: translateY(-2px);
}

:deep(.el-menu-item.is-active) {
  background: rgba(255, 255, 255, 0.8) !important;
  color: rgba(123,104,238) !important;
  border: none;
}

:deep(.el-menu-item.is-active .button-icon) {
  filter: brightness(0) saturate(100%) invert(40%) sepia(98%) saturate(1234%) hue-rotate(234deg) brightness(89%) contrast(101%);
}

:deep(.el-menu-item.is-active .button-title) {
  color: rgba(123,104,238) !important;
  font-weight: bold;
}

.content-area {
  width: 88%;
  padding: 20px;
  background-color: transparent;
  overflow: hidden;
  position: relative;
}

.scrollable-content {
  height: 100%;
  overflow-y: auto;
  scrollbar-width: none;
  -ms-overflow-style: none;
  padding-right: 10px;
}

.scrollable-content::-webkit-scrollbar {
  display: none;
}

.scrollable-content::-webkit-scrollbar-track {
  background: transparent;
}

.scrollable-content::-webkit-scrollbar-thumb {
  background-color: var(--primary-color);
  border-radius: 3px;
}

.button-link {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  gap: 16px;
  padding: 16px 20px;
}

.button-icon {
  width: 18px;
  height: 18px;
  margin-right: 0;
  transition: transform 0.3s ease;
  filter: brightness(0) saturate(100%) invert(42%) sepia(8%) saturate(1234%) hue-rotate(201deg) brightness(94%) contrast(89%);
}

:deep(.el-menu-item:hover .button-icon) {
  transform: scale(1.1);
}

.button-title {
  font-size: 14px;
  color: #686B86;
  font-family: "Microsoft YaHei", "微软雅黑", sans-serif;
  white-space: nowrap;
  transition: all 0.3s ease-in-out;
  letter-spacing: 0.3px;
}

.user-profile-trigger {
  display: flex;
  align-items: center;
  gap: 16px;
  cursor: pointer;
}

.user-profile-content {
  padding: 24px;
  background: #ffffff;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 20px;
  padding-bottom: 24px;
  border-bottom: 1px solid rgba(123,104,238, 0.1);
}

.avatar-wrapper {
  position: relative;
}

.status-badge {
  position: absolute;
  bottom: 2px;
  right: 2px;
  width: 12px;
  height: 12px;
  background: #67c23a;
  border: 2px solid #ffffff;
  border-radius: 50%;
  box-shadow: 0 0 0 2px rgba(103,194,58,0.2);
}

.user-details h3 {
  margin: 0;
  font-size: 18px;
  color: #2c3e50;
  font-weight: 600;
}

.user-details p {
  margin: 4px 0 0;
  font-size: 14px;
  color: #909399;
}

.message-section {
  margin-top: 24px;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 16px;
  border: 1px solid rgba(123,104,238, 0.1);
}

.message-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.message-header h4 {
  font-size: 16px;
  color: #2c3e50;
  margin: 0;
  font-weight: 600;
}

.view-all {
  font-size: 14px;
  color: rgba(123,104,238);
  padding: 4px 8px;
  border-radius: 4px;
  transition: all 0.3s ease;
}

.view-all:hover {
  background: rgba(123,104,238, 0.1);
}

.message-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.message-item {
  display: flex;
  align-items: center;
  padding: 16px;
  background: #ffffff;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid rgba(123,104,238, 0.1);
}

.message-item:hover {
  transform: translateX(4px);
  box-shadow: 0 4px 16px rgba(123,104,238, 0.1);
  border-color: rgba(123,104,238, 0.2);
}

.message-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 44px;
  height: 44px;
  background: rgba(123,104,238, 0.1);
  border-radius: 12px;
  margin-right: 16px;
}

.message-icon .el-icon {
  font-size: 22px;
  color: rgba(123,104,238);
}

.message-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.message-title {
  font-size: 15px;
  color: #2c3e50;
  font-weight: 500;
}

.message-desc {
  font-size: 13px;
  color: #909399;
}

.message-arrow {
  color: #909399;
  font-size: 16px;
  transition: all 0.3s ease;
}

.message-item:hover .message-arrow {
  transform: translateX(4px);
  color: rgba(123,104,238);
}

.action-buttons {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-top: 24px;
}

.action-button {
  width: 100%;
  height: 48px;
  padding: 0 20px;
  border: none;
  background: #f8f9fa;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  cursor: pointer;
  transition: all 0.3s ease;
}

.action-button:hover {
  background: rgba(123,104,238, 0.1);
  transform: translateX(4px);
}

.action-button.feedback {
  color: rgba(123,104,238);
}

.action-button.logout {
  color: #f56c6c;
}

.button-content {
  display: flex;
  align-items: center;
  gap: 12px;
}

.button-content .el-icon {
  font-size: 20px;
}

.button-content span {
  font-size: 15px;
  font-weight: 500;
}

.button-arrow {
  font-size: 16px;
  transition: transform 0.3s ease;
}

.action-button:hover .button-arrow {
  transform: translateX(4px);
}

:deep(.el-badge__content) {
  background: #f56c6c;
  border: 2px solid #ffffff;
  font-size: 12px;
  padding: 0 6px;
  height: 18px;
  line-height: 18px;
}

:deep(.el-popover) {
  padding: 0;
  border-radius: 16px;
  border: 1px solid rgba(123,104,238, 0.1);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.08);
}
</style>