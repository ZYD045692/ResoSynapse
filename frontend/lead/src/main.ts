import { createApp } from 'vue'
import App from './App.vue'
import { createPinia } from 'pinia'
// import 'jquery/dist/jquery.min'
// import 'bootstrap/dist/css/bootstrap.css';
// import 'bootstrap';
import router from './router/index';
import axios from 'axios'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

// 全局配置 Axios
// axios.defaults.withCredentials = true; // 允许跨域携带 Cookie
// axios.defaults.baseURL = 'http://localhost:8080/AITool/userPage/getUserBlocks'; // 根据实际情况修改
const app = createApp(App);
const pinia = createPinia();
app.use(ElementPlus)
app.config.globalProperties.$axios = axios;
app.use(pinia);
app.use(router);
// // 将 ElMessage 挂载到全局属性
// app.config.globalProperties.$message = app.config.globalProperties.$message || {};
app.mount('#app');
