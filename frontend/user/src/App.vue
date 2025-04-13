<template>
  <router-view v-slot="{ Component }">
    <transition name="fade" mode="out-in">
      <component :is="Component" />
    </transition>
  </router-view>
</template>

<script setup lang="ts">
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html, body {
  height: 100%;
  width: 100%;
}

#app {
  font-family: Arial, sans-serif;
  box-sizing: border-box;
  background-color: rgba(240, 247, 255, 0.95);
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

:root {
  --primary-color: #0d8377; /* 主色调 */
  --primary-dark: #0a665d; /* 主色调的深色版本 */
  --background-color: #f8f9fa; /* 背景颜色 */
  --text-color: #495057; /* 文字颜色 */
  --hover-background: #e9ecef; /* 悬停时的背景颜色 */
  --sidebar-background: #9e9aa1ec; /* 侧边栏背景颜色 */
  --container-background: #e4dfe9ec; /* 容器背景颜色 */
  --border-radius: 30px; /* 圆角大小 */
  --transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1); /* 动画过渡效果 */
  --gap: 12px; /* 间距 */
  --padding: 12px 20px; /* 填充 */
  --box-shadow: 0 4px 6px -1px rgba(13, 131, 119, 0.2); /* 阴影 */
  --font-size-small: 12px; /* 小字体大小 */
  --font-size-medium: 14px; /* 中等字体大小 */
  --font-size-large: 18px; /* 大字体大小 */
  --button-background: var(--primary-color); /* 按钮背景色 */
  --button-border: 1px solid var(--primary-dark); /* 按钮边框 */
  --button-hover-background: var(--primary-dark); /* 按钮悬停背景色 */
  --button-hover-border: 1px solid var(--primary-color); /* 按钮悬停边框 */
}
</style>