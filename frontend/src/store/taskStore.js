import { create } from 'zustand'
import { tasksAPI } from '../services/api'

const useTaskStore = create((set, get) => ({
  tasks: [],
  page: 0,
  size: 5,
  totalPages: 0,
  totalElements: 0,
  loading: false,
  error: null,

  fetchTasks: async (page = 0) => {
    set({ loading: true, error: null })
    try {
      const { size } = get()
      const { data } = await tasksAPI.getAll({ page, size })
      set({
        tasks: data.content ?? data,
        page: data.number ?? page,
        totalPages: data.totalPages ?? 0,
        totalElements: data.totalElements ?? 0,
        loading: false,
      })
    } catch (err) {
      set({ loading: false, error: err.response?.data?.message || 'Error al cargar tareas' })
    }
  },

  completeTask: async (id) => {
    await tasksAPI.complete(id)
    const { page } = get()
    get().fetchTasks(page)
  },

  createTask: async (taskData) => {
    const data = { ...taskData }
    if (data.fechaVencimiento) {
      data.fechaVencimiento = data.fechaVencimiento + 'T00:00:00'
    }
    await tasksAPI.create(data)
    get().fetchTasks(0)
  },

  updateTask: async (id, taskData) => {
    const data = { ...taskData }
    if (data.fechaVencimiento) {
      data.fechaVencimiento = data.fechaVencimiento + 'T00:00:00'
    }
    await tasksAPI.update(id, data)
    const { page } = get()
    get().fetchTasks(page)
  },

  deleteTask: async (id) => {
    await tasksAPI.delete(id)
    const { page } = get()
    get().fetchTasks(page)
  },

  goToPage: (page) => {
    get().fetchTasks(page)
  },
}))

export default useTaskStore
