import { useState, useEffect } from 'react'
import useTaskStore from '../store/taskStore'

const emptyForm = { titulo: '', descripcion: '', fechaVencimiento: '' }

export default function TaskModal({ isOpen, onClose, task }) {
  const { createTask, updateTask } = useTaskStore()
  const [form, setForm] = useState(emptyForm)
  const [error, setError] = useState('')
  const isEditing = !!task

  useEffect(() => {
    if (task) {
      setForm({
        titulo: task.titulo || '',
        descripcion: task.descripcion || '',
        fechaVencimiento: task.fechaVencimiento ? task.fechaVencimiento.slice(0, 10) : '',
      })
    } else {
      setForm(emptyForm)
    }
    setError('')
  }, [task, isOpen])

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value })

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')

    if (!form.titulo.trim()) {
      setError('El título es obligatorio')
      return
    }

    try {
      if (isEditing) {
        await updateTask(task.id, form)
      } else {
        await createTask(form)
      }
      onClose()
    } catch (err) {
      setError(err.response?.data?.message || 'Error al guardar la tarea')
    }
  }

  if (!isOpen) return null

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40">
      <div className="bg-white rounded-lg shadow-xl w-full max-w-lg mx-4 p-6">
        <h2 className="text-xl font-bold mb-4">{isEditing ? 'Editar Tarea' : 'Nueva Tarea'}</h2>

        {error && (
          <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-2 rounded mb-4">{error}</div>
        )}

        <form onSubmit={handleSubmit}>
          <div className="mb-4">
            <label className="block text-gray-700 mb-1">Título *</label>
            <input
              type="text"
              name="titulo"
              value={form.titulo}
              onChange={handleChange}
              className="w-full px-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>

          <div className="mb-4">
            <label className="block text-gray-700 mb-1">Descripción</label>
            <textarea
              name="descripcion"
              value={form.descripcion}
              onChange={handleChange}
              rows={3}
              className="w-full px-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>

          <div className="mb-6">
            <label className="block text-gray-700 mb-1">Fecha de Vencimiento</label>
            <input
              type="date"
              name="fechaVencimiento"
              value={form.fechaVencimiento}
              onChange={handleChange}
              className="w-full px-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>

          <div className="flex justify-end gap-3">
            <button
              type="button"
              onClick={onClose}
              className="px-4 py-2 border rounded hover:bg-gray-100 transition"
            >
              Cancelar
            </button>
            <button
              type="submit"
              className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 transition"
            >
              {isEditing ? 'Actualizar' : 'Crear'}
            </button>
          </div>
        </form>
      </div>
    </div>
  )
}
