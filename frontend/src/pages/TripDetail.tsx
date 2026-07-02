import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { tripApi } from '@/api/tripApi'
import type { SeatAvailabilityViewModel, TripViewModel } from '@/types/dto'
import { Card } from '@/components/ui/Card'
import { Badge } from '@/components/ui/Badge'
import { Button } from '@/components/ui/Button'
import { SeatMap } from '@/components/trip/SeatMap'
import { formatCurrency, formatDateTime } from '@/lib/format'
import { useBookingCart } from '@/context/BookingCartContext'
import { useAuth } from '@/context/AuthContext'

export function TripDetail() {
  const { tripId } = useParams<{ tripId: string }>()
  const navigate = useNavigate()
  const { account } = useAuth()
  const { setTrip: setCartTrip, setPassengers } = useBookingCart()

  const [trip, setTrip] = useState<TripViewModel | null>(null)
  const [seats, setSeats] = useState<SeatAvailabilityViewModel[]>([])
  const [selectedSeats, setSelectedSeats] = useState<string[]>([])
  const [isLoading, setIsLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    if (!tripId) return
    setIsLoading(true)
    Promise.all([tripApi.get(tripId), tripApi.seats(tripId)])
      .then(([tripData, seatData]) => {
        setTrip(tripData)
        setSeats(seatData)
      })
      .catch((err) => setError(err instanceof Error ? err.message : 'Không tải được thông tin chuyến xe'))
      .finally(() => setIsLoading(false))
  }, [tripId])

  const toggleSeat = (seatCode: string) => {
    setSelectedSeats((prev) =>
      prev.includes(seatCode) ? prev.filter((s) => s !== seatCode) : [...prev, seatCode],
    )
  }

  const handleContinue = () => {
    if (!trip || selectedSeats.length === 0) return
    setCartTrip(trip)
    setPassengers(selectedSeats.map((seatCode) => ({ passengerName: '', seatCode, luggageWeightKg: 0 })))
    navigate(account ? '/checkout' : '/login', { state: { from: { pathname: '/checkout' } } })
  }

  if (isLoading) return <p className="text-slate-500">Đang tải...</p>
  if (error) return <p className="text-red-600">{error}</p>
  if (!trip) return null

  return (
    <div className="grid grid-cols-1 gap-6 md:grid-cols-3">
      <Card className="p-6 md:col-span-2">
        <div className="flex items-center gap-2">
          <h1 className="text-xl font-bold text-slate-800">
            {trip.destinationFrom} → {trip.destinationTo}
          </h1>
          <Badge tone="green">{trip.status}</Badge>
        </div>
        <p className="mt-2 text-sm text-slate-500">
          Khởi hành: {formatDateTime(trip.departureTime)} · Đến nơi: {formatDateTime(trip.arrivalTime)}
        </p>
        <p className="mt-1 text-sm text-slate-500">
          {trip.vehicleType} · Biển số {trip.busLicensePlate} · Tài xế {trip.driverName}
        </p>

        <h2 className="mt-6 mb-3 text-sm font-semibold text-slate-700">Chọn ghế ({selectedSeats.length} đã chọn)</h2>
        <SeatMap seats={seats} selected={selectedSeats} onToggle={toggleSeat} />
      </Card>

      <Card className="h-fit p-6">
        <h2 className="text-sm font-semibold text-slate-700">Tóm tắt</h2>
        <div className="mt-3 space-y-2 text-sm text-slate-600">
          <div className="flex justify-between">
            <span>Giá vé / ghế</span>
            <span>{formatCurrency(trip.price)}</span>
          </div>
          <div className="flex justify-between">
            <span>Số ghế đã chọn</span>
            <span>{selectedSeats.length}</span>
          </div>
          <div className="flex justify-between font-semibold text-slate-800">
            <span>Tạm tính</span>
            <span>{formatCurrency(trip.price * selectedSeats.length)}</span>
          </div>
        </div>
        <Button className="mt-4 w-full" disabled={selectedSeats.length === 0} onClick={handleContinue}>
          Tiếp tục
        </Button>
      </Card>
    </div>
  )
}
