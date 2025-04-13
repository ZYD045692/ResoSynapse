export interface RingChartType1 {
    title: string;
    data: { value: number; name: string }[];
    color: string[];
    radius: string[]
}

export interface RingChartType2 {
    title: string;
    data: { value: number; name: string }[];
    color: string[];
    radius: string[]|null;
}