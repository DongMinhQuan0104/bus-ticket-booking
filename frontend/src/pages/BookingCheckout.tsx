import { useState } from 'react'
import type { FormEvent } from 'react'
import { Navigate, useNavigate } from 'react-router-dom'
import { useBookingCart } from '@/context/BookingCartContext'
import { bookingApi } from '@/api/bookingApi'
import { Card } from '@/components/ui/Card'
import { Input } from '@/components/ui/Input'
import { Button } from '@/components/ui/Button'
import { formatCurrency } from '@/lib/format'

export function BookingCheckout() {
  const { trip, passengers, setPassengers, setBookingId, totalPrice } = useBookingCart()
  const navigate = useNavigate()
  const [note, setNote] = useState('')
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [error, setError] = useState<string | null>(null)

  if (!trip || passengers.length === 0) {
    return <Navigate to="/trips" replace />
  }

  const updatePassenger = (index: number, field: 'passengerName' | 'luggageWeightKg', value: string) => {
    const next = [...passengers]
    next[index] = {
      ...next[index],
      [field]: field === 'luggageWeightKg' ? Number(value) : value,
    }
    setPassengers(next)
  }

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault()
    setError(null)
    setIsSubmitting(true)
    try {
      const booking = await bookingApi.create({
        tripId: trip.id,
        bookingType: 'ONEWAY',
        note,
        passengers,
      })
      setBookingId(booking.id)
      navigate(`/payment/${booking.id}`)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Không tạo được đơn đặt vé')
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
    <div className="grid grid-cols-1 gap-6 md:grid-cols-3">
      <Card className="p-6 md:col-span-2">
        <h1 className="text-xl font-bold text-slate-800">Thông tin hành khách</h1>
        <form onSubmit={handleSubmit} className="mt-4 space-y-4">
          {passengers.map((passenger, index) => (
            <div key={passenger.seatCode} className="grid grid-cols-1 gap-3 rounded-lg border border-slate-200 p-4 sm:grid-cols-3">
              <div className="sm:col-span-1">
                <span className="text-xs font-semibold uppercase text-slate-400">Ghế {passenger.seatCode}</span>
              </div>
              <Input
                label="Họ tên hành khách"
                required
                value={passenger.passengerName}
                onChange={(e) => updatePassenger(index, 'passengerName', e.target.value)}
              />
              <Input
                label="Hành lý (kg)"
                type="number"
                min={0}
                value={passenger.luggageWeightKg}
                onChange={(e) => updatePassenger(index, 'luggageWeightKg', e.target.value)}
              />
            </div>
          ))}

          <Input label="Ghi chú" value={note} onChange={(e) => setNote(e.target.value)} />

          {error && <p className="text-sm text-red-600">{error}</p>}

          <Button type="submit" isLoading={isSubmitting} className="w-full">
            Xác nhận đặt vé
          </Button>
        </form>
      </Card>

      <Card className="h-fit p-6">
        <h2 className="text-sm font-semibold text-slate-700">Chuyến đi</h2>
        <p className="mt-2 text-sm text-slate-600">
          {trip.destinationFrom} → {trip.destinationTo}
        </p>
        <div className="mt-4 space-y-2 text-sm text-slate-600">
          <div className="flex justify-between">
            <span>Số ghế</span>
            <span>{passengers.map((p) => p.seatCode).join(', ')}</span>
          </div>
          <div className="flex justify-between font-semibold text-slate-800">
            <span>Tổng tiền</span>
            <span>{formatCurrency(totalPrice)}</span>
          </div>
        </div>
      </Card>
    </div>
  )
}
