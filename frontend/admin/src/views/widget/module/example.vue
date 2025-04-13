<template>
  <div class="calendar-heatmap-container">
    <CalendarHeatmap :year="currentYear" :height="chartHeight" :data="customData" class="heatmap-chart" />
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
const currentYear = ref('2025')
const chartHeight = computed(() => '400px')
// 定义一个函数来生成随机数值
function generateRandomValue(min: number, max: number): number {
  return Math.floor(Math.random() * (max - min + 1)) + min;
}

// 定义一个函数来生成从开始日期到结束日期的所有日期
function generateDates(startDate: string, endDate: string): string[] {
  const start = new Date(startDate);
  const end = new Date(endDate);
  const dates: string[] = [];
  const currentDate = new Date(start);

  while (currentDate <= end) {
    const year = currentDate.getFullYear();
    const month = String(currentDate.getMonth() + 1).padStart(2, '0');
    const day = String(currentDate.getDate()).padStart(2, '0');
    dates.push(`${year}-${month}-${day}`);

    currentDate.setDate(currentDate.getDate() + 1);
  }

  return dates;
}

// 生成从2025年1月1日到2025年3月27日的所有日期
const allDates = generateDates('2025-01-01', '2025-03-27');

// 定义初始数据
const initialData: [string, number][] = [
  ['2025-01-01', 500],
  ['2025-01-02', 1200],
  ['2025-01-03', 800],
  ['2025-01-04', 1500],
  ['2025-01-05', 200],
  ['2025-01-06', 900],
  ['2025-01-07', 1100],
  ['2025-01-08', 700],
  ['2025-01-09', 1300],
  ['2025-01-10', 600]
];

// 生成完整的数据
const customData = ref<[string, number][]>(
  allDates.map(date => {
    // 检查日期是否在初始数据中
    const existingData = initialData.find(item => item[0] === date);
    if (existingData) {
      return existingData;
    } else {
      // 为不在初始数据中的日期生成随机值
      return [date, generateRandomValue(100, 2000)];
    }
  })
);
</script>

<style lang="scss" scoped>
.calendar-heatmap-container {
  padding: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

h2 {
  margin-bottom: 20px;
  color: var(--el-text-color-primary);
}

.heatmap-chart {
  width: 100%;
  max-width: 800px;
  margin-bottom: 20px;
}
</style>