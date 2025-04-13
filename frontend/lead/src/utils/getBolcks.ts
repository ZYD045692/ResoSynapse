// import type { ModuleType } from "@/api/model/moduleModel";
import type { BlockType } from '@/api/model/blockModel';

export function getBlocks(data:any): BlockType[] {
    if (!data || Object.keys(data).length === 0) {
        return [];
    }
    const mappedBlocks = data.map((item: any) => ({
        id: item.displayOrder,
        name: item.name,
        iconUrl: item.iconUrl,
        displayOrder: item.displayOrder,
        moduleWidgets:item.moduleWidgetUserDTOs
    }));
    return mappedBlocks|| [];
}