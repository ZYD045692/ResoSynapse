import { RadarChartType } from "@/api/model/radarChartModel";
import { BlockList } from '@/mock/temp/blockList';

const moduleList = BlockList[0].moduleList;
const module = moduleList[0];

// 定义维度名称
const dimName = {
    dim1: 'dim1',
    dim2: 'dim2',
    dim3: 'dim3'
}

// 初始化雷达图配置
const RadarChartData: RadarChartType = {
    indicator: [],
    chartData: [],
    colors: []
};

// 定义雷达图颜色
const radarcolor = ['#8BD8FC', '#409EFF'];

// 计算每个维度的最大值和平均值
const dimension1Values = moduleList.map(module => module.dimension1 ?? 0);
const maxDimension1 = Math.max(...dimension1Values);
const avgDimension1 = calculateMean(dimension1Values);

const dimension2Values = moduleList.map(module => module.dimension2 ?? 0);
const maxDimension2 = Math.max(...dimension2Values);
const avgDimension2 = calculateMean(dimension2Values);

const dimension3Values = moduleList.map(module => module.dimension3 ?? 0);
const maxDimension3 = Math.max(...dimension3Values);
const avgDimension3 = calculateMean(dimension3Values);

const clickCountValues = moduleList.map(module => module.clickCount ?? 0);
const maxClickCount = Math.max(...clickCountValues);
const avgClickCount = calculateMean(clickCountValues);

// 计算平均维度的最大值
const avgMaxDimension = (maxDimension1 + maxDimension2 + maxDimension3) / 3;

// 设置雷达图指标
RadarChartData.indicator = [
    { name: dimName.dim1, max: maxDimension1 },
    { name: dimName.dim2, max: maxDimension2 },
    { name: dimName.dim3, max: maxDimension3 },
    { name: '平均打分', max: avgMaxDimension },
    { name: '点击数', max: maxClickCount }
];

// 设置雷达图数据
RadarChartData.chartData = [
    {
        name: module.name,
        value: [
            module.dimension1||0,
            module.dimension2||0,
            module.dimension3||0,
            ((module.dimension1||0 )+ (module.dimension2||0) + (module.dimension3||0)) / 3,
            module.clickCount||0
        ]
    },
    {
        name: '组内平均',
        value: [avgDimension1, avgDimension2, avgDimension3, avgMaxDimension, avgClickCount]
    }
];

// 设置雷达图颜色
RadarChartData.colors = radarcolor;

// 计算平均值的辅助函数
function calculateMean(values: number[]): number {
    if (values.length === 0) {
        return 0;
    }
    const sum = values.reduce((acc, val) => acc + val, 0);
    return sum / values.length;
}
export {RadarChartData}