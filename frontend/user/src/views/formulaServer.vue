<template>
  <el-row class="container" style="width: 100%;height: 100%;">
    <!-- 左边容器 -->
    <el-main class="left-container"
      style="width: 50%;height: 100%;margin: 0;padding: 0;box-sizing: border-box;display:flex;flex-direction:column;">
      <!-- 上部分 -->
      <el-header style="padding: 0; margin: 0; height: 30%; position: relative;">
        <el-upload class="upload-container" drag :action="apiUrls.OcrFormula_API_URL" :show-file-list="false"
          :on-success="handleTopSuccess" :on-error="handleError" :before-upload="beforeUpload" name="image"
          accept="image/*,video/*,audio/*">
          <el-icon class="el-icon--upload"><upload-filled /></el-icon>
          <div class="el-upload__text">
            拖拽文件到此处或 <em>点击上传</em>
          </div>
        </el-upload>
        <!-- 预览区域 -->
        <div class="preview-container" v-if="previewUrl">
          <!-- 清除按钮 -->
          <el-button class="clear-preview-btn" circle @click="clearPreview" type="danger" plain>
            <el-icon>
              <Close />
            </el-icon>
          </el-button>
          <!-- 图片预览 -->
          <el-image v-if="fileType === 'image'" :src="previewUrl" fit="contain" class="preview-content" />
          <!-- 视频预览 -->
          <video v-else-if="fileType === 'video'" :src="previewUrl" controls class="preview-content video-player"
            preload="metadata" playsinline>
            <source :src="previewUrl" type="video/mp4">
            您的浏览器不支持视频播放
          </video>
          <!-- 音频预览 -->
          <div v-else-if="fileType === 'audio'" class="audio-preview">
            <el-icon class="audio-icon">
              <Headset />
            </el-icon>
            <span class="audio-text">{{ fileName }}</span>
          </div>
        </div>
      </el-header>

      <!-- 中部分 -->
      <el-main style="padding: 0;margin: 0; height: 40%; border-bottom: 1px solid #ebeef5;">
        <div style="height: 100%; padding: 10px;">
          <el-input v-model="inputMessage" type="textarea" placeholder="请输入文本" resize="none"
            style="height: 100%; width: 100%;" :autosize="{ minRows: 4 }" />
        </div>
      </el-main>
      <!-- 下部分 -->
      <el-footer style="padding: 0;margin: 0; height: 30%;">
        <div style="height: 100%; display: flex; justify-content: center; align-items: center;">
          <!-- 动态渲染 mathml -->
          <div v-html="inputMessage" style="text-align: center;"></div>
        </div>
      </el-footer>
    </el-main>
    <!-- 右边容器 -->
    <el-main class="right-container">
      <div class="formula-header">
        <el-button @click="changeFormulaType">
          {{ formulaType ? "MathMl" : "Latex" }}
        </el-button>
      </div>
      <div class="formula-content">
        <el-row v-for="(MathFormula, index) in mathFormulaTList" :key="index">
          <el-col v-html="MathFormula.mathml" class="math-formula" @click="copyFormula(MathFormula)"
            title="点击复制公式"></el-col>
        </el-row>
      </div>
    </el-main>
  </el-row>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue';
import type { mathFormulaType } from "@/api/model/mathFormulaModel";
import { MathFormulaTList } from '@/mock/temp/mathFormulaList'
import { UploadFilled, Close, Headset } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus';
import { apiUrls } from '@/api/api';
import axios from 'axios';

const formulaType = ref(true)
const dialogVisible = ref(false)
const dialogImageUrl = ref('')
const inputMessage = ref<string>('');
const previewUrl = ref('');
const fileType = ref<'image' | 'video' | 'audio' | null>(null);
const fileName = ref('');
const isLoading = ref(false);

const changeFormulaType = () => {
  formulaType.value = !formulaType.value;
};

// 响应式数据
const mathFormulaTList = ref<mathFormulaType[]>(MathFormulaTList);

// 上传前验证
const beforeUpload = (file: File) => {
  const isImage = file.type.startsWith('image/');
  const isVideo = file.type.startsWith('video/');
  const isAudio = file.type.startsWith('audio/');
  const isLt10M = file.size / 1024 / 1024 < 10;

  if (!isImage && !isVideo && !isAudio) {
    ElMessage.error('只能上传图片、视频或音频文件!');
    return false;
  }
  if (!isLt10M) {
    ElMessage.error('文件大小不能超过 10MB!');
    return false;
  }

  isLoading.value = true;

  // 创建预览URL
  previewUrl.value = URL.createObjectURL(file);
  fileName.value = file.name;

  // 设置文件类型
  if (isImage) {
    fileType.value = 'image';
  } else if (isVideo) {
    fileType.value = 'video';
  } else if (isAudio) {
    fileType.value = 'audio';
  }

  return true;
};

// 转换函数建议提取到工具类中
const convertOcrResultToArray = (result: Record<string, mathFormulaType>): mathFormulaType[] => {
  return Object.values(result);
};

// 上传成功处理
const handleTopSuccess = async (response: any) => {
  isLoading.value = false;
  if (response.stateCode === 1) {
    ElMessage.success('识别成功');
    try {
      const formulaList = convertOcrResultToArray(response.result);
      mathFormulaTList.value =formulaList
    } catch (error) {
      console.error('数据转换异常:', error);
      ElMessage.warning('识别结果格式异常');
    }

  } else {
    ElMessage.error('未识别到文字');
  }
};

// 上传失败处理
const handleError = () => {
  isLoading.value = false;
  ElMessage.error('上传失败');
  // 清除预览
  previewUrl.value = '';
  fileType.value = null;
  fileName.value = '';
};

// 清除预览
const clearPreview = () => {
  previewUrl.value = '';
  fileType.value = null;
  fileName.value = '';
  inputMessage.value = '';
  ElMessage.success('已清除预览');
};

// 发送消息
const sendMessage = () => {
  if (inputMessage.value.trim()) {
    // 这里添加发送消息的逻辑
    console.log('发送消息:', inputMessage.value);
    inputMessage.value = '';
  }
};

// 处理图片预览
const handlePictureCardPreview = (file: any) => {
  dialogImageUrl.value = file.url;
  dialogVisible.value = true;
};

// 处理图片删除
const handleRemove = (file: any, fileList: any[]) => {

  console.log(file, fileList);
};

// 复制功能实现
const copyFormula = async (formula: mathFormulaType) => {
  try {
    // 创建临时textarea元素
    const textarea = document.createElement('textarea');

    if (formulaType.value) {
      // 设置要复制的内容（这里复制原始mathml代码）
      textarea.value = formula.mathml;
    } else {
      textarea.value = formula.latex;
    }

    // 防止屏幕闪烁
    textarea.style.position = 'fixed';
    textarea.style.opacity = '0';

    document.body.appendChild(textarea);
    textarea.select();

    // 使用现代 Clipboard API
    if (navigator.clipboard) {
      await navigator.clipboard.writeText(textarea.value);
    } else {
      // 降级方案
      document.execCommand('copy');
    }

    // 移除临时元素
    document.body.removeChild(textarea);
    ElMessage.success('公式已复制到剪贴板');
  } catch (err) {
    // 错误处理
    console.error('复制失败:', err);
    ElMessage.error('复制失败');

  }
};
</script>

<style scoped>
.container {
  background-color: #f8f9fa;
  flex-direction: row;
  height: 100vh;
  width: 100%;
  overflow: hidden;
  border-radius: 15px;
}


.left-container {
  border-radius: 10px 0 0 10px;
  /* 左上和左下圆角 */
  overflow: hidden;
  /* 确保内部元素的圆角效果 */
}


/* 调整输入框字体和边框 */
:deep(.el-textarea__inner) {
  font-family: monospace;
  font-size: 14px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  padding: 10px;
}

/* 聚焦状态样式 */
:deep(.el-textarea__inner:focus) {
  border-color: #409eff;
}

:deep(mjx-container) {
  display: inline-block !important;
  padding: 5px;
}

.right-container {
  width: 50%;
  height: 100%;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  background-color: #ffffff;
  border-radius: 10px;
}

.formula-header {
  padding: 0 20px 0 0;
  display: flex;
  justify-content: flex-end;
  background-color: #ffffff;
  border-bottom: 1px solid #ebeef5;
}

.formula-content {
  flex: 1;
  overflow-y: auto;
  overflow-x: hidden;
  padding: 0 20px 20px;
  background-color: #ffffff;

  &::-webkit-scrollbar {
    display: none;
  }

  -ms-overflow-style: none;
  scrollbar-width: none;

  .math-formula {
    margin: 20px 0;
    padding: 10px;
    border-radius: 5px;
    cursor: pointer;
    background-color: #ffffff;
    transition: all 0.3s ease;
    border: 1px solid #0d0d0d;
  }

  .math-formula:hover {
    background-color: transparent;
    box-shadow: 0 2px 12px transparent;
    transform: translateY(-2px);
  }

  :deep(mjx-container) {
    display: inline-block !important;
    padding: 5px;
  }
}

.upload-container {
  :deep(.el-upload) {
    width: 100%;
    height: 100%;
    display: block;
  }

  :deep(.el-upload-dragger) {
    width: 100%;
    height: 100%;
    border: none;
    padding: 0;
    margin: 0;
    background-color: transparent;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }

  :deep(.el-upload-dragger:hover) {
    background-color: #f5f7fa;
  }

  /* 修复图标位置 */
  .el-icon--upload {
    margin-top: -10px;
    /* 抵消默认的margin-bottom */
  }
}

/* 预览区域样式 */
.preview-container {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: #fff;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1;
}

.clear-preview-btn {
  position: absolute;
  top: 10px;
  right: 10px;
  z-index: 2;
}

.preview-content {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
}

.video-player {
  width: 100%;
  height: 100%;
}

.audio-preview {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 10px;
}

.audio-icon {
  font-size: 48px;
  color: #409eff;
}

.audio-text {
  font-size: 14px;
  color: #606266;
  max-width: 80%;
  text-align: center;
  word-break: break-all;
}

/* 加载状态样式 */
.is-loading {
  position: relative;
  pointer-events: none;

  &::after {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(255, 255, 255, 0.7);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 2;
  }
}
</style>