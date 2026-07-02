import { Navigate, Outlet, useLocation } from 'react-router-dom'
import { useAuth } from '@/context/AuthContext'
import type { Role } from '@/types/enums'

export function ProtectedRoute({ allowedRoles }: { allowedRoles?: Role[] }) {
  const { account, isLoading } = useAuth()
  const location = useLocation()

  if (isLoading) {
    return <div className="p-8 text-center text-slate-500">Đang tải...</div>
  }

  if (!account) {
    return <Navigate to="/login" state={{ from: location }} replace />
  }

  if (allowedRoles && !allowedRoles.includes(account.role)) {
    return <Navigate to="/" replace />
  }

  return <Outlet />
}
