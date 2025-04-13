import axios from 'axios';
import {apiUrls}  from '@/api/api';

export async function UserLogin(userName: string, passwordHash: string) {
    try {
        // 构造 POST 请求的参数
        const requestBody = {
            userName:userName,
            passwordHash:passwordHash
        };
        // 发起 POST 请求
        const response = await axios.post(apiUrls.UserLogin_API_URL, requestBody);
        if (response.data.code == '200'){
            return response.data.stateCode
        }
    } catch (err: unknown) {
        return 3
    }
}