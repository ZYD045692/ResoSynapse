<template>
  <el-container style="height: 100%">
    <el-aside :style="{ width: asideWidth + '%' }" style="height: 100%; padding: 0">
      <markdown ref="mm" :initValue="initValue" />
    </el-aside>
    <div class="resize-handle" @mousedown="startResize"></div>
    <el-main :style="{ width: (100 - asideWidth) + '%' }" style="padding: 0;">
      <div class="chat-window">
        <div class="chat-messages" ref="chatMessages">
          <div v-for="(message, index) in messages" :key="index" class="message" :class="message.type">
            <img :src="message.type === 'user' ? USER_AVATAR : ASSISTANT_AVATAR" alt="Avatar" class="avatar" />
            <div class="message-content">
              <div class="bubble" :class="message.type">
                <div v-if="message.type === 'assistant' && message.streaming" class="streaming-content">
                  {{ message.content }}
                </div>
                <div v-else>
                  <div class="message-text">{{ message.content }}</div>
                  <div v-if="message.type === 'user' && message.files && message.files.length > 0"
                    class="message-files">
                    <div class="file-grid">
                      <div class="file-item" v-for="(file, index) in message.files" :key="index">
                        <div class="file-preview" v-if="isImageFile(file.name)">
                          <el-image :src="getFilePreview(file)" fit="cover" class="file-thumbnail" />
                          <div class="file-name">{{ truncateFileName(file.name) }}</div>
                        </div>
                        <div v-else class="file-preview">
                          <el-icon class="file-icon">
                            <Document />
                          </el-icon>
                          <div class="file-name">{{ truncateFileName(file.name) }}</div>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div v-if="message.type === 'assistant'" class="bubble-actions">
                    <el-button type="primary" size="small" class="action-btn" @click="copyMessage(message.content)"
                      :icon="CopyDocument" plain>
                      复制
                    </el-button>
                    <el-button type="primary" size="small" class="action-btn" @click="retryMessage(message)"
                      :icon="Refresh" plain>
                      再试一次
                    </el-button>
                  </div>
                </div>
              </div>
              <div class="message-actions">
                <el-button v-if="message.type === 'assistant' && message.streaming" type="primary" size="small"
                  class="stop-streaming-btn" @click="stopStreaming" plain>
                  中断输出
                </el-button>
              </div>
            </div>
          </div>
        </div>
        <el-row class="section">
          <el-col :span="24">
            <div v-if="fileList.length > 0" class="file-list-section">
              <div class="file-grid">
                <div class="file-item" v-for="(file, index) in fileList" :key="index">
                  <div class="file-preview" v-if="isImageFile(file.name)">
                    <el-image :src="getFilePreview(file)" fit="cover" class="file-thumbnail" />
                    <div class="file-name">{{ truncateFileName(file.name) }}</div>
                    <el-icon class="delete-icon" @click="removeFile(index)">
                      <Close />
                    </el-icon>
                  </div>
                  <div v-else class="file-preview">
                    <el-icon class="file-icon">
                      <Document />
                    </el-icon>
                    <div class="file-name">{{ truncateFileName(file.name) }}</div>
                    <el-icon class="delete-icon" @click="removeFile(index)">
                      <Close />
                    </el-icon>
                  </div>
                </div>
              </div>
            </div>
          </el-col>
          <el-col
            style="padding: 15px 20px; background-color: white;border-radius: 12px;position: relative;height: 200px; gap: 0;">
            <el-row class="chatandbutton">
              <el-col style="height: 80%; padding: 0;">
                <el-input v-model="inputMessage" type="textarea" :rows="4"
                  :placeholder="fileList.length > 0 ? '请整理这份文件' : '请输入消息...'" @keydown.ctrl.enter="sendMessage"
                  class="input-box" resize="none" />
              </el-col>
              <el-col style="padding: 0px; display: flex; justify-content: flex-end;">
                <div class="button-group">
                  <div class="icon-container">
                    <el-tooltip :content="enableStreaming ? '当前为流式输出模式，点击切换为直接输出模式' : '当前为直接输出模式，点击切换为流式输出模式'"
                      placement="top" effect="light">
                      <el-icon class="action-icon" @click="toggleStreaming" :class="{ 'is-active': enableStreaming }">
                        <component :is="enableStreaming ? ChatDotRound : ChatLineRound" />
                      </el-icon>
                    </el-tooltip>
                    <el-tooltip content="点击上传文件（支持txt、doc、docx、pdf、jpg、jpeg、png）" placement="top" effect="light">
                      <el-upload class="action-icon" action="#" :auto-upload="false" :show-file-list="false"
                        :on-change="handleFileChange" :file-list="fileList"
                        :accept="'.txt, .doc, .docx, .pdf, .jpg, .jpeg, .png'">
                        <el-icon>
                          <Link />
                        </el-icon>
                      </el-upload>
                    </el-tooltip>
                    <el-tooltip :content="getSendButtonTooltip" placement="top" effect="light">
                      <div class="action-icon-wrapper">
                        <el-icon class="action-icon" @click="sendMessage" :class="{
                          'is-loading': isSending,
                          'disabled': inputMessage.trim() === '' && fileList.length === 0
                        }">
                          <Promotion />
                        </el-icon>
                      </div>
                    </el-tooltip>
                  </div>
                </div>
              </el-col>
            </el-row>
          </el-col>
        </el-row>
      </div>
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
// Types
interface Message {
  type: string;
  content: string;
  streaming?: boolean;
  files?: UploadFile[];
}

// Imports
import { ref, onMounted, nextTick, watch, computed } from 'vue';
import { ElInput, ElButton, ElUpload, UploadFile } from 'element-plus';
import { Promotion, Link, ChatDotRound, ChatLineRound, Close, CopyDocument, Refresh, Document } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';
import markdown from './markdown.vue';
import { ResoSynapsexintroduction, ResoSynapsexmind, ResoSynapsexintroduction1 } from '@/mock/temp/ResoSynapse';
import { apiUrls } from '@/api/api';
import axios from 'axios';
// Constants
// const USER_AVATAR = 'https://tse4-mm.cn.bing.net/th/id/OIP-C.g5M-iZUiocFCi9YAzojtRAAAAA?rs=1&pid=ImgDetMain';
const USER_AVATAR = './src/assets/avatar.jpg';
const ASSISTANT_AVATAR = 'https://www.keaitupian.cn/cjpic/frombd/1/253/3663778712/1545220977.jpg';
const MIN_RESPONSE_LENGTH = 100;
const MAX_RESPONSE_LENGTH = 500;

// Refs
const chatMessages = ref<HTMLElement | null>(null);
const messages = ref<Message[]>([]);
const inputMessage = ref('');
const isSending = ref(false);
const fileList = ref<UploadFile[]>([]);
const asideWidth = ref(65);
const isResizing = ref(false);
const startX = ref(0);
const startWidth = ref(0);
const enableStreaming = ref(true);
const streamingController = ref<AbortController | null>(null);
const mm = ref<any>(null);
const initValue = ref('');
// Methods
const handleFileChange = (uploadFile: UploadFile, uploadFiles: UploadFile[]) => {
  fileList.value = uploadFiles;
};

const removeFile = (index: number) => {
  fileList.value.splice(index, 1);
};

const generateRandomResponse = (): string => {
  const length = Math.floor(Math.random() * (MAX_RESPONSE_LENGTH - MIN_RESPONSE_LENGTH + 1)) + MIN_RESPONSE_LENGTH;
  const characters = '用户的问题是关于零一分布的表达式，这是一个概率论中的基础概念。用户明确要求以 JSON';
  return Array(length).fill(characters).map(() => characters.charAt(Math.floor(Math.random() * characters.length))).join('');
};

const scrollToBottom = () => {
  if (chatMessages.value) {
    nextTick(() => {
      chatMessages.value!.scrollTo({
        top: chatMessages.value!.scrollHeight,
        behavior: 'smooth'
      });
    });
  }
};

const sendMessage = async () => {
  if (inputMessage.value.trim() === '') {
    if (fileList.value.length > 0) {
      inputMessage.value = "请整理这份文件";
    } else {
      return;
    }
  }

  isSending.value = true;
  messages.value.push({
    type: 'user',
    content: inputMessage.value,
    files: [...fileList.value]
  });
  

  await nextTick();
  scrollToBottom();

  let assistantResponse = '服务器繁忙，请稍后再试。';
  // mock response
  // if (messages.value[messages.value.length - 1].content === "请介绍一下智汇研析") {
  //   assistantResponse = ResoSynapsexintroduction;
  // } else if (messages.value[messages.value.length - 1].content === "请结合文档介绍一下智汇研析") {
  //   // 使用 setTimeout 实现10秒延迟
  //   setTimeout(() => {
  //   initValue.value = ResoSynapsexmind;
  //   }, 50000); // 10000毫秒 = 10秒
  //   assistantResponse = ResoSynapsexintroduction1;
  // } else {
  //   assistantResponse = generateRandomResponse();
  // }


  const formData = new FormData();
  formData.append('text', inputMessage.value);
  if (fileList.value instanceof File) {
    formData.append('files', fileList.value);
  }
  inputMessage.value = '';
  fileList.value = [];


  await axios.post(apiUrls.MDAIGC_API_URL, formData, {
  headers: {
    'Content-Type': 'multipart/form-data',
  },
  }).then((response) => {
    if(response.status == 200) {
      if (response.data.content){
        assistantResponse = response.data.content;
      }
      if (response.data.markdown){
        initValue.value = response.data.markdown;
        mm.value?.fit();
      }
      else{
        initValue.value = ResoSynapsexmind;
        mm.value?.fit();
      }
    }
    else {
      return;
    }
  }).catch((error) => {
    console.error('Error:', error);
    return;
  });
  messages.value.push({ type: 'assistant', content: '', streaming: enableStreaming.value });

  // Create new abort controller for this streaming session
  streamingController.value = new AbortController();

  if (enableStreaming.value) {
    try {
      for (let i = 0; i < assistantResponse.length; i++) {
        // Check if streaming was stopped
        if (streamingController.value.signal.aborted) {
          break;
        }

        await new Promise(resolve => setTimeout(resolve, 30));
        if (assistantResponse) {
          messages.value[messages.value.length - 1].content += assistantResponse[i];
        }
        await nextTick();
        scrollToBottom();
      }
    } catch (error: any) {
      // Handle abort error
      if (error.name === 'AbortError') {
        console.log('Streaming was stopped');
      }
    }
  } else {
    messages.value[messages.value.length - 1].content = assistantResponse;
    await nextTick();
    scrollToBottom();
  }

  messages.value[messages.value.length - 1].streaming = false;
  isSending.value = false;
  streamingController.value = null;
  scrollToBottom();
};

const toggleStreaming = () => {
  enableStreaming.value = !enableStreaming.value;
};

const stopStreaming = () => {
  if (messages.value.length > 0) {
    const lastMessage = messages.value[messages.value.length - 1];
    if (lastMessage.type === 'assistant' && lastMessage.streaming) {
      lastMessage.streaming = false;
      isSending.value = false;
      // Abort the streaming process
      if (streamingController.value) {
        streamingController.value.abort();
      }
    }
  }
};

const copyMessage = async (content: string) => {
  try {
    await navigator.clipboard.writeText(content);
    ElMessage.success('复制成功');
  } catch (err) {
    ElMessage.error('复制失败');
  }
};

const retryMessage = async (message: Message) => {
  const index = messages.value.indexOf(message);
  if (index === -1) return;

  // Store the user message that triggered this assistant message
  const userMessage = messages.value[index - 1];
  if (!userMessage || userMessage.type !== 'user') return;

  // Remove both the user message and assistant message
  messages.value.splice(index - 1, 2);

  // Add user message back to input
  inputMessage.value = userMessage.content;
  if (userMessage.files) {
    fileList.value = [...userMessage.files];
  }

  // Send the message again
  await sendMessage();
};

// Resize handlers
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

  asideWidth.value = Math.min(Math.max(newWidth, 20), 65);
};

const stopResize = () => {
  isResizing.value = false;
  document.removeEventListener('mousemove', handleResize);
  document.removeEventListener('mouseup', stopResize);
  // 触发 fitToScreen 方法
  nextTick(() => {
    mm.value?.fit();
  });
};

// Watchers
watch(() => messages.value, scrollToBottom, { deep: true });
watch(() => fileList.value, scrollToBottom, { deep: true });

// Lifecycle hooks
onMounted(() => {
  nextTick(() => {
    scrollToBottom();
    mm.value?.fit();
  });
});

// Computed properties
const getSendButtonTooltip = computed(() => {
  if (isSending.value) {
    return '正在发送...';
  }
  if (inputMessage.value.trim() === '' && fileList.value.length === 0) {
    return '请输入消息或上传文件';
  }
  return '点击发送消息';
});

// Add these methods
const isImageFile = (filename: string): boolean => {
  const imageExtensions = ['.jpg', '.jpeg', '.png', '.gif', '.webp'];
  return imageExtensions.some(ext => filename.toLowerCase().endsWith(ext));
};

const getFilePreview = (file: UploadFile): string => {
  if (file.url) return file.url;
  if (file.raw) {
    return URL.createObjectURL(file.raw);
  }
  return '';
};

const truncateFileName = (filename: string): string => {
  if (filename.length <= 12) return filename;
  const ext = filename.split('.').pop();
  const name = filename.substring(0, 8);
  return `${name}...${ext}`;
};
</script>

<style scoped>
.chatandbutton {
  padding: 10px;
  background-color: #f5f5f5;
  border-radius: 12px;
  height: 100%;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  border: 2px solid #f5f5f5;
}

.chatandbutton:hover {
  border-color: #409eff;
  background-color: white;
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.15);
}

/* Layout */
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

.el-aside::-webkit-scrollbar {
  display: none;
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

/* Chat Window */
.chat-window {
  display: flex;
  flex-direction: column;
  height: 100%;
  width: 100%;
  background-color: #ffffffe6;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 24px;
  scroll-behavior: smooth;
}

/* Messages */
.message {
  display: flex;
  flex-direction: column;
  margin-bottom: 24px;
  align-items: flex-start;
  min-height: 60px;
  position: relative;
  padding-left: 60px;
  padding-right: 60px;
}

.message-content {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  width: 100%;
  max-width: 85%;
}

.message.user .message-content {
  align-items: flex-end;
}

.message.user .avatar {
  margin-left: 0;
  position: absolute;
  right: 0;
  top: 0;
  border: 2px solid #ffffffe6;
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.2);
}

.message.assistant {
  align-items: flex-start;
}

.message.assistant .avatar {
  position: absolute;
  left: 0;
  top: 0;
  border: 2px solid #67c23a;
  box-shadow: 0 2px 8px rgba(103, 194, 58, 0.2);
}

.avatar {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  margin-bottom: 8px;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.avatar:hover {
  transform: scale(1.05);
}

.bubble {
  max-width: 100%;
  padding: 12px 16px;
  border-radius: 16px;
  margin-bottom: 4px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  animation: fadeIn 0.3s ease;
  will-change: transform;
  backface-visibility: hidden;
  transform: translateZ(0);
  position: relative;
  line-height: 1.6;
}

.message.user .bubble {
  background: linear-gradient(135deg, #409eff 0%, #3a8ee6 100%);
  color: white;
  border-bottom-right-radius: 4px;
}

.message.assistant .bubble {
  background: white;
  color: #303133;
  border-bottom-left-radius: 4px;
}

/* Streaming Content */
.streaming-content {
  white-space: pre-wrap;
  min-height: 24px;
  contain: content;
  line-height: 1.6;
}

.message-text {
  margin-bottom: 8px;
  font-size: 14px;
}

.message-files {
  margin-top: 12px;
  padding-top: 12px;
  border-top: 1px solid rgba(0, 0, 0, 0.08);
}

.message-files .file-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.message-files .file-item {
  width: 90px;
  flex-grow: 0;
  flex-shrink: 0;
}

.message-files .file-preview {
  width: 100%;
  aspect-ratio: 1;
  border-radius: 8px;
  overflow: hidden;
  background-color: rgba(255, 255, 255, 0.9);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  position: relative;
  border: 1px solid rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
}

.message-files .file-preview:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.message-files .file-name {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 4px 6px;
  background: rgba(0, 0, 0, 0.6);
  color: white;
  font-size: 11px;
  text-align: center;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  backdrop-filter: blur(4px);
}

.message-files .file-icon {
  font-size: 28px;
  color: #909399;
}

.bubble-actions {
  display: flex;
  gap: 8px;
  margin-top: 12px;
  padding-top: 12px;
  border-top: 1px solid rgba(0, 0, 0, 0.08);
}

.action-btn {
  padding: 6px 12px;
  font-size: 12px;
  border-radius: 6px;
  transition: all 0.3s ease;
  opacity: 0.9;
  border: 1px solid #409eff;
  display: flex;
  align-items: center;
  gap: 4px;
  background-color: white;
  color: #409eff;
  font-weight: 500;
}

.action-btn:hover {
  opacity: 1;
  background-color: #ecf5ff;
  transform: translateY(-1px);
}

.action-btn:active {
  background-color: #409eff;
  color: white;
  transform: translateY(0);
}

.stop-streaming-btn {
  padding: 6px 12px;
  font-size: 12px;
  border-radius: 6px;
  transition: all 0.3s ease;
  opacity: 0.9;
  border: 1px solid #409eff;
  display: flex;
  align-items: center;
  gap: 4px;
  margin-top: 8px;
  background-color: white;
  color: #409eff;
  font-weight: 500;
}

.stop-streaming-btn:hover {
  opacity: 1;
  background-color: #ecf5ff;
  transform: translateY(-1px);
}

.stop-streaming-btn:active {
  background-color: #409eff;
  color: white;
  transform: translateY(0);
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(8px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* Input Area */
.section {
  background: white;
  border-top: 1px solid #e0e0e0;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.05);
  margin: 0;
}

.file-list-section {
  padding: 12px;
  background: #f8f9fa;
  border-bottom: 1px solid #e0e0e0;
  max-height: 120px;
  overflow-y: auto;
}

.chat-section {
  padding: 16px;
  background: white;
}

.area {
  width: 100%;
}

.wrapper {
  position: relative;
  width: 100%;
}

.input-box {
  width: 100%;
  height: 100%;
  border: none;
  background-color: transparent;
}

.input-box :deep(.el-textarea__inner) {
  background-color: transparent;
  line-height: 1.6;
  font-size: 14px;
  overflow-y: auto;
  resize: none;
  scrollbar-width: none;
  -ms-overflow-style: none;
  height: 100%;
  border: none;
  /* 新增：移除所有交互状态样式 */
  box-shadow: none !important;
}

.input-box :deep(.el-textarea__inner:focus),
.input-box :deep(.el-textarea__inner:hover) {
  background-color: transparent;
  border: none;
  /* 新增：禁用聚焦时的阴影效果 */
  box-shadow: none !important;
}

/* 新增：禁用输入框的外轮廓 */
.input-box :deep(.el-textarea__inner) {
  outline: none !important;
}

/* Buttons */
.button-group {
  width: 180px;
  bottom: 0;
  padding: 0;
}

.icon-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  bottom: 0;
  padding: 0;
}

.action-icon {
  font-size: 24px;
  color: #909399;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
}

.action-icon:hover {
  color: #409eff;
}

.action-icon.is-active {
  color: #909399;
}

.action-icon.is-active:hover {
  color: #409eff;
}

.action-icon.disabled {
  color: #c0c4cc !important;
  cursor: not-allowed;
  pointer-events: none;
  opacity: 0.6;
}

.action-icon.is-loading {
  animation: rotate 1s linear infinite;
  color: #409eff !important;
}

/* File List */
.file-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.file-item {
  width: 90px;
  flex-grow: 0;
  flex-shrink: 0;
}

.file-preview {
  width: 100%;
  aspect-ratio: 1;
  border-radius: 6px;
  overflow: hidden;
  background-color: #f5f7fa;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  position: relative;
  border: 1px solid #e0e0e0;
  transition: all 0.3s ease;
}

.file-preview:hover {
  border-color: #409eff;
  box-shadow: 0 2px 6px rgba(64, 158, 255, 0.1);
  transform: translateY(-1px);
}

.file-thumbnail {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.file-icon {
  font-size: 28px;
  color: #909399;
}

.file-name {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 4px;
  background: rgba(0, 0, 0, 0.5);
  color: white;
  font-size: 11px;
  text-align: center;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.delete-icon {
  position: absolute;
  top: 4px;
  right: 4px;
  width: 18px;
  height: 18px;
  border-radius: 50%;
  background: rgba(0, 0, 0, 0.5);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.delete-icon:hover {
  background: rgba(245, 108, 108, 0.9);
}

/* Resize Handle */
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

/* Animations */
@keyframes typing {
  to {
    opacity: 1;
    transform: none;
  }
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }

  to {
    transform: rotate(360deg);
  }
}

/* Scrollbar */
.el-aside::-webkit-scrollbar,
.chat-messages::-webkit-scrollbar {
  display: none;
}

/* 在 style 部分添加 */
.action-icon-wrapper {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
}

.stop-streaming-btn {
  padding: 4px 8px;
  font-size: 12px;
  border-radius: 4px;
  transition: background-color 0.15s ease, color 0.15s ease;
  opacity: 0.8;
  border: 1px solid #409eff;
  display: flex;
  align-items: center;
  gap: 4px;
  margin-top: 4px;
  background-color: white;
  color: #409eff;
}

.stop-streaming-btn:hover {
  opacity: 1;
  background-color: #ecf5ff;
}

.stop-streaming-btn:active {
  background-color: #409eff;
  color: white;
}

.stop-streaming-btn:focus {
  outline: none;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}

.message-actions {
  display: flex;
  gap: 8px;
  margin-top: 4px;
}

.bubble-actions {
  display: flex;
  gap: 8px;
  margin-top: 8px;
  padding-top: 8px;
  border-top: 1px solid rgba(0, 0, 0, 0.1);
}

.action-btn {
  padding: 4px 8px;
  font-size: 12px;
  border-radius: 4px;
  transition: background-color 0.15s ease, color 0.15s ease;
  opacity: 0.8;
  border: 1px solid #409eff;
  display: flex;
  align-items: center;
  gap: 4px;
  background-color: white;
  color: #409eff;
}

.action-btn:hover {
  opacity: 1;
  background-color: #ecf5ff;
}

.action-btn:active {
  background-color: #409eff;
  color: white;
}

.action-btn:focus {
  outline: none;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}

.message-text {
  margin-bottom: 8px;
}

.message-files {
  margin-top: 8px;
  padding-top: 8px;
  border-top: 1px solid rgba(0, 0, 0, 0.1);
}

.message-files .file-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.message-files .file-item {
  width: 80px;
  flex-grow: 0;
  flex-shrink: 0;
}

.message-files .file-preview {
  width: 100%;
  aspect-ratio: 1;
  border-radius: 6px;
  overflow: hidden;
  background-color: rgba(255, 255, 255, 0.8);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  position: relative;
  border: 1px solid rgba(0, 0, 0, 0.1);
}

.message-files .file-name {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 2px 4px;
  background: rgba(0, 0, 0, 0.5);
  color: white;
  font-size: 10px;
  text-align: center;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.message-files .file-icon {
  font-size: 24px;
  color: #909399;
}
</style>