<template>
  <div class="block-list">
    <div v-for="(block, index) in props.blockList" 
         :key="`section-${block.name}`" 
         :id="`section-${block.name}`"
         :ref="`scrollTarget-${block.id}`"
         class="block-item">
      <div class="block-header">
        <img :src="block.iconUrl" :alt="block.name" class="block-icon" />
        <span class="block-title">{{ block.name }}</span>
      </div>
      
      <div class="module-grid">
        <div v-for="(module, idx) in block.moduleWidgets" 
             :key="`module-${idx}`"
             class="module-item"
             @click="navigateTo(module.url, module.id)">
          <div class="module-icon">
            <img :src="module.iconUrl" :alt="module.name" />
          </div>
          <span class="module-name">{{ module.name }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
// import {BlockList} from '@/mock/temp/BlockList'
import type { BlockType } from '@/api/model/blockModel'
import { defineProps } from 'vue';
import { useUserStore } from '@/store/User';
import { apiUrls } from '@/api/api';
import axios from 'axios';
const userStore = useUserStore();

// const blockList = blockList;
const props = defineProps({
  blockList: {
    type: Array as () => BlockType[], // 使用 Array 构造函数
    required: true
  }
});


function navigateTo(url: string, widget_id: number) {
  if (userStore.userId == 0) {
    userStore.isLoginandRegVisible = !userStore.isLoginandRegVisible;
    return;
  }
  const parser = new DOMParser();
  const doc = parser.parseFromString(url, "text/html");
  const aTag = doc.querySelector("a");
  const actualUrl = aTag ? aTag.href : url;
  console.log(widget_id);
  const data = {
    "widgetId": widget_id,
    "userId": userStore.userId
  }
  try {
    axios.post(apiUrls.ApplyUrl_API_URL, data);
  } catch (error) {
    console.error("Error sending data:", error);
  }


  window.open(actualUrl, "_blank");
}

</script>

<style scoped>
.block-list {
  width: 80%;
  margin: 0 auto;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 32px;
}

.block-item {
  background-color: rgba(255, 255, 255, 0.8);
  border-radius: 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  padding: 24px;
}

.block-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 24px;
  padding: 0 8px;
}

.block-icon {
  width: 14px;
  height: 14px;
}

.block-title {
  font-size: 16px;
  font-weight: 500;
  color: #333;
}

.module-grid {
  display: grid;
  grid-template-columns: repeat(8, 1fr);
  padding: 6px;
}

.module-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
  padding: 8px;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  background: transparent;
  border: 1px solid transparent;
}

.module-icon {
  width: 42px;
  height: 42px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.module-icon img {
  width: 24px;
  height: 24px;
  object-fit: contain;
}

.module-name {
  font-size: 12px;
  color: #333;
  text-align: center;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  width: 100%;
  font-weight: 500;
}

.module-item:hover {
  background-color: #fbfbfb;
  transform: translateY(-2px);
  border-color: #fbfbfb;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.module-item:hover .module-icon {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  background-color: #f8faff;
}

.module-item:hover .module-icon img {
  transform: scale(1.1);
}

.module-item:hover .module-name {
  color: rgba(0, 0, 0, 0.912);
  font-weight: bold;
}

@media (max-width: 1400px) {
  .module-grid {
    grid-template-columns: repeat(6, 1fr);
  }
}

@media (max-width: 1100px) {
  .module-grid {
    grid-template-columns: repeat(4, 1fr);
  }
}

@media (max-width: 768px) {
  .block-list {
    gap: 24px;
    padding: 16px;
  }
  
  .block-item {
    padding: 20px;
  }
  
  .module-grid {
    grid-template-columns: repeat(auto-fill, minmax(100px, 1fr));
  }
  
  .module-icon {
    width: 48px;
    height: 48px;
  }
  
  .module-icon img {
    width: 28px;
    height: 28px;
  }
  
  .module-name {
    font-size: 13px;
  }
}
</style>