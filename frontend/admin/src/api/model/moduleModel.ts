export interface ModuleType {
    id: number
    name: string
    url: string
    iconUrl: string
    fatherId: number
    dimension1?: number
    dimension2?: number
    dimension3?: number
    displayOrder: number
    isVisible: boolean
    clickCount?: number
    introduction:string
}


export interface ModuleCategoryType {
    fatherId: number
    name: string
    url: string
    iconUrl: string
    isVisible: boolean
    introduction:string
}

