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

function normalizeNutrition(dish) {
  return {
    calories: Number(dish.calories) || 0,
    protein: Number(dish.protein) || 0,
    fat: Number(dish.fat) || 0,
    carbs: Number(dish.carbs) || 0
  }
}

function getCartStorageKey(userId) {
  return userId ? `cart_${userId}` : 'cart_guest'
}

export const useCartStore = defineStore('cart', () => {
  const items = ref([])

  const totalPrice = computed(() => {
    return items.value.reduce((sum, item) => sum + item.price * item.quantity, 0).toFixed(2)
  })

  const totalCount = computed(() => {
    return items.value.reduce((sum, item) => sum + item.quantity, 0)
  })

  function persistCart(userId) {
    if (!userId) return
    localStorage.setItem(getCartStorageKey(userId), JSON.stringify(items.value))
  }

  function loadCart(userId) {
    if (!userId) {
      items.value = []
      return
    }
    try {
      const saved = localStorage.getItem(getCartStorageKey(userId))
      items.value = saved ? JSON.parse(saved) : []
    } catch (error) {
      console.error('加载购物车失败:', error)
      items.value = []
    }
  }

  function addItem(dish, quantity = 1) {
    const nutrition = normalizeNutrition(dish)
    const existing = items.value.find(item => item.dishId === dish.dishId)
    if (existing) {
      existing.quantity += quantity
      Object.assign(existing, nutrition)
    } else {
      items.value.push({
        dishId: dish.dishId,
        name: dish.name,
        price: dish.price,
        imageUrl: dish.imageUrl,
        categoryName: dish.categoryName,
        ...nutrition,
        quantity
      })
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

  function clearCart(userId) {
    items.value = []
    if (userId) {
      localStorage.removeItem(getCartStorageKey(userId))
    }
  }

  return { items, totalPrice, totalCount, addItem, removeItem, updateQuantity, clearCart, loadCart, persistCart }
})
