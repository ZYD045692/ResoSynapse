import type { BlockType } from '@/api/model/blockModel';
export default function getLeftNavigationTarget(Blocks:BlockType[]) {
    const sousuo = ['搜索'];
    const BlocksTarget = Blocks.map(block => block.name);
    return sousuo.concat(BlocksTarget)
}