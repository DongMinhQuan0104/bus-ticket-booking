import { Link } from 'react-router-dom'
import { Button } from '@/components/ui/Button'

export function NotFound() {
  return (
    <div className="flex flex-col items-center justify-center gap-4 py-24 text-center">
      <h1 className="text-4xl font-bold text-slate-800">404</h1>
      <p className="text-slate-500">Không tìm thấy trang bạn yêu cầu.</p>
      <Link to="/">
        <Button>Về trang chủ</Button>
      </Link>
    </div>
  )
}
