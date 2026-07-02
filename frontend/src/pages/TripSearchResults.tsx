import { useEffect, useState } from 'react'
import { useSearchParams } from 'react-router-dom'
import { tripApi } from '@/api/tripApi'
import type { TripViewModel } from '@/types/dto'
import { TripCard } from '@/components/trip/TripCard'
import { Input } from '@/components/ui/Input'
import { Button } from '@/components/ui/Button'

export function TripSearchResults() {
  const [searchParams, setSearchParams] = useSearchParams()
  const [trips, setTrips] = useState<TripViewModel[]>([])
  const [isLoading, setIsLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const from = searchParams.get('from') ?? ''
  const to = searchParams.get('to') ?? ''
  const date = searchParams.get('date') ?? ''

  useEffect(() => {
    setIsLoading(true)
    setError(null)
    tripApi
      .search({ from: from || undefined, to: to || undefined, date: date || undefined })
      .then(setTrips)
      .catch((err) => setError(err instanceof Error ? err.message : 'Không tải được danh sách chuyến xe'))
      .finally(() => setIsLoading(false))
  }, [from, to, date])

  const updateParam = (key: string, value: string) => {
    const next = new URLSearchParams(searchParams)
    if (value) next.set(key, value)
    else next.delete(key)
    setSearchParams(next)
  }

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold text-slate-800">Kết quả tìm kiếm</h1>

      <div className="grid grid-cols-1 gap-4 rounded-xl border border-slate-200 bg-white p-4 md:grid-cols-4">
        <Input label="Điểm đi" value={from} onChange={(e) => updateParam('from', e.target.value)} />
        <Input label="Điểm đến" value={to} onChange={(e) => updateParam('to', e.target.value)} />
        <Input label="Ngày đi" type="date" value={date} onChange={(e) => updateParam('date', e.target.value)} />
        <div className="flex items-end">
          <Button variant="secondary" className="w-full" onClick={() => setSearchParams({})}>
            Xóa bộ lọc
          </Button>
        </div>
      </div>

      {isLoading && <p className="text-slate-500">Đang tải chuyến xe...</p>}
      {error && <p className="text-red-600">{error}</p>}
      {!isLoading && !error && trips.length === 0 && (
        <p className="text-slate-500">Không tìm thấy chuyến xe phù hợp.</p>
      )}

      <div className="space-y-4">
        {trips.map((trip) => (
          <TripCard key={trip.id} trip={trip} />
        ))}
      </div>
    </div>
  )
}
