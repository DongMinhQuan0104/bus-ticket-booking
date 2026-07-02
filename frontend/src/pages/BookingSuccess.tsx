import { Link, useParams } from 'react-router-dom'
import { Card } from '@/components/ui/Card'
import { Button } from '@/components/ui/Button'

export function BookingSuccess() {
  const { bookingId } = useParams<{ bookingId: string }>()

  return (
    <div className="mx-auto max-w-lg text-center">
      <Card className="p-10">
        <div className="mx-auto flex h-14 w-14 items-center justify-center rounded-full bg-green-100 text-2xl text-green-600">
          ✓
        </div>
        <h1 className="mt-4 text-xl font-bold text-slate-800">Đặt vé thành công!</h1>
        <p className="mt-2 text-sm text-slate-500">
          Mã đặt vé của bạn là <span className="font-mono font-semibold">{bookingId}</span>. Vui lòng lưu lại để
          tra cứu.
        </p>
        <div className="mt-6 flex justify-center gap-3">
          <Link to="/bookings">
            <Button variant="secondary">Xem vé của tôi</Button>
          </Link>
          <Link to="/">
            <Button>Về trang chủ</Button>
          </Link>
        </div>
      </Card>
    </div>
  )
}
