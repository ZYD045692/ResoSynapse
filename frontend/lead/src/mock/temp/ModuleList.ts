import type { ModuleType } from '@/api/model/moduleModel';
import icon from '@/assets/svg/baidu.svg';

const Module: ModuleType = {
    displayOrder: 0,
    iconUrl: icon,
    name: "baidu",
    id: 0,
    url: "https://www.baidu.com", // 确保 URL 是一个简单的字符串
};

export const Module_list15: ModuleType[] = Array.from({ length: 15 }, (_, index) => ({
    ...Module,
    id: index + 1,
    displayOrder: index + 1,
    name: "baidu" + (index + 1)
}));

export const Module_list7: ModuleType[] = Array.from({ length: 7 }, (_, index) => ({
    ...Module,
    id: index + 1,
    displayOrder: index + 1,
    name: "baidu" + (index + 1)
}));