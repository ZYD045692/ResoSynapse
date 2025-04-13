<template>
  <div>
    <div class="modal-container" v-if="props.isApplyVisible">
      <div class="form-container">
        <h2>申请收录</h2>
        <h4>{{ props.ApplyUrltext }}</h4>
        <form @submit.prevent="handleSubmit">
          <div class="form-group">
            <label for="widget_url">*URL链接地址:</label>
            <input type="text" id="url" v-model="formData.widget_url" placeholder="完整链接或域名" />
          </div>
          <div class="form-group">
            <label for="category">*选择分类:</label>
            <select id="category" v-model="formData.block_id">
              <option value="">请选择</option>
              <option v-for="Block in props.blockList" :key="Block.name" :value="Block.id">
                {{ Block.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label for="website-name">*网站名称:</label>
            <input type="text" id="website-name" v-model="formData.name" placeholder="填写网站名称" />
          </div>
          <div class="form-group">
            <label for="logo">网站图标:</label>
            <input type="text" id="logo" v-model="formData.icon_url" placeholder="填写网站的URL地址" />
          </div>
          <div class="form-group">
            <label for="captcha">*验证码:</label>
            <div class="captcha-group">
              <input type="text" id="captcha" v-model="formData.captcha" placeholder="验证码" />
              <img :src="Captcha_path" alt="验证码" @click="refreshCaptcha" class="captcha-image" />
            </div>
          </div>
          <div class="button-group">
            <button type="button" @click="handleReturn">返回</button>
            <button type="submit">提交</button>
          </div>

        </form>
        <div v-if="submitted" class="result-message">
          提交成功！我们会尽快处理您的反馈～
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, defineEmits, defineProps } from 'vue';
import captcha_path from '@/assets/img/background.jpg';
import { apiUrls } from '@/api/api'
import type { BlockType } from '@/api/model/blockModel'
import axios from 'axios';
import { watch } from 'vue'
import { ElMessage } from 'element-plus'
const emit = defineEmits(['update:modelValue']);
const submitted = ref(false);
const Captcha_path = ref(captcha_path);
const props = defineProps({
  modelValue: Boolean,
  ApplyUrltext: {
    type: String,
    required: false
  },
  isApplyVisible: {
    type: Boolean,
    required: true
  },
  blockList: {
    type: Array as () => BlockType[], // 使用 Array 构造函数
    required: true
  }
});
watch(
  () => props.isApplyVisible,
  (visible) => {
    if (visible) {
      refreshCaptcha()
    }
  },
  { immediate: true }
)

// 表单数据
const formData = reactive({
  widget_url: '',
  block_id: '',
  name: '',
  icon_url: '',
  captcha: '',
});


// 提交表单
const handleSubmit = async () => {
  // 基本验证
  if (
    !formData.widget_url ||
    !formData.block_id ||
    !formData.name ||
    !formData.icon_url ||
    !formData.captcha
  ) {
    ElMessage.warning('请填写所有必填项!');
    return;
  }

  // 在这里处理表单提交逻辑
  const CheckApplyUrlCaptchapostdata = {
    codeInput: formData.captcha,
    userId: 1
  }
  const CheckApplyUrlCaptcharesopnse = await axios.post(apiUrls.CheckApplyUrlCaptcha_API_URL, CheckApplyUrlCaptchapostdata,
  { withCredentials:true }
  );
  const CheckApplyUrlCaptchastateCode = CheckApplyUrlCaptcharesopnse.data.stateCode;
  switch (CheckApplyUrlCaptchastateCode) {
    case 0:
      ElMessage.warning('验证码错误');
      return;
    case 1:
      ElMessage.warning('验证码超时');
      return;
    case 2:
      break;
    default:
      ElMessage.error(`未知错误，状态码 ${CheckApplyUrlCaptchastateCode}`);
      return;
  }

  const ApplyUrlpostdata = {
    widgetUrl: formData.widget_url,
    blockId: formData.block_id,
    name: formData.name,
    iconUrl: formData.icon_url,
  }
  const ApplyUrlresopnse = await axios.post(apiUrls.ApplyUrl_API_URL, ApplyUrlpostdata);
  if (ApplyUrlresopnse.status == 200) {
    if (ApplyUrlresopnse.data.stateCode == 1) {
      ElMessage.success('提交成功');
      submitted.value = true;
      // 关闭模态框
      emit('update:modelValue', false);
      // 清除表单数据
      Object.assign(formData, {
        url: '',
        category: '',
        websiteName: '',
        logo: '',
        captcha: '',
      });
    }
    else {
      ElMessage.error('抱歉，提交失败，请稍后再试！');
    }
  }
};

const refreshCaptcha = async () => {
  try {
    const response = await axios.post(
      apiUrls.GetApplyUrlCaptcha_API_URL,
      {
        userId: 1
      },
      { withCredentials:true }
    )

    // 调试输出
    console.log('验证码接口响应:', {
      status: response.status,
      data: response.data,
      headers: response.headers
    })

    if (response.status === 200 && response.data) {
      if (response.data.image.includes('base64')) {
        Captcha_path.value = response.data.image
      }
      // 情况2：返回纯base64字符串
      else {
        // 根据实际图片类型调整MIME类型
        Captcha_path.value = `data:image/png;base64,${response.data.image}`
      }
    }
  } catch (error) {
    console.error('验证码请求失败:', error)
    Captcha_path.value = captcha_path
  }
}

// 返回
const handleReturn = () => {
  // 清除表单数据
  Object.assign(formData, {
    url: '',
    category: '',
    websiteName: '',
    logo: '',
    captcha: '',
  });
  // 关闭模态框
  emit('update:modelValue', false);
};
</script>

<style>
.modal-container {
  position: fixed;
  /* 固定模态框位置，使其悬浮在页面上 */
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  /* 背景透明，可根据需要调整 */
  background-color: rgba(0, 0, 0, 0.2);
  z-index: 999;
}

.form-container {
  background: #ffffff;
  border-radius: 8px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  padding: 2rem;
  width: 400px;
  position: relative;
  z-index: 1001;
  /* 确保内容在模态框内部 */
}

.form-container h2 {
  text-align: center;
  margin-bottom: 1.5rem;
  color: #333;
}

.form-group {
  margin-bottom: 1.2rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  color: #555;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 0.8rem;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 0.9rem;
}

.captcha-group {
  display: flex;
  align-items: center;
}

.captcha-group input {
  flex: 1;
  margin-right: 0.5rem;
}

.captcha-image {
  cursor: pointer;
  width: 100px;
  height: 40px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.button-group {
  display: flex;
  justify-content: space-between;
  margin-top: 1rem;
}

button {
  padding: 0.8rem;
  background: #4caf50;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
  transition: background 0.3s;
}

button:hover {
  background: #45a049;
}

button[type="button"] {
  background: #008cba;
}

button[type="button"]:hover {
  background: #006b9a;
}

.result-message {
  text-align: center;
  font-size: 18px;
  color: green;
}
</style>