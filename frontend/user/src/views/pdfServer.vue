<template>
  <div class="container">
    <!-- 左边栏 -->
    <div class="sidebar">
      <el-col style="display: flex; gap: 10px; justify-content: space-between;">
        <el-select v-model="sortField" placeholder="请选择排序方式" style="width: 150px;">
          <el-option label="按服务名称" value="serviceName"></el-option>
          <el-option label="按创建时间" value="createdTime"></el-option>
          <el-option label="按修改时间" value="lastTime"></el-option>
        </el-select>
        <div style="display: flex; gap: 10px;">
          <el-button @click="toggleSortOrder" style="padding: 0 10px;margin: 0;">
            <el-icon v-if="sortOrder === 'asc'" style="margin-right: 4px;">
              <Top />
            </el-icon>
            <el-icon v-else style="margin-right: 4px;">
              <Bottom />
            </el-icon>
            {{ sortOrder === 'asc' ? '升序' : '降序' }}
          </el-button>
          <el-button @click="createService" style="padding: 0 10px;margin: 0;">
            <el-icon style="margin-right: 4px;">
              <Plus />
            </el-icon>新建
          </el-button>
        </div>
      </el-col>
      <div class="tag-list">
        <el-tag v-for="service in sortedPdfServiceList" :key="service.serviceId" class="service-tag"
          @click="selectService(service)">
          <div class="tag-content">
            <span class="service-name">{{ service.serviceName }}</span>
            <div class="action-wrapper">
              <el-popconfirm title="确定要删除该服务吗？" confirm-button-text="确认" cancel-button-text="取消"
                @confirm="deleteService(service.serviceId)">
                <template #reference>
                  <el-button class="delete-btn" link type="danger" @click.stop>
                    <el-icon>
                      <Close />
                    </el-icon>
                  </el-button>
                </template>
              </el-popconfirm>
            </div>
          </div>
        </el-tag>
      </div>
    </div>
    <!-- 右边栏 -->
    <div class="content" v-if="Service && ServiceDetailList">
      <el-row style="display: flex; justify-content: flex-end;">
        <el-col :lg="6" :md="6" :sm="10" :xs="6" style="display: flex; justify-content: end; padding: 10px 0;">
          <el-popover placement="bottom" :width="200" trigger="click">
            <template #reference>
              <el-button>导出</el-button>
            </template>
            <div style="display: flex; flex-direction: column; gap: 8px;">
              <el-button @click="handleExport('pdf')">导出PDF</el-button>
              <el-button @click="handleExport('zip')">导出ImagesZip</el-button>
            </div>
          </el-popover>
        </el-col>
      </el-row>
      <div class="ofset">
        <VueDraggable class="drag-box" v-model="ServiceDetailList" :animation="150" ghostClass="ghost" group="people"
          draggable=".drag-item" handle=".drag-handle" @update="onUpdate" @move="onMove">
          <div v-for="ServiceDetail in ServiceDetailList" :key="ServiceDetail.positionIndex" class="drag-item">
            <div v-if="ServiceDetail.id === 0" class="ServiceDetailsytle drag-handle">
              <el-upload class="avatar-uploader" :action="apiUrls.AddPage_API_URL" :show-file-list="false"
                :before-upload="(file: File) => handleFileUpload(file, Service?.serviceId, ServiceDetail?.positionIndex)"
                accept="image/*,application/pdf">
                <el-icon class="avatar-uploader-icon">
                  <Plus />
                </el-icon>
              </el-upload>
              <p class="position-index">{{ ServiceDetail.positionIndex }}</p>
            </div>

            <div v-else class="ServiceDetailsytle drag-handle">
              <img :src="ServiceDetail.data" :alt="String(ServiceDetail.id)" />
              <div class="button-group">
                <el-upload class="action-btn" :action="apiUrls.AddPage_API_URL" :show-file-list="false"
                  :before-upload="(file: File) => handleFileUpload(file, Service?.serviceId, ServiceDetail?.positionIndex)"
                  accept="image/*,application/pdf">
                  <el-button type="primary" :icon="Upload" circle />
                </el-upload>
                <el-button type="danger" :icon="Delete"
                  @click.stop="deleteImage(Service.serviceId, ServiceDetail.positionIndex)" circle />
              </div>
              <p class="position-index">{{ ServiceDetail.positionIndex }}</p>
            </div>
          </div>
        </VueDraggable>
      </div>
    </div>
    <div class="content" v-else>
      <!-- 如果没有选中服务，右边栏为空白 -->
    </div>
  </div>

  <!-- 创建服务对话框 -->
  <el-dialog v-model="dialogVisible" title="创建新服务" width="30%" :close-on-click-modal="false"
    :close-on-press-escape="false" :show-close="false">
    <el-form>
      <el-form-item label="服务名称">
        <el-input v-model="serviceName" placeholder="请输入服务名称" />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="handleCancel">取消</el-button>
        <el-button type="primary" @click="handleCreateService">创建</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import type { pdfServiceType } from "../api/model/pdfServiceModel";
import { PdfServiceList } from '../mock/temp/pdfServiceList';
import type { PdfServiceDetailType } from "../api/model/pdfServiceDetailModel";
import { PdfServiceDetailList } from "../mock/temp/pdfServiceDatailList";
import { VueDraggable } from 'vue-draggable-plus';
import { reactive, ref, computed, onMounted } from 'vue';
import { apiUrls } from "../api/api";
import { ElMessage } from 'element-plus';
import axios from 'axios';
import { Delete, Upload, Plus, Close, Top, Bottom } from '@element-plus/icons-vue'
import { useUserStore } from '@/store/User';
import { getPdfService } from '../api/PdfServerAPi';

const userStore = useUserStore();
const pdfServiceList = reactive<pdfServiceType[]>([]);

// 在组件挂载时获取数据
onMounted(async () => {
  try {
    console.log('Fetching services for user:', userStore.userId);
    const data = await getPdfService(userStore.userId);
    console.log('Received data:', data);

    if (Array.isArray(data)) {
      pdfServiceList.splice(0);
      data.forEach(item => {
        console.log('Adding service:', item);
        pdfServiceList.push(item);
      });
    } else {
      console.warn('Invalid response format:', data);
      ElMessage.warning('获取服务列表失败');
      // 添加测试数据
      pdfServiceList.push({
        serviceId: 1,
        serviceName: '测试服务',
        createdTime: new Date().toISOString(),
        lastTime: new Date().toISOString()
      });
    }
  } catch (error) {
    console.error('Error fetching services:', error);
    ElMessage.error('获取服务列表失败');
    // 添加测试数据
    pdfServiceList.push({
      serviceId: 1,
      serviceName: '测试服务',
      createdTime: new Date().toISOString(),
      lastTime: new Date().toISOString()
    });
  }
});

const Service = ref<pdfServiceType | null>(null);
const ServiceDetailList = ref<PdfServiceDetailType[] | null>(null);
// 排序相关变量
const sortField = ref<string>('serviceName'); // 默认按服务名称排序
const sortOrder = ref<'asc' | 'desc'>('asc'); // 默认升序

// 计算排序后的服务列表
const sortedPdfServiceList = computed(() => {
  return [...pdfServiceList].sort((a, b) => {
    const field = sortField.value as keyof typeof a; // 断言为有效的键
    if (a[field] < b[field]) {
      return sortOrder.value === 'asc' ? -1 : 1;
    }
    if (a[field] > b[field]) {
      return sortOrder.value === 'asc' ? 1 : -1;
    }
    return 0;
  });
});

// 点击服务列表项时更新选中的服务
const selectService = async (service: pdfServiceType) => {
  Service.value = service;
  try {
    const response = await axios.get(apiUrls.DetailedPdfService_API_URL, { params: { serviceId: service.serviceId } });

    if (response.data.stateCode === 1) {
      ServiceDetailList.value = response.data.imagelist
        .sort((a: PdfServiceDetailType, b: PdfServiceDetailType) => a.positionIndex - b.positionIndex)
        .map((item: PdfServiceDetailType) => ({
          ...item,
          data: `data:image/png;base64,${item.data}`
        }));
      const nullPdfServiceDetail: PdfServiceDetailType = {
        id: 0,
        positionIndex: (ServiceDetailList.value || []).length + 1,
        data: ''
      };
      const hasId0 = ServiceDetailList.value?.some(item => item.id === 0);
      if (!hasId0) {
        ServiceDetailList.value?.push(nullPdfServiceDetail);
      }
    } else {
      ElMessage.error('获取服务详情失败');
      ServiceDetailList.value = null;
    }
  } catch (error) {
    console.error('Error fetching service details:', error);
    ElMessage.error('获取服务详情失败');
    ServiceDetailList.value = null;
  }
};

const deleteService = async (serviceId: number) => {
  try {
    const params = {
      serviceId: serviceId
    }
    // 发送删除请求
    const response = await axios.get(apiUrls.DeleteService_API_URI, { params })
    if (response.status === 200) {
      switch (response.data.stateCode) {
        case 0:
          ElMessage.error('服务删除失败');
          break;
        case 1:
          ElMessage.success('服务删除成功');
          pdfServiceList.splice(
            pdfServiceList.findIndex(item => item.serviceId === serviceId),
            1
          );
          break;
        default:
          ElMessage.error('未知错误');
      }
    }
  } catch (error) {
    console.error('Error deleting service:', error);
    ElMessage.error('服务删除失败');
  }
};

// 切换排序顺序
const toggleSortOrder = () => {
  sortOrder.value = sortOrder.value === 'asc' ? 'desc' : 'asc';
};
const onMove = (event: any) => {
  const clonedDataId = event.clonedData.id;
  const dataId = event.data.id;
  return !(dataId === 0 || clonedDataId === 0);
};
const onUpdate = (event: any) => {
  console.log("Update:", event);
  let updatedServiceDetailList: PdfServiceDetailType[];

  if (ServiceDetailList.value) {
    updatedServiceDetailList = ServiceDetailList.value.filter(item => item.id !== 0);
  } else {
    updatedServiceDetailList = [];
  }

  const nullPdfServiceDetail: PdfServiceDetailType = {
    id: 0,
    positionIndex: updatedServiceDetailList.length + 1,
    data: ''
  };
  updatedServiceDetailList.push(nullPdfServiceDetail);
  ServiceDetailList.value = updatedServiceDetailList;

  // 收集需要更新的图片位置
  const updates = updatedServiceDetailList
    .filter(item => item.id !== 0)
    .reduce((acc, item, index) => {
      acc[item.id] = index + 1;
      return acc;
    }, {} as Record<number, number>);

  if (Object.keys(updates).length > 0) {
    console.log("需要更新的图片位置:", updates);
  }
  axios.post(apiUrls.MovePage_API_URL, updates);
  // 更新所有图片的位置索引
  updatedServiceDetailList.forEach((detail: PdfServiceDetailType, index: number) => {
    detail.positionIndex = index + 1;
  });
};

// const onMove = (event: any) => {
//   console.log("Move:", event);
//   // 在这里添加你需要的逻辑，例如阻止某些拖拽行为
//   return true; // 返回 true 允许拖拽，返回 false 阻止拖拽
// };

const handleExport = async (type: 'pdf' | 'zip') => {
  if (!Service.value) return;

  try {
    const url = type === 'pdf' ? apiUrls.DownloadPdf_API_URL : apiUrls.DownloadImages_API_URL;
    const response = await axios.post(url,
      { serviceId: Service.value.serviceId },
      {
        responseType: 'blob',
        headers: {
          'Content-Type': 'application/json'
        }
      }
    );

    if (response.data) {
      const blob = new Blob([response.data], {
        type: type === 'pdf' ? 'application/pdf' : 'application/zip'
      });
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.download = `${Service.value.serviceName}.${type}`;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      window.URL.revokeObjectURL(url);
      ElMessage.success('导出成功');
    } else {
      ElMessage.error('导出失败');
    }
  } catch (error) {
    console.error('Export error:', error);
    ElMessage.error('导出失败');
  }
};

const deleteImage = async (serviceId: number, positionIndex: number) => {
  const params = {
    serviceId: serviceId,
    positionIndex: positionIndex
  }
  const response = await axios.get(apiUrls.DeletePage_API_URL, { params })
  if (response.status === 200) {
    switch (response.data.stateCode) {
      case 0:
        ElMessage.error('删除失败');
        break;
      case 1:
        if (ServiceDetailList.value) { // 检查是否为null
          ServiceDetailList.value = ServiceDetailList.value.filter(
            (item) => item.positionIndex !== positionIndex
          );
          ServiceDetailList.value.forEach((detail: PdfServiceDetailType, index: number) => {
            detail.positionIndex = index + 1;
          });
        }
        ElMessage.success('删除成功');
        break;
      default:
        ElMessage.error('未知错误');
    }
  }
};

const handleFileUpload = (
  file: File,
  serviceId: number | undefined,   // 推荐小驼峰命名
  positionIndex: number | undefined
) => {
  const formData = new FormData();
  formData.append('file', file);
  formData.append('serviceId', serviceId?.toString() || '');
  formData.append('insertIndex', String(positionIndex ?? 1));

  axios.post(apiUrls.AddPage_API_URL, formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  })
    .then(() => {
      ElMessage.success('文件上传成功');
      if (Service.value) {
        selectService(Service.value);
      }
    })
    .catch(() => {
      ElMessage.error('文件上传失败');
    });

  return false; // 阻止默认上传行为
};

const dialogVisible = ref(false)
const serviceName = ref('')

const createService = () => {
  dialogVisible.value = true;
};

const handleCreateService = async () => {
  if (!serviceName.value.trim()) {
    ElMessage.warning('请输入服务名称');
    return;
  }

  try {

    let params = {
      serviceName: serviceName.value,
      userId: userStore.userId,
    };

    const response = await axios.get(apiUrls.CreatePdfService_API_URL, { params });
    if (response.status === 200) {
      switch (response.data.stateCode) {
        case 0:
          ElMessage.error('创建失败');
          break;
        case 1:
          ElMessage.success('服务创建成功');
          // 刷新服务列表
          const newServiceList = await getPdfService(userStore.userId);
          pdfServiceList.length = 0;
          pdfServiceList.push(...newServiceList);
          dialogVisible.value = false;
          serviceName.value = '';
          break;
        case 2:
          ElMessage.warning('服务名称已存在，请重新输入');
          break;
        default:
          ElMessage.error('未知错误');
      }
    }
  } catch (error) {
    console.error('创建服务失败:', error);
    ElMessage.error('创建服务失败');
  }
};

const handleCancel = () => {
  dialogVisible.value = false;
  serviceName.value = '';
};

</script>

<style scoped>
.custom-icon {
  color: black;
}

.container {
  display: flex;
  width: 100%;
  height: 100%;
  overflow: hidden;
  border-radius: 15px;
}

.sidebar {
  width: 25%;
  background: #f5f7fa;
  padding: 20px;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.1);
  overflow-y: auto;
  overflow-x: hidden;
  scrollbar-width: none;
  -ms-overflow-style: none;
  height: 100%;
}

.sidebar::-webkit-scrollbar {
  display: none;
}

.sort-controls {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
  margin-top: 10%;

  .el-select {
    flex: 1;
  }
}

.content {
  flex: 1;
  padding: 10px 20px;
  background-color: #fff;
  overflow-y: auto;
  overflow-x: hidden;
  scrollbar-width: none;
  -ms-overflow-style: none;
  height: 100%;
}

.content::-webkit-scrollbar {
  display: none;
}

.drag-box {
  display: flex;
  flex-wrap: wrap;
  width: 100%;
  justify-content: flex-start;
  gap: calc((100% - 18% * 5 - 5 * 2 * 1px) / 4);
}

.drag-item {
  width: 18%;
  margin: 5px 0;
}

.ServiceDetailsytle {
  position: relative;
  width: 100%;
}

.ServiceDetailsytle img {
  width: 100%;
  height: auto;
  aspect-ratio: 210 / 297;
  border: 1px solid #909399;
  border-radius: 12px;
  transition: filter 0.3s ease;
}

.ServiceDetailsytle .plusicon {
  width: 100%;
  aspect-ratio: 210 / 297;
  border: none;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.action-btn.el-icon-plus {
  width: 100%;
  height: auto;
  aspect-ratio: 210 / 297;
  border: 1px solid #909399;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

.cover-icon-container {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.cover-icon {
  border-radius: 12px;
  width: 100%;
  height: auto;
  aspect-ratio: 1;
  object-fit: cover;
}

.upload-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 30%;
  height: 30%;

  :deep(.el-icon) {
    font-size: 32px;
    color: #909399;
  }
}

.button-group {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  display: none;
  flex-direction: column;
  align-items: center;
  gap: 16px;
}

.ServiceDetailsytle:hover .button-group {
  display: flex;
}

.position-index {
  text-align: center;
}

.tag-list {
  margin-top: 20px;
}

.service-tag {
  --tag-height: 40px;
  width: 100%;
  height: var(--tag-height);
  margin: 8px 0;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s;

  &:hover {
    background: #ecf5ff;
    border-color: #409eff;
  }

  :deep(.el-tag__content) {
    width: 100%;
    height: 100%;
    padding: 0 12px;
    max-width: calc(100% - 24px);
  }
}

.tag-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.service-name {
  flex: 1;
  text-align: left;
  /* 防止文字过长溢出 */
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.action-wrapper {
  margin-left: 8px;
  flex-shrink: 0;
}

:deep(.el-tag) {
  width: 100%;
  margin-bottom: 8px;
}

:deep(.el-tag__content) {
  width: 100%;
}

.delete-btn {
  padding: 0;
  height: auto;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  transition: all 0.2s;

  &:hover {
    background-color: rgba(245, 108, 108, 0.1);
    transform: scale(1.1);
  }
}

.avatar-uploader {
  width: 100%;
  aspect-ratio: 210 / 297;
}

.avatar-uploader :deep(.el-upload) {
  border: 1px dashed #d9d9d9;
  border-radius: 12px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s;
}

.avatar-uploader :deep(.el-upload:hover) {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  width: 50%;
  height: auto;
  aspect-ratio: 1;
  color: #8c939d;
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar-uploader-icon :deep(svg) {
  width: 100%;
  height: 100%;
}

.avatar {
  width: 100%;
  height: 100%;
  display: block;
}
</style>