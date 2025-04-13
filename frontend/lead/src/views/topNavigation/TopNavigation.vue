<template>
  <div class="research-hub ">
    <nav class="top-nav">
      <div class="nav-left">
        <TopTools :LeftNavigation="props.LeftNavigation" :blockList="props.blockList" :ApplyUrltext="props.ApplyUrltext"/>
      </div>
      <div class="nav-right">
        <DateTimeComponent></DateTimeComponent>
      </div>
    </nav>
    <div class="hero-section">
      <text1>{{props.TopNavigationtext1}}</text1>
      <text2>{{props.TopNavigationtext2}}</text2>
    </div>
    <ResearchHub :PlatformList="PlatformList"/>
  </div>
</template>
<script setup lang="ts">
import DateTimeComponent from './DateTimeComponent.vue';
import ResearchHub from './ResearchHub.vue';
import TopTools from './TopTools.vue';
// import {PageInfo} from '@/mock/temp/PageInfo'
import { defineProps } from 'vue';
import type { PropType } from 'vue';
import type { leftNavigationType } from '@/api/model/leftNavigationModel'
import type { BlockType } from '@/api/model/blockModel'
import type { PlatformType } from "@/api/model/platformModel";
const props = defineProps({
  ApplyUrltext: {
    type: String,
    required: true
  },
  TopNavigationtext1: {
    type: String,
    required: true
  },
  TopNavigationtext2: {
    type: String,
    required: true
  },
  blockList: {
    type: Array as () => BlockType[], // 使用 Array 构造函数
    required: true
  },
  LeftNavigation:{
    type: Object as PropType<leftNavigationType>,
    required: true
  },
  PlatformList:{
    type: Array as () => PlatformType[], // 使用 Array 构造函数
    required: true
  }
});


</script>

<style scoped>
.research-hub {
  font-family: Arial, sans-serif;
}

.top-nav {
  display: flex;
  justify-content: space-between;
  padding: 10px 20px;
}

.nav-left a {
  margin-right: 20px;
  text-decoration: none;
  color: #333;
}

.nav-right {
  display: flex;
  align-items: center;
}

.nav-right span {
  margin-left: 20px;
  color: #666;
}

.hero-section {
  margin-top: -30px;
  height: auto;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: #ffffff;
  text-align: center;
  background-size: cover;
  background-position: center;
  padding-bottom: 30px;
}

.hero-section text1 {
  font-size: 36px;
  /* 字体大小 */
  color: #ffffff;
  /* 字体颜色 */
  margin-bottom: 5px;
  /* 底部外边距 */
  font-family: 'Arial', sans-serif;
  /* 字体族 */
  text-shadow: 0.2px 0.2px 0 #000, -0.2px -0.2px 0 #000, 0.2px -0.2px 0 #000, -0.2px 0.2px 0 #000;
  /* 描边效果 */
  font-weight: bolder; 
}

.hero-section text2 {
  font-size: 14px;
  color: #ffffff;
  /* 字体颜色 */
  margin-bottom: 10px;
  /* 底部外边距 */
  font-family: 'Arial', sans-serif;
  /* 字体族 */
  /* 描边效果 */
  text-shadow: 0.2px 0.2px 0 #000, -0.2px -0.2px 0 #000, 0.2px -0.2px 0 #000, -0.2px 0.2px 0 #000;
  font-weight: bold;
}

.search-section {
  padding: 20px;
  text-align: center;
}

.search-buttons {
  margin-bottom: 20px;
}

.search-buttons button {
  margin: 5px;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  background-color: #f0f0f0;
  cursor: pointer;
}

.search-buttons button:hover {
  background-color: #e0e0e0;
}

.search-box input {
  width: 60%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  margin-right: 10px;
}

.search-box button {
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  background-color: #0078d4;
  color: #ffffff;
  cursor: pointer;
}

.search-box button:hover {
  background-color: #005a9e;
}
</style>