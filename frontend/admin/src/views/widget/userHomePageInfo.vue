<template>
  <el-container class="pageinfo">
    <el-aside class="left">
      <el-card class="info-card">
        <template #header>
          <div class="card-header">
            <span>页面信息</span>
          </div>
        </template>
        <el-row>
          <el-form-item label="顶部导航文本1" prop="TopNavigationtext1">
            <el-input v-model="pageInfo.TopNavigationtext1" :disabled="!isEdit" />
          </el-form-item>

          <el-form-item label="顶部导航文本2" prop="TopNavigationtext2">
            <el-input v-model="pageInfo.TopNavigationtext2" :disabled="!isEdit" />
          </el-form-item>

          <el-form-item label="申请URL文本" prop="ApplyUrltext">
            <el-input v-model="pageInfo.ApplyUrltext" :disabled="!isEdit" />
          </el-form-item>

          <el-form-item label="反馈文本" prop="FeedBacktext">
            <el-input v-model="pageInfo.FeedBacktext" :disabled="!isEdit" />
          </el-form-item>

          <el-form-item label="维度1" prop="FeedBackdim1">
            <el-input v-model="pageInfo.FeedBackdim1" :disabled="!isEdit" />
          </el-form-item>

          <el-form-item label="维度2" prop="FeedBackdim2">
            <el-input v-model="pageInfo.FeedBackdim2" :disabled="!isEdit" />
          </el-form-item>

          <el-form-item label="维度3" prop="FeedBackdim3">
            <el-input v-model="pageInfo.FeedBackdim3" :disabled="!isEdit" />
          </el-form-item>

          <template v-if="!isEdit">
            <el-form-item label="背景" prop="iconUrl">
              <div class="input-with-preview">
                <div class="preview" v-if="pageInfo.BackGroundUrl">
                  <img :src="pageInfo.BackGroundUrl" class="icon-preview" />
                </div>
              </div>
            </el-form-item>
          </template>

          <template v-else>
            <el-form-item label="背景" prop="BackGroundUrl">
              <el-input v-model="pageInfo.BackGroundUrl" />
            </el-form-item>
          </template>
          <el-col style="justify-content: flex-end;">
            <el-button @click="toggleEdit" plain>
              {{ isEdit ? '取消编辑' : '编辑' }}
            </el-button>
          </el-col>
        </el-row>
      </el-card>
    </el-aside>
    <el-main class="right">
      <UserHome :BlockList="BlockList" :PlatformList="PlatformList" :LeftNavigation="LeftNavigation"
        :PageInfo="UserHomePageInfo" />
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
import UserHome from './userHomePage/UserHome.vue';
import { UserHomePageInfoType } from '@/api/model/userHomePageInfoModel'
import { reactive, onMounted, ref } from 'vue'

import { BlockList } from '@/mock/temp/blockList';
import { PlatformList } from '@/mock/temp/platformList';
import { LeftNavigation } from '@/mock/temp/LeftNavigation';
import { UserHomePageInfo } from '@/mock/temp/userHomePageInfo';
import { getUserHomePageInfo } from '@/api/PageInfoApi'

const pageInfo = reactive<UserHomePageInfoType>(UserHomePageInfo);
const originalPageInfo = ref<UserHomePageInfoType>({ ...UserHomePageInfo });
const isEdit = ref(false)

const loadPageInfo = async () => {
  try {
    const res = await getUserHomePageInfo()
    if (res) {
      Object.assign(pageInfo, res)
    }
  } catch (err) {
    console.error('获取页面信息失败:', err)
  }
}

const toggleEdit = () => {
  if (isEdit.value) {
    // 取消编辑，恢复原始数据
    Object.assign(pageInfo, toRaw(originalPageInfo.value))
  } else {
    // 进入编辑状态，保存当前状态
    originalPageInfo.value = { ...toRaw(pageInfo) }
  }
  isEdit.value = !isEdit.value
}

onMounted(() => {
  loadPageInfo()
})
</script>

<style lang="scss" scoped>
.pageinfo {
  width: 100%;
  height: 100%;
  border-style: none;
  $border-color: #eee;
  display: flex;
  justify-content: flex-start;
  gap: 10px;

  :deep(.el-aside) {
    width: 30% !important;
    height: auto;
    overflow: hidden;
    color: rgba(#fff, 0.8);
    text-align: center;
    border: 1px solid rgb(var(--art-gray-300-rgb), 0.8);
    border-radius: 12px !important;
    padding: 20px;
  }

  :deep(.el-main) {
    width: 70% !important;
    height: auto;
    padding: 0;
  }

  .info-card {
    background-color: transparent;
    border: none;

    :deep(.el-card__header) {
      padding: 10px;
      border-bottom: 1px solid rgba(var(--art-gray-300-rgb), 0.8);
    }

    :deep(.el-descriptions) {
      padding: 10px;
    }

    :deep(.el-descriptions__label) {
      color: rgba(#fff, 0.8);
    }

    :deep(.el-descriptions__content) {
      color: rgba(#fff, 0.8);
    }
  }
}


.input-with-preview {
  display: flex;
  align-items: center;
  gap: 10px;

  .preview {
    .icon-preview {
      width: 100%;
      height: auto;
    }

    .img-preview {
      width: auto;
      height: 150px;
      object-fit: cover;
    }

  }
}

.form-item-right :deep(.el-form-item__content) {
  justify-content: flex-end;
}

/* 确保朴素按钮正常 */
:deep(.el-button--plain) {
  background-color: transparent !important;
  border-color: var(--el-button-border-color) !important;
}

:deep(.el-button:focus) {
  outline: none !important;
  box-shadow: none !important;
}

</style>