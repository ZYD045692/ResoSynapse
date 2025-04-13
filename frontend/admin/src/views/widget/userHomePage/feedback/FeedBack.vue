<template>
    <div class="feedback-overlay" v-if="isFeedbackVisible"></div>
    <div class="feedback-container" v-if="isFeedbackVisible">
        <div class="feedback-header">
            <p>{{props.FeedBacktext}}</p>
        </div>

        <div v-for="(dimension, index) in dimensions" :key="index" class="feedback-dimension">
            <div class="dimension-label">{{ dimension.label }}</div>
            <div class="rating-stars">
                <span v-for="star in 5" :key="star" @click="rateDimension(dimension.key, star)" :data-rating="star"
                    :class="[
                        'star',
                        responses[dimension.key] >= star ? 'active' : ''
                    ]">
                    ★
                </span>
            </div>
        </div>

        <button class="submit-button" @click="submitResponses" :disabled="!canSubmit">
            提交
        </button>

        <div v-if="submitted" class="result-message">
            提交成功！我们会尽快处理您的反馈～
        </div>

        <button class="close-button" @click="hideFeedback">关闭</button>
    </div>
    <svg
        t="1740833269551"
        class="feedback-toggle-icon"
        viewBox="0 0 1024 1024"
        version="1.1"
        xmlns="http://www.w3.org/2000/svg"
        p-id="21694"
        width="48"
        height="48"
        @click="showFeedback"
        v-if="!isFeedbackVisible"
    >
        <path
            d="M92.749604 497.963836l52.475627 0.597284c26.451129 0.511957 40.956587 16.211982 41.12724 43.516374 0.170652 69.967503 0 139.935005 0 209.902508v207.172069c0 33.447879-13.908174 47.526706-46.75877 47.612032-32.594617 0-65.189234 0.170652-97.783852 0-25.939172-0.255979-41.553871-15.102741-41.639197-41.041913C0 823.739355 0 681.841847 0.170652 539.944338c0-25.256562 14.932089-40.785935 40.103325-41.383218 34.983751-0.853262 69.967503-0.767936 104.951254 0z m494.380135-302.054828c19.369053 17.918507 29.949504 40.359303 34.55712 66.213148 11.689693 66.042496 10.239147 131.573036-7.850012 196.420965-0.511957 1.791851-0.426631 3.754354-0.853262 8.020665 64.335972 0 127.136072-0.341305 189.936172 0.341305 13.652196 0.085326 27.987001 1.877177 40.956587 5.972836 43.6017 14.078827 72.527289 64.335972 62.885426 110.326806-23.208733 109.132239-47.953337 217.923173-70.820765 327.140738-6.484793 30.376135-14.846763 58.448463-41.383218 77.646863-14.505458 10.495125-29.693526 18.771769-48.635947 18.771769H245.142238c-2.303808 0-4.692942-0.341305-8.788601-0.68261-0.341305-5.119573-0.853262-10.068494-0.853262-15.102741 0.170652-160.242646 0.426631-320.485293 0.426631-480.642613 0-10.580452 2.559787-14.505458 13.737522-16.211983 92.83493-15.017415 169.969836-100.172986 177.222565-193.946504 1.962503-25.768519 2.133156-51.451712 13.054912-75.940338 28.498958-63.824015 105.804516-66.895759 147.187734-28.413633zM829.968169 11.433714c9.471211-15.35872 20.222315-15.188068 29.864178 0.170652 15.614699 24.915257 30.71744 50.342471 46.844097 74.831098 3.413049 5.290226 10.239147 10.239147 16.297308 11.945671 27.987001 7.508708 56.315307 13.652196 84.472961 20.648946 18.345138 4.607616 21.331556 14.761437 9.044579 29.522873-18.345138 21.843513-37.287559 43.345721-55.035413 65.701191a33.021248 33.021248 0 0 0-6.911424 20.222315c1.279893 28.157654 3.925006 56.144655 6.228814 84.216982 1.791851 22.099492-6.484793 28.157654-27.133739 19.625031-26.451129-10.751104-52.731606-22.014165-79.353387-32.167986a28.754937 28.754937 0 0 0-18.771769 0.085327c-26.707108 10.239147-52.902258 21.331556-79.353387 32.167986-20.478293 8.276644-28.157654 2.901092-27.304392-18.686443 2.303808-27.133739 4.266311-54.352804 6.996751-81.486543 1.023915-9.897842-0.341305-17.662528-7.508708-25.597867-19.1984-21.075577-37.372886-43.175069-55.80335-64.933255-12.116324-14.249479-9.129906-23.891342 8.959254-28.413633 28.157654-6.99675 56.315307-13.737522 84.558286-20.136988a26.451129 26.451129 0 0 0 18.686443-13.481543c14.505458-25.000583 29.864178-49.659862 45.222898-74.233814z"
            fill="#7653D6"
            p-id="21695"
        />
    </svg>
</template>

<script setup lang="ts">
import { ref, reactive, computed } from 'vue';
import axios from 'axios';
// import {FeedBack_API_URL} from '@/api/api';
import {defineProps } from 'vue';

  // const blockList = blockList;
  const props = defineProps({
    'FeedBacktext':{
        type: String,
        required: true
    },
    'FeedBackdim1':{
        type: String,
        required: true
    },
    'FeedBackdim2':{
        type: String,
        required: true
    },
    'FeedBackdim3':{
        type: String,
        required: true
    },
  });

// 定义响应式数据
const dimensions = [
    { key: 'dim1', label: props.FeedBackdim1 },
    { key: 'dim2', label: props.FeedBackdim2 },
    { key: 'dim3', label: props.FeedBackdim3 }
];
const responses = reactive({
  dim1: 0,
  dim2: 0,
  dim3: 0,
}) as Record<string, number>;
const submitted = ref(false);
const isFeedbackVisible = ref(false); // 控制反馈页面的显示状态

// 定义计算属性
const canSubmit = computed(() => {
    return Object.values(responses).every(score => score > 0);
});

// 定义方法
const rateDimension = (dimensionKey: keyof typeof responses, rating: number) => {
  responses[dimensionKey] = rating;
};

const submitResponses = async () => {
    if (!canSubmit.value) {
        alert('请完成所有评分项！');
        return;
    }
    try {
        // await axios.post(FeedBack_API_URL, responses);
        submitted.value = true;
        for (const key in responses) {
                responses[key] = 0;
            }
    } catch (error) {
        alert('抱歉，提交失败，请稍后再试！');
    }
};

const showFeedback = () => {
    isFeedbackVisible.value = true;
};

const hideFeedback = () => {
    isFeedbackVisible.value = false;
};
</script>

<style scoped>
.feedback-overlay {
    /* position: fixed; */
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: 4;
    background: rgba(0, 0, 0, 0.5);
}

.feedback-container {
    z-index: 4;
}
.feedback-container {
    width: 60vh;
    height: auto;
    margin: 0 auto;
    padding: 20px;
    border: 1px solid #ccc;
    border-radius: 8px;
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    z-index: 4;
    background-color: white;
}

.feedback-header {
    text-align: center;
    margin-bottom: 20px;
}

.feedback-dimension {
    display: flex;
}

.dimension-label {
    flex: 1; /* 让标签占据部分宽度 */
    padding-right: 1rem; /* 可选：控制标签和星星之间的间距 */
}

.rating-stars {
    display: flex; /* 如果需要星星横向排列 */
}

.star {
    font-size: 24px;
    cursor: pointer;
    color: #ccc;
}

.star.active {
    color: #ffd700;
}

.submit-button {
    display: block;
    margin: 20px auto;
    padding: 10px 20px;
    font-size: 16px;
    background-color: #4CAF50;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    z-index: 3;
}

.submit-button:disabled {
    background-color: gray;
    cursor: not-allowed;
}

.result-message {
    text-align: center;
    font-size: 18px;
    color: green;
}

.close-button {
    position: absolute;
    top: 10px;
    right: 10px;
    padding: 5px 10px;
    font-size: 14px;
    background-color: #f44336;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
}

.feedback-toggle-icon {
    cursor: pointer;
    position: fixed;
    bottom: 20px;
    left: 30px; /* 调整为左下角 */
    background: white;
    border-radius: 50%;
    padding: 5px;
    transition: all 0.3s ease;
}
.feedback-toggle-icon:hover {
    transform: scale(1.1);
    background: #f0f0f0;
}
</style>