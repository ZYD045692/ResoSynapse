import axios from 'axios'
import { ApiStatus } from '@/utils/http/status'

interface LoginResponse {
  accessToken: string
  userInfo: {
    id: string
    username: string
    email: string
    avatar?: string
  }
}

interface ApiResponse<T> {
  code: number
  message: string
  data: T
}

export class UserService {
  static async login(params: { body: string }): Promise<ApiResponse<LoginResponse>> {
    const response = await axios.post('/api/login', JSON.parse(params.body))
    return response.data
  }

  static async getUserInfo(): Promise<ApiResponse<LoginResponse['userInfo']>> {
    const response = await axios.get('/api/user/info')
    return response.data
  }
} 