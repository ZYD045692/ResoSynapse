<template>
  <div id="app">
    <router-link to="/"></router-link>
    <router-view />
    <agent/>
  </div>

</template>

<script setup lang="ts">
import { useUserStore } from '@/store/User';
import { onMounted} from 'vue';

const userStore = useUserStore();
onMounted(() => { // 页面挂载时调用 LoadHomePageInfo
  userStore.LoadHomePageInfo();
});
</script>
