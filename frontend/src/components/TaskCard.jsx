import useTaskStore from '../store/taskStore'

const statusConfig = {
  PENDIENTE: { bg: 'bg-yellow-50 border-yellow-300', badge: 'bg-yellow-100 text-yellow-800', label: 'Pendiente' },
  COMPLETA: { bg: 'bg-green-50 border-green-300', badge: 'bg-green-100 text-green-800', label: 'Completa' },
  VENCIDA: { bg: 'bg-red-50 border-red-300', badge: 'bg-red-100 text-red-800', label: 'Vencida' },
}

export default function TaskCard({ task, onEdit }) {
  const { completeTask, deleteTask } = useTaskStore()
  const config = statusConfig[task.estado] || statusConfig.PENDIENTE

  const isPastDue = task.estado === 'VENCIDA'

  return (
    <div className={`border rounded-lg p-4 mb-3 shadow-sm ${config.bg}`}>
      <div className="flex items-start justify-between">
        <div className="flex-1">
          <div className="flex items-center gap-2 mb-1">
            <h3 className="font-semibold text-gray-800">{task.titulo}</h3>
            <span className={`text-xs font-medium px-2 py-0.5 rounded-full ${config.badge}`}>
              {config.label}
            </span>
          </div>

          {task.descripcion && (
            <p className="text-gray-600 text-sm mb-2">{task.descripcion}</p>
          )}

          {task.fechaVencimiento && (
            <p className={`text-xs ${isPastDue ? 'text-red-600 font-medium' : 'text-gray-500'}`}>
              {isPastDue && '⚠ '}Vence: {new Date(task.fechaVencimiento).toLocaleDateString()}
            </p>
          )}
        </div>

        <div className="flex gap-2 ml-4">
          {task.estado !== 'COMPLETA' && (
            <button
              onClick={() => completeTask(task.id)}
              className="bg-green-600 text-white px-3 py-1 rounded text-xs hover:bg-green-700 transition"
              title="Marcar como completa"
            >
              ✓
            </button>
          )}
          <button
            onClick={() => onEdit(task)}
            className="bg-blue-600 text-white px-3 py-1 rounded text-xs hover:bg-blue-700 transition"
          >
            Editar
          </button>
          <button
            onClick={() => {
              if (window.confirm(`¿Desea eliminar la tarea "${task.titulo}"?`)) {
                deleteTask(task.id)
              }
            }}
            className="bg-gray-600 text-white px-3 py-1 rounded text-xs hover:bg-gray-700 transition"
          >
            ✕
          </button>
        </div>
      </div>
    </div>
  )
}
