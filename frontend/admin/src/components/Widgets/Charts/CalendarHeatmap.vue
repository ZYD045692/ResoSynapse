<template>
    <div ref="chartRef" class="calendar-heatmap" :style="{ height: props.height }"></div>
  </template>
  
  <script setup lang="ts">
  import { useChartOps, useChart } from '@/composables/useChart'
  import { getCssVariable } from '@/utils/utils'
  import { EChartsOption } from 'echarts'
  import * as echarts from 'echarts'
  
  const {
    chartRef,
    isDark,
    updateChart,
    initChart
  } = useChart()
  
  interface Props {
    year?: string
    height?: string
    data?: [string, number][]
  }
  
  const props = withDefaults(defineProps<Props>(), {
    year: '2017',
    height: useChartOps().chartHeight,
    data: () => []
  })
  
  const options: () => EChartsOption = () => {
    return {
      visualMap: {
        show: false,
        min: 0,
        max: 10000,
        inRange: {
          color: ['#d8bfd8', '#9400D3']
        }
      },
      calendar: {
        range: props.year,
        splitLine: {
          show: true,
          lineStyle: {
            color: '#d3d3d3',
            width: 1,
            type: 'solid'
          }
        },
        cellSize: [20, 20],
        backgroundColor: isDark.value ? '#333' : '#fff'
      },
      series: [{
        type: 'heatmap',
        coordinateSystem: 'calendar',
        data: props.data.length > 0 ? props.data : generateData(),
        itemStyle: {
          borderRadius: 5,
          color: '#999',
          borderColor: '#d3d3d3',
          borderWidth: 0
        }
      }]
    }
  }
  
  const generateData = () => {
    const date = +echarts.time.parse(props.year + '-01-01')
    const end = +echarts.time.parse(props.year + '-12-31')
    const dayTime = 3600 * 24 * 1000
    const data = []
    for (let time = date; time <= end; time += dayTime) {
      data.push([
        echarts.time.format(time, '{yyyy}-{MM}-{dd}', false),
        Math.floor(Math.random() * 10000)
      ])
    }
    return data
  }
  
  onMounted(() => {
  initChart(options()) // 初始化图表
})

// 监听所有相关props变化并更新图表
watchEffect(() => {
  updateChart(options())
})


  watch(isDark, () => {
    initChart(options())
  })
  </script>
  
  <style lang="scss" scoped>
  .calendar-heatmap {
    width: 100%;
  }
  </style>