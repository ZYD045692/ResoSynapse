<template>
  <div class="search-section" :id="`section-搜索`">
    <div class="search-buttons">
      <button v-for="platform in props.Platform" :key="platform.value" @click="selectPlatform(platform)"
        :class="{ active: selectedPlatform === platform.value }">
        <img :src="platform.logo" :alt="platform.label" class="platform-icon" />
        {{ platform.label }}
      </button>
    </div>
    <div class="search-box">
      <input class="input" type="text" :placeholder="selectedPlatformHint" v-model="searchQuery" />
      <button style="min-width: 60px;" @click="performSearch">
        <svg t="1740868302686" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
          p-id="18908" width="24" height="24">
          <path
            d="M448 85.333333a362.666667 362.666667 0 0 1 284.842667 587.178667l193.28 193.28a42.666667 42.666667 0 0 1-60.288 60.373333l-193.365334-193.28A362.666667 362.666667 0 1 1 448 85.333333z m0 85.333334a277.333333 277.333333 0 1 0 0 554.666666 277.333333 277.333333 0 0 0 0-554.666666z"
            fill="#ffffff" p-id="18909"></path>
        </svg>
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, defineProps,onMounted } from 'vue';
import type { PlatformType } from '@/api/model/platformModel'
// import {PlatformList} from '@/mock/temp/PlatformList'


// const platformList = PlatformList

const props = defineProps({
  Platform:{
    type: Array as () => PlatformType[], // 使用 Array 构造函数
    required: true
  }
});
// 初始化默认平台
const selectedPlatform = ref<string>(''); // 当前选择的搜索平台
const selectedPlatformLabel = ref<string>(''); // 当前选择的搜索平台标签
const selectedPlatformHint = ref<string>(''); // 当前选择的搜索平台提示
const searchQuery = ref<string>(''); // 搜索框内容
// 在组件挂载时初始化默认平台
onMounted(() => {
  const firstItem = props.Platform[0];
  if (firstItem) {
    selectedPlatform.value = firstItem.value;
    selectedPlatformLabel.value = firstItem.label;
    selectedPlatformHint.value = firstItem.hint;
  }
});


// 选择搜索平台
function selectPlatform(platform: PlatformType) {
  selectedPlatform.value = platform.value;
  selectedPlatformLabel.value = platform.label;
  selectedPlatformHint.value = platform.hint;
}

// 执行搜索
function performSearch() {
  if (searchQuery.value === '') {
    alert('请输入搜索内容');
    return;
  }

  const platform = props.Platform.find(p => p.value === selectedPlatform.value);
  if (platform) {
    const url = `${platform.url}${encodeURIComponent(searchQuery.value)}`;
    window.open(url, '_blank');
  } else {
    alert('未找到选中的平台');
  }
}
</script>

<style scoped>
.search-section {
  padding: 5px;
  text-align: center;
}

.search-buttons {
  margin-bottom: 20px;
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
  width: 75%;
  margin: 0 auto;
  gap:10px;
  /* 居中对齐 */
}

.search-buttons button {
  position: relative!important;
  /* margin: 5px; */
  padding: 6px 3px;
  border: none;
  border-radius: 6px;
  background: #ffffffAD;
  background: rgba(255, 255, 255, 70%);
  cursor: pointer;
  transition: background-color 0.3s;
  /* 添加过渡效果 */
  flex: 1;
  max-width: 10vh;
  height: 5vh;
  /* 调小按钮的最大宽度 */
  font-size: 8px;
  /* 调小字体大小 */
  font-weight: bold;
  color: #333;
  /* 设置文字颜色为黑色 */
}

.search-buttons button.active {
  background-color: rgba(255, 255, 255, 90%);
}

.platform-icon {
  width: 10px;
  height: 10px;
  margin-right: 3px;
  vertical-align: middle;
}

.search-buttons button:hover {
  background-color: rgba(255, 255, 255, 90%);
}

.search-box {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
}

.search-box .input {
  position: relative!important;
  height: 3vh!important;
  width: 70%;
  padding: 10px;
  border: 1px solid #ccc;
  border-right: none;
  /* 移除右侧边框 */
  border-top-left-radius: 10px;
  /* 左边上角圆角 */
  border-bottom-left-radius: 10px;
  /* 左边下角圆角 */
  border-top-right-radius: 0;
  /* 右边上角直角 */
  border-bottom-right-radius: 0;
  /* 右边下角直角 */
  font-size: 12px;
  font-weight: bold;
  padding-left: 16px;
}

.search-box input:focus {
  border-color: #0078d4;
  /* 点击时边框变为深蓝色 */
  outline: none;
  /* 移除默认的 focus 边框 */
}

.search-box button {
  position: relative;
  width: 5%;
  height: 6vh;
  padding: 10px 16px;
  border: 1px solid #4465d6;
  border-left: none;
  /* 移除左侧边框 */
  border-top-left-radius: 0;
  /* 左上角直角 */
  border-bottom-left-radius: 0;
  /* 左下角直角 */
  border-top-right-radius: 10px;
  /* 右上角圆角 */
  border-bottom-right-radius: 10px;
  /* 右下角圆角 */
  background-color: #4465d6;
  color: #ffffff;
  cursor: pointer;
  transition: background-color 0.3s;
  font-size: 14px;
}

.search-box button:hover {
  background-color: #345bd9;
}
</style>