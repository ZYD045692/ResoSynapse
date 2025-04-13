export interface RadarChartType {
    indicator: { name: string; max: number }[];
    chartData: { name: string; value: number[] }[];
    colors: string[];
}