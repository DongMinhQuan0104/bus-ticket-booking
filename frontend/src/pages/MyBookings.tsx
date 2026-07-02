import { useEffect, useState } from 'react'
import { bookingApi } from '@/api/bookingApi'
import type { BookingViewModel } from '@/types/dto'
import { Card } from '@/components/ui/Card'
import { Badge } from '@/components/ui/Badge'
import { formatCurrency, formatDateTime } from '@/lib/format'

export function MyBookings() {
  const [bookings, setBookings] = useState<BookingViewModel[]>([])
  const [isLoading, setIsLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    bookingApi
      .myBookings()
      .then(setBookings)
      .catch((err) => setError(err instanceof Error ? err.message : 'Không tải được danh sách vé'))
      .finally(() => setIsLoading(false))
  }, [])

  return (
    <div className="space-y-4">
      <h1 className="text-2xl font-bold text-slate-800">Vé của tôi</h1>

      {isLoading && <p className="text-slate-500">Đang tải...</p>}
      {error && <p className="text-red-600">{error}</p>}
      {!isLoading && !error && bookings.length === 0 && (
        <p className="text-slate-500">Bạn chưa có vé nào. Hãy tìm chuyến xe và đặt vé!</p>
      )}

      {bookings.map((booking) => (
        <Card key={booking.id} className="p-5">
          <div className="flex items-center justify-between">
            <span className="font-mono text-sm text-slate-500">#{booking.id.slice(0, 8)}</span>
            <Badge tone="blue">{booking.bookingType}</Badge>
          </div>
          <p className="mt-2 text-sm text-slate-600">Ngày đặt: {formatDateTime(booking.dateBooked)}</p>
          <ul className="mt-2 space-y-1 text-sm text-slate-600">
            {booking.details.map((detail) => (
              <li key={detail.id}>
                Ghế {detail.seatCode} · {detail.passengerName} · {formatCurrency(detail.subTotal)}
              </li>
            ))}
          </ul>
          <p className="mt-2 font-semibold text-brand-700">Tổng tiền: {formatCurrency(booking.totalPrice)}</p>
        </Card>
      ))}
    </div>
  )
}
