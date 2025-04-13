// router/index.ts
import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router';
import Layout from '@/router/Layout.vue'; // 布局组件
import LoginView from '../views/login.vue';
import { useUserStore } from '@/store/User';

export const HOME_PAGE = '/pdfServer';

const routes: Array<RouteRecordRaw> = [
  {
    path: '/login',
    name: 'login',
    component: LoginView,
    meta: { requiresAuth: false }
  },
  {
    path: '/',
    component: Layout,
    meta: { requiresAuth: true },
    children: [
      {
        path: '/pdfServer',
        name: 'PDF编辑魔方',
        component: () => import('@/views/pdfServer.vue'),
      },
      {
        path: '/formulaServer',
        name: '公式快闪通',
        component: () => import('@/views/formulaServer.vue'),
      },
      {
        path: '/mdAIGC',
        name: 'MD显示器',
        component: () => import('@/views/mdAIGC.vue'),
      },
      {
        path: '/translate',
        name: '文本智提侠',
        component: () => import('@/views/translate.vue'),
      },
      {
        path: '',
        redirect: HOME_PAGE
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/login'
  }
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
});

// 路由守卫
router.beforeEach((to, from, next) => {
  const userStore = useUserStore();
  const isAuthenticated = userStore.checkAuth();
  
  if (to.meta.requiresAuth && !isAuthenticated) {
    next('/login');
  } else if (to.path === '/login' && isAuthenticated) {
    next(HOME_PAGE);
  } else {
    next();
  }
});

export default router;