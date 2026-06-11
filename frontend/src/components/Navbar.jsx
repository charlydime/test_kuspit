import { useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export default function Navbar() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  return (
    <nav className="bg-white shadow px-6 py-3 flex items-center justify-between">
      <h1 className="text-xl font-bold text-gray-800">Task Manager</h1>
      <div className="flex items-center gap-4">
        <span className="text-gray-600">{user?.email}</span>
        <button
          onClick={handleLogout}
          className="bg-red-500 text-white px-4 py-1.5 rounded hover:bg-red-600 transition text-sm"
        >
          Cerrar Sesión
        </button>
      </div>
    </nav>
  )
}
