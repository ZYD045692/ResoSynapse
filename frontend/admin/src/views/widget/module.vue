<template>
    <div class="page-content">
        <div class="list">
            <div v-for="block in blockList" :key="block.id" class="block-group">
                <div class="block-header">
                    <img :src="block.iconUrl">
                    <h2>{{ block.name }}</h2>
                </div>
                <el-row class="offset">
                    <VueDraggable class="drag-box" v-model="block.moduleList" :animation="150" ghostClass="ghost"
                        group="people" @add="onAdd" @remove="onRemove" @update="onUpdate">
                        <el-col v-for="module in block.moduleList" class="module-container" @click="toDetail(module)"
                            :key="block.id" :lg="3" :md="3" :sm="6" :xs="8"
                            style="display: flex; justify-content: start">
                            <div class="icon-center" :class="{ 'grayed-out': !module.isVisible }">
                                <img :src="module.iconUrl" :alt="module.name" />
                                <div class="icon-center-text">{{ module.name }}</div>
                            </div>
                        </el-col>
                    </VueDraggable>
                </el-row>
            </div>
        </div>
    </div>
</template>

<script lang="ts" setup>
import { BlockType } from '@/api/model/blockModel';
import { ModuleType } from '@/api/model/moduleModel';
import { ElMessage } from 'element-plus';
import { VueDraggable } from 'vue-draggable-plus';
import { router } from '@/router';
import { BlockList } from '@/mock/temp/blockList';
import { GetAdminBlocks } from '@/api/blockApi'
import {apiUrls} from '@/api/api'
import axios from 'axios';
const blockList = ref<BlockType[]>([]);
const last_blockList = ref<BlockType[]>([]);
const isLoading = ref(false)
onMounted(async () => {
    isLoading.value = true
    try {
        const Blocks = await GetAdminBlocks(1)
        blockList.value = Blocks
        last_blockList.value = Blocks
        console.log(last_blockList)
    } catch (error) {
        console.error('获取板块列表失败:', error)
    } finally {
        isLoading.value = false
    }
})

const toDetail = (item: ModuleType) => {
    router.push({
        path: `/widget/module/detail`,
        query: {
            id: item.id,
            // fatherId:item.fatherId
        }
    });
};

function printModuleIdsAndDisorder(blockList: BlockType[]) {
    blockList.forEach((block, blockIdx) => {
        console.log(`Block ${blockIdx + 1}:`);
        block.moduleList.forEach((module, moduleIdx) => {
            console.log(`Module ${moduleIdx + 1} Id: ${module.id} Disorder: ${module.displayOrder}`);
        });
    });
}
function findBlocksWithChangedModules(): { added: number | null, removed: number | null } {
    let blockWithAddedModule = null;
    let blockWithRemovedModule = null;

    for (let i = 0; i < blockList.value.length; i++) {
        const currentBlock = blockList.value[i];
        const lastBlock = last_blockList.value[i];

        if (currentBlock.moduleList.length > lastBlock.moduleList.length) {
            console.log(`Block ${currentBlock.name} (id: ${currentBlock.id}) 的 moduleList 多了一个元素`);
            blockWithAddedModule = currentBlock.id;
        } else if (currentBlock.moduleList.length < lastBlock.moduleList.length) {
            console.log(`Block ${currentBlock.name} (id: ${currentBlock.id}) 的 moduleList 少了一个元素`);
            blockWithRemovedModule = currentBlock.id;
        }
    }
    return {
        added: blockWithAddedModule,
        removed: blockWithRemovedModule
    };
}

// 拖拽添加事件（跨组）
const onAdd = (event: any) => {
    // const toBlock = blockList.value.find(block => block.moduleList === event.list);
    // const fromBlock = blockList.value.find(block => block.moduleList === event.from.list);

    // if (toBlock && fromBlock) {
    //     const item = event.item;
    //     const clonedItem = { ...item };

    //     toBlock.moduleList.splice(event.newIndex, 0, clonedItem);

    //     // 如果是跨组拖拽，需要从原 block 移除
    //     if (toBlock !== fromBlock) {
    //         fromBlock.moduleList.splice(event.oldIndex, 1);
    //     }
    // }
    console.log('onAdd')
    // console.log(event)

    // printModuleIdsAndDisorder(blockList.value)
};

// 拖拽移除事件（跨组）
const onRemove = (event: any) => {
    console.log('onRemove')
    // console.log(event)
    // printModuleIdsAndDisorder(blockList.value)
    // const ChangeBlock = findBlocksWithChangedModules()
    // if (ChangeBlock.added !== null && ChangeBlock.removed !== null) {
    //     //
    // }
    const changedModules: Array<{ 
        id: number; 
        displayOrder: number; 
        fatherId: number 
    }> = [];
    blockList.value.forEach((block) => {
        block.moduleList.forEach((module, index) => {
            if (module.displayOrder !== index + 1) {
                changedModules.push({
                    id: module.id,
                    displayOrder: index + 1,
                    fatherId: block.id
                });
                module.displayOrder = index + 1;
            }
        });
    });
    console.log(changedModules)
    console.log(blockList.value)
    last_blockList.value = blockList.value
    const response = axios.post(apiUrls.PostUpdateModuleOrder_API_URL,changedModules)
};

// 拖拽更新事件（组内）
const onUpdate = (event: any) => {
    const changedModules: Array<{ 
        id: number; 
        displayOrder: number; 
        fatherId: number 
    }> = [];
    blockList.value.forEach((block) => {
        block.moduleList.forEach((module, index) => {
            if (module.displayOrder !== index + 1) {
                changedModules.push({
                    id: module.id,
                    displayOrder: index + 1,
                    fatherId: block.id
                });
                module.displayOrder = index + 1;
            }
        });
    });
    console.log('onUpdate', changedModules);
    const response = axios.post(apiUrls.PostUpdateModuleOrder_API_URL,changedModules)
};
</script>

<style lang="scss" scoped>
.page-content {
    width: 100%;
    height: 100%;
    border-style: none;
    $border-color: #eee;

    .block-group {
        margin-bottom: 20px;
        overflow: hidden;
        color: rgba(#fff, 0.8);
        text-align: center;
        border: 1px solid rgb(var(--art-gray-300-rgb), 0.8);
        border-radius: 12px !important;
        padding: 20px;

        .block-header {
            margin-left: auto;
            display: flex;
            align-items: center;
            justify-content: flex-start;
            gap: 20px;

            h2 {
                font-size: 18px;
            }

            img {
                max-width: 24px;
                max-height: 24px;
                transition: color 0.3s ease;
            }
        }

        .offset {
            display: flex;
            flex-wrap: wrap;
            margin: 20px 10px;

            .drag-box {
                display: flex;
                flex-wrap: wrap;
                width: 100%;
                height: auto;
                justify-content: flex-start;
            }

            .module-container {
                width: auto;
                height: auto;
                aspect-ratio: 1 / 1;

                .icon-center {
                    position: relative;
                    display: flex;
                    flex-direction: column;
                    justify-content: space-between;
                    align-items: center;
                    width: auto;
                    height: auto;
                    aspect-ratio: 1 / 1;
                    border: 1px solid rgb(var(--art-gray-300-rgb), 0.8);
                    border-radius: 12px !important;
                    padding: 6px;
                    box-sizing: border-box;
                    overflow: hidden;
                    color: rgba(#fff, 0.8);
                    text-align: center;
                    cursor: pointer;
                    margin-bottom: 1.4286%;
                    margin: 10px;


                    &:hover {
                        cursor: pointer;
                        background: var(--art-gray-100);
                        font-weight: bold;
                        border: 2px solid rgb(var(--art-gray-300-rgb), 0.8);
                    }

                    i,
                    img {
                        font-size: 26px;
                        width: 50%;
                        height: 50%;
                        transition: color 0.3s ease;
                        margin: auto;
                        border: 1px solid rgb(var(--art-gray-300-rgb), 0.8);
                        border-radius: 12px !important;
                    }

                    .icon-center-text {
                        font-size: 10px;
                        position: absolute;
                        bottom: 8px;
                        color: var(--art-text-gray-600);
                    }

                    &.grayed-out {
                        opacity: 0.5;
                        background: var(--art-gray-100);
                        font-weight: bold;
                        border: 2px solid rgb(var(--art-gray-300-rgb), 0.8);

                        img {
                            filter: grayscale(100%);
                        }

                        .icon-center-text {
                            color: #999 !important; // 覆盖默认颜色
                        }

                        // 禁用 hover 效果
                        &:hover {
                            border: 1px solid rgb(var(--art-gray-300-rgb), 0.8); // 恢复默认边框
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
</style>