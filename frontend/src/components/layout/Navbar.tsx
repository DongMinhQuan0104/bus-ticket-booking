import { Link, NavLink, useNavigate } from 'react-router-dom'
import { useAuth } from '@/context/AuthContext'
import { Button } from '@/components/ui/Button'

const navLinkClass = ({ isActive }: { isActive: boolean }) =>
  `text-sm font-medium transition-colors ${isActive ? 'text-brand-700' : 'text-slate-600 hover:text-brand-600'}`

export function Navbar() {
  const { account, logout } = useAuth()
  const navigate = useNavigate()

  const handleLogout = async () => {
    await logout()
    navigate('/')
  }

  return (
    <header className="sticky top-0 z-40 border-b border-slate-200 bg-white/90 backdrop-blur">
      <div className="mx-auto flex max-w-6xl items-center justify-between px-4 py-3">
        <Link to="/" className="flex items-center gap-2 text-lg font-bold text-brand-700">
          <span className="rounded-lg bg-brand-600 px-2 py-1 text-white">BUS</span>
          TicketBooking
        </Link>

        <nav className="hidden items-center gap-6 md:flex">
          <NavLink to="/" end className={navLinkClass}>
            Trang chủ
          </NavLink>
          <NavLink to="/trips" className={navLinkClass}>
            Tìm chuyến
          </NavLink>
          {account && (
            <NavLink to="/bookings" className={navLinkClass}>
              Vé của tôi
            </NavLink>
          )}
          {account?.role === 'ADMIN' && (
            <NavLink to="/admin" className={navLinkClass}>
              Quản trị
            </NavLink>
          )}
        </nav>

        <div className="flex items-center gap-3">
          {account ? (
            <>
              <Link to="/profile" className="text-sm font-medium text-slate-700 hover:text-brand-600">
                {account.fullName || account.email}
              </Link>
              <Button variant="secondary" onClick={handleLogout}>
                Đăng xuất
              </Button>
            </>
          ) : (
            <>
              <Link to="/login">
                <Button variant="secondary">Đăng nhập</Button>
              </Link>
              <Link to="/register">
                <Button>Đăng ký</Button>
              </Link>
            </>
          )}
        </div>
      </div>
    </header>
  )
}
