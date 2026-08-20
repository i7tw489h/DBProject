import axios from 'axios'

const instance = axios.create({
  baseURL: '/api',
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 响应拦截器
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

// 用户登录注册
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
  updateUser(data) {
    return instance.put('/user/update', data)
  }
}

// 健康档案（初始化页面）
export const healthProfileApi = {
  save(data) {
    return instance.post('/healthProfile/save', data)
  },
  getByUserId(userId) {
    return instance.get(`/healthProfile/get/${userId}`)
  }
}

// 饮食打卡
export const dietRecordApi = {
  save(data) {
    return instance.post('/dietRecord/save', data)
  },
  listByUserId(userId) {
    return instance.get(`/dietRecord/list/${userId}`)
  }
}

// 烟酒填报
export const smokeAlcoholApi = {
  save(data) {
    return instance.post('/smokeAlcohol/save', data)
  },
  listByUserId(userId) {
    return instance.get(`/smokeAlcohol/list/${userId}`)
  }
}

// 亲属绑定、留言
export const familyApi = {
  bind(data) {
    return instance.post('/family/bind', data)
  },
  getFamilyList(userId) {
    return instance.get(`/family/list/${userId}`)
  },
  sendMessage(data) {
    return instance.post('/family/message', data)
  }
}

// 电子萌宠
export const petApi = {
  getPet(userId) {
    return instance.get(`/pet/get/${userId}`)
  },
  updatePet(data) {
    return instance.post('/pet/update', data)
  }
}

// 食谱
export const recipeApi = {
  getRecipeByTag(tagIds) {
    return instance.get('/recipe/list', { params: { tagIds } })
  }
}

// 周报统计
export const reportApi = {
  getWeekReport(userId) {
    return instance.get(`/report/week/${userId}`)
  }
}