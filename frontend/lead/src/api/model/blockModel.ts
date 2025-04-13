import type { ModuleType } from "@/api/model/moduleModel";
export interface BlockType {
    id: number
    name: string
    iconUrl: string
    displayOrder: number
    moduleWidgets:ModuleType[]
}

export interface BlockCategoryType {
    id: number
    name: string
}

