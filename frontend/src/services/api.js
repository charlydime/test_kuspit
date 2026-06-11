import axios from 'axios'

const redirectLogin = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  window.location.href = '/login'
}

const isTokenExpired = (token) => {
  try {
    const payload = JSON.parse(atob(token.split('.')[1]))
    return payload.exp * 1000 < Date.now()
  } catch {
    return true
  }
}

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || '/api',
  headers: { 'Content-Type': 'application/json' },
})

api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      if (isTokenExpired(token)) {
        redirectLogin()
        return Promise.reject(new Error('Token expirado'))
      }
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => Promise.reject(error)
)

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      redirectLogin()
    }
    return Promise.reject(error)
  }
)

export const authAPI = {
  login: (data) => api.post('/auth/login', data),
  register: (data) => api.post('/auth/register', data),
}

export const tasksAPI = {
  getAll: (params) => api.get('/tareas', { params }),
  getById: (id) => api.get(`/tareas/${id}`),
  create: (data) => api.post('/tareas', data),
  update: (id, data) => api.put(`/tareas/${id}`, data),
  delete: (id) => api.delete(`/tareas/${id}`),
  complete: (id) => api.put(`/tareas/${id}/complete`),
}

export default api
