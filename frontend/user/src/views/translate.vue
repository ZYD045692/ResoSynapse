<template>
  <el-container style="height: 100%">
    <el-aside :style="{ width: asideWidth + '%' }" style="height: 100%; padding: 0">
      <el-container>
        <el-header height="50%">
          <el-upload class="upload-container" drag :action="apiUrls.UploadFiletransform_URL_API" :show-file-list="false"
            :on-success="handleTopSuccess" :on-error="handleError" :before-upload="beforeUpload"
            accept="image/*,video/*,audio/*">
            <el-icon class="el-icon--upload"><upload-filled /></el-icon>
            <div class="el-upload__text">
              拖拽文件到此处或 <em>点击上传</em>
            </div>
            <template #tip>
              <div class="el-upload__tip">
                支持图片、视频、音频文件
              </div>
            </template>
          </el-upload>

          <!-- 预览区域 -->
          <div class="preview-container" v-if="previewUrl">
            <!-- 清除按钮 -->
            <el-button 
              class="clear-preview-btn" 
              circle 
              @click="clearPreview"
              type="danger"
              plain
            >
              <el-icon><Close /></el-icon>
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
        <el-main style="padding: 0; height: 50%;">
          <el-card class="content-card">
            <template #header>
              <div class="header-section">
                <el-icon class="file-icon">
                  <Document />
                </el-icon>
                <span class="section-title">原文</span>
                <el-button-group class="button-group">
                  <el-button plain @click="handleCopyOriginal">
                    <el-icon>
                      <CopyDocument />
                    </el-icon>
                    复制
                  </el-button>
                </el-button-group>
              </div>
            </template>
            <div class="text-item">
              <template v-if="textsisLoading">
                <el-empty :image-size="100">
                  <template #image>
                    <el-icon class="loading-icon">
                      <Loading />
                    </el-icon>
                  </template>
                  <template #description>
                    <span>正在处理中...</span>
                  </template>
                </el-empty>
              </template>
              <template v-else>
                {{ transformText.texts }}
              </template>
            </div>
          </el-card>
        </el-main>
      </el-container>
    </el-aside>
    <div class="resize-handle" @mousedown="startResize"></div>
    <el-main :style="{ width: (100 - asideWidth) + '%' }" style="padding: 0;">
      <el-card class="content-card">
        <template #header>
          <div class="header-section">
            <el-icon class="file-icon">
              <Document />
            </el-icon>
            <span class="section-title">译文</span>
            <el-button-group class="button-group">
              <el-button plain @click="handleCopyTranslated">
                <el-icon>
                  <CopyDocument />
                </el-icon>
                复制
              </el-button>
              <el-select v-model="selectedLanguage" style="width: 130px">
                <el-option v-for="option in languageOptions" :key="option.value" :label="option.label"
                  :value="option.value" />
              </el-select>
              <el-button plain :disabled="!transformText.texts || translatedisLoading" @click="handleTranslate">
                <el-icon>
                  <Position />
                </el-icon>
                翻译
              </el-button>
            </el-button-group>
          </div>
        </template>
        <div class="text-item">
          <template v-if="translatedisLoading">
            <el-empty :image-size="100">
              <template #image>
                <el-icon class="loading-icon">
                  <Loading />
                </el-icon>
              </template>
              <template #description>
                <span>正在处理中...</span>
              </template>
            </el-empty>
          </template>
          <template v-else>
            {{ transformText.translated }}
          </template>
        </div>
      </el-card>
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { UploadFilled, Document, CopyDocument, Download, SetUp, Position, Headset, Loading, Close } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';
import { apiUrls } from '@/api/api';
import { transformTextType } from '@/api/model/transformTextModel';
import { transformtext } from '@/mock/temp/transformresult';
import axios from 'axios';

const asideWidth = ref(63);
const isResizing = ref(false);
const startX = ref(0);
const startWidth = ref(0);
const transformText = ref<transformTextType>({
  texts: '',
  translated: ''
});

interface LanguageOption {
  label: string;
  value: string;
}

const languageOptions = ref<LanguageOption[]>([
  { label: '中文（简体）', value: 'zh' },
  { label: '英语', value: 'en' },
  { label: '中文（繁体）', value: 'zh-CHT' },
  { label: '阿拉伯语', value: 'ar' },
  { label: '俄语', value: 'ru' },
  { label: '法语', value: 'fr' },
  { label: '德语', value: 'de' },
  { label: '西班牙语', value: 'es' },
  { label: '葡萄牙语', value: 'pt' },
  { label: '意大利语', value: 'it' },
  { label: '日语', value: 'ja' },
  { label: '韩语', value: 'ko' },
  { label: '希腊语', value: 'el' },
  { label: '荷兰语', value: 'nl' },
  { label: '印地语', value: 'hi' },
  { label: '土耳其语', value: 'tr' },
  { label: '马来语', value: 'ms' },
  { label: '泰语', value: 'th' },
  { label: '越南语', value: 'vi' },
  { label: '印尼语', value: 'id' }
]);

const selectedLanguage = ref('zh');

const textsisLoading = ref(false);
const translatedisLoading = ref(false);


const startResize = (e: MouseEvent) => {
  isResizing.value = true;
  startX.value = e.clientX;
  startWidth.value = asideWidth.value;

  document.addEventListener('mousemove', handleResize);
  document.addEventListener('mouseup', stopResize);
};

const handleResize = (e: MouseEvent) => {
  if (!isResizing.value) return;

  const diff = e.clientX - startX.value;
  const containerWidth = document.querySelector('.el-container')?.clientWidth || 0;
  const newWidth = startWidth.value + (diff / containerWidth) * 100;

  asideWidth.value = Math.min(Math.max(newWidth, 30), 63);
};

const stopResize = () => {
  isResizing.value = false;
  document.removeEventListener('mousemove', handleResize);
  document.removeEventListener('mouseup', stopResize);
};

const previewUrl = ref('');
const fileType = ref<'image' | 'video' | 'audio' | null>(null);
const fileName = ref('');

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

  textsisLoading.value = true;
  translatedisLoading.value = true;

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

const handleTopSuccess = (response: any) => {
  switch (response.stateCode) {
    case 0:
      ElMessage.error('未识别到文字');
      break
    case 1:
      transformText.value.texts = response.texts;
      transformText.value.translated = response.translated;
      ElMessage.success('识别成功');
      break
  }
  textsisLoading.value = false;
  translatedisLoading.value = false;
};

const handleError = () => {
  textsisLoading.value = false;
  translatedisLoading.value = false;
  ElMessage.error('上传失败');
  // 清除预览
  previewUrl.value = '';
  fileType.value = null;
  fileName.value = '';
};

const copyToClipboard = async (text: string) => {
  try {
    await navigator.clipboard.writeText(text);
    ElMessage.success('复制成功');
  } catch (err) {
    ElMessage.error('复制失败');
  }
};

const handleCopyOriginal = () => {
  copyToClipboard(transformText.value.texts);
};

const handleCopyTranslated = () => {
  copyToClipboard(transformText.value.translated);
};

// 添加翻译处理函数
const handleTranslate = async () => {
  if (!transformText.value.texts) return;
  if (!selectedLanguage.value) return;
  translatedisLoading.value = true;
  try {
    let body = {
      texts: transformText.value.texts,
      toLanguage: selectedLanguage.value
    }
    let response = await axios.post(apiUrls.TextTransform_URL_API, body);
    if (response.status == 200) {
      switch (response.data.stateCode) {
        case 0:
          ElMessage.error('翻译失败');
          break;
        case 1:
          transformText.value.translated = response.data.translated;
          ElMessage.success('翻译成功');
          break;
      }
    }
    else{
      ElMessage.error('翻译失败');
    }
  } catch (error) {
    ElMessage.error('翻译失败');
  } finally {
    translatedisLoading.value = false;
  }
};

// 添加清除预览函数
const clearPreview = () => {
  previewUrl.value = '';
  fileType.value = null;
  fileName.value = '';
  transformText.value.texts = '';
  transformText.value.translated = '';
  ElMessage.success('已清除预览');
};
</script>

<style scoped>
.el-container {
  height: 100%;
  background: #f5f5f5;
  border-radius: 12px;
  overflow: hidden;
  position: relative;
  display: flex;
}

.el-aside {
  background: #f5f5f5;
  box-shadow: 2px 0 6px rgba(0, 0, 0, 0.1);
  border-radius: 12px 0 0 12px;
  position: relative;
  overflow: hidden;
  z-index: 2;
  transition: width 0.1s;
}

.el-aside .el-container {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.el-aside .el-header {
  padding: 0;
  overflow: hidden;
  position: relative;
}

.el-aside .el-main {
  padding: 0;
  overflow: hidden;
  position: relative;
}

.upload-container {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
}

.upload-container :deep(.el-upload) {
  width: 100%;
  height: 100%;
}

.upload-container :deep(.el-upload-dragger) {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background: rgba(255, 255, 255, 0.9);
}

.el-main {
  padding: 0;
  background: #f5f5f5;
  border-radius: 0 12px 12px 0;
  position: relative;
  overflow: hidden;
  z-index: 2;
  transition: width 0.1s;
}

.content-card {
  height: 100%;
  margin: 0;
  border-radius: 0;
}

.content-card :deep(.el-card__header) {
  padding: 0;
  border-bottom: 1px solid #e4e7ed;
}

.content-card :deep(.el-card__body) {
  height: calc(100% - 50px);
  padding: 0;
}

.text-item {
  padding: 20px 30px;
  height: 100%;
  overflow-y: auto;
  font-size: 14px;
  line-height: 1.5;
  white-space: pre-wrap;
  word-break: break-word;
  /* 隐藏滚动条 - Webkit浏览器 */
  &::-webkit-scrollbar {
    display: none;
  }
  /* 隐藏滚动条 - Firefox */
  scrollbar-width: none;
  /* 隐藏滚动条 - IE */
  -ms-overflow-style: none;
}

.resize-handle {
  width: 4px;
  background-color: #e0e0e0;
  cursor: col-resize;
  transition: background-color 0.3s;
  z-index: 10;
}

.resize-handle:hover {
  background-color: #409eff;
}

/* 确保父容器也隐藏滚动条 */
.el-aside,
.el-main {
  &::-webkit-scrollbar {
    display: none;
  }
  scrollbar-width: none;
  -ms-overflow-style: none;
}

.header-section {
  display: flex;
  align-items: center;
  padding: 10px 20px;
  background-color: transparent;
  border: none;
}

.file-icon {
  font-size: 20px;
  margin-right: 10px;
  color: #409eff;
}

.section-title {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
  flex: 1;
}

.button-group {
  margin-left: auto;
  display: flex;
  gap: 12px;
}

.button-group .el-button {
  padding: 8px 15px;
  margin: 0;
}

.button-group .el-icon {
  margin-right: 4px;
}

.button-group .el-button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.preview-container {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: #fff;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 2;
}

.preview-content {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
}

.audio-preview {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  padding: 20px;
}

.audio-icon {
  font-size: 48px;
  color: #409eff;
}

.audio-text {
  font-size: 14px;
  color: #606266;
  word-break: break-all;
  text-align: center;
}

.video-player {
  width: 100%;
  height: 100%;
  background: #000;
  border-radius: 4px;
}

/* 播放按钮样式 */
.video-player::-webkit-media-controls-play-button {
  background-color: rgba(255, 255, 255, 0.9);
  border-radius: 50%;
  width: 32px;
  height: 32px;
  margin: 0 5px;
  transition: all 0.3s ease;
  border: none;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.video-player::-webkit-media-controls-play-button:hover {
  background-color: #ffffff;
  transform: scale(1.1);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
}

.video-player::-webkit-media-controls-play-button:active {
  transform: scale(0.95);
}

/* 进度条样式 */
.video-player::-webkit-media-controls-timeline {
  background-color: rgba(255, 255, 255, 0.2);
  border-radius: 2px;
  height: 4px;
  margin: 0 10px;
  cursor: pointer;
}

.video-player::-webkit-media-controls-timeline::-webkit-slider-thumb {
  background-color: #ffffff;
  border-radius: 50%;
  width: 12px;
  height: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.video-player::-webkit-media-controls-timeline::-webkit-slider-thumb:hover {
  transform: scale(1.2);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
}

/* 音量控制样式 */
.video-player::-webkit-media-controls-volume-slider {
  background-color: rgba(255, 255, 255, 0.2);
  border-radius: 2px;
  height: 4px;
  margin: 0 10px;
  cursor: pointer;
}

.video-player::-webkit-media-controls-volume-slider::-webkit-slider-thumb {
  background-color: #ffffff;
  border-radius: 50%;
  width: 12px;
  height: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.video-player::-webkit-media-controls-volume-slider::-webkit-slider-thumb:hover {
  transform: scale(1.2);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
}

/* 全屏按钮样式 */
.video-player::-webkit-media-controls-fullscreen-button {
  background-color: transparent;
  border: none;
  margin-left: 10px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.video-player::-webkit-media-controls-fullscreen-button:hover {
  transform: scale(1.1);
}

.loading-icon {
  font-size: 48px;
  color: #409eff;
  animation: rotate 2s linear infinite;
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }

  to {
    transform: rotate(360deg);
  }
}

.clear-preview-btn {
  position: absolute;
  top: 10px;
  right: 10px;
  z-index: 3;
  background: rgba(255, 255, 255, 0.9);
  border: none;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.clear-preview-btn:hover {
  background: rgba(255, 255, 255, 1);
}
</style>