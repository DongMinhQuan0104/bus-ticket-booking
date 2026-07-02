import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { tripApi } from '@/api/tripApi'
import type { TripViewModel } from '@/types/dto'
import { Card } from '@/components/ui/Card'
import { Badge } from '@/components/ui/Badge'
import { Button } from '@/components/ui/Button'
import { formatCurrency, formatDateTime } from '@/lib/format'

export function TripList() {
  const [trips, setTrips] = useState<TripViewModel[]>([])
  const [isLoading, setIsLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    tripApi
      .search({})
      .then(setTrips)
      .catch((err) => setError(err instanceof Error ? err.message : 'Không tải được danh sách chuyến xe'))
      .finally(() => setIsLoading(false))
  }, [])

  return (
    <div className="space-y-4">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold text-slate-800">Chuyến xe</h1>
        <Link to="/admin/trips/new">
          <Button>+ Thêm chuyến xe</Button>
        </Link>
      </div>

      {isLoading && <p className="text-slate-500">Đang tải...</p>}
      {error && <p className="text-red-600">{error}</p>}

      <Card className="divide-y divide-slate-100">
        {trips.map((trip) => (
          <div key={trip.id} className="flex flex-col justify-between gap-2 p-4 sm:flex-row sm:items-center">
            <div>
              <p className="font-medium text-slate-800">
                {trip.destinationFrom} → {trip.destinationTo}
              </p>
              <p className="text-sm text-slate-500">
                {formatDateTime(trip.departureTime)} · {trip.busLicensePlate} · {trip.driverName}
              </p>
            </div>
            <div className="flex items-center gap-3">
              <span className="font-semibold text-brand-700">{formatCurrency(trip.price)}</span>
              <Badge tone="green">{trip.status}</Badge>
            </div>
          </div>
        ))}
        {!isLoading && trips.length === 0 && <p className="p-4 text-sm text-slate-500">Chưa có chuyến xe nào.</p>}
      </Card>
    </div>
  )
}
