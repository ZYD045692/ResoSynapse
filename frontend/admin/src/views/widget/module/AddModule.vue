<template>
  <div class="infobox-style">
    <h1 class="title">基本设置</h1>
    <el-form :model="newModule" class="form" ref="ruleFormRef" :rules="rules" label-width="100px" label-position="top">
      <el-row>
        <el-form-item label="所属模块" prop="fatherId">
          <el-select v-model="selectedBlock" placeholder="选择模块" @change="handleBlockChange">
            <el-option v-for="block in blockList" :key="block.id" :label="block.name" :value="block.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="是否可见" prop="isVisible" class="right-input">
          <el-switch v-model="newModule.isVisible" />
        </el-form-item>
      </el-row>
      <el-row>
        <el-form-item label="名称" prop="name">
          <el-input v-model="newModule.name" class="name-input" />
        </el-form-item>
        <el-form-item label="URL" prop="url">
          <el-input v-model="newModule.url" class="url-preview" />
        </el-form-item>
      </el-row>

      <el-row>
        <el-form-item label="图标" prop="iconUrl">
          <div class="el-top upload-container">
            <el-upload class="cover-uploader" :action="uploadImageUrl" :headers="uploadHeaders" :show-file-list="false"
              :on-success="onIconSuccess" :on-error="onError" :before-upload="beforeUpload">
              <div class="cover-icon-container">
                <div v-if="!newModule.iconUrl" class="upload-placeholder">
                  <el-icon class="upload-icon">
                    <Plus />
                  </el-icon>
                </div>
                <img v-else :src="newModule.iconUrl" class="cover-icon" />
                <div class="upload-overlay">
                </div>
              </div>
            </el-upload>
          </div>
        </el-form-item>


      </el-row>

      <el-form-item label="简介" prop="abstract">
        <el-input type="textarea" :rows="4" v-model="newModule.introduction" />
      </el-form-item>

      <div class="el-form-item-right">
        <el-button style="width: 90px" v-ripple @click="resetForm">重置</el-button>
        <el-button style="width: 90px" v-ripple @click="submitForm">保存</el-button>
      </div>
    </el-form>
  </div>
</template>
<script setup lang="ts">
import { FormInstance, FormRules, ElMessage } from 'element-plus'
import { ModuleCategoryType } from '@/api/model/moduleModel';
import EmojiText from '@/utils/emojo'
import { useUserStore } from '@/store/modules/user'
import { BlockType } from '@/api/model/blockModel';
import { BlockList } from '@/mock/temp/blockList';
import { GetAdminBlocks } from '@/api/blockApi'
const router = useRoute()
const userStore = useUserStore()
let { accessToken } = userStore
const isLoading = ref(true);

// 新模块数据
const newModule = ref<ModuleCategoryType>({
  fatherId: 0,
  name: '',
  iconUrl: '',
  url: '',
  introduction: '',
  isVisible: true,
});
const blockList = ref<BlockType[]>([]);
// 上传路径
const uploadImageUrl = `${import.meta.env.VITE_API_URL}/api/common/upload`
// 传递 token
const uploadHeaders = { Authorization: accessToken }
const selectedBlock = ref<number>(newModule.value.fatherId);

onMounted(async () => {
  scrollToTop();
  try {
    const Blocks = await GetAdminBlocks(1)
    blockList.value = Blocks;
    const blockId = Number(router.query.id) || 1;
    selectedBlock.value = blockId;

  } catch (error) {
    console.error('Error:', error);
  } finally {
    isLoading.value = false;
  }
});
const handleBlockChange = (value: number) => {
  newModule.value.fatherId = value;
};

const rules = reactive<FormRules>({
  name: [
    { required: true, message: '请输入名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  iconUrl: [
    { required: true, message: '请上传图标', trigger: 'blur' }
  ],
  url: [
    { required: true, message: '请输入 URL', trigger: 'blur' }
  ],
  abstract: [
    { required: true, message: '请输入简介', trigger: 'blur' },
    { min: 2, max: 200, message: '长度在 2 到 200 个字符', trigger: 'blur' }
  ]
});

const ruleFormRef = ref<FormInstance>();

const submitForm = async () => {
  const formEl = ruleFormRef.value;
  if (!formEl) return;
  await formEl.validate((valid, fields) => {
    if (valid) {
      console.log('submit!', newModule.value);
      // 在这里添加提交逻辑
      ElMessage.success('提交成功');
      // 重置表单
      resetForm();
    } else {
      console.log('error submit!', fields);
    }
  });
};

const resetForm = () => {
  newModule.value = {
    fatherId: 0,
    name: '',
    iconUrl: '',
    url: '',
    introduction: '',
    isVisible: true,
  };
};

const onIconSuccess = (response: any) => {
  newModule.value.iconUrl = response.data.url;
  ElMessage.success(`图片上传成功 ${EmojiText[200]}`);
};

const onError = () => {
  ElMessage.error(`图片上传失败 ${EmojiText[500]}`);
};

// 添加上传前的校验
const beforeUpload = (file: File) => {
  const isImage = file.type.startsWith('image/');
  const isLt2M = file.size / 1024 / 1024 < 2;

  if (!isImage) {
    ElMessage.error('只能上传图片文件!');
    return false;
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB!');
    return false;
  }
  return true;
};

const scrollToTop = () => {
  window.scrollTo({ top: 0, behavior: 'smooth' })
}
</script>
<style lang="scss" scoped>
$box-radius: calc(var(--custom-radius) + 4px);

.infobox-style {
  padding: 20px;
  flex: 1;
  overflow: hidden;
  border-radius: $box-radius;
  background-color: var(--art-main-bg-color);
  border: 1px solid var(--art-card-border) !important;

  .title {
    padding-bottom: 20px;
    font-size: 20px;
    font-weight: 400;
    color: var(--art-text-gray-800);
    border-bottom: 2px solid var(--art-border-color);
    margin-bottom: 20px;
  }

  .form {
    box-sizing: border-box;
    padding: 0px 20px;
    display: flex;
    flex-direction: column;

    > .el-row {
      display: flex;
      margin-bottom: 20px;

      .el-form-item {
        flex: 1;
        min-width: 200px;

        &:last-child {
          margin-left: 20px;
        }

        .el-input,
        .el-select {
          width: 100%;
        }
      }
    }

    .cover-icon-container {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 10px;

      .cover-icon {
        border-radius: $box-radius;
        width: auto;
        height: 150px;
        object-fit: cover;
      }

      .upload-placeholder {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        border: 1px dashed var(--art-text-gray-400);
        border-radius: $box-radius;
        width: auto;
        height: 150px;
        aspect-ratio: 1 / 1;

        .upload-icon {
          font-size: 24px;
          color: var(--art-text-gray-400);
        }

        .upload-text {
          margin-top: 8px;
          color: var(--art-text-gray-600);
          font-size: 14px;
        }
      }
    }

    .el-form-item-right {
      display: flex;
      justify-content: flex-end;
      gap: 10px;

      .el-button {
        min-width: 90px;
      }
    }

    .el-textarea {
      width: 100%;
    }
  }

  // 自定义下拉框样式
  .el-select {
    width: 200px;
    border-radius: $box-radius;
    border: 1px solid var(--art-border-color);

    &:hover {
      border-color: var(--art-primary-color);
    }

    .el-select__tags {
      display: flex;
      align-items: center;
      gap: 8px;
    }

    .el-select-dropdown__item {
      padding: 8px 12px;
      border-radius: 4px;

      &:hover {
        background-color: var(--art-bg-color-hover);
      }
    }

    .el-select-dropdown__item.selected {
      color: var(--art-primary-color);
      font-weight: bold;
    }
  }

  // 自定义输入框样式
  .el-input {
    width: 100%;
    border-radius: $box-radius;
    border: 1px solid var(--art-border-color);

    &:hover {
      border-color: var(--art-primary-color);
    }
  }
}
</style>