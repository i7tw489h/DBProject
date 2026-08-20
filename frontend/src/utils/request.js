import axios from 'axios'

const service = axios.create({
  baseURL: 'http://127.0.0.1:8080',
  timeout: 10000
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

//响应拦截器
service.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
    console.log(error)
    return Promise.reject(error)
  }
)

export default service