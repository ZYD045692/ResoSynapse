import {LineChartType} from '@/api/model/lineChartModel';
export const LineChartdata:LineChartType = {
    title: '访客量',
    data: [58, 15, 82, 35, 90, 82, 85],
    xAxisData: ['一月', '二月', '三月', '四月', '五月', '六月', '七月'],
    showAreaColor: true
};

// 随机打乱数组的函数（基于 Fisher-Yates 算法）
function shuffleArray(array: number[]): number[] {
    const newArray = [...array]; // 创建原始数组的副本，避免修改原数组
    for (let i = newArray.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1)); // 生成 0 到 i 的随机索引
        [newArray[i], newArray[j]] = [newArray[j], newArray[i]]; // 交换元素
    }
    return newArray;
}

// 随机打乱 data 并返回 LineChartdata 的函数
export function getRandomLineChartdata(): LineChartType {
    const newData = shuffleArray(LineChartdata.data);
    return {
        ...LineChartdata,
        data: newData
    };
}