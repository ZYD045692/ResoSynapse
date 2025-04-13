import {ModuleType} from '@/api/model/moduleModel'
import img1 from '@/assets/img/1.jpg'
import ico1 from '@/assets/img/favicon.ico'

export const Module1:ModuleType={
        id: 1,
        name: 'baidu',
        url: 'https://www.baidu.com/',
        iconUrl: ico1,
        dimension1: 2,
        dimension2: 3,
        dimension3: 4,
        displayOrder: 0,
        isVisible: true,
        clickCount: 10,
        introduction:'this is a abstract',
        fatherId:1
}
const Module2:ModuleType={
        id: 1,
        name: '百度',
        url: 'https://www.baidu.com/',
        iconUrl: img1,
        dimension1: 2,
        dimension2: 3,
        dimension3: 4,
        displayOrder: 0,
        isVisible: false,
        clickCount: 20,
        introduction:'this is a abstract',
        fatherId:2
}
const Module3:ModuleType={
    id: 1,
    name: '度百',
    url: 'https://www.baidu.com/',
    iconUrl: ico1,
    dimension1: 2,
    dimension2: 3,
    dimension3: 4,
    displayOrder: 0,
    isVisible: false,
    clickCount: 40,
    introduction:'this is a abstract',
    fatherId:3
}
export const ModuleList1: ModuleType[] = [
    ...Array.from({ length: 15 }, (_, index) => ({
        ...Module1,
        id: Module1.id + index,
        display_order: Module1.displayOrder + index,
        name: `baidu${index + 1}`
    })),
    // ...Array.from({ length: 6 }, (_, index) => ({
    //     ...Module3,
    //     id: Module3.id + index,
    //     display_order: Module3.display_order + index,
    //     name: `度百${index + 1}`
    // }))
];
export const ModuleList2: ModuleType[] = [
    ...Array.from({ length: 9 }, (_, index) => ({
        ...Module1,
        id: Module1.id + index,
        display_order: Module1.displayOrder + index,
        name: `baidu${index + 1}`
    })),
    ...Array.from({ length: 5 }, (_, index) => ({
        ...Module2,
        id: Module1.id + index,
        display_order: Module1.displayOrder + index,
        name: `百度${index + 1}`
    })),
    // ...Array.from({ length: 9 }, (_, index) => ({
    //     ...Module3,
    //     id: Module3.id + index,
    //     display_order: Module3.display_order + index,
    //     name: `度百${index + 1}`
    // }))
];
