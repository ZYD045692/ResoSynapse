import axios from 'axios';
import {apiUrls}  from '@/api/api';
import {UserHomePageInfoType} from '@/api/model/userHomePageInfoModel'
interface ResponseUserHomePageInfoType {
    id: number;
    pageName: string;
    topNavigationText1: string;  // 注意原始字段命名规范
    topNavigationText2: string;
    backgroundUrl: string;
    applyUrlText: string;
    feedbackText: string;
    dim1: string;
    dim2: string;
    dim3: string;
  }
  
  function convertResponseToUserInfo(
    response: ResponseUserHomePageInfoType
  ): UserHomePageInfoType {
    return {
      TopNavigationtext1: response.topNavigationText1,
      TopNavigationtext2: response.topNavigationText2,
      BackGroundUrl: response.backgroundUrl,
      ApplyUrltext: response.applyUrlText,
      FeedBacktext: response.feedbackText,
      FeedBackdim1: response.dim1,
      FeedBackdim2: response.dim2,
      FeedBackdim3: response.dim3
    };
  }
  export async function getUserHomePageInfo(): Promise<UserHomePageInfoType | undefined> {
    try {
      const response = await axios.get<ResponseUserHomePageInfoType>(
        apiUrls.Get_Page_Info_API_URL
      );
      return convertResponseToUserInfo(response.data);
    } catch (err: unknown) {
      if (err instanceof Error) {
        console.error(`Data fetch error: ${err.message}`);
      } else {
        console.error(`Data fetch error: ${String(err)}`);
      }
      return undefined;
    }
  }
