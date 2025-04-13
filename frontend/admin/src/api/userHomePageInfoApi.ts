import axios from 'axios';
import { apiUrls } from '@/api/api';
import {UserHomePageInfoType} from '@/api/model/userHomePageInfoModel'

type ResponseUserHomePageInfoType = UserHomePageInfoType & {
    applyUrlText: string;
    backgroundUrl: string;
    dim1: string;
    dim2: string;
    dim3: string;
    feedbackText: string;
    topNavigationText1: string;
    topNavigationText2: string;
};
export async function GetUserHomePageInfo(): Promise<UserHomePageInfoType> {
    try {
        const response = await axios.get(apiUrls.GetUersHomePageInfo_API_URL);

        if (response.status === 200) {
            const responseData = response.data as ResponseUserHomePageInfoType;
            const UserHomePageInfo: UserHomePageInfoType = {
                TopNavigationtext1: responseData.topNavigationText1,
                TopNavigationtext2: responseData.topNavigationText2,
                BackGroundUrl: responseData.backgroundUrl,
                ApplyUrltext: responseData.applyUrlText,
                FeedBacktext: responseData.feedbackText,
                FeedBackdim1: responseData.dim1,
                FeedBackdim2: responseData.dim2,
                FeedBackdim3: responseData.dim3
            };

            return UserHomePageInfo;
        } else {
            console.error('API 请求失败，响应码:', response.status);
            return {} as UserHomePageInfoType; // 返回一个空对象，确保类型匹配
        }
    } catch (err) {
        console.error('请求失败:', err);
        return {} as UserHomePageInfoType; // 返回一个空对象，确保类型匹配
    }
}