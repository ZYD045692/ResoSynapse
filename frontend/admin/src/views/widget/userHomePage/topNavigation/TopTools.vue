<template>
  <LeftNavigation :LeftNavigation="props.LeftNavigation" :isLeftNavigationVisible="isLeftNavigationVisible"/>
  <LoginReg :isLoginandRegVisible="isLoginandRegVisible" v-model="isLoginandRegVisible"/>
  <ApplyUrl :blockList="props.blockList":ApplyUrltext="ApplyUrltext" :isApplyVisible="isApplyVisible"  v-model="isApplyVisible"/>
  <div class="LeftNavigation-button" @click="toggleBackdrop('isLeftNavigationVisible')">
    <svg t="1740581004086" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
      p-id="12171" width="28" height="28">
      <path
        d="M982.109091 44.218182c-16.290909-11.636364-34.909091-11.636364-51.2-4.654546l-30.254546 16.290909-607.418181 321.163637v53.527273l558.545454-295.563637 53.527273-27.927273 46.545454-25.6h2.327273v2.327273l-137.309091 681.890909c-2.327273 6.981818-6.981818 13.963636-13.963636 18.618182-6.981818 4.654545-13.963636 4.654545-23.272727 2.327273l-11.636364-4.654546-44.218182-13.963636-260.654545-79.127273v-4.654545l363.054545-418.909091 23.272727-25.6-13.963636-13.963636-20.945454-18.618182-134.981819 118.690909L418.909091 556.218182l-128 114.036363h-2.327273l-13.963636-4.654545-48.872727-18.618182-137.309091-55.854545c-9.309091-4.654545-16.290909-13.963636-18.618182-23.272728 0-11.636364 4.654545-20.945455 13.963636-25.6l160.581818-86.10909v-53.527273l-183.854545 97.745454c-4.654545 2.327273-9.309091 6.981818-13.963636 11.636364-16.290909 13.963636-23.272727 34.909091-23.272728 58.181818 0 13.963636 4.654545 27.927273 13.963637 39.563637 6.981818 9.309091 18.618182 18.618182 32.581818 25.6l162.909091 65.163636 37.236363 13.963636c16.290909 6.981818 37.236364 2.327273 51.2-9.309091l4.654546-4.654545 125.672727-111.709091 134.981818-118.690909-158.254545 181.527273c-11.636364 11.636364-13.963636 30.254545-9.309091 46.545454 0 2.327273 0 2.327273 2.327273 4.654546 4.654545 13.963636 16.290909 25.6 30.254545 30.254545l288.581818 88.436364 30.254546 9.309091c6.981818 2.327273 13.963636 2.327273 20.945454 2.327272 13.963636 0 25.6-4.654545 37.236364-11.636363 18.618182-11.636364 30.254545-27.927273 34.909091-48.872728L1000.727273 93.090909c2.327273-18.618182-4.654545-37.236364-18.618182-48.872727z m-148.945455 181.527273l-9.309091 39.563636 23.272728-25.6-13.963637-13.963636z"
        fill="#FFFFFF" p-id="12172"></path>
      <path
        d="M954.181818 83.781818l-137.309091 681.890909c-2.327273 6.981818-6.981818 13.963636-13.963636 18.618182-6.981818 4.654545-13.963636 4.654545-23.272727 2.327273l-11.636364-4.654546c0-2.327273 2.327273-4.654545 2.327273-6.981818l134.981818-667.927273 46.545454-25.6s0-2.327273 2.327273 2.327273c0-2.327273 0-2.327273 0 0z"
        fill="#000000" p-id="12173"></path>
      <path
        d="M444.509091 991.418182c-4.654545 0-11.636364 0-16.290909-2.327273-20.945455-6.981818-32.581818-25.6-32.581818-46.545454v-107.054546c0-16.290909 9.309091-32.581818 23.272727-41.890909 13.963636-9.309091 32.581818-9.309091 46.545454-2.327273l60.509091 30.254546c13.963636 6.981818 23.272727 18.618182 25.6 32.581818 2.327273 13.963636 0 30.254545-9.309091 41.890909l-60.50909 76.8c-9.309091 11.636364-23.272727 18.618182-37.236364 18.618182z m0-158.254546H442.181818v107.054546s0 2.327273 2.327273 2.327273h2.327273l60.509091-76.8 9.30909-23.272728-9.30909 20.945455-62.836364-30.254546z"
        fill="#FFFFFF" p-id="12174"></path>
    </svg>
  </div>
  <div class="button-container">
    <div class="button-container LoginandReg" @click="toggleBackdrop('isLoginandRegVisible')">
      登录
    </div>
    <div class="button-container apply" @click="toggleBackdrop('isApplyVisible')">
      申请收录
    </div>
  </div>
</template>

<script setup lang="ts">
import ApplyUrl from './ApplyUrl.vue';
import LoginReg from './LoginReg.vue';
import LeftNavigation from './LeftNavigation.vue';
import type { BlockType } from '@/api/model/blockModel'
import type { PropType } from 'vue';
import type { leftNavigationType } from '@/api/model/leftNavigationModel'
import {ref,defineProps} from 'vue'

const props = defineProps({
  blockList: {
    type: Array as () => BlockType[],
    required: true
  },
  LeftNavigation:{
    type: Object as PropType<leftNavigationType>,
    required: true
  }
});
const isLeftNavigationVisible  = ref(false);
const isLoginandRegVisible  = ref(false);
const isApplyVisible  = ref(false);
const ApplyUrltext = ref('这是一个字符串');

const toggleBackdrop = (stateKey:string) => {
  if (stateKey === 'isLeftNavigationVisible') {
    isLeftNavigationVisible.value = !isLeftNavigationVisible.value;
  } else if (stateKey === 'isLoginandRegVisible') {
    isLoginandRegVisible.value = !isLoginandRegVisible.value;
  } else if (stateKey === 'isApplyVisible') {
    isApplyVisible.value = !isApplyVisible.value;
  }
};
</script>

<style>
.LeftNavigation-button {
  /* position: fixed; */
  left: 25px;
  top: 35px;
  background-color: transparent;
  border: 1px solid transparent;
  cursor: pointer;
  transition: all 0.3s ease;
  z-index: 4;
}

.LeftNavigation-button:hover {
  transform: scale(1.2);
}

.LeftNavigation-button:active {
  transform: scale(1.2);
}

.button-container {
  position: relative;
  left: 30px;
  margin-top: 15px;
  display: flex;
  gap: 15px;
  z-index: 4;
}

.button-container.LoginandReg {
  background: transparent;
  color: white;
  border: none;
  cursor: pointer;
  font-size: 16px;
  font-weight: bold;

}

.button-container.apply {
  background: transparent;
  color: white;
  border: none;
  cursor: pointer;
  font-size: 16px;
  font-weight: bold;
}
</style>
