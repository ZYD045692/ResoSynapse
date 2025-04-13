import UserHome from '@/views/UserHome.vue'
import { createRouter, createWebHistory } from 'vue-router';

const routes = [
    { path: '/', component: UserHome },

];
const router = createRouter({
    history: createWebHistory(),
    routes
});
export default router;