<template>
  <div class="page-content article-list">
    <el-row justify="space-between" :gutter="10">
      <el-col :lg="6" :md="6" :sm="14" :xs="16">
        <el-input
          v-model="searchVal"
          :prefix-icon="Search"
          clearable
          :placeholder="placeholderText"
          @keyup.enter="searchBlock"
          @input="handleInput"
        />
      </el-col>
      <el-col :lg="12" :md="12" :sm="0" :xs="0">
        <div class="custom-segmented">
          <el-segmented
            v-model="searchtype"
            :options="options"
            @change="handleTypeChange"
          />
        </div>
      </el-col>
      <el-col :lg="6" :md="6" :sm="10" :xs="6" style="display: flex; justify-content: end">
        <el-button @click="toAddBlock">新增板块</el-button>
        <el-button @click="toEditBlock">编辑顺序</el-button>
      </el-col>
    </el-row>

    <div class="list">
      <div class="offset">
        <div class="item" v-for="item in blockList" :key="item.id" @click="toDetail(item)">
          <!-- 骨架屏 -->
          <el-skeleton animated :loading="isLoading" style="width: 100%; height: 100%">
            <template #template>
              <div class="top">
                <el-skeleton-item variant="image" style="width: 100%; height: 100%; border-radius: 10px" />
                <div style="padding: 16px 0">
                  <el-skeleton-item variant="p" style="width: 80%" />
                  <el-skeleton-item variant="p" style="width: 40%; margin-top: 10px" />
                </div>
              </div>
            </template>

            <template #default>
              <div class="top">
                <el-image class="cover" :src="item.imgUrl" lazy fit="cover">
                  <template #error>
                    <div class="image-slot">
                      <el-icon><icon-picture /></el-icon>
                    </div>
                  </template>
                </el-image>
              </div>
              <div class="bottom">
                <h2>{{ item.name }}</h2>
                <div class="info">
                  <div class="text">
                    <i class="iconfont-sys">&#xe6f7;</i>
                    <span>{{ item.introduction }}</span>
                    <div class="line"></div>
                    <i class="iconfont-sys">&#xe689;</i>
                    <span>{{ item.isVisible ? '可见' : '不可见' }}</span>
                  </div>
                </div>
              </div>
            </template>
          </el-skeleton>
        </div>
      </div>
    </div>

    <div style="margin-top: 16vh" v-if="showEmpty">
      <el-empty :description="`未找到相关数据 ${EmojiText[0]}`" />
    </div>
  </div>
</template>
<script setup lang="ts">
import { Picture as IconPicture } from '@element-plus/icons-vue'
import { ref, onMounted, computed } from 'vue'
import { BlockType } from '@/api/model/blockModel'
import { router } from '@/router'
import { Search } from '@element-plus/icons-vue'
import EmojiText from '@/utils/emojo'
import { BlockList } from '@/mock/temp/blockList'
import { GetAdminBlocks } from '@/api/blockApi'

const searchtype = ref('All')
const searchVal = ref('')

const options = ['All', '名称', '简介']
const optionshint = {
  'All': 'All',
  '名称': '输入板块名称',
  '简介': '输入板块简介'
}

const placeholderText = computed(() => {
  return optionshint[searchtype.value as keyof typeof optionshint] || '输入查询内容'
})

const blockList = ref<BlockType[]>([])
const oriBlockList = ref<BlockType[]>([])
const isLoading = ref(true)

const showEmpty = computed(() => {
  return blockList.value.length === 0 && !isLoading.value
})

onMounted(async () => {
  isLoading.value = true
  try {
    const Blocks = await GetAdminBlocks(1)
    blockList.value = Blocks
    oriBlockList.value = Blocks
  } catch (error) {
    console.error('获取板块列表失败:', error)
  } finally {
    isLoading.value = false
  }
})

// 搜索板块
const searchBlock = () => {
  getBlockList()
}

// 输入框内容改变时触发
const handleInput = () => {
  if (searchtype.value === '名称' || searchtype.value === '简介') {
    getBlockList()
  }
}

// 切换类型时清空搜索框内容
const handleTypeChange = () => {
  searchVal.value = ''
  blockList.value = oriBlockList.value
  getBlockList()
}

const getBlockList = () => {
  let filteredList = [...oriBlockList.value]

  if (searchtype.value === '名称') {
    filteredList = filteredList.filter(item =>
      item.name.toLowerCase().includes(searchVal.value.toLowerCase())
    )
  } else if (searchtype.value === '简介') {
    filteredList = filteredList.filter(item =>
      item.introduction.toLowerCase().includes(searchVal.value.toLowerCase())
    )
  }

  blockList.value = filteredList
}

const toDetail = (item: BlockType) => {
  router.push({
    path: `/widget/block/detail`,
    query: {
      id: item.id
    }
  })
}

const toAddBlock = () => {
  router.push({
    path: `/widget/addblock`
  })
}

const toEditBlock = () => {
  router.push({
    path: `/widget/block/editorder`,
  })
}
</script>
<!-- <script setup lang="ts">
import { Picture as IconPicture } from '@element-plus/icons-vue'
import { ref, onMounted, computed } from 'vue'
import { BlockType } from '@/api/model/blockModel'
import { router } from '@/router'
import { Search } from '@element-plus/icons-vue'
import EmojiText from '@/utils/emojo'
import { BlockList } from '@/mock/temp/blockList'
import { GetAdminBlocks } from '@/api/blockApi'
const searchtype = ref('All')
const searchVal = ref('')

const options = ['All', '名称', '简介']
const optionshint = {
  'All': 'All',
  '名称': '输入板块名称',
  '简介': '输入板块简介'
}
const Blocks = GetAdminBlocks();
console.log('Blocks:',Blocks)
const placeholderText = computed(() => {
  return optionshint[searchtype.value as keyof typeof optionshint] || '输入查询内容'
})

const blockList = ref<BlockType[]>([])
const isLoading = ref(true)

const showEmpty = computed(() => {
  return blockList.value.length === 0 && !isLoading.value
})

onMounted(() => {
  getBlockList()
})

// 搜索板块
const searchBlock = () => {
  getBlockList()
}

// 输入框内容改变时触发
const handleInput = () => {
  if (searchtype.value === '名称' || searchtype.value === '简介') {
    getBlockList()
  }
}
// 切换类型时清空搜索框内容
const handleTypeChange = () => {
  searchVal.value = ''
  getBlockList()
}


const getBlockList = async () => {
  isLoading.value = true

  // 模拟API调用
  let filteredList = BlockList

  if (searchtype.value === '名称') {
    filteredList = BlockList.filter(item =>
      item.name.toLowerCase().includes(searchVal.value.toLowerCase())
    )
  } else if (searchtype.value === '简介') {
    filteredList = BlockList.filter(item =>
      item.introduction.toLowerCase().includes(searchVal.value.toLowerCase())
    )
  } else if (searchtype.value === 'All') {
    filteredList = BlockList
  }

  blockList.value = filteredList

  isLoading.value = false
}

const toDetail = (item: BlockType) => {
  router.push({
    path: `/widget/block/detail`,
    query: {
      id: item.id
    }
  })
}


const toAddBlock = () => {
  router.push({
    path: `/widget/addblock`
  })
}

const toEditBlock = () => {
  router.push({
    path: `/widget/block/editorder`,
  })
}
</script> -->

<style lang="scss" scoped>
.article-list {
  .list {
    margin-top: 20px;

    .offset {
      display: flex;
      flex-wrap: wrap;
      width: calc(100% + 20px);

      .item {
        box-sizing: border-box;
        width: calc(20% - 20px);
        margin: 0 20px 20px 0;
        cursor: pointer;
        border: 1px solid var(--art-border-color);
        border-radius: calc(var(--custom-radius) / 2 + 2px) !important;

        &:hover {
          .el-button {
            opacity: 1 !important;
          }
        }

        .top {
          position: relative;
          aspect-ratio: 16/9.5;

          .cover {
            display: flex;
            align-items: center;
            justify-content: center;
            width: 100%;
            height: 100%;
            object-fit: cover;
            background: var(--art-gray-200);
            border-radius: calc(var(--custom-radius) / 2 + 2px) calc(var(--custom-radius) / 2 + 2px) 0 0;

            .image-slot {
              font-size: 26px;
              color: var(--art-gray-400);
            }
          }

          .type {
            position: absolute;
            top: 5px;
            right: 5px;
            padding: 5px 4px;
            font-size: 12px;
            color: rgba(#fff, 0.8);
            background: rgba($color: #000, $alpha: 60%);
            border-radius: 4px;
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

@media only screen and (max-width: $device-notebook) {
  .article-list {
    .list {
      .offset {
        .item {
          width: calc(25% - 20px);
        }
      }
    }
  }
}

@media only screen and (max-width: $device-ipad-pro) {
  .article-list {
    .list {
      .offset {
        .item {
          width: calc(33.333% - 20px);

          .bottom {
            h2 {
              font-size: 16px;
            }
          }
        }
      }
    }
  }
}

@media only screen and (max-width: $device-ipad) {
  .article-list {
    .list {
      .offset {
        .item {
          width: calc(50% - 20px);
        }
      }
    }
  }
}

@media only screen and (max-width: $device-phone) {
  .article-list {
    .list {
      .offset {
        .item {
          width: calc(100% - 20px);
        }
      }
    }
  }
}
</style>