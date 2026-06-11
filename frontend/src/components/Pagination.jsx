import useTaskStore from '../store/taskStore'

export default function Pagination() {
  const { page, totalPages, totalElements, goToPage } = useTaskStore()

  if (totalPages <= 1) return null

  return (
    <div className="flex items-center justify-between mt-6 bg-white px-4 py-3 rounded-lg shadow-sm">
      <p className="text-sm text-gray-600">
        Total: {totalElements} tareas — Página {page + 1} de {totalPages}
      </p>

      <div className="flex gap-2">
        <button
          onClick={() => goToPage(page - 1)}
          disabled={page === 0}
          className="px-3 py-1 rounded border text-sm disabled:opacity-40 hover:bg-gray-100 transition"
        >
          Anterior
        </button>

        {Array.from({ length: totalPages }, (_, i) => (
          <button
            key={i}
            onClick={() => goToPage(i)}
            className={`px-3 py-1 rounded text-sm border transition ${
              i === page
                ? 'bg-blue-600 text-white border-blue-600'
                : 'hover:bg-gray-100'
            }`}
          >
            {i + 1}
          </button>
        ))}

        <button
          onClick={() => goToPage(page + 1)}
          disabled={page >= totalPages - 1}
          className="px-3 py-1 rounded border text-sm disabled:opacity-40 hover:bg-gray-100 transition"
        >
          Siguiente
        </button>
      </div>
    </div>
  )
}
