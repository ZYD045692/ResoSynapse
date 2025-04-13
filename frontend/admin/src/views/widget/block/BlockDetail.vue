<template>
    <div class="infobox-style">
        <h1 class="title">基本设置</h1>
        <el-form :model="block" class="form" ref="ruleFormRef" :rules="rules" label-width="100px" label-position="top">
            <el-row>
                <el-form-item label="名称" prop="name">
                    <el-input v-model="block.name" :disabled="!isEdit" />
                </el-form-item>
                <el-form-item label="是否可见" prop="isVisible" class="right-input">
                    <el-switch v-model="block.isVisible" :active-value="1" :inactive-value="0" :disabled="!isEdit" />
                </el-form-item>
            </el-row>

            <el-row>
                <el-form-item v-if="!isEdit" label="图标" prop="iconUrl">
                    <div class="input-with-preview" :style="isEdit ? '' : 'cursor: not-allowed;'">
                        <div class="preview" v-if="block.iconUrl">
                            <img :src="block.iconUrl" class="icon-preview" />
                        </div>
                    </div>
                </el-form-item>

                <el-form-item v-else label="图标" prop="iconUrl">
                    <div class="el-top upload-container">
                        <el-upload class="cover-uploader" :action="uploadImageUrl" :headers="uploadHeaders"
                            :show-file-list="false" :on-success="onIconSuccess" :on-error="onError"
                            :before-upload="beforeUpload">
                            <div class="cover-icon-container">
                                <img :src="block.iconUrl" class="cover-icon" />
                                <div class="upload-overlay">
                                    <!-- <div class="upload-text">点击图片上传图标</div> -->
                                </div>
                            </div>
                        </el-upload>
                    </div>
                </el-form-item>

                <el-form-item v-if="!isEdit" label="首页图片" prop="homeImgUrl">
                    <div class="input-with-preview" :style="isEdit ? '' : 'cursor: not-allowed;'">
                        <div class="preview" v-if="block.imgUrl">
                            <img :src="block.imgUrl" class="img-preview" />
                        </div>
                    </div>
                </el-form-item>
                <el-form-item v-else label="首页图片" prop="homeImgUrl">
                    <div class="el-top upload-container">
                        <el-upload class="cover-uploader" :action="uploadImageUrl" :headers="uploadHeaders"
                            :show-file-list="false" :on-success="onImgSuccess" :on-error="onError"
                            :before-upload="beforeUpload">
                            <div class="cover-img-container">
                                <img :src="block.imgUrl" class="cover-img" />
                                <div class="upload-overlay">
                                    <!-- <div class="upload-text">点击图片上传首页图片</div> -->
                                </div>
                            </div>
                        </el-upload>
                    </div>
                </el-form-item>
            </el-row>

            <el-form-item label="展示顺序" prop="displayOrder">
                <div class="readonly-input">
                    <span>{{ block.displayOrder }}</span>
                </div>
            </el-form-item>

            <el-form-item label="简介" prop="introduction">
                <el-input type="textarea" :rows="4" v-model="block.introduction" :disabled="!isEdit" />
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
            <!-- 折线图 -->
            <el-col :xs="24" :md="12" :lg="12">
                <div class="card art-custom-card">
                    <div class="card-header">
                        <span>{{ lineChart.title }}</span>
                    </div>
                    <LineChart v-bind="lineChart" />
                </div>
            </el-col>

            <el-col :xs="24" :md="12" :lg="12">
                <div class="card art-custom-card">
                    <div class="card-header">
                        <span>{{ ringChart.title }}</span>
                    </div>
                    <RingChart :data="ringChart.data" :color="ringChart.color" :radius="ringChart.radius" 
                    :height="245"
                    />
                </div>
            </el-col>
        </el-row>
        <TopProducts :block="blockProducts" :dimName="dimName"/>
    </div>
</template>

<script setup lang="ts">
import { FormInstance, FormRules } from 'element-plus'
import type { DatePickerProps } from 'element-plus'
import EmojiText from '@/utils/emojo'
import TopProducts from './TopProducts.vue';
import { BlockList } from '@/mock/temp/blockList'
import { BlockType } from '@/api/model/blockModel';
import { ModuleType } from '@/api/model/moduleModel';
import { LineChartType } from '@/api/model/lineChartModel';
import { LineChartdata } from '@/mock/temp/lineChartData';
import { RingChartType1 } from '@/api/model/ringChartModel';
import { RingChart1 } from '@/mock/temp/ringChartData';
import { useUserStore } from '@/store/modules/user'
import { GetAdminBlocks } from '@/api/blockApi'
import {GetUserHomePageInfo} from '@/api/userHomePageInfoApi'
import {useWorktabStore} from '@/store/modules/worktab'
import {getRandomLineChartdata} from '@/mock/temp/lineChartData';

const router = useRoute()
const userStore = useUserStore()
const worktabStore = useWorktabStore()

let { accessToken } = userStore
const BlockId = ref(1);
const isLoading = ref(true);
const isEdit = ref(false);
const originalBlock = ref<BlockType | null>(null);
const dimName = reactive({
  dim1: 'dim1',
  dim2: 'dim2',
  dim3: 'dim3'
});

// 上传路径
const uploadImageUrl = `${import.meta.env.VITE_API_URL}/api/common/upload`
// 传递 token
const uploadHeaders = { Authorization: accessToken }

const block = ref<BlockType>({
    id: 1,
    name: '示例名称',
    iconUrl: '',
    imgUrl: '',
    introduction: '这是一个示例简介，用于展示基本设置的功能。',
    isVisible: true,
    displayOrder: 2, // 新增展示顺序字段
    moduleList: [],
})
const blockProducts = ref<BlockType>({
    id: 1,
    name: '示例名称',
    iconUrl: '',
    imgUrl: '',
    introduction: '这是一个示例简介，用于展示基本设置的功能。',
    isVisible: true,
    displayOrder: 2, // 新增展示顺序字段
    moduleList: [],
})

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


const lineChart = reactive<LineChartType>({
    title: '',
    data: [],
    xAxisData: [],
    showAreaColor: true,
});

const ringChart = reactive<RingChartType1>({
    title: '',
    data: [],
    color: [],
    radius: ['0%', '80%'],
});

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

const fetchBlockInfo = async (blockId: number) => {
    try {
        const Blocks = await GetAdminBlocks(1)
        const blockList = Blocks;
        const foundBlock = blockList.find(b => b.id === blockId);
        if (foundBlock) {
            block.value = foundBlock;
        }
    } catch (error) {
        ElMessage.error('数据加载失败')
    }
}
const fetchBlockData = async (blockId: number) => {
    try {
        const Blocks = await GetAdminBlocks(dayInterval.value - 1)
        const blockList = Blocks;
        const foundBlock = blockList.find(b => b.id === blockId);
        if (foundBlock) {
            Object.assign(lineChart, getRandomLineChartdata());
            // console.log(foundBlock)
            const BlockRingChart: RingChartType1 = {
                title: '饼图',
                color: ['#4C87F3', '#93F1B4', '#8BD8FC'],
                radius: ['0%', '80%'],
                data: foundBlock.moduleList
                    .map((module) => {
                        return { value: module.clickCount||0, name: module.name };
                    })
            }
            Object.assign(ringChart, BlockRingChart);
            blockProducts.value = foundBlock;
        }
        else {
            Object.assign(lineChart, getRandomLineChartdata());
            Object.assign(ringChart, RingChart1);
        }

    } catch (error) {
        ElMessage.error('数据加载失败')
    }
}

const handleDateChange = async(val: [Date, Date] | null) => {
    console.log('选择日期范围:', val)
    console.log(dateRange.value)
    await fetchBlockData(BlockId.value)
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
        const blockId = Number(router.query.id) || 1;
        // console.log('blockId',blockId)
        BlockId.value = blockId
        fetchBlockInfo(BlockId.value);
        fetchBlockData(BlockId.value);
    } catch (error) {
        console.error('Error:', error);
    } finally {
        isLoading.value = false;
    }
});


const rules = reactive<FormRules>({
    name: [
        { required: true, message: '请输入名称', trigger: 'blur' },
        { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
    ],
    iconUrl: [
        { required: true, message: '请输入图标 URL', trigger: 'blur' }
    ],
    homeImgUrl: [
        { required: true, message: '请输入首页图片 URL', trigger: 'blur' }
    ],
    introduction: [
        { required: true, message: '请输入简介', trigger: 'blur' },
        { min: 2, max: 200, message: '长度在 2 到 200 个字符', trigger: 'blur' }
    ]
})

const onImgSuccess = (response: any) => {
    block.value.imgUrl = response.data.url
    ElMessage.success(`图片上传成功 ${EmojiText[200]}`)
}

const onIconSuccess = (response: any) => {
    block.value.iconUrl = response.data.url
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
        originalBlock.value = JSON.parse(JSON.stringify(block.value));
    }
    isEdit.value = !isEdit.value;
};

const cancelwithdel = (event?: MouseEvent) => {
  // 阻止事件冒泡
  event?.stopPropagation()

  if (isEdit.value) {
    // 编辑状态下：恢复数据
    if (originalBlock.value) {
        block.value = JSON.parse(JSON.stringify(originalBlock.value))
    }
    isEdit.value = false
  } else {
    // 非编辑状态下：确认删除
    ElMessageBox.confirm('确定要删除该板块吗？', '删除确认', {
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

                .img-preview {
                    border-radius: $box-radius;
                    width: auto;
                    height: 150px;
                    object-fit: cover;
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

@media (max-width: 768px) {
    .infobox-style .form>.el-row {
        flex-direction: column;
    }

    .infobox-style .form {
        padding: 15px;
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
        padding: 20px;
        background-color: var(--art-main-bg-color);
        border-radius: var(--custom-radius);

        .card-header {
            padding-bottom: 15px;

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