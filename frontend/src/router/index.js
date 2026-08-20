import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect:'/login'
  },
  {
    path: '/login',
    component: () => import("@/views/login.vue")
  },
  {
    path: '/initProfile',
    component: () => import("@/views/initProfile.vue")
  },
  {
    path: '/home',
    component: () => import("@/views/home.vue")
  },
  {
    path: '/dietAdd',
    component: () => import("@/views/dietAdd.vue")
  },
  {
    path: '/smokeAlcohol',
    component: () => import("@/views/smokeAlcohol.vue")
  },
  {
    path: '/family/bind',
    component: () => import("@/views/family/bind.vue")
  },
  {
    path: '/family/message',
    component: () => import("@/views/family/message.vue")
  },
  {
    path: '/pet',
    component: () => import("@/views/pet.vue")
  },
  {
    path: '/report',
    component: () => import("@/views/report.vue")
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to,from,next)=>{
  const userId = localStorage.getItem('userId')
  if(to.path !== '/login' && !userId){
    return next('/login')
  }
  next()
})

export default router