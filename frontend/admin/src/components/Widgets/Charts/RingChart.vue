<template>
  <div ref="chartRef" 
       class="ring-chart"
       :style="{
         height: props.height + 'px',
         paddingTop: props.padding_top + 'px',
         paddingRight: props.padding_right + 'px',
         paddingBottom: props.padding_bottom + 'px',
         paddingLeft: props.padding_left + 'px'
       }">
  </div>
</template>

<script setup lang="ts">
  import type { EChartsOption } from 'echarts'
  import { useChart, useChartOps } from '@/composables/useChart'
  const { chartRef, initChart, isDark,updateChart } = useChart()

  interface Props {
    data?: Array<{ value: number; name: string }>
    height?: number
    padding_top?: number
    padding_right?: number
    padding_bottom?: number
    padding_left?: number
    color?: string[]
    radius?: string[]
  }

  const props = withDefaults(defineProps<Props>(), {
    data: () => [
      { value: 35, name: '分类1' },
      { value: 25, name: '分类2' },
      { value: 20, name: '分类3' },
      { value: 20, name: '分类4' }
    ],
    height: 200,
    padding_top:0,
    padding_right:0,
    padding_bottom:10,
    padding_left:0,
    color: () => [],
    radius: () => ['50%', '80%']
  })

  const options: () => EChartsOption = () => {
    return {
      series: [
        {
          name: '数据占比',
          type: 'pie',
          radius: props.radius,
          avoidLabelOverlap: false,
          itemStyle: {
            borderRadius: 10
          },
          label: {
            show: true,
            formatter: '{b}\n{d}%',
            position: 'outside',
            color: '#999',
            fontSize: 12
          },
          emphasis: {
            label: {
              show: true,
              fontSize: 14,
              fontWeight: 'bold'
            }
          },
          labelLine: {
            show: true,
            length: 15,
            length2: 25,
            smooth: true
          },
          data: props.data,
          color: props.color
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