import { NavLink, Outlet } from 'react-router-dom'

const linkClass = ({ isActive }: { isActive: boolean }) =>
  `block rounded-lg px-3 py-2 text-sm font-medium transition-colors ${
    isActive ? 'bg-brand-600 text-white' : 'text-slate-600 hover:bg-slate-100'
  }`

export function AdminLayout() {
  return (
    <div className="grid grid-cols-1 gap-6 md:grid-cols-[220px_1fr]">
      <aside className="space-y-1">
        <NavLink to="/admin" end className={linkClass}>
          Tổng quan
        </NavLink>
        <NavLink to="/admin/stations" className={linkClass}>
          Bến xe
        </NavLink>
        <NavLink to="/admin/trips" className={linkClass}>
          Chuyến xe
        </NavLink>
        <NavLink to="/admin/accounts" className={linkClass}>
          Tài khoản
        </NavLink>
      </aside>
      <section>
        <Outlet />
      </section>
    </div>
  )
}
