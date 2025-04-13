<template>
  <div class="infobox-style">
    <h1 class="title">基本设置</h1>
    <el-form :model="newBlock" class="form" ref="ruleFormRef" :rules="rules" label-width="100px" label-position="top">
      <el-row>
        <el-form-item label="名称" prop="name">
          <el-input v-model="newBlock.name" />
        </el-form-item>
        <el-form-item label="是否可见" prop="isVisible" class="right-input">
          <el-switch v-model="newBlock.isVisible" />
        </el-form-item>
      </el-row>

      <el-row>
        <el-form-item label="图标" prop="iconUrl">
          <div class="el-top upload-container">
            <el-upload class="cover-uploader" :action="uploadImageUrl" :headers="uploadHeaders" :show-file-list="false"
              :on-success="onIconSuccess" :on-error="onError" :before-upload="beforeUpload">
              <div class="cover-icon-container">
                <div v-if="!newBlock.iconUrl" class="upload-placeholder">
                  <el-icon class="upload-icon">
                    <Plus />
                  </el-icon>
                  <!-- <div class="upload-text">点击上传图标</div> -->
                </div>
                <img v-else :src="newBlock.iconUrl" class="cover-icon" />
                <div class="upload-overlay">
                  <!-- <div class="upload-text">点击图片上传图标</div> -->
                </div>
              </div>
            </el-upload>
          </div>
        </el-form-item>

        <el-form-item label="首页图片" prop="imgurl">
          <div class="el-top upload-container">
            <el-upload class="cover-uploader" :action="uploadImageUrl" :headers="uploadHeaders" :show-file-list="false"
              :on-success="onIconSuccess" :on-error="onError" :before-upload="beforeUpload">
              <div class="cover-img-container">
                <div v-if="!newBlock.imgUrl" class="upload-placeholder">
                  <el-icon class="upload-icon">
                    <Plus />
                  </el-icon>
                  <!-- <div class="upload-text">点击上传图标</div> -->
                </div>
                <img v-else :src="newBlock.imgUrl" class="cover-img" />
                <div class="upload-overlay">
                  <!-- <div class="upload-text">点击图片上传图标</div> -->
                </div>
              </div>
            </el-upload>
          </div>
        </el-form-item>
      </el-row>

      <el-form-item label="简介" prop="abstract">
        <el-input type="textarea" :rows="4" v-model="newBlock.introduction" />
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
import { BlockCategoryType } from '@/api/model/blockModel';
import EmojiText from '@/utils/emojo'
import { useUserStore } from '@/store/modules/user'
import { t } from '@wangeditor/editor';
const router = useRoute()
const userStore = useUserStore()
let { accessToken } = userStore


// 新模块数据
const newBlock = ref<BlockCategoryType>({
  name: '',
  iconUrl: '',
  introduction: '',
  imgUrl: '',
  isVisible: true
});

// 上传路径
const uploadImageUrl = `${import.meta.env.VITE_API_URL}/api/common/upload`
// 传递 token
const uploadHeaders = { Authorization: accessToken }

const rules = reactive<FormRules>({
  name: [
    { required: true, message: '请输入名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  iconUrl: [
    { required: true, message: '请上传图标', trigger: 'blur' }
  ],
  imgurl: [
    { required: true, message: '请上传首页图片', trigger: 'blur' }
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
      console.log('submit!', newBlock.value);
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
  newBlock.value = {
    name: '',
    iconUrl: '',
    introduction: '',
    imgUrl: '',
    isVisible: true
  };
};

const onIconSuccess = (response: any) => {
  newBlock.value.iconUrl = response.data.url;
  ElMessage.success(`图片上传成功 ${EmojiText[200]}`);
};
const onImgSuccess = (response: any) => {
  newBlock.value.imgUrl = response.data.url;
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

    >.el-row {
      display: flex;

      .el-form-item {
        flex: 1;
        min-width: 200px;

        .el-input,
        .el-select {
          width: 100%;
        }
      }

      .right-input {
        margin-left: 20px;
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

    .cover-img-container {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 10px;

      .cover-img {
        border-radius: $box-radius;
        width: auto;
        height: 150px;
        object-fit: cover;
        aspect-ratio: 16 / 9;
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
        aspect-ratio: 16 / 9;

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
}
</style>