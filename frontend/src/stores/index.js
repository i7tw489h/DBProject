import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { userApi } from '@/api'

export const useUserStore = defineStore('user', () => {
  const user = ref(null)
  const token = ref(localStorage.getItem('token') || null)

  const isLogin = computed(() => !!token.value && !!user.value)

  function login(userInfo) {
    user.value = userInfo
    token.value = String(userInfo.id)
    localStorage.setItem('token', token.value)
  }

  async function autoLogin() {
    if (token.value && !user.value) {
      try {
        const userInfo = await userApi.getUserInfo(token.value)
        user.value = userInfo
      } catch (error) {
        console.error('自动登录失败:', error)
        logout()
      }
    }
  }

  function logout() {
    user.value = null
    token.value = null
    localStorage.removeItem('token')
  }

  return { user, token, isLogin, login, logout, autoLogin }
})