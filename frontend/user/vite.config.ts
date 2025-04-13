import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5200, // 设置开发服务器的端口号为 5200
  },
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src') // 使用 path.resolve 解析路径
    }
  },
})