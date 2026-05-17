import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/admin-login',
    name: 'AdminLogin',
    component: () => import('../views/AdminLogin.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/Home.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/cart',
    name: 'Cart',
    component: () => import('../views/Cart.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/orders',
    name: 'Orders',
    component: () => import('../views/Orders.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/nutrition',
    name: 'Nutrition',
    component: () => import('../views/Nutrition.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/admin',
    name: 'Admin',
    component: () => import('../views/Admin.vue'),
    meta: { requiresAuth: true, isCanteenAdmin: true }
  },
  {
    path: '/dish-detail/:id',
    name: 'DishDetail',
    component: () => import('../views/DishDetail.vue'),
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  const canteenAdmin = localStorage.getItem('canteenAdmin')

  // 如果访问食堂管理员页面
  if (to.meta.isCanteenAdmin) {
    if (!canteenAdmin) {
      next('/admin-login')
      return
    }
    next()
    return
  }

  // 如果访问学生页面且需要登录
  if (to.meta.requiresAuth) {
    if (!userStore.isLogin) {
      next('/login')
      return
    }
  }

  // 已登录用户访问登录/注册页，跳转到首页
  if (to.path === '/login' || to.path === '/register') {
    if (userStore.isLogin) {
      next('/')
      return
    }
  }

  next()
})

export default router