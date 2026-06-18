import axios from 'axios'

const instance = axios.create({
  baseURL: '/api',
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json'
  }
})

instance.interceptors.response.use(
  response => {
    const { code, message, data } = response.data
    if (code === 200) {
      return data
    } else {
      return Promise.reject(new Error(message || '请求失败'))
    }
  },
  error => {
    console.error('API Error:', error)
    return Promise.reject(error)
  }
)

export const userApi = {
  register(data) {
    return instance.post('/user/register', data)
  },
  login(data) {
    return instance.post('/user/login', data)
  },
  getUserInfo(userId) {
    return instance.get(`/user/info/${userId}`)
  },
  updateProfile(data) {
    return instance.put('/user/update-profile', data)
  },
  updateUser(data) {
    return instance.put('/user/update', data)
  },
  updatePassword(data) {
    return instance.put('/user/password', data)
  }
}

export const dishApi = {
  getDishes(params) {
    return instance.get('/dishes', { params })
  },
  getDishById(id) {
    return instance.get(`/dishes/${id}`)
  },
  getCategories() {
    return instance.get('/categories')
  },
  getWindows() {
    return instance.get('/windows')
  },
  searchDishes(keyword) {
    return instance.get('/dishes/search', { params: { keyword } })
  }
}

export const cartApi = {
  getCart(userId) {
    return instance.get(`/cart/${userId}`)
  },
  addToCart(data) {
    return instance.post('/cart/add', data)
  },
  updateCart(data) {
    return instance.put('/cart/update', data)
  },
  removeFromCart(cartId) {
    return instance.delete(`/cart/${cartId}`)
  },
  clearCart(userId) {
    return instance.delete(`/cart/clear/${userId}`)
  }
}

export const orderApi = {
  submitOrder(data) {
    return instance.post('/order/submit', data)
  },
  getOrders(userId, status, page = 1, pageSize = 10) {
    return instance.get('/order/list', { params: { userId, status, page, pageSize } })
  },
  getOrderDetail(orderId) {
    return instance.get(`/order/detail/${orderId}`)
  },
  cancelOrder(orderId) {
    return instance.put(`/order/cancel/${orderId}`)
  },
  getAllOrders(params) {
    return instance.get('/order/all', { params })
  },
  acceptOrder(orderId) {
    return instance.put(`/order/accept/${orderId}`)
  },
  serveOrder(orderId) {
    return instance.put(`/order/serve/${orderId}`)
  },
  finishOrder(orderId) {
    return instance.put(`/order/finish/${orderId}`)
  },
  deleteOrder(orderId) {
    return instance.delete(`/order/${orderId}`)
  }
}

export const nutritionApi = {
  getTodayNutrition(userId) {
    return instance.get(`/nutrition/today/${userId}`)
  },
  getHistoryNutrition(userId, days) {
    return instance.get(`/nutrition/history/${userId}`, { params: { days } })
  },
  getHealthEvaluation(userId) {
    return instance.get(`/nutrition/evaluation/${userId}`)
  },
  getNutritionTargets(userId) {
    return instance.get(`/nutrition/targets/${userId}`)
  }
}

export const restrictionApi = {
  getUserRestrictions(userId) {
    return instance.get(`/user/restrictions/${userId}`)
  },
  saveUserRestriction(data) {
    return instance.post('/user/restriction', data)
  },
  deleteUserRestriction(restrictionId) {
    return instance.delete(`/user/restriction/${restrictionId}`)
  },
  getRecommendedDishes(userId) {
    return instance.get(`/dishes/recommend/${userId}`)
  }
}

export const aiApi = {
  recommendDishes(userId) {
    // 添加时间戳参数防止缓存
    return instance.get(`/ai/recommend/${userId}`, { params: { t: Date.now() } })
  },
  intelligentMeal(userId, type) {
    return instance.get(`/ai/meal/${userId}`, { params: { type } })
  },
  salesPrediction() {
    return instance.get('/ai/prediction')
  }
}

export const adminApi = {
  getDishList(params) {
    return instance.get('/admin/dishes', { params })
  },
  addDish(data) {
    return instance.post('/admin/dishes', data)
  },
  updateDish(data) {
    return instance.put('/admin/dishes', data)
  },
  deleteDish(id) {
    return instance.delete(`/admin/dishes/${id}`)
  },
  toggleDishStatus(id) {
    return instance.put(`/admin/dishes/status/${id}`)
  },
  batchToggleStatus(ids, isActive) {
    return instance.put('/admin/dishes/batch/status', { ids, isActive })
  },
  batchDeleteDishes(ids) {
    return instance.delete('/admin/dishes/batch', { data: { ids } })
  },
  getSalesRanking() {
    return instance.get('/admin/sales-ranking')
  },
  getSalesStatistics() {
    return instance.get('/admin/sales-statistics')
  },
  getLowStockAlert() {
    return instance.get('/admin/low-stock')
  },
  getStudentPreferences() {
    return instance.get('/admin/preferences')
  }
}

// AI销量预测API
export const predictionApi = {
  // 预测指定日期的销量
  predictSales(targetDate, forceRegenerate = false) {
    return instance.get('/admin/prediction/predict', { 
      params: { targetDate, forceRegenerate } 
    })
  },
  // 获取明天预测结果
  getTomorrowPrediction() {
    return instance.get('/admin/prediction/tomorrow')
  },
  // 获取预测历史
  getPredictionHistory(days = 7) {
    return instance.get('/admin/prediction/history', { 
      params: { days } 
    })
  },
  // 获取预测统计数据
  getStatistics() {
    return instance.get('/admin/prediction/statistics')
  },
  // 获取备餐建议
  getSuggestions() {
    return instance.get('/admin/prediction/suggestions')
  },
  // 更新实际销量
  updateActualSales(predictDate) {
    return instance.post('/admin/prediction/update-actual', null, {
      params: { predictDate }
    })
  },
  // 重置预测数据
  resetPredictions() {
    return instance.delete('/admin/prediction/reset')
  }
}
