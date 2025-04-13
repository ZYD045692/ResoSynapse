import type { BlockType } from '@/api/model/blockModel'
import {Module_list7,Module_list15} from '@/mock/temp/ModuleList'
import icon from '@/assets/svg/置顶.svg';
export const BlockList: BlockType[] = [
  {
    id: 1,
    name: '置顶网页1',
    iconUrl: icon,
    displayOrder: 1,
    moduleWidgets:Module_list15,
  },
  {
    id: 2,
    name: '置顶网页2',
    iconUrl: icon,
    displayOrder: 2,
    moduleWidgets:Module_list7,
  },
  {
    id: 3,
    name: '置顶网页3',
    iconUrl: icon,
    displayOrder: 3,
    moduleWidgets:Module_list15,
  }
]