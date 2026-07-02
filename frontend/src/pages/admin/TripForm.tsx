import { useState } from 'react'
import type { FormEvent } from 'react'
import { useNavigate } from 'react-router-dom'
import { tripApi } from '@/api/tripApi'
import type { CreateTripForm } from '@/types/dto'
import { Card } from '@/components/ui/Card'
import { Input } from '@/components/ui/Input'
import { Button } from '@/components/ui/Button'

const emptyForm: CreateTripForm = {
  destinationFrom: '',
  destinationTo: '',
  busCode: '',
  busLicensePlate: '',
  vehicleType: '',
  driverName: '',
  departureTime: '',
  arrivalTime: '',
  price: 0,
}

export function TripForm() {
  const navigate = useNavigate()
  const [form, setForm] = useState<CreateTripForm>(emptyForm)
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const setField = <K extends keyof CreateTripForm>(field: K, value: CreateTripForm[K]) => {
    setForm((prev) => ({ ...prev, [field]: value }))
  }

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault()
    setError(null)
    setIsSubmitting(true)
    try {
      await tripApi.create(form)
      navigate('/admin/trips')
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Tạo chuyến xe thất bại')
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
    <div className="mx-auto max-w-2xl">
      <Card className="p-8">
        <h1 className="text-xl font-bold text-slate-800">Thêm chuyến xe</h1>
        <form onSubmit={handleSubmit} className="mt-6 grid grid-cols-1 gap-4 sm:grid-cols-2">
          <Input
            label="Điểm đi"
            required
            value={form.destinationFrom}
            onChange={(e) => setField('destinationFrom', e.target.value)}
          />
          <Input
            label="Điểm đến"
            required
            value={form.destinationTo}
            onChange={(e) => setField('destinationTo', e.target.value)}
          />
          <Input
            label="Giờ khởi hành"
            type="datetime-local"
            required
            value={form.departureTime}
            onChange={(e) => setField('departureTime', e.target.value)}
          />
          <Input
            label="Giờ đến"
            type="datetime-local"
            required
            value={form.arrivalTime}
            onChange={(e) => setField('arrivalTime', e.target.value)}
          />
          <Input label="Mã xe" required value={form.busCode} onChange={(e) => setField('busCode', e.target.value)} />
          <Input
            label="Biển số xe"
            required
            value={form.busLicensePlate}
            onChange={(e) => setField('busLicensePlate', e.target.value)}
          />
          <Input
            label="Loại xe"
            required
            value={form.vehicleType}
            onChange={(e) => setField('vehicleType', e.target.value)}
          />
          <Input
            label="Tên tài xế"
            required
            value={form.driverName}
            onChange={(e) => setField('driverName', e.target.value)}
          />
          <Input
            label="Giá vé"
            type="number"
            min={0}
            required
            value={form.price}
            onChange={(e) => setField('price', Number(e.target.value))}
          />

          {error && <p className="text-sm text-red-600 sm:col-span-2">{error}</p>}

          <Button type="submit" isLoading={isSubmitting} className="sm:col-span-2">
            Lưu chuyến xe
          </Button>
        </form>
      </Card>
    </div>
  )
}
