<template>
    <transition name="slide">
        <div class="navigation-container" v-if="props.isLeftNavigationVisible">
            <div class="navigation">
                <div v-for="(Target, index) in NavigationTarget" :key="`major-nav-${index}`" class="major-item"
                    :style="{ '--hover-color': Navigationcolors[index] }">
                    <div class="major-link" @click="scrollToTopPage(Target)">
                        <span class="major-name">{{ Target }}</span>
                    </div>
                </div>
            </div>
        </div>
    </transition>
</template>



<script setup lang="ts">
// import {LeftNavigation} from '@/mock/temp/LeftNavigation'
import { computed,defineProps } from 'vue';
import type { PropType } from 'vue';
import type { leftNavigationType } from '@/api/model/leftNavigationModel'
const props = defineProps({
  isLeftNavigationVisible: {
    type: Boolean,
    required: true
  },
  LeftNavigation:{
    type: Object as PropType<leftNavigationType>,
    required: true
  }
});

const NavigationTarget = computed(() => props.LeftNavigation.NavigationTarget);
const Navigationcolors = computed(() => props.LeftNavigation.Navigationcolors);


const scrollToTopPage = (TargetName: string) => {
    const targetElement = document.getElementById(`section-${TargetName}`);
    targetElement?.scrollIntoView({
        behavior: 'smooth',
        block: 'start',
    });
};
</script>


<style scoped>
/* 修改容器样式 */
.navigation-container {
    position: fixed;
    top: 50px;
    left: 0;
    z-index: 3;
}

/* 调整动画定义顺序 */
.slide-enter-active {
    transition: transform 0.3s ease-out;
}

.slide-leave-active {
    transition: transform 0.3s ease-in;
}

.slide-enter-from {
    transform: translateX(-100%);
}

.slide-enter-to {
    transform: translateX(0);
}

.slide-leave-from {
    transform: translateX(0);
}

.slide-leave-to {
    transform: translateX(-100%);
}


/* 调整导航栏定位方式 */
.navigation {
    position: relative;
    border-radius: 0 10px 10px 0;
    background-color: #ffffffe6;
    margin-top: 60px;
    font-size: 14px;
    box-shadow: 0 3px 12px 0 rgb(0 0 0 / 50%);
    display: flex;
    flex-direction: column;
    gap: 10px;
    overflow: hidden;
    z-index: 3;
}

.major-item {
    white-space: nowrap;
    padding: 10px 20px 10px 10px;
    box-sizing: border-box;
    position: relative;
    overflow: hidden;
    cursor: pointer;
    transition: color 0.3s;
    /* 文字颜色过渡 */
}

.major-item::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    width: 0;
    height: 100%;
    background: var(--hover-color);
    transition: width 0.3s ease-in-out;
    z-index: -1;
}

.major-item:hover {
    color: #fff;
    font-weight: bold;
}

.major-item:hover::before {
    width: 100%;
}

.major-name {
    display: inline-block;
}
</style>
