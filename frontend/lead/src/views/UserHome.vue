<template>
  <div class="outer-container">
    <div class="userapp-container" :style="backgroundStyle">
      <div v-if="loading" class="loading-indicator"></div>
      <TopNavigation :PlatformList="PlatformList" :LeftNavigation="leftNavigation" :blockList="blockList"
        :ApplyUrltext="applyUrlText" :TopNavigationtext1="topNavigationtext1" :TopNavigationtext2="topNavigationtext2" />
      <block :blockList="blockList" />
      <FeedBack :FeedBacktext="feedBacktext" :FeedBackdim1="feedBackdim1" :FeedBackdim2="feedBackdim2"
        :FeedBackdim3="feedBackdim3" :blockList="blockList" />
    </div>
  </div>
</template>

<script setup lang="ts">
import block from './block/block.vue';
import TopNavigation from './topNavigation/TopNavigation.vue'
import FeedBack from './feedback/FeedBack.vue';
// import agent from './agent/agent.vue';
import {
  computed,
} from 'vue';
import { PlatformList } from '@/mock/temp/PlatformList';


// import { BlockList } from '@/mock/temp/BlockList';
// import { LeftNavigation } from '@/mock/temp/LeftNavigation';
// import { PageInfo } from '@/mock/temp/PageInfo';
// import Live2DWidget from '@/views/live2d/live2d.vue';
// import ElMessage from '@/views/topNavigation/ElMessage.vue'
// const loading = ref(false);
// const blockList = reactive(BlockList);
// const leftNavigation = reactive(LeftNavigation)
// const topNavigationtext1 = ref(PageInfo.topNavigationText1)
// const topNavigationtext2 = ref(PageInfo.topNavigationText2)
// const feedBacktext = ref(PageInfo.feedbackText)
// const feedBackdim1 = ref(PageInfo.dim1)
// const feedBackdim2 = ref(PageInfo.dim2)
// const feedBackdim3 = ref(PageInfo.dim3)
// const backgroundUrl = ref(PageInfo.backgroundUrl)
// const applyUrlText = ref(PageInfo.applyUrlText)

import { useUserStore } from '@/store/User';
const userStore = useUserStore();
const loading = computed(() => userStore.loading);
const blockList = computed(() => userStore.Blocks);
const leftNavigation = computed(() => userStore.LeftNavigation);
console.log(userStore.PageInfo)
const topNavigationtext1 = computed(() => userStore.PageInfo.topNavigationText1);
const topNavigationtext2 = computed(() => userStore.PageInfo.topNavigationText2);
const feedBacktext = computed(() => userStore.PageInfo.feedbackText);
const feedBackdim1 = computed(() => userStore.PageInfo.dim1);
const feedBackdim2 = computed(() => userStore.PageInfo.dim2);
const feedBackdim3 = computed(() => userStore.PageInfo.dim3);
const backgroundUrl = computed(() => userStore.PageInfo.backgroundUrl);
const applyUrlText = computed(() => userStore.PageInfo.applyUrlText);


const backgroundStyle = computed(() => ({
  backgroundImage: `linear-gradient(rgba(255, 255, 255, 0) 0%,rgba(255, 255, 255, 0) 20%, rgba(244, 248, 251, 0.8) 70%, rgba(244, 248, 251, 1) 100%), url(${backgroundUrl.value})`,
  backgroundSize: '100% 100vh',
  backgroundPosition: 'top center',
  backgroundRepeat: 'no-repeat',
  width: '100%',
  margin: '0',
  border: '0 solid transparent',
  borderradius: '10px',
}));


</script>

<style>
.outer-container {
  position: relative;
  width: 100%;
  min-height: 100vh;
  overflow-y: auto;
  scrollbar-width: none; /* Firefox */
  -ms-overflow-style: none; /* IE and Edge */
}

.outer-container::-webkit-scrollbar {
  display: none; /* Chrome, Safari, Opera */
}

.userapp-container {
  font-family: 'San Francisco', -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
  border-radius: 10px;
  border: 1px solid transparent;
  min-height: 100vh;
  position: relative;
  background-color: rgba(244, 248, 251, 100%);
  overflow: hidden;
  overflow-y: auto;
  scrollbar-width: none; /* Firefox */
  -ms-overflow-style: none; /* IE and Edge */
}


</style>