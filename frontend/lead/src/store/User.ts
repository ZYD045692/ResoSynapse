import { defineStore } from 'pinia';
import type { BlockType } from '@/api/model/blockModel';
import type { leftNavigationType } from '@/api/model/leftNavigationModel'
import type{ PageInfoType } from '@/api/model/pageInfoModel';
// import background from '@/assets/img/background.jpg';
import { fetchBlockData } from '@/api/BlockApi';
import { getBlocks } from '@/utils/getBolcks';
import { fetchPage_Info } from '@/api/PageInfoApi';
import { getPageInfo } from '@/utils/getPageInfo';


// 定义状态的类型
interface UserState {
    userId:number
    UserName:string
    profilePhotoUrl:string
    isLoginandRegVisible:Boolean
    Blocks: BlockType[];
    LeftNavigation: leftNavigationType,
    PageInfo:PageInfoType
    loading: boolean;
    error: Error | null;
}

export const useUserStore = defineStore('user', {
    state: (): UserState => ({
        userId:0,
        UserName:'',
        profilePhotoUrl:'',
        isLoginandRegVisible:false,
        Blocks: [],
        LeftNavigation: {
            NavigationTarget: [],
            Navigationcolors: []
        },
        PageInfo:{
            backgroundUrl:'',
            topNavigationText1: '科研，从这里开始！',
            topNavigationText2: '明明只是一具行尸走肉，却编造着，活着的谎言。',
            applyUrlText: '申请收录',
            feedbackText: '感谢',
            dim1: '满意度',
            dim2: '速度',
            dim3: '复杂度',
        },
        loading: false,
        error: null,
    }),
    actions: {
        async LoadHomePageInfo() {
            this.loading = true;
            this.userId = Number(localStorage.getItem('userId'))||0;
            try {
                // 并行发起请求
                const [BlockData, Page_InfoData] = await Promise.all([
                    fetchBlockData().catch(() => null),    // 捕获请求错误返回 null
                    fetchPage_Info().catch(() => null)     // 捕获请求错误返回 null
                ]);
                console.log('BlockData', BlockData);
                // 统一检查数据有效性
                if (!BlockData) throw new Error('Failed to load Block data');
                if (!Page_InfoData) throw new Error('Failed to load Page_Info data');
        
                // 处理 Block 数据
                const processedBlockData = getBlocks(BlockData);
                
                this.Blocks = processedBlockData;
                const BlocksTarget = processedBlockData.map(block => block.name);
                const sousuo = ['搜索'];
                this.LeftNavigation.NavigationTarget = sousuo.concat(BlocksTarget);
                const processedPage_InfoData = getPageInfo(Page_InfoData);
                this.PageInfo.backgroundUrl = processedPage_InfoData.backgroundUrl,
                this.PageInfo.topNavigationText1 = processedPage_InfoData.topNavigationText1,
                this.PageInfo.topNavigationText2 = processedPage_InfoData.topNavigationText2,
                this.PageInfo.applyUrlText = processedPage_InfoData.applyUrlText,
                this.PageInfo.feedbackText = processedPage_InfoData.feedbackText,
                this.PageInfo.dim1 = processedPage_InfoData.dim1,
                this.PageInfo.dim2 = processedPage_InfoData.dim2,
                this.PageInfo.dim3 = processedPage_InfoData.dim3
                console.log('Page_InfoData', this.PageInfo);
        
            } catch (err: unknown) {
                if (err instanceof Error) {
                    console.error(`Data fetch error: ${err.message}`);
                } else {
                    console.error(`Data fetch error: ${String(err)}`);
                }
            } finally {
                this.loading = false;
            }
        },
        setUserId(userId: number) {
            this.userId = userId;
            localStorage.setItem('userId', userId.toString());
        },
        setUserName(userNmae:string){
            this.UserName = userNmae;
        },
        setprofilePhotoUrl(profilePhotoUrl:string){
            this.profilePhotoUrl = profilePhotoUrl;
        }
    },
    getters: {
    },
});