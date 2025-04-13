<template>
    <div class="container" :style="{ margin: '0px' }">
        <div v-if="isLoading">加载中...</div>
        <div class="list" v-if="!isLoading">
            <el-row justify="end" :gutter="10" class="operatebutton">
                <el-col :lg="6" :md="6" :sm="10" :xs="6" style="display: flex; justify-content: end">
                    <el-button @click="resetorder">重置顺序</el-button>
                    <el-button @click="saveorder">保存顺序</el-button>
                </el-col>
            </el-row>
            <div class="blockList-content">
                <el-row justify="start" :gutter="20">
                    <VueDraggable class="drag-box" v-model="blockList" :animation="150" ghost-class="ghost"
                        group="blocks" draggable=".el-col" @update="onUpdate">
                        <el-col v-for="block in blockList" :key="block.id" :xs="12" :sm="8" :md="6" :lg="6"
                            class="drag-item">
                            <div class="block-content">
                                <div class="top">
                                    <el-image class="cover" :src="block.imgUrl" lazy fit="cover"
                                        :style="{ 'object-fit': 'cover' }">
                                        <template #error>
                                            <div class="image-slot">
                                                <el-icon>
                                                    <Picture />
                                                </el-icon>
                                            </div>
                                        </template>
                                    </el-image>
                                    <div class="bottom">
                                        <h2>{{ block.name }}</h2>
                                        <div class="info">
                                            <div class="text">
                                                <i class="iconfont-sys">&#xe6f7;</i>
                                                <span>{{ block.introduction }}</span>
                                                <div class="line"></div>
                                                <i class="iconfont-sys">&#xe689;</i>
                                                <span>{{ block.isVisible ? '可见' : '不可见' }}</span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </el-col>
                    </VueDraggable>
                </el-row>

            </div>

        </div>
    </div>

</template>

<script setup lang="ts">
import { ref } from 'vue'
import EmojiText from '@/utils/emojo'
import { ElMessage } from 'element-plus'
import { VueDraggable } from 'vue-draggable-plus'
import { BlockList } from '@/mock/temp/blockList'
import { BlockType } from '@/api/model/blockModel'
import { GetAdminBlocks } from '@/api/blockApi'
import { PostUpdateBlockOrder } from '@/api/blockApi'
const originalblockList = ref<BlockType[]>(BlockList)
const blockList = ref<BlockType[]>(BlockList)
const isLoading = ref(true)


onMounted(() => {
    getBlockList()
})

const getBlockList = async () => {
    isLoading.value = true
    // 模拟API调用
    let filteredList = await GetAdminBlocks(1)
    originalblockList.value = filteredList
    blockList.value = filteredList
    originalblockList.value = filteredList
    isLoading.value = false
}
const saveorder = async () => {
    const blockDisplayOrderList = blockList.value.map(block => ({
        id: block.id,
        displayOrder: block.displayOrder
    }));
    try {
        const response = await PostUpdateBlockOrder(blockDisplayOrderList)
        switch (response) {
            case 0:
                ElMessage.error(`顺序同步失败 ${EmojiText[500]}`)
                break
            case 1:
                originalblockList.value = blockList.value
                ElMessage.success(`顺序已同步到云端 ${EmojiText[200]}`)
                break
        }

    }
    catch {
        ElMessage.error(`顺序同步失败 ${EmojiText[500]}`)
    }
}
const resetorder = () => {
    blockList.value = originalblockList.value
    ElMessage.success(`顺序已重置 ${EmojiText[200]}`)
}

function onUpdate(): void {
    console.log('onUpdate:')
    blockList.value.forEach((item, index) => {
        item.displayOrder = index + 1;
    });
    blockList.value.sort((a, b) => {
        if (a.isVisible && !b.isVisible) return -1;
        if (!a.isVisible && b.isVisible) return 1;
        return 0;
    });
    console.log(blockList.value)
}

</script>

<style lang="scss" scoped>
.container {
    display: flex;
    flex-direction: column;
    margin: 0px !important;


    .list {
        width: calc(100% + 40px);
        padding-top: 20px;
        border: 1px solid var(--art-border-color);
        border-radius: calc(var(--custom-radius) / 2 + 2px) !important;

        .operatebutton {
            padding-bottom: 20px;
        }

        .blockList-content {
            display: flex;
            padding: 0;

            .drag-box {
                margin-left: 20px;
                margin-right: 20px;
                padding: 0;
                display: flex;
                flex-wrap: wrap;
                justify-content: flex-start;
                width: calc(100%);

                // border: 1px solid var(--art-border-color);
                // border-radius: calc(var(--custom-radius) / 2 + 2px) !important;

                .drag-item {
                    box-sizing: border-box;
                    cursor: pointer;
                    padding-bottom: 20px;

                    &:hover {
                        .el-button {
                            opacity: 1 !important;
                        }
                    }

                    .top {
                        position: relative;
                        width: 100%;
                        height: auto;
                        border: 1px solid var(--art-border-color);
                        border-radius: calc(var(--custom-radius) / 2 + 2px) !important;

                        .cover {
                            display: flex;
                            align-items: center;
                            justify-content: center;
                            width: 100%;
                            height: auto;
                            aspect-ratio: 16/9.5;
                            object-fit: cover;
                            background: var(--art-gray-200);
                            border-radius: calc(var(--custom-radius) / 2 + 2px) calc(var(--custom-radius) / 2 + 2px) 0 0;

                            .image-slot {
                                font-size: 26px;
                                color: var(--art-gray-400);
                            }
                        }

                    }

                    .bottom {
                        padding: 5px 10px;

                        h2 {
                            font-size: 16px;
                            font-weight: 500;
                            color: #333;

                            @include ellipsis();
                        }

                        .info {
                            display: flex;
                            justify-content: space-between;
                            width: 100%;
                            height: 25px;
                            margin-top: 6px;
                            line-height: 25px;

                            .text {
                                display: flex;
                                align-items: center;
                                color: var(--art-text-gray-600);

                                i {
                                    margin-right: 5px;
                                    font-size: 14px;
                                }

                                span {
                                    font-size: 13px;
                                    color: var(--art-gray-600);
                                }

                                .line {
                                    width: 1px;
                                    height: 12px;
                                    margin: 0 15px;
                                    background-color: var(--art-border-dashed-color);
                                }
                            }

                            .el-button {
                                opacity: 0;
                                transition: all 0.3s;
                            }
                        }
                    }
                }
            }
        }
    }
}


.ghost {
    opacity: 0.5;
    background-color: #f0f0f0 !important;
}

@media (max-width: 768px) {
    .container {
        flex-direction: column;
    }

    .preview-container {
        flex-direction: column;
    }
}
</style>