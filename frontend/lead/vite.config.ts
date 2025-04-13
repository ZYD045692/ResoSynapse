import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': '/src'
    }
  },
  server: {
    port: 5173,
    proxy: {
      '/AITool': {
        // target: 'http://localhost:8080/AITool/',
        // target: 'http://192.168.137.80:8080/AITool/',
        changeOrigin: true,
        secure: false
      }
    }
  },
  build: {
    terserOptions: {
      compress: {
        drop_console: true,
      },
    },
  },
});