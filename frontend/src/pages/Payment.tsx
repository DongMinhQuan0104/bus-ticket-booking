import { useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { paymentApi } from '@/api/paymentApi'
import type { PaymentMethod } from '@/types/enums'
import { Card } from '@/components/ui/Card'
import { Button } from '@/components/ui/Button'
import { useBookingCart } from '@/context/BookingCartContext'
import { formatCurrency } from '@/lib/format'

const methods: { value: PaymentMethod; label: string; hint: string }[] = [
  { value: 'CASH', label: 'Tiền mặt', hint: 'Thanh toán trực tiếp tại quầy vé' },
  { value: 'COD', label: 'Thanh toán khi nhận vé (COD)', hint: 'Trả tiền khi nhận vé lên xe' },
]

export function Payment() {
  const { bookingId } = useParams<{ bookingId: string }>()
  const { totalPrice, clear } = useBookingCart()
  const navigate = useNavigate()
  const [method, setMethod] = useState<PaymentMethod>('CASH')
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const handlePay = async () => {
    if (!bookingId) return
    setError(null)
    setIsSubmitting(true)
    try {
      await paymentApi.pay({ bookingId, paymentMethod: method })
      clear()
      navigate(`/booking-success/${bookingId}`)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Thanh toán thất bại')
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
    <div className="mx-auto max-w-lg">
      <Card className="p-8">
        <h1 className="text-xl font-bold text-slate-800">Thanh toán</h1>
        <p className="mt-1 text-sm text-slate-500">Mã đặt vé: {bookingId}</p>

        {totalPrice > 0 && (
          <p className="mt-4 text-lg font-semibold text-brand-700">Tổng tiền: {formatCurrency(totalPrice)}</p>
        )}

        <div className="mt-6 space-y-3">
          {methods.map((m) => (
            <label
              key={m.value}
              className={`flex cursor-pointer items-start gap-3 rounded-lg border p-4 transition-colors ${
                method === m.value ? 'border-brand-600 bg-brand-50' : 'border-slate-200'
              }`}
            >
              <input
                type="radio"
                name="paymentMethod"
                className="mt-1"
                checked={method === m.value}
                onChange={() => setMethod(m.value)}
              />
              <span>
                <span className="block font-medium text-slate-800">{m.label}</span>
                <span className="block text-sm text-slate-500">{m.hint}</span>
              </span>
            </label>
          ))}
        </div>

        {error && <p className="mt-4 text-sm text-red-600">{error}</p>}

        <Button className="mt-6 w-full" isLoading={isSubmitting} onClick={handlePay}>
          Xác nhận thanh toán
        </Button>
      </Card>
    </div>
  )
}
