import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { userApi } from '@/api'

export const useUserStore = defineStore('user', () => {
  const user = ref(null)
  const token = ref(localStorage.getItem('token') || null)

  const isLogin = computed(() => !!token.value && !!user.value)

  function login(userInfo) {
    user.value = userInfo
    token.value = userInfo.userId.toString()
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

export const useCartStore = defineStore('cart', () => {
  const items = ref([])

  const totalPrice = computed(() => {
    return items.value.reduce((sum, item) => sum + item.price * item.quantity, 0).toFixed(2)
  })

  const totalCount = computed(() => {
    return items.value.reduce((sum, item) => sum + item.quantity, 0)
  })

  function addItem(dish) {
    const existing = items.value.find(item => item.dishId === dish.dishId)
    if (existing) {
      existing.quantity++
    } else {
      items.value.push({ ...dish, quantity: 1 })
    }
  }

  function removeItem(dishId) {
    const index = items.value.findIndex(item => item.dishId === dishId)
    if (index > -1) {
      items.value.splice(index, 1)
    }
  }

  function updateQuantity(dishId, quantity) {
    const item = items.value.find(item => item.dishId === dishId)
    if (item) {
      if (quantity <= 0) {
        removeItem(dishId)
      } else {
        item.quantity = quantity
      }
    }
  }

  function clearCart() {
    items.value = []
  }

  return { items, totalPrice, totalCount, addItem, removeItem, updateQuantity, clearCart }
})