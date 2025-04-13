<template>
    <div class="feedback-overlay" v-if="isFeedbackVisible"></div>
    <div class="feedback-container" v-if="isFeedbackVisible && !submitted">
        <div class="feedback-header">
            <p>{{ props.FeedBacktext }}</p>
        </div>
        <div class="dropdown-group">
            <div class="dropdown-item">
                <label for="block-select">选择板块：</label>
                <select id="block-select" v-model="selectedBlockId">
                    <option disabled value="">请选择板块</option>
                    <option v-for="block in props.blockList" :key="block.id" :value="block.id">
                        {{ block.name }}
                    </option>
                </select>
            </div>

            <div class="dropdown-item">
                <label for="module-select">选择模块：</label>
                <select id="module-select" v-model="selectedModuleId" :disabled="!selectedBlockId">
                    <option disabled value="">请先选择板块</option>
                    <option v-for="module in selectedBlockModules" :key="module.id" :value="module.id">
                        {{ module.name }}
                    </option>
                </select>
            </div>
        </div>
        <div v-for="(dimension, index) in dimensions" :key="index" class="feedback-dimension">
            <div class="dimension-label">{{ dimension.label }}</div>
            <div class="rating-stars">
                <span v-for="star in 5" :key="star" @click="rateDimension(dimension.key, star)"
                    :class="['star', responses[dimension.key] >= star ? 'active' : '']">
                    ★
                </span>
            </div>
        </div>

        <button class="submit-button" @click="submitResponses" :disabled="!canSubmit">
            提交
        </button>

        <button class="close-button" @click="hideFeedback">关闭</button>
    </div>

    <div class="feedback-container" v-if="isFeedbackVisible && submitted">
        <div class="feedback-header">
            <img :src="feedbackresulticon" class="icon">
        </div>
        <p style="text-align: center;">感谢你的评价</p>
        <button class="close-button" @click="hideFeedback">关闭</button>
    </div>
    <img :src="feedbackbutton" class="feedback-toggle-icon" @click="showFeedback">
</template>

<script setup lang="ts">
import { ref, reactive, computed, watchEffect } from 'vue';
import axios from 'axios';
import { apiUrls } from '@/api/api';
import { ElMessage } from 'element-plus'
import type { BlockType } from '@/api/model/blockModel';
import feedbackresulticon from '@/assets/svg/feedbackresult.svg'
import feedbackbutton from '@/assets/svg/feedbackbutton.svg'
const selectedBlockId = ref<number | null>(null);
const selectedModuleId = ref<number | null>(null);
import { useUserStore } from '@/store/User';
const userStore = useUserStore();
const selectedBlockModules = computed(() => {
    const block = props.blockList.find(b => b.id === Number(selectedBlockId.value));
    return block ? block.moduleWidgets : [];
});

const submitted = ref(false);
const isFeedbackVisible = ref(false);

const props = defineProps({
    FeedBacktext: { type: String, required: true },
    FeedBackdim1: { type: String, required: true },
    FeedBackdim2: { type: String, required: true },
    FeedBackdim3: { type: String, required: true },
    blockList: { type: Array as () => BlockType[], required: true }
});

const dimensions = reactive<{ key: string; label: string }[]>([]);
const responses = reactive({ dimension1: 0, dimension2: 0, dimension3: 0 } as Record<string, number>);

watchEffect(() => {
    if (props.FeedBackdim1 && props.FeedBackdim2 && props.FeedBackdim3) {
        dimensions.splice(0, dimensions.length);
        dimensions.push(
            { key: 'dimension1', label: props.FeedBackdim1 },
            { key: 'dimension2', label: props.FeedBackdim2 },
            { key: 'dimension3', label: props.FeedBackdim3 }
        );
    }
});

const canSubmit = computed(() => Object.values(responses).every(score => score > 0));

const rateDimension = (dimensionKey: keyof typeof responses, rating: number) => {
    responses[dimensionKey] = rating;
};

const resetForm = () => {
    selectedBlockId.value = null;
    selectedModuleId.value = null;
    Object.keys(responses).forEach(key => responses[key] = 0);
    submitted.value = true;
};

const submitResponses = async () => {
    if (!canSubmit.value) {
        ElMessage.warning('请完成所有评分项！');
        return;
    }

    if (!selectedBlockId.value || !selectedModuleId.value) {
        ElMessage.warning('请选择板块和模块！');
        return;
    }

    const postData = {
        userId: userStore.userId,
        widgetId: selectedModuleId.value,
        ...responses,
    };
    const FeedBackresponse = await axios.post(apiUrls.FeedBack_API_URL, postData);

    if (FeedBackresponse.data.stateCode == 1) {
        ElMessage.success('提交成功');
        resetForm();
    } else {
        ElMessage.error('提交失败，请稍后再试！');
    }
};


const showFeedback = () => {
    if (userStore.userId){
        isFeedbackVisible.value = true;
    }
    else{
        userStore.isLoginandRegVisible = true;
    }
}
const hideFeedback = () => {
    isFeedbackVisible.value = false;
    Object.keys(responses).forEach(key => responses[key] = 0);
    submitted.value = false;
};
</script>

<style scoped>
.feedback-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: 4;
    background: rgba(0, 0, 0, 0.5);
}

.dropdown-group {
    margin: 20px 0;
}

.dropdown-item {
    margin-bottom: 15px;
}

.dropdown-item label {
    display: block;
    margin-bottom: 5px;
    color: #666;
    font-size: 14px;
}

.icon {
    width: 64px;
    height: 64px;
}

.dropdown-item select {
    width: 100%;
    padding: 8px;
    border: 1px solid #ddd;
    border-radius: 4px;
    background-color: #fff;
    font-size: 14px;
}

.dropdown-item select:disabled {
    background-color: #f5f5f5;
    cursor: not-allowed;
}

.feedback-container {
    width: 40vh;
    height: auto;
    margin: 0 auto;
    padding: 20px;
    border: 1px solid #ccc;
    border-radius: 8px;
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    z-index: 9999;
    background-color: white;
}

.feedback-header {
    text-align: center;
    margin-bottom: 20px;
    padding-top: 25px;
    padding-left: 10px;
    padding-right: 10px;
}

.feedback-dimension {
    display: flex;
}

.dimension-label {
    flex: 1;
    padding-right: 1rem;
}

.rating-stars {
    display: flex;
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
    margin: 10px auto;
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
    width: 48px;
    height: 48px;
    bottom: 20px;
    left: 30px;
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