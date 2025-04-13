<template>
    <div class="infobox-style">
        <h1 class="title">基本设置</h1>
        <el-form :model="Module" class="form" ref="ruleFormRef" :rules="rules" label-width="100px" label-position="top">
            <el-row>
                <el-form-item label="名称" prop="name">
                    <el-input v-model="Module.name" :disabled="!isEdit" />
                </el-form-item>
                <el-form-item label="是否可见" prop="isVisible" class="right-input">
                    <el-switch v-model="Module.isVisible" :active-value="1" :inactive-value="0" :disabled="!isEdit" />
                </el-form-item>
            </el-row>

            <el-row>
                <el-form-item v-if="!isEdit" label="图标" prop="iconUrl">
                    <div class="input-with-preview" :style="isEdit ? '' : 'cursor: not-allowed;'">
                        <div class="preview" v-if="Module.iconUrl">
                            <img :src="Module.iconUrl" class="icon-preview" />
                        </div>
                    </div>
                </el-form-item>

                <el-form-item v-else label="图标" prop="iconUrl">
                    <div class="el-top upload-container">
                        <el-upload class="cover-uploader" :action="uploadImageUrl" :headers="uploadHeaders"
                            :show-file-list="false" :on-success="onIconSuccess" :on-error="onError"
                            :before-upload="beforeUpload">
                            <div class="cover-icon-container">
                                <img :src="Module.iconUrl" class="cover-icon" />
                                <div class="upload-overlay">
                                    <!-- <div class="upload-text">点击图片上传图标</div> -->
                                </div>
                            </div>
                        </el-upload>
                    </div>
                </el-form-item>

                <el-form-item label="URL" prop="url">
                    <div class="preview" v-if="!isEdit">
                        <a :href="Module.url" target="_blank" style="margin: 0 0 4px 15px;" class="url-preview">{{
                            Module.url }}</a>
                    </div>
                    <div v-else>
                        <el-input v-model="Module.url" :disabled="!isEdit" class="url-preview" />
                    </div>
                </el-form-item>

            </el-row>

            <el-form-item label="展示顺序" prop="displayOrder">
                <div class="readonly-input">
                    <span>{{ Module.displayOrder }}</span>
                </div>
            </el-form-item>

            <el-form-item label="简介" prop="introduction">
                <el-input type="textarea" :rows="4" v-model="Module.introduction" :disabled="!isEdit" />
            </el-form-item>

            <div class="el-form-item-right">
                <el-button  style="width: 90px" v-ripple @click.stop="cancelwithdel">
                    {{ isEdit ? '取消' : '删除' }}
                </el-button>
                <el-button  style="width: 90px" v-ripple @click="edit">
                    {{ isEdit ? '保存' : '编辑' }}
                </el-button>
            </div>
        </el-form>
    </div>
    <div class="charts">
        <el-row :lg="6" :md="6" :sm="10" :xs="6"
            style="display: flex; justify-content: flex-end; align-items: center ;padding:20px;">
            <div class="block">
                <span class="demonstration"></span>
                <el-date-picker v-model="dateRange" type="daterange" range-separator="至" start-placeholder="开始日期"
                    end-placeholder="结束日期" :shortcuts="shortcuts" :disabled-date="disabledDate"
                    value-format="YYYY-MM-DD" @change="handleDateChange" />
            </div>
        </el-row>

        <el-row :gutter="20">
            <el-col :xs="24" :md="12" :lg="12">
                <div class="card art-custom-card">
                    <div class="card-header">
                        <span>雷达图</span>
                    </div>
                    <RadarChart :indicator="radarChart.indicator" :data="radarChart.chartData"
                        :colors="radarChart.colors" />
                </div>
            </el-col>
            <el-col :xs="24" :md="12" :lg="12">
                <div class="card art-custom-card">
                    <div class="card-header">
                        <span>折线图（渐变背景）</span>
                    </div>
                    <LineChart v-bind="lineChart" />
                </div>
            </el-col>
        </el-row>
        <el-row :gutter="20">
            <el-col :xs="24" :md="12" :lg="8">
                <div class="card art-custom-card">
                    <div class="card-header">
                        <span>{{ dimName.dim1 }}</span>
                    </div>
                    <RingChart :data="ringChartList.dim1.data" :height="250" :padding_bottom="10"
                        :color=ringChartList.dim3.color></RingChart>
                </div>
            </el-col><el-col :xs="24" :md="12" :lg="8">
                <div class="card art-custom-card">
                    <div class="card-header">
                        <span>{{ dimName.dim2 }}</span>
                    </div>
                    <RingChart :data="ringChartList.dim2.data" :height="250" :padding_bottom="10"
                        :color=ringChartList.dim3.color></RingChart>
                </div>
            </el-col><el-col :xs="24" :md="12" :lg="8">
                <div class="card art-custom-card">
                    <div class="card-header">
                        <span>{{ dimName.dim3 }}</span>
                    </div>
                    <RingChart :data="ringChartList.dim3.data" :height="250" :padding_bottom="10"
                        :color=ringChartList.dim3.color></RingChart>
                </div>
            </el-col>
        </el-row>
    </div>
    <div class="charts">
        <example />
    </div>
</template>

<script setup lang="ts">
import { FormInstance, FormRules } from 'element-plus'
import type { DatePickerProps } from 'element-plus'
import { ModuleType } from '@/api/model/moduleModel';
import { BlockType } from '@/api/model/blockModel';
import { RadarChartType } from '@/api/model/radarChartModel';
import { RadarChartData } from '@/mock/temp/radarChartData';
import { LineChartType } from '@/api/model/lineChartModel';
import { LineChartdata } from '@/mock/temp/lineChartData';
import { RingChart2, RingChart3 } from '@/mock/temp/ringChartData';
import { RingChartType2 } from '@/api/model/ringChartModel';
import { ModuleList1 } from '@/mock/temp/moduleList'
import EmojiText from '@/utils/emojo'
import example from './example.vue';
import { useUserStore } from '@/store/modules/user'
import { GetAdminBlocks } from '@/api/blockApi'
import { GetUserHomePageInfo } from '@/api/userHomePageInfoApi'
import {useWorktabStore} from '@/store/modules/worktab'
import {getRandomLineChartdata} from '@/mock/temp/lineChartData';
import {getRandomStarRingChartdata} from '@/mock/temp/ringChartData';

const router = useRoute()
const userStore = useUserStore()
const worktabStore = useWorktabStore()

let { accessToken } = userStore
const isLoading = ref(true);
const isEdit = ref(false);
const originalModule = ref<ModuleType | null>(null);
const ModuleId = ref(1);
const Module = ref<ModuleType>(ModuleList1[0]);

// 上传路径
const uploadImageUrl = `${import.meta.env.VITE_API_URL}/api/common/upload`
// 传递 token
const uploadHeaders = { Authorization: accessToken }
const dimName = reactive({
    dim1: 'dim1',
    dim2: 'dim2',
    dim3: 'dim3'
});

const radarChart = reactive<RadarChartType>(
    {
        indicator: [],
        chartData: [],
        colors: []
    }
)

const lineChart = reactive<LineChartType>({
    title: '',
    data: [],
    xAxisData: [],
    showAreaColor: true,
});

const ringChartList = reactive<Record<string, RingChartType2>>({
    dim1: RingChart2,
    dim2: RingChart2,
    dim3: RingChart2
});

const dateRange = ref<[string, string]>()

// 计算天数间隔
const dayInterval = computed(() => {
    if (!dateRange.value || dateRange.value.length !== 2) return 0

    const [startStr, endStr] = dateRange.value
    const start = new Date(startStr)
    const end = new Date(endStr)

    // 标准化日期（清除时分秒）
    const normalizeDate = (date: Date) => {
        return new Date(date.getFullYear(), date.getMonth(), date.getDate())
    }

    const normalizedStart = normalizeDate(start).getTime()
    const normalizedEnd = normalizeDate(end).getTime()

    // 计算天数差
    return Math.ceil((normalizedEnd - normalizedStart) / (1000 * 3600 * 24)) + 1 // +1 包含起始当天
})

const shortcuts = reactive<DatePickerProps['shortcuts']>([
    {
        text: '最近一周',
        value: () => {
            const end = new Date()
            const start = new Date()
            start.setDate(start.getDate() - 7)
            return [start, end]
        }
    },
    {
        text: '最近一个月',
        value: () => {
            const end = new Date()
            const start = new Date()
            start.setMonth(start.getMonth() - 1)
            return [start, end]
        }
    },
    {
        text: '最近三个月',
        value: () => {
            const end = new Date()
            const start = new Date()
            start.setMonth(start.getMonth() - 3)
            return [start, end]
        }
    }
])






//日期标准化函数
const normalizeDate = (date: Date, time: 'start' | 'end' = 'start') => {
    const d = new Date(date)
    if (time === 'start') {
        d.setHours(0, 0, 0, 0)
    } else {
        d.setHours(23, 59, 59, 999)
    }
    return d
}

//禁用未来日期（精确到天）
const disabledDate = (current: Date) => {
    const today = normalizeDate(new Date())
    return normalizeDate(current) > today
}

const fetchModuleInfo = async (moduleId: number) => {
    try {
        const Blocks = await GetAdminBlocks(1)
        const blockList = Blocks;
        const filteredModule = blockList
            .flatMap(block => block.moduleList)
            .find(module => module.id == moduleId);
        if (filteredModule) {
            Module.value = filteredModule;
        }
    } catch (error) {
        ElMessage.error('数据加载失败')
    }
}

// 计算平均值的辅助函数
function calculateMean(values: number[]): number {
    if (values.length === 0) {
        return 0;
    }
    const sum = values.reduce((acc, val) => acc + val, 0);
    return sum / values.length;
}

const fetchModuleData = async (moduleId: number) => {
    try {
        const Blocks = await GetAdminBlocks(dayInterval.value - 1)
        const blockList = Blocks;
        // 筛选出符合条件的模块及其所属的 block
        const filteredModuleWithBlock = blockList
            .flatMap(block => block.moduleList.map(module => ({ block, module }))) // 将每个模块与其所属的 block 关联
            .find(item => item.module.id === moduleId); // 筛选符合条件的模块

        if (filteredModuleWithBlock) {
            let filteredBlock = filteredModuleWithBlock.block
            let filteredModule = filteredModuleWithBlock.module
            originalModule.value = filteredModule
            Module.value = filteredModule


            // 定义雷达图颜色
            const radarcolor = ['#8BD8FC', '#409EFF'];

            // 计算每个维度的平均值
            const dimension1Values = filteredBlock.moduleList.map(module => module.dimension1 ?? 0);
            const avgDimension1 = calculateMean(dimension1Values);

            const dimension2Values = filteredBlock.moduleList.map(module => module.dimension2 ?? 0);
            const avgDimension2 = calculateMean(dimension2Values);

            const dimension3Values = filteredBlock.moduleList.map(module => module.dimension3 ?? 0);
            const avgDimension3 = calculateMean(dimension3Values);

            const clickCountValues = filteredBlock.moduleList.map(module => module.clickCount ?? 0);
            const maxClickCount = Math.max(...clickCountValues);
            const avgClickCount = calculateMean(clickCountValues);

            // 计算平均维度的最大值
            const avgDimension = (avgDimension1 + avgDimension2 + avgDimension3) / 3;

            let indicator = [
                { name: dimName.dim1, max: 5 },
                { name: dimName.dim2, max: 5 },
                { name: dimName.dim3, max: 5 },
                { name: '平均打分', max: 5 },
                { name: '点击数', max: maxClickCount }
            ];
            let chartData = [
                {
                    name: filteredModule.name,
                    value: [
                        filteredModule.dimension1 || 0,
                        filteredModule.dimension2 || 0,
                        filteredModule.dimension3 || 0,
                        ((filteredModule.dimension1 || 0) + (filteredModule.dimension2 || 0) + (filteredModule.dimension3 || 0)) / 3,
                        filteredModule.clickCount || 0
                    ]
                },
                {
                    name: '组内平均',
                    value: [avgDimension1, avgDimension2, avgDimension3, avgDimension, avgClickCount]
                }
            ];

            let RadarChartData: RadarChartType = {
                indicator: indicator,
                chartData: chartData,
                colors: radarcolor
            };

            Object.assign(lineChart, getRandomLineChartdata());
            Object.assign(radarChart, RadarChartData);
            ringChartList.dim1 = getRandomStarRingChartdata();
            ringChartList.dim2 = getRandomStarRingChartdata();
            ringChartList.dim3 = getRandomStarRingChartdata();
        }
        else {
            Object.assign(radarChart, RadarChartData);
            Object.assign(lineChart, getRandomLineChartdata());
            ringChartList.dim1 = getRandomStarRingChartdata();
            ringChartList.dim2 = getRandomStarRingChartdata();
            ringChartList.dim3 = getRandomStarRingChartdata();
        }

    } catch (error) {
        ElMessage.error('数据加载失败')
    }
}

const handleDateChange = async (val: [Date, Date] | null) => {
    console.log('选择日期范围:', val)
    console.log(dateRange.value)
    await fetchModuleData(ModuleId.value)
}



onMounted(async () => {
    scrollToTop();
    // 设置默认时间范围
    const today = new Date()
    const yesterday = new Date(today)
    today.setDate(today.getDate() - 1)
    yesterday.setDate(yesterday.getDate() - 2)
    // 日期格式化函数
    const formatDate = (date: Date): string => {
        return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
    }
    dateRange.value = [formatDate(yesterday), formatDate(today)]

    console.log(dateRange)
    try {
        const UserHomePageInfo = await GetUserHomePageInfo();
        let UserHomePageInfodimName = {
            dim1: UserHomePageInfo.FeedBackdim1,
            dim2: UserHomePageInfo.FeedBackdim2,
            dim3: UserHomePageInfo.FeedBackdim3,
        };
        Object.assign(dimName, UserHomePageInfodimName);
    } catch (error) {
        console.error('Error:', error);
    } finally {
    }

    try {
        const moduleId = Number(router.query.id||21);
        console.log('querymoduleId',router.query.id)
        console.log('moduleId',moduleId)
        ModuleId.value = moduleId
        fetchModuleInfo(ModuleId.value);
        fetchModuleData(ModuleId.value);
    } catch (error) {
        console.error('Error:', error);
    } finally {
        isLoading.value = false;
    }
    scrollToTop();
});


const rules = reactive<FormRules>({
    name: [
        { required: true, message: '请输入名称', trigger: 'blur' },
        { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
    ],
    iconUrl: [
        { required: true, message: '请输入图标 URL', trigger: 'blur' }
    ],
    url: [
        { required: true, message: '请输入 URL', trigger: 'blur' }
    ],
    introduction: [
        { required: true, message: '请输入简介', trigger: 'blur' },
        { min: 2, max: 200, message: '长度在 2 到 200 个字符', trigger: 'blur' }
    ]
});



const onIconSuccess = (response: any) => {
    Module.value.iconUrl = response.data.url
    ElMessage.success(`图片上传成功 ${EmojiText[200]}`)
}

const onError = () => {
    ElMessage.error(`图片上传失败 ${EmojiText[500]}`)
}

// 添加上传前的校验
const beforeUpload = (file: File) => {
    const isImage = file.type.startsWith('image/')
    const isLt2M = file.size / 1024 / 1024 < 2

    if (!isImage) {
        ElMessage.error('只能上传图片文件!')
        return false
    }
    if (!isLt2M) {
        ElMessage.error('图片大小不能超过 2MB!')
        return false
    }
    return true
}

const edit = () => {
    if (!isEdit.value) {
        // 正确保存原始数据
        originalModule.value = JSON.parse(JSON.stringify(Module.value));
    } else {
        // 这里添加实际保存到服务器的逻辑
    }
    isEdit.value = !isEdit.value;
};

const cancelwithdel = (event?: MouseEvent) => {
  // 阻止事件冒泡
  event?.stopPropagation()

  if (isEdit.value) {
    // 编辑状态下：恢复数据
    if (originalModule.value) {
      Module.value = JSON.parse(JSON.stringify(originalModule.value))
    }
    isEdit.value = false
  } else {
    // 非编辑状态下：确认删除
    ElMessageBox.confirm('确定要删除该模块吗？', '删除确认', {
      confirmButtonText: '确定删除',
      cancelButtonText: '取消',
      type: 'warning',
      customClass: 'art-message-box'
    })
    .then(async () => {
      // 执行删除逻辑
      try {
        // await deleteModule(Module.value.id)
        ElMessage.success('删除成功')
        // router.back() // 返回上一页
        worktabStore.removeTab(router.path)
      } catch (error) {
        ElMessage.error('删除失败')
      }
    })
    .catch(() => {
      // 取消删除
    })
  }
}
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

        .input-with-preview {
            display: flex;
            align-items: center;
            gap: 10px;

            .preview {
                .icon-preview {
                    border-radius: $box-radius;
                    width: auto;
                    height: 150px;
                }

                .url-preview {
                    display: inline-block;
                    width: 100%;
                    text-decoration: none;
                    margin-left: 100px;
                    color: var(--art-text-color);
                    border-radius: $box-radius;
                    padding: 10px;
                    background-color: var(--art-main-bg-color);
                    border: 1px solid var(--art-card-border);

                    &:hover {
                        text-decoration: underline;
                        cursor: pointer;
                    }
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
        }

        .readonly-input {
            border-radius: $box-radius;
            background-color: var(--art-main-bg-color);
            border: 1px solid var(--art-card-border) !important;
            background-color: var(--art-main-bg-color);
            width: 200px;
            cursor: not-allowed;

            span {
                padding-left: 20px;
                color: var(--art-text-gray-600);
                font-size: 14px;

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

.charts {
    padding-top: 20px;

    .page-title {
        margin: 20px 0 15px;
        font-size: 22px;
        font-weight: 500;

        &:first-child {
            margin-top: 0;
        }
    }

    .card {
        background-color: var(--art-main-bg-color);
        border-radius: var(--custom-radius);

        .card-header {
            padding: 15px 0 0 20px;

            span {
                font-size: 16px;
                font-weight: 500;
            }
        }
    }

    .el-col {
        margin-bottom: 20px;
    }
}
</style>