import { createContext, useContext, useState, useEffect, useCallback } from 'react'
import { authAPI } from '../services/api'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    const stored = localStorage.getItem('user')
    return stored ? JSON.parse(stored) : null
  })
  const [token, setToken] = useState(() => localStorage.getItem('token') || null)
  const [loading, setLoading] = useState(false)

  const saveSession = useCallback((tokenValue, userData) => {
    localStorage.setItem('token', tokenValue)
    localStorage.setItem('user', JSON.stringify(userData))
    setToken(tokenValue)
    setUser(userData)
  }, [])

  const clearSession = useCallback(() => {
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    setToken(null)
    setUser(null)
  }, [])

  const login = useCallback(async (credentials) => {
    setLoading(true)
    try {
      const { data } = await authAPI.login(credentials)
      saveSession(data.token, data.user || { email: credentials.email })
      return data
    } finally {
      setLoading(false)
    }
  }, [saveSession])

  const register = useCallback(async (userData) => {
    setLoading(true)
    try {
      const { data } = await authAPI.register(userData)
      saveSession(data.token, data.user || { email: userData.email })
      return data
    } finally {
      setLoading(false)
    }
  }, [saveSession])

  const logout = useCallback(() => {
    clearSession()
  }, [clearSession])

  return (
    <AuthContext.Provider value={{ user, token, loading, login, register, logout, isAuthenticated: !!token }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  const context = useContext(AuthContext)
  if (!context) throw new Error('useAuth must be used within AuthProvider')
  return context
}
