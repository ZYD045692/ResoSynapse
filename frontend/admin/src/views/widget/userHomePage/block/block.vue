<template>
    <div class="module_majors-container">
      <!-- 遍历所有的block -->
      <div v-for="(block, index) in props.blockList" :key="`block-${index}`" class="top-page" :ref="`scrollTarget-${block.id}`">
        <!-- 显示当前block的图标加名称 -->
        <div :id="`section-${block.name}`" class="module_major-container">
          <div class="module_major-info">
            <img :src="block.iconUrl" :alt="block.name" class="module_major-icon" />
            <span class="module_major-name">{{ block.name }}</span>
          </div>
          <!-- 直接遍历当前block中的所有 module -->
          <div class="module_minor-grid">
            <!-- 遍历 module -->
            <div class="module_minor-item" v-for="(module, idx) in block.moduleList" :key="`module-${idx}`">
              <!-- 包裹图标和名称 -->
              <div class="module_minor-container" @click="navigateTominor(module.url,module.id)">
                <!-- 显示module的图标 -->
                <img :src="module.iconUrl" :alt="module.iconUrl" />
                <!-- 显示module的名称 -->
                <p class="module_minor-name">{{ module.name }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script setup lang="ts">
  // import { navigateTo } from '@/utils/scripts/navigateTo';
  // import {BlockList} from '@/mock/temp/BlockList'
  import type { BlockType } from '@/api/model/blockModel'
  import {defineProps } from 'vue';

  // const blockList = blockList;
  const props = defineProps({
  blockList: {
    type: Array as () => BlockType[], // 使用 Array 构造函数
    required: true
  }
  });

  const navigateTominor = (url:string,widget_id:number) => {
    // navigateTo(url,widget_id);
  };
  
  </script>
  
  <style scoped>
  .module_major-container {
    background: #ffffffe6;
    padding: 10px;
    max-width: 100%;
    border-radius: 8px;
    margin-bottom: 10px;
    border-radius: 20px;
    overflow: hidden;
  }
  
  .module_major-info {
    display: flex;
    align-items: center;
    gap: 10px;
  }
  
  .module_major-icon {
    width: 24px;
    height: 24px;
  }
  
  .module_major-name {
    font-size: 16px;
    font-weight: bold;
  }
  
  .module_majors-container {
    max-width: 80%;
    /* min-width: 600px; */
    margin: auto;
    padding: 20px;
  }
  
  .top-page {
    margin-bottom: 40px;
  }
  
  .module_minor-grid {
    display: flex;
    flex-wrap: wrap;
    padding: 0 15px;
    width: 100%;
  }
  
  .module_minor-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    width: 12.5%;
    cursor: pointer;
    padding-top: 10px;
    border-radius: 10px;
    text-align: center;
  }
  
  .module_minor-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 0px;
  }
  
  .module_minor-item img {
    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, "Noto Sans", sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol", "Noto Color Emoji";
    line-height: 1.5;
    list-style: none;
    font-size: 14px;
    color: #555;
    box-sizing: border-box;
    display: block;
    width: 48px;
    height: 48px;
    padding: 8px;
    background: #fff;
    border: 2px solid #eee;
    border-radius: 10px;
    margin: 0 auto;
  }
  
  .module_minor-name {
    font-size: 16px;
    color: #333;
    width: 100%;
    text-align: center;
    margin-bottom: 5px;
  }
  
  /* 修改鼠标悬停时的效果 */
  .module_minor-item:hover {
    background-color: #eeeeee;
    box-shadow: 0 2px 2px rgba(0, 0, 0, 0.1);
  }
  
  .module_minor-item:hover .module_minor-name {
    font-weight: bold;
  }
  
  .module_minor-item:hover .module_minor-content img {
    filter: brightness(0.8);
    transition: filter 0.3s ease;
  }
  </style>
  