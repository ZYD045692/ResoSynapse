<template>
  <div ref="chartRef" class="h-bar-chart" :style="{ height: props.height }"></div>
</template>

<script setup lang="ts">
  import type { EChartsOption } from 'echarts'
  import { useChart, useChartOps } from '@/composables/useChart'
  import { getCssVariable } from '@/utils/utils'
  import * as echarts from 'echarts'

  const {
    chartRef,
    isDark,
    initChart,
    updateChart,
    getAxisLineStyle,
    getAxisLabelStyle,
    getAxisTickStyle,
    getSplitLineStyle
  } = useChart()

  interface Props {
    data?: number[]
    xAxisData?: string[]
    color?: string
    height?: string
    barWidth?: string | number
  }

  const props = withDefaults(defineProps<Props>(), {
    data: () => [0, 0, 0, 0, 0, 0, 0],
    xAxisData: () => ['一月', '二月', '三月', '四月', '五月', '六月', '七月'],
    color: '',
    height: useChartOps().chartHeight,
    barWidth: '36%'
  })

  const options: () => EChartsOption = () => {
    const computedColor =
      props.color ||
      new echarts.graphic.LinearGradient(0, 0, 1, 0, [
        {
          offset: 0,
          color: getCssVariable('--el-color-primary')
        },
        {
          offset: 1,
          color: getCssVariable('--el-color-primary-light-3')
        }
      ])

    return {
      grid: {
        top: 10,
        right: 10,
        bottom: 0,
        left: 0,
        containLabel: true
      },
      tooltip: {
        trigger: 'item'
      },
      xAxis: {
        type: 'value',
        axisTick: getAxisTickStyle(),
        axisLine: getAxisLineStyle(),
        axisLabel: getAxisLabelStyle(),
        splitLine: getSplitLineStyle()
      },
      yAxis: {
        type: 'category',
        data: props.xAxisData,
        axisTick: getAxisTickStyle(),
        axisLabel: getAxisLabelStyle(),
        axisLine: getAxisLineStyle()
      },
      series: [
        {
          data: props.data,
          type: 'bar',
          itemStyle: {
            color: computedColor,
            borderRadius: 4
          },
          barWidth: props.barWidth
        }
      ]
    }
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
