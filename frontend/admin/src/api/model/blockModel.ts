import {ModuleType} from '@/api/model/moduleModel'
export interface BlockType {
    id: number
    name: string
    iconUrl: string
    introduction:string
    imgUrl:string
    isVisible: boolean
    moduleList:ModuleType[]
    displayOrder: number
}



export interface BlockCategoryType {
    name: string
    iconUrl: string
    introduction:string
    imgUrl:string
    isVisible: boolean
}
