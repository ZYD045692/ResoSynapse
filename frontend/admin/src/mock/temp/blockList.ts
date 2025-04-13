import { BlockType } from '@/api/model/blockModel'
import cover1 from '@imgs/cover/img1.jpg'
import cover2 from '@imgs/cover/img2.jpg'
import cover3 from '@imgs/cover/img3.jpg'
import cover4 from '@imgs/cover/img4.jpg'
import cover5 from '@imgs/cover/img5.jpg'
import cover6 from '@imgs/cover/img6.jpg'
import cover7 from '@imgs/cover/img7.jpg'
import cover8 from '@imgs/cover/img8.jpg'
import cover9 from '@imgs/cover/img9.jpg'
import cover10 from '@imgs/cover/img10.jpg'
import {ModuleList1} from '@/mock/temp/moduleList'
import {ModuleList2} from '@/mock/temp/moduleList'
import SVGIcons from '@/SVG_ICONS'
import img1 from '@/assets/img/1.jpg'

export const BlockList: BlockType[] = [
    {
        id: 1,
        name: '41',
        iconUrl: img1,
        introduction:'我喜欢张译丹',
        moduleList: ModuleList1,
        imgUrl: cover1,
        displayOrder: 1,
        isVisible: true,
    },
    {
        id: 2,
        name: '42',
        iconUrl: img1,
        introduction:'啦啦啦啦啦',
        moduleList: ModuleList2,
        imgUrl: cover2,
        displayOrder: 2,
        isVisible: true,
    },
]
//     {
//         id: 3,
//         name: '43',
//         icon_url: img1,
//         abstract:'我喜欢张译丹',
//         moduleList: ModuleList2,
//         img_url: cover3,
//         display_order: 3,
//         is_visible: true,
//     },
//     {
//         id: 4,
//         name: '44',
//         icon_url: img1,
//         moduleList: ModuleList2,
//         abstract:'我喜欢张译丹',
//         img_url: cover4,
//         display_order: 4,
//         is_visible: true,
//     },
//     {
//         id: 5,
//         name: '45',
//         icon_url: img1,
//         moduleList: ModuleList2,
//         abstract:'我喜欢张译丹',
//         img_url: cover5,
//         display_order: 5,
//         is_visible: true,
//     },
//     {
//         id: 6,
//         name: '46',
//         icon_url: img1,
//         moduleList: ModuleList2,
//         abstract:'我喜欢张译丹',
//         img_url: cover6,
//         display_order: 6,
//         is_visible: true,
//     },
//     {
//         id: 7,
//         name: '47',
//         icon_url: img1,
//         moduleList: ModuleList2,
//         abstract:'我喜欢张译丹',
//         img_url: cover7,
//         display_order: 7,
//         is_visible: true,
//     },
//     {
//         id: 8,
//         name: '48',
//         icon_url: img1,
//         moduleList: ModuleList2,
//         abstract:'我喜欢张译丹',
//         img_url: cover8,
//         display_order: 8,
//         is_visible: true,
//     },
//     {
//         id: 9,
//         name: '49',
//         icon_url: img1,
//         moduleList: ModuleList2,
//         abstract:'我喜欢张译丹',
//         img_url: cover9,
//         display_order: 9,
//         is_visible: true,
//     },
//     {
//         id: 10,
//         name: '50',
//         icon_url: img1,
//         moduleList: ModuleList2,
//         abstract:'我喜欢张译丹',
//         img_url: cover10,
//         display_order: 10,
//         is_visible: true,
//     }
// ]
