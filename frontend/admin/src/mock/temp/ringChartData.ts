import { RingChartType1, RingChartType2 } from "@/api/model/ringChartModel";
export const RingChart1: RingChartType1 = {
    title: '饼图',
    data: [
        { value: 30, name: '分类A' },
        { value: 25, name: '分类B' },
        { value: 45, name: '分类C' },
        { value: 45, name: '分类C' },
    ],
    color: ['#4C87F3', '#93F1B4', '#8BD8FC'],
    radius: ['0%', '80%']
}

export const RingChart2: RingChartType2 = {
    title: '饼图',
    data: [
        { value: 30, name: '分类A' },
        { value: 25, name: '分类B' },
        { value: 45, name: '分类C' },
        { value: 45, name: '分类C' },
    ],
    color: ['#4C87F3', '#93F1B4', '#8BD8FC'],
    radius: null
}

export const RingChart3: RingChartType2 = {
    title: '饼图',
    data: [
        { value: 30, name: '分类A1' },
        { value: 25, name: '分类B1' },
        { value: 45, name: '分类C1' },
        { value: 45, name: '分类C1' },
    ],
    color: ['#4C87F3', '#93F1B4', '#8BD8FC'],
    radius: null
}



// 随机生成一星到五星的数量
function generateRandomStarData(): { value: number; name: string }[] {
    const starNames = ['一星', '二星', '三星', '四星', '五星'];
    const randomValues = starNames.map(() => Math.floor(Math.random() * 100) + 1); // 随机生成 1 到 100 的值
    return starNames.map((name, index) => ({
        value: randomValues[index],
        name: name
    }));
}

// 获取随机五星评价数据和固定颜色
export function getRandomStarRingChartdata(): RingChartType1 {
    const randomStarData = generateRandomStarData();
    // 固定颜色数组
    const fixedColors = ['#4C87F3', '#93F1B4', '#8BD8FC', '#FFA500', '#FF5733'];
    console.log(randomStarData)
    return {
        title: '反馈情况',
        data: randomStarData,
        color: fixedColors,
        radius: ['0%', '80%']
    };
}