<template>

  <div class="userapp-container" :style="backgroundStyle">
    <div v-if="loading" class="loading-indicator"></div>
    <TopNavigation :Platform="Platform":LeftNavigation="leftNavigation":blockList="BlockList" 
    :TopNavigationtext1="topNavigationtext1" :TopNavigationtext2="topNavigationtext2" />
    <block :blockList="blockList"/>
    <FeedBack :FeedBacktext="feedBacktext":FeedBackdim1="feedBackdim1"
    :FeedBackdim2="feedBackdim2":FeedBackdim3="feedBackdim3" />
  </div>

</template>

<script setup lang="ts">
import block from './block/block.vue';
import TopNavigation from './topNavigation/TopNavigation.vue'
import FeedBack from './feedback/FeedBack.vue';
import { computed,ref,reactive } from 'vue';
import { BlockType } from '@/api/model/blockModel';
import {PlatformType} from '@/api/model/platformModel'
import type { leftNavigationType } from '@/api/model/leftNavigationModel'
import {UserHomePageInfoType} from '@/api/model/userHomePageInfoModel'
const props = defineProps({
  BlockList: {
    type: Array as PropType<BlockType[]>,
    required: true
  },
  PlatformList:{
    type: Array as PropType<PlatformType[]>,
    required: true
  },
  LeftNavigation:{
    type: Object as PropType<leftNavigationType>,
    required: true
  },
  PageInfo:{
    type: Object as PropType<UserHomePageInfoType>,
    required: true,
  },
});


const loading = ref(false);
const blockList = reactive(props.BlockList);
const Platform = reactive(props.PlatformList);
const leftNavigation = reactive(props.LeftNavigation)
const topNavigationtext1 = ref(props.PageInfo.TopNavigationtext1)
const topNavigationtext2 = ref(props.PageInfo.TopNavigationtext2)
const feedBacktext = ref(props.PageInfo.FeedBacktext)
const feedBackdim1 = ref(props.PageInfo.FeedBackdim1)
const feedBackdim2 = ref(props.PageInfo.FeedBackdim2)
const feedBackdim3 = ref(props.PageInfo.FeedBackdim3)
const BackGroundUrl = ref(props.PageInfo.BackGroundUrl);


const backgroundStyle = computed(() => ({
  backgroundImage: `linear-gradient(rgba(255, 255, 255, 0) 0%, rgb(244 248 251 / 0.6) 50%, rgb(244 248 251) 100%), url(${BackGroundUrl.value})`,
  backgroundSize: 'cover',
  backgroundPosition: 'center',
  backgroundRepeat: 'no-repeat',
  height: 'auto',
  width: 'auto',
  margin: '0'
}));

</script>

<style>
.userapp-container {
  font-family: 'San Francisco', -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
  border-radius: 10px;
  border: 1px solid transparent;
}
</style>