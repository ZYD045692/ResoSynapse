<template>
  <div class="datetime-container">
    <div class="datetime-content">
      <div class="date">{{ formattedDate }}</div>
      <div class="time">{{ formattedTime }}</div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount } from 'vue';

const formattedDate = ref('');
const formattedTime = ref('');

const updateTime = () => {
  const now = new Date();
  formattedDate.value = now.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    weekday: 'long'
  });

  // 修改时间格式为HH:MM，并每秒更新确保及时性
  formattedTime.value = now.toLocaleTimeString('zh-CN', {
    hour12: false,
    hour: '2-digit',
    minute: '2-digit'
  });
};

let intervalId: NodeJS.Timeout | null = null;

onMounted(() => {
  updateTime();
  // 将间隔改为每秒更新一次
  intervalId = setInterval(updateTime, 1000);
});

onBeforeUnmount(() => {
  if (intervalId !== null) {
    clearInterval(intervalId);
  }
});
</script>

<style scoped>
.datetime-container {
  top: 10px;
  right: 10px;
  background: transparent;
  padding: 10px 20px;
  border-radius: 8px;
  z-index: 3;
}

.datetime-content {
  text-align: center;
}

.date {
  font-size: 12px;
  color: #ffffff;
  margin-bottom: 5px;
  font-weight: bold;
  text-shadow: 1px 1px 2px #000;
  font-weight: bold;

}

.time {
  font-size: 24px;
  font-weight: bold;
  color: #ffffff;
  text-shadow: 1px 1px 2px #000;
  font-weight: bold;
}
</style>