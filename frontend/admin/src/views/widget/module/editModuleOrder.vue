<template>
    <div class="page-content">
        <div class="list">
            <div v-for="block in blockList" :key="block.id" class="block-group">
                <div class="block-header">
                    <img :src="block.iconUrl">
                    <h2>{{ block.name }}</h2>
                </div>
                <div class="offset">
                    <VueDraggable class="drag-box" v-model="block.moduleList" :animation="150" ghostClass="ghost"
                        group="people" @add="onAdd" @remove="onRemove" @update="onUpdate">
                        <div v-for="module in block.moduleList" :key="module.id" class="icon-center"
                            @click="toDetail(module)">
                            <img :src="module.iconUrl" :alt="module.name" />
                            <div class="icon-center-text">{{ module.name }}</div>
                        </div>
                    </VueDraggable>
                </div>
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

const blockList = ref<BlockType[]>(BlockList);
const last_blockList = ref<BlockType[]>(BlockList);
const toDetail = (item: ModuleType) => {
    router.push({
        path: `/widget/module/detail`,
        query: {
            id: item.id
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

// 拖拽添加事件（跨组或组内）
const onAdd = (event: any) => {
    console.log(event)
    const toBlock = blockList.value.find(block => block.moduleList === event.list);
    const fromBlock = blockList.value.find(block => block.moduleList === event.from.list);

    if (toBlock && fromBlock) {
        const item = event.item;
        const clonedItem = { ...item };

        // 添加到目标 block
        toBlock.moduleList.splice(event.newIndex, 0, clonedItem);

        // 如果是跨组拖拽，需要从原 block 移除
        if (toBlock !== fromBlock) {
            fromBlock.moduleList.splice(event.oldIndex, 1);
        }
    }
    console.log('onAdd')
    // printModuleIdsAndDisorder(blockList.value)
};

// 拖拽移除事件（跨组）
const onRemove = (event: any) => {
    console.log('onRemove', event.clonedData.id)
    // printModuleIdsAndDisorder(blockList.value)
    const ChangeBlock = findBlocksWithChangedModules()
    if (ChangeBlock.added !== null && ChangeBlock.removed !== null) {
        //
    }
    last_blockList.value = blockList.value
};

// 拖拽更新事件（组内）
const onUpdate = (event: any) => {
    const changedModules: { [key: number]: number } = {};
    blockList.value.forEach((block) => {
        block.moduleList.forEach((module, index) => {
            if (module.displayOrder !== index) {
                changedModules[module.id] = index;
                module.displayOrder = index;
                // console.log(`Block ID: ${block.id}, Module ID: ${module.id}, Index: ${index}, display_order: ${module.display_order}`);
            }
        });
    });

    // blockList.value.forEach((block: any) => {
    //     block.moduleList = block.moduleList.map((module: any, index: number) => ({
    //         ...module,
    //         display_order: index
    //     }));
    // });
    console.log('onUpdate')
    printModuleIdsAndDisorder(blockList.value)
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
            gap: 10px;
            padding: 10px;
            margin: 20px;

            .drag-box {
                display: flex;
                flex-wrap: wrap;
                width: 100%;
                justify-content: flex-start;
                gap: 2.8571%;
            }

            .icon-center {
                position: relative;
                display: flex;
                flex-direction: column;
                justify-content: space-between;
                align-items: center;
                width: 10%;
                height: auto;
                aspect-ratio: 1 / 1;
                border: 1px solid rgb(var(--art-gray-300-rgb), 0.8);
                border-radius: 12px !important;
                padding: 8px;
                box-sizing: border-box;
                overflow: hidden;
                color: rgba(#fff, 0.8);
                text-align: center;
                cursor: pointer;
                margin-bottom: 1.4286%;

                &:hover {
                    cursor: pointer;
                    background: var(--art-gray-100);
                }

                i,
                img {
                    font-size: 26px;
                    max-width: 50px;
                    max-height: 50px;
                    transition: color 0.3s ease;
                    margin: auto;
                    border: 1px solid rgb(var(--art-gray-300-rgb), 0.8);
                    border-radius: 12px !important;

                }

                .icon-center-text {
                    font-size: 12px;
                    position: absolute;
                    bottom: 8px;
                    color: var(--art-text-gray-600);
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