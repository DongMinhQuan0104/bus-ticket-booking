import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { stationApi } from '@/api/stationApi'
import type { StationViewModel } from '@/types/dto'
import { Card } from '@/components/ui/Card'
import { Badge } from '@/components/ui/Badge'
import { Button } from '@/components/ui/Button'

export function StationList() {
  const [stations, setStations] = useState<StationViewModel[]>([])
  const [isLoading, setIsLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const load = () => {
    setIsLoading(true)
    stationApi
      .list()
      .then(setStations)
      .catch((err) => setError(err instanceof Error ? err.message : 'Không tải được danh sách bến xe'))
      .finally(() => setIsLoading(false))
  }

  useEffect(load, [])

  const handleDelete = async (id: string) => {
    if (!confirm('Xóa bến xe này?')) return
    await stationApi.remove(id).catch((err) => setError(err instanceof Error ? err.message : 'Xóa thất bại'))
    load()
  }

  return (
    <div className="space-y-4">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold text-slate-800">Bến xe</h1>
        <Link to="/admin/stations/new">
          <Button>+ Thêm bến xe</Button>
        </Link>
      </div>

      {isLoading && <p className="text-slate-500">Đang tải...</p>}
      {error && <p className="text-red-600">{error}</p>}

      <Card className="divide-y divide-slate-100">
        {stations.map((station) => (
          <div key={station.id} className="flex items-center justify-between p-4">
            <div>
              <p className="font-medium text-slate-800">{station.name}</p>
              <p className="text-sm text-slate-500">{station.address}</p>
            </div>
            <div className="flex items-center gap-3">
              <Badge tone={station.status === 'AVAILABLE' ? 'green' : 'red'}>{station.status}</Badge>
              <Link to={`/admin/stations/${station.id}/edit`}>
                <Button variant="secondary">Sửa</Button>
              </Link>
              <Button variant="danger" onClick={() => handleDelete(station.id)}>
                Xóa
              </Button>
            </div>
          </div>
        ))}
        {!isLoading && stations.length === 0 && <p className="p-4 text-sm text-slate-500">Chưa có bến xe nào.</p>}
      </Card>
    </div>
  )
}
