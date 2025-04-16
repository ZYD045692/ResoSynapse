// import request from '@/utils/http'
// import { BaseResult } from '@/types/axios'
import { SystemInfo } from '@/config/setting'
import { BaseResult } from '@/types/axios'
import { UserInfo } from '@/types/store'
import avatar from '@imgs/user/avatar.jpg'
import { AdminLoginType } from '@/api/model/adminLoginModel'
import axios from 'axios'
import { apiUrls } from './api'
import api from '@/utils/http/index'
import { c } from 'vite/dist/node/moduleRunnerTransport.d-CXw_Ws6P'

interface RefreshTokenResponse {
  stateCode: number;
  accessToken?: string;
}
export class UserService {
  // 模拟登录接口
  static async login(username: string, password: string): Promise<BaseResult> {
    return new Promise(async (resolve) => {

      const response = await api.post<AdminLoginType>({
        url: apiUrls.AdminLogin_API_URL,
        data: {
          adminName: username,
          adminPasswordHash: password
        }
      });
      if (response) {
        resolve({
          code: 200,
          message: '登录成功',
          data: response
        });
      } else {
        resolve({
          code: 401,
          message: '用户名或密码错误',
          data: null
        });
      }
    });
  }

  // 刷新Token
  static async RefreshAccessToken(refreshToken: string): Promise<string> {
    try {
      const response = await api.post<RefreshTokenResponse>({
        url: apiUrls.Admin_Refresh_Token_API_URL,
        data: {
          refreshToken: refreshToken,
        }
      });
      if (response.stateCode !== 1) {
        return "";
      }else{
        return response.accessToken||"";
      }
    } catch (error) {
      return "";
    }
  }

  // 获取用户信息
  static getUserInfo(): Promise<BaseResult<UserInfo>> {
    return new Promise((resolve) => {
      resolve({
        code: 200,
        message: '获取用户信息成功',
        data: {
          id: 1,
          name: '张三',
          username: '一半耳机',
          avatar: avatar,
          email: '2214845593@qq.com'
        }
      })
    })
  }
}
