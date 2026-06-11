import { useEffect, useState } from 'react'
import useTaskStore from '../store/taskStore'
import TaskCard from '../components/TaskCard'
import TaskModal from '../components/TaskModal'
import Pagination from '../components/Pagination'
import Layout from '../components/Layout'

export default function Dashboard() {
  const { tasks, loading, fetchTasks } = useTaskStore()
  const [modalOpen, setModalOpen] = useState(false)
  const [editingTask, setEditingTask] = useState(null)

  useEffect(() => {
    fetchTasks(0)
  }, [fetchTasks])

  const openCreate = () => {
    setEditingTask(null)
    setModalOpen(true)
  }

  const openEdit = (task) => {
    setEditingTask(task)
    setModalOpen(true)
  }

  const closeModal = () => {
    setModalOpen(false)
    setEditingTask(null)
  }

  return (
    <Layout>
      <div className="flex items-center justify-between mb-6">
        <h2 className="text-2xl font-bold text-gray-800">Mis Tareas</h2>
        <button
          onClick={openCreate}
          className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 transition"
        >
          + Nueva Tarea
        </button>
      </div>

      {loading ? (
        <p className="text-center text-gray-500 py-10">Cargando tareas...</p>
      ) : tasks.length === 0 ? (
        <p className="text-center text-gray-500 py-10">No hay tareas disponibles. ¡Crea una nueva!</p>
      ) : (
        <>
          {tasks.map((task) => (
            <TaskCard key={task.id} task={task} onEdit={openEdit} />
          ))}
          <Pagination />
        </>
      )}

      <TaskModal isOpen={modalOpen} onClose={closeModal} task={editingTask} />
    </Layout>
  )
}
