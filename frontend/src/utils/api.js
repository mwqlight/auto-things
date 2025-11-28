import axios from 'axios'

// 创建axios实例
const api = axios.create({
  baseURL: 'http://localhost:8080/api', // 后端服务地址
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
api.interceptors.request.use(
  config => {
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
api.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
    console.error('API请求错误:', error)
    return Promise.reject(error)
  }
)

// 搜索商品
export const searchProducts = (keyword) => {
  return api.get(`/search/products?keyword=${encodeURIComponent(keyword)}`)
}

// 获取推荐
export const getRecommendation = (products) => {
  return api.post('/recommendation', products)
}

export default api