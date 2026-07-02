import { Link } from 'react-router-dom'
import type { TripViewModel } from '@/types/dto'
import { Card } from '@/components/ui/Card'
import { Badge } from '@/components/ui/Badge'
import { Button } from '@/components/ui/Button'
import { formatCurrency, formatDateTime } from '@/lib/format'

const statusTone: Record<string, 'green' | 'red' | 'amber' | 'slate'> = {
  SCHEDULED: 'green',
  AVAILABLE: 'green',
  FULL: 'amber',
  CANCELLED: 'red',
  COMPLETED: 'slate',
  NOT_AVAILABLE: 'red',
}

export function TripCard({ trip }: { trip: TripViewModel }) {
  return (
    <Card className="flex flex-col gap-4 p-5 md:flex-row md:items-center md:justify-between">
      <div className="flex-1">
        <div className="flex items-center gap-2">
          <h3 className="text-lg font-semibold text-slate-800">
            {trip.destinationFrom} → {trip.destinationTo}
          </h3>
          <Badge tone={statusTone[trip.status] ?? 'slate'}>{trip.status}</Badge>
        </div>
        <p className="mt-1 text-sm text-slate-500">
          Khởi hành: {formatDateTime(trip.departureTime)} · Đến nơi: {formatDateTime(trip.arrivalTime)}
        </p>
        <p className="mt-1 text-sm text-slate-500">
          {trip.vehicleType} · {trip.busLicensePlate} · Tài xế {trip.driverName}
        </p>
        <p className="mt-1 text-sm text-slate-500">Còn {trip.availableSeats}/{trip.totalSeats} chỗ trống</p>
      </div>

      <div className="flex flex-col items-end gap-2">
        <span className="text-xl font-bold text-brand-700">{formatCurrency(trip.price)}</span>
        <Link to={`/trips/${trip.id}`}>
          <Button disabled={trip.availableSeats <= 0}>Chọn chuyến</Button>
        </Link>
      </div>
    </Card>
  )
}
