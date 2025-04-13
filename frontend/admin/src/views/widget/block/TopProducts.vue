<template>
  <div class="custom-card art-custom-card top-products">
    <div class="custom-card-header">
      <span class="title">{{ t('analysis.topProducts.title') }}</span>
    </div>
    <div class="custom-card-body">
      <art-table :data="products" style="width: 100%" :pagination="false">
        <el-table-column prop="name" label="模块" width="200">
          <template #default="scope">
            <span @click="toDetail(scope.row.id)" style="cursor: pointer; color: #007bff; text-decoration: underline;">
              {{ scope.row.name }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="popularity" :label="t('analysis.topProducts.columns.popularity')">
          <template #default="scope">
            <el-progress :percentage="scope.row.popularity" :color="getColor(scope.row.popularity)" :stroke-width="5"
              :show-text="false" />
          </template>
        </el-table-column>
        <el-table-column prop="sales" :label=props.dimName.dim1 width="80">
          <template #default="scope">
            <span :style="{
              color: getColor(scope.row.popularity),
              backgroundColor: `rgba(${hexToRgb(getColor(scope.row.popularity))}, 0.08)`,
              border: '1px solid',
              padding: '3px 6px',
              borderRadius: '4px',
              fontSize: '12px'
            }">{{ scope.row.dim1 }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="sales" :label=props.dimName.dim2 width="80">
          <template #default="scope">
            <span :style="{
              color: getColor(scope.row.popularity),
              backgroundColor: `rgba(${hexToRgb(getColor(scope.row.popularity))}, 0.08)`,
              border: '1px solid',
              padding: '3px 6px',
              borderRadius: '4px',
              fontSize: '12px'
            }">{{ scope.row.dim2 }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="sales" :label=props.dimName.dim3 width="80">
          <template #default="scope">
            <span :style="{
              color: getColor(scope.row.popularity),
              backgroundColor: `rgba(${hexToRgb(getColor(scope.row.popularity))}, 0.08)`,
              border: '1px solid',
              padding: '3px 6px',
              borderRadius: '4px',
              fontSize: '12px'
            }">{{ scope.row.dim3 }}</span>
          </template>
        </el-table-column>
      </art-table>
    </div>
  </div>
</template>

<script setup lang="ts">
import { hexToRgb } from '@/utils/color'
import { computed } from 'vue'
import { useI18n } from 'vue-i18n'
import { BlockType } from '@/api/model/blockModel';
import { router } from '@/router';

const { t } = useI18n()
const props = withDefaults(
  defineProps<{
    block: BlockType;
    dimName?: {
      dim1: string;
      dim2: string;
      dim3: string;
    };
  }>(),
  {
    dimName: () => ({
      dim1: 'dim1',
      dim2: 'dim2',
      dim3: 'dim3'
    })
  }
);

// 定义 Product 接口
interface Product {
  id: number; // 添加 id 字段
  name: string;
  popularity: number;
  dim1: string;
  dim2: string;
  dim3: string;
}

const products = ref<Product[]>([]);

watch(
  () => props.block,
  (newBlock) => {
    if (newBlock) {
      const totalPopularity = newBlock.moduleList.reduce(
        (sum, module) => sum + (module.clickCount||0),
        0
      );

      // console.log("Received block data:", newBlock);

      products.value = newBlock.moduleList.map((module) => ({
        id: module.id, // 确保模块有 id 字段
        name: module.name,
        popularity: totalPopularity ? (module.clickCount||0) * 100 / totalPopularity : 0, // 避免除以零
        dim1: (module.dimension1 || 0).toFixed(2),
        dim2: (module.dimension2 || 0).toFixed(2),
        dim3: (module.dimension3 || 0).toFixed(2),
      })).sort((a, b) => b.popularity - a.popularity);
    }
  },
  { immediate: true } // 立即执行一次，确保初始值正确
);
console.log(products.value);



const getColor = (percentage: number) => {
  if (percentage < 5) return '#00E096'
  if (percentage < 10) return '#0095FF'
  if (percentage < 20) return '#884CFF'
  return '#FE8F0E'
}

// 添加跳转方法
const toDetail = (moduleId: number) => {
  router.push({
      path: `/widget/module/detail`,
      query: {
          id: moduleId
      }
  });
};
</script>

<style lang="scss" scoped>
:deep(.custom-card) {
  background: var(--art-main-bg-color);
  border-radius: calc(var(--custom-radius) + 4px) !important;
}

.custom-card {
  height: 330px;
  overflow-y: scroll;
  border-radius: var(--custom-radius);

  // 隐藏滚动条
  &::-webkit-scrollbar {
    display: none;
  }

  &-body {
    padding: 0 6px;
  }
}

@media (width <=1200px) {
  .custom-card {
    height: auto;
  }
}

// 卡片头部
:deep(.custom-card-header) {
  position: relative;
  box-sizing: border-box;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 18px 20px;

  .title {
    font-size: 20px;
    font-weight: 400;
    color: var(--art-text-gray-900);
  }

  .subtitle {
    position: absolute;
    bottom: 2px;
    left: 21px;
    font-size: 13px;
    color: var(--art-gray-600);
  }
}

.el-card {
  border: 1px solid #e8ebf1;
  box-shadow: none;
}

.mt-20 {
  margin-top: 20px;
}

.dark {
  :deep(.custom-card) {
    box-shadow: 0 4px 20px rgb(0 0 0 / 50%);
  }
}

@media (width <=1200px) {
  .mt-20 {
    margin-top: 0;
  }

  :deep(.custom-card) {
    margin-bottom: 20px;
  }
}
</style>