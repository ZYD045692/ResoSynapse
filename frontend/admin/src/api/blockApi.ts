import axios from 'axios';
import { apiUrls } from '@/api/api';
import { BlockType } from '@/api/model/blockModel';
import { ModuleType } from '@/api/model/moduleModel';
import request from '@/utils/http/index'

type ResponseModuleType = ModuleType & {
    displayOrder: number;
    fatherId: number;
    iconUrl: string;
    id: number;
    isVisible: boolean;
    name: string;
    url: string;
    avgDimension1: number;
    avgDimension2: number;
    avgDimension3: number;
    clickCount: number;
};

type ResponseBlockType = BlockType & {
    moduleWidgetAdminDTOs?: ResponseModuleType[];
};

export async function GetAdminBlocks(quireDay: Number): Promise<BlockType[]> {
    try {
        const response = await request.get<ResponseBlockType[]>({
            url: apiUrls.GetAdminBlocks_API_URL,
            params: { day: quireDay || 1 }
        })
        if (response) {
            return (response as ResponseBlockType[]).map(block => {
                // 使用解构移除 moduleWidgetAdminDTOs 字段
                const { moduleWidgetAdminDTOs, ...restBlock } = block;
                return {
                    ...restBlock,  // 这里会保留除 moduleWidgetAdminDTOs 外的所有属性
                    moduleList: moduleWidgetAdminDTOs?.map(module => ({
                        id: module.id,
                        name: module.name,
                        url: module.url,
                        iconUrl: module.iconUrl,
                        fatherId: module.fatherId,
                        dimension1: module.avgDimension1,
                        dimension2: module.avgDimension2,
                        dimension3: module.avgDimension3,
                        displayOrder: module.displayOrder,
                        isVisible: module.isVisible,
                        clickCount: module.clickCount,
                        introduction: module.introduction,
                    })) || []
                };
            }).sort((a, b) => a.displayOrder - b.displayOrder);
        } else {
            console.error('response为空');
            return [];
        }
    } catch (err) {
        console.error('处理失败:', err);
        return [];
    }
}

// export async function GetAdminBlocks(quireDay: Number): Promise<BlockType[]> {
//     try {
//         const params = { day: quireDay || 1 };
//         const response = await axios.get(apiUrls.GetAdminBlocks_API_URL, { params });
//         if (response.status === 200) {
//             return (response.data as ResponseBlockType[]).map(block => {
//                 // 使用解构移除 moduleWidgetAdminDTOs 字段
//                 const { moduleWidgetAdminDTOs, ...restBlock } = block;

//                 return {
//                     ...restBlock,  // 这里会保留除 moduleWidgetAdminDTOs 外的所有属性
//                     moduleList: moduleWidgetAdminDTOs?.map(module => ({
//                         id: module.id,
//                         name: module.name,
//                         url: module.url,
//                         iconUrl: module.iconUrl,
//                         fatherId: module.fatherId,
//                         dimension1: module.avgDimension1,
//                         dimension2: module.avgDimension2,
//                         dimension3: module.avgDimension3,
//                         displayOrder: module.displayOrder,
//                         isVisible: module.isVisible,
//                         clickCount: module.clickCount,
//                         introduction: module.introduction,
//                     })) || []
//                 };
//             }).sort((a, b) => a.displayOrder - b.displayOrder);


//         } else {
//             console.error('API 请求失败，响应码:', response.data.code);
//             return [];
//         }
//     } catch (err) {
//         console.error('请求失败:', err);
//         return [];
//     }
// }



interface BlockDisplayOrderItem {
    id: number;
    displayOrder: number;
}

type BlockDisplayOrderArray = Array<BlockDisplayOrderItem>;

export async function PostUpdateBlockOrder(blockDisplayOrderArray: BlockDisplayOrderArray): Promise<Number> {
    try {
        const response = await axios.post(apiUrls.PostUpdateBlockOrder_API_URL, blockDisplayOrderArray);

        if (response.status === 200) {
            if (response.data.stateCode){
                return 1
            }else{
                return 0
            }
        } else {
            console.error('API 请求失败，响应码:', response.data.code);
            return 0;
        }
    } catch (err) {
        console.error('请求失败:', err);
        return 0;
    }
}