<template>
  <LeftNavigation :LeftNavigation="props.LeftNavigation" :isLeftNavigationVisible="isLeftNavigationVisible"
    v-model="isLeftNavigationVisible" />
  <LoginReg :isLoginandRegVisible="isLoginandRegVisible" v-model="isLoginandRegVisible" />
  <ApplyUrl :blockList="props.blockList" :ApplyUrltext="props.ApplyUrltext" :isApplyVisible="isApplyVisible"
    v-model="isApplyVisible" />
  <div class="LeftNavigation-button" @click="toggleBackdrop('isLeftNavigationVisible')">
    <svg t="1740581004086" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
      p-id="12171" width="28" height="28">
      <path
        d="M982.109091 44.218182c-16.290909-11.636364-34.909091-11.636364-51.2-4.654546l-30.254546 16.290909-607.418181 321.163637v53.527273l558.545454-295.563637 53.527273-27.927273 46.545454-25.6h2.327273v2.327273l-137.309091 681.890909c-2.327273 6.981818-6.981818 13.963636-13.963636 18.618182-6.981818 4.654545-13.963636 4.654545-23.272727 2.327273l-11.636364-4.654546-44.218182-13.963636-260.654545-79.127273v-4.654545l363.054545-418.909091 23.272727-25.6-13.963636-13.963636-20.945454-18.618182-134.981819 118.690909L418.909091 556.218182l-128 114.036363h-2.327273l-13.963636-4.654545-48.872727-18.618182-137.309091-55.854545c-9.309091-4.654545-16.290909-13.963636-18.618182-23.272728 0-11.636364 4.654545-20.945455 13.963636-25.6l160.581818-86.10909v-53.527273l-183.854545 97.745454c-4.654545 2.327273-9.309091 6.981818-13.963636 11.636364-16.290909 13.963636-23.272727 34.909091-23.272728 58.181818 0 13.963636 4.654545 27.927273 13.963637 39.563637 6.981818 9.309091 18.618182 18.618182 32.581818 25.6l162.909091 65.163636 37.236363 13.963636c16.290909 6.981818 37.236364 2.327273 51.2-9.309091l4.654546-4.654545 125.672727-111.709091 134.981818-118.690909-158.254545 181.527273c-11.636364 11.636364-13.963636 30.254545-9.309091 46.545454 0 2.327273 0 2.327273 2.327273 4.654546 4.654545 13.963636 16.290909 25.6 30.254545 30.254545l288.581818 88.436364 30.254546 9.309091c6.981818 2.327273 13.963636 2.327273 20.945454 2.327272 13.963636 0 25.6-4.654545 37.236364-11.636363 18.618182-11.636364 30.254545-27.927273 34.909091-48.872728L1000.727273 93.090909c2.327273-18.618182-4.654545-37.236364-18.618182-48.872727z m-148.945455 181.527273l-9.309091 39.563636 23.272728-25.6-13.963637-13.963636z"
        fill="#FFFFFF" p-id="12172"></path>
      <path
        d="M954.181818 83.781818l-137.309091 681.890909c-2.327273 6.981818-6.981818 13.963636-13.963636 18.618182-6.981818 4.654545-13.963636 4.654545-23.272727 2.327273l-11.636364-4.654546c0-2.327273 2.327273-4.654545 2.327273-6.981818l134.981818-667.927273 46.545454-25.6s0-2.327273 2.327273 2.327273c0-2.327273 0-2.327273 0 0z"
        fill="#000000" p-id="12173"></path>
      <path
        d="M444.509091 991.418182c-4.654545 0-11.636364 0-16.290909-2.327273-20.945455-6.981818-32.581818-25.6-32.581818-46.545454v-107.054546c0-16.290909 9.309091-32.581818 23.272727-41.890909 13.963636-9.309091 32.581818-9.309091 46.545454-2.327273l60.509091 30.254546c13.963636 6.981818 23.272727 18.618182 25.6 32.581818 2.327273 13.963636 0 30.254545-9.309091 41.890909l-60.50909 76.8c-9.309091 11.636364-23.272727 18.618182-37.236364 18.618182z m0-158.254546H442.181818v107.054546s0 2.327273 2.327273 2.327273h2.327273l60.509091-76.8 9.30909-23.272728-9.30909 20.945455-62.836364-30.254546z"
        fill="#FFFFFF" p-id="12174"></path>
    </svg>
  </div>
  <div class="button-container">
    <div v-if="!userStore.userId" class="button-container LoginandReg" @click="toggleBackdrop('isLoginandRegVisible')">
      登录
    </div>
    <div v-else class="button-container UserCenter" @click="goToUserCenter">
      个人中心
    </div>
    <div class="button-container apply" @click="toggleBackdrop('isApplyVisible')">
      申请收录
    </div>
  </div>
  <div class="modal-container" v-if="isUserCenterVisible">
    <div class="user-center-dialog">
      <div class="dialog-header">
        <h2>个人中心</h2>
        <span class="close-btn" @click="isUserCenterVisible = false">×</span>
      </div>
      <div class="dialog-body">
        <div class="user-profile">
          <div class="avatar-container">
            <el-image :src="userInfo.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'"
              fit="cover" class="avatar-image" />
          </div>
          <div class="info-container">
            <div class="info-item">
              <span class="info-label">用户名：</span>
              <span class="info-value">{{ userInfo.username }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">电子邮箱：</span>
              <span class="info-value">{{ userInfo.email }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">手机号码：</span>
              <span class="info-value">{{ userInfo.phone }}</span>
            </div>
          </div>
          <div class="button-group">
            <el-button plain @click="handleEdit">修改信息</el-button>
            <el-button plain @click="handleLogout">退出登录</el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, defineProps, onMounted } from 'vue'
import ApplyUrl from './ApplyUrl.vue'
import LoginReg from './LoginReg.vue'
import LeftNavigation from './LeftNavigation.vue'
import type { BlockType } from '@/api/model/blockModel'
import type { PropType } from 'vue'
import type { leftNavigationType } from '@/api/model/leftNavigationModel'
import { useUserStore } from '@/store/User'
import avatarImage from '@/assets/img/avatar.jpg'
import { ElMessage} from 'element-plus'
interface UserInfo {
  avatar: string
  username: string
  email: string
  phone: string
}

const userStore = useUserStore();
const props = defineProps({
  blockList: {
    type: Array as () => BlockType[],
    required: true
  },
  LeftNavigation: {
    type: Object as PropType<leftNavigationType>,
    required: true
  },
  ApplyUrltext: {
    type: String,
    required: true
  }
});
const isLeftNavigationVisible = ref(false);
const isLoginandRegVisible = computed(() => userStore.isLoginandRegVisible);
const isApplyVisible = ref(false);
const isUserCenterVisible = ref(false)

const userInfo = ref<UserInfo>({
  avatar: '',
  username: '',
  email: '',
  phone: '',
})

const toggleBackdrop = (stateKey: string) => {
  if (stateKey === 'isLeftNavigationVisible') {
    isLeftNavigationVisible.value = !isLeftNavigationVisible.value;
    console.log(isLeftNavigationVisible.value)
  } else if (stateKey === 'isLoginandRegVisible') {
    userStore.isLoginandRegVisible = !userStore.isLoginandRegVisible;
  } else if (stateKey === 'isApplyVisible') {
    if (userStore.userId) {
      isApplyVisible.value = !isApplyVisible.value;
    }
    else {
      userStore.isLoginandRegVisible = !userStore.isLoginandRegVisible;
    }
  }
};

// 获取用户信息
const fetchUserInfo = async () => {
  try {
    return {
      data: {
        avatar: avatarImage,
        username: 'ResoSynapse',
        email: 'contact@example.com',
        phone: '13800138000'
      }
    }
  } catch (error) {
    throw new Error('获取用户信息失败')
  }
}

// 进入个人中心
const goToUserCenter = async () => {
  try {
    isUserCenterVisible.value = true
    const response: any = await fetchUserInfo()
    userInfo.value = {
      avatar: response.data.avatar,
      username: response.data.username,
      email: response.data.email,
      phone: response.data.phone
    }
  } catch (error) {
    console.error('用户信息获取错误:', error)
  }
}

// 初始化用户信息
onMounted(async () => {
  try {
    const response: any = await fetchUserInfo()
    userInfo.value = {
      avatar: response.data.avatar,
      username: response.data.username,
      email: response.data.email,
      phone: response.data.phone
    }
  } catch (error) {
    console.error('初始化用户信息失败:', error)
  }
})

// 处理退出登录
const handleLogout = () => {
  userStore.userId = 0
  userStore.UserName = ''
  userStore.profilePhotoUrl = ''
  isUserCenterVisible.value = false
  userStore.setUserId(0)
  ElMessage.success('退出登录成功')
}

// 处理修改信息
const handleEdit = () => {
  // TODO: 实现修改信息的逻辑
  console.log('修改信息')
}
</script>

<style>
.LeftNavigation-button {
  position: fixed;
  left: 25px;
  top: 35px;
  background-color: transparent;
  border: 1px solid transparent;
  cursor: pointer;
  transition: all 0.3s ease;
  z-index: 4;
}

.LeftNavigation-button:hover {
  transform: scale(1.2);
}

.LeftNavigation-button:active {
  transform: scale(1.2);
}

.button-container {
  position: relative;
  left: 30px;
  margin-top: 15px;
  display: flex;
  gap: 15px;
  z-index: 4;
}

.button-container.LoginandReg {
  background: transparent;
  color: white;
  border: none;
  cursor: pointer;
  font-size: 16px;
  font-weight: bold;

}

.button-container .UserCenter {
  background: transparent;
  color: white;
  border: none;
  cursor: pointer;
  font-size: 16px;
  font-weight: bold;
}

.button-container.apply {
  background: transparent;
  color: white;
  border: none;
  cursor: pointer;
  font-size: 16px;
  font-weight: bold;
}

.modal-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: rgba(0, 0, 0, 0.2);
  z-index: 9999;
}

.user-center-dialog {
  position: relative;
  width: 500px;
  background: white;
  border-radius: 10px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.dialog-header {
  padding: 24px 24px 16px;
  border-bottom: 1px solid #ebeef5;
  background: linear-gradient(135deg, #f6f8fc 0%, #ffffff 100%);
  display: flex;
  justify-content: space-between;
  align-items: center;

  h2 {
    margin: 0;
    font-size: 20px;
    font-weight: 600;
    color: #1f2329;
  }

  .close-btn {
    font-size: 24px;
    color: #909399;
    cursor: pointer;
    transition: color 0.3s;

    &:hover {
      color: #1f2329;
    }
  }
}

.dialog-body {
  padding: 24px;
  background-color: #ffffff;
}

.user-profile {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 24px;
  padding: 16px 0;
}

.avatar-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  margin-bottom: 8px;
}

.avatar-image {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  border: 3px solid #ffffff;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;
}

.avatar-image:hover {
  transform: scale(1.05);
}

.user-name {
  font-size: 20px;
  font-weight: 600;
  color: #1f2329;
  letter-spacing: 0.5px;
}

.info-container {
  width: 100%;
  max-width: 400px;
  margin: 24px auto 0;
  padding: 0 20px;
}

.info-item {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  padding: 12px;
  background-color: #f6f8fc;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.info-item:hover {
  background-color: #eef2f7;
  transform: translateX(4px);
}

.info-label {
  width: 100px;
  color: #606266;
  font-weight: 500;
}

.info-value {
  flex: 1;
  color: #1f2329;
  word-break: break-all;
}

.button-group {
  display: flex;
  gap: 12px;
  margin-top: 24px;
  width: 100%;
  justify-content: center;
}

.button-group .el-button {
  min-width: 120px;
  padding: 10px 20px;
  font-size: 14px;
  border-radius: 4px;
  transition: all 0.3s ease;
  background-color: white;
  border-color: #dcdfe6;
  color: #606266;
}

.button-group .el-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  background-color: white;
  border-color: #c0c4cc;
  color: #606266;
}

.button-group .el-button:active {
  background-color: white;
  border-color: #c0c4cc;
  color: #606266;
}

/* 响应式设计 */
@media screen and (max-width: 768px) {
  .user-center-dialog {
    width: 90%;
    margin: 0 auto;
  }

  .dialog-body {
    padding: 16px;
  }

  .info-container {
    padding: 0 16px;
  }

  .info-item {
    margin-bottom: 16px;
    padding: 10px;
  }
}
</style>
