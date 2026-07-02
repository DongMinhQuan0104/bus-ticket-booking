import { useEffect, useState } from 'react'
import type { FormEvent } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { stationApi } from '@/api/stationApi'
import { Card } from '@/components/ui/Card'
import { Input } from '@/components/ui/Input'
import { Button } from '@/components/ui/Button'

export function StationForm() {
  const { stationId } = useParams<{ stationId: string }>()
  const isEditing = Boolean(stationId)
  const navigate = useNavigate()

  const [name, setName] = useState('')
  const [address, setAddress] = useState('')
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    if (!stationId) return
    stationApi.get(stationId).then((station) => {
      setName(station.name)
      setAddress(station.address)
    })
  }, [stationId])

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault()
    setError(null)
    setIsSubmitting(true)
    try {
      if (isEditing && stationId) {
        await stationApi.update(stationId, { name, address })
      } else {
        await stationApi.create({ name, address })
      }
      navigate('/admin/stations')
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Lưu bến xe thất bại')
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
    <div className="mx-auto max-w-lg">
      <Card className="p-8">
        <h1 className="text-xl font-bold text-slate-800">{isEditing ? 'Sửa bến xe' : 'Thêm bến xe'}</h1>
        <form onSubmit={handleSubmit} className="mt-6 space-y-4">
          <Input label="Tên bến xe" required value={name} onChange={(e) => setName(e.target.value)} />
          <Input label="Địa chỉ" required value={address} onChange={(e) => setAddress(e.target.value)} />
          {error && <p className="text-sm text-red-600">{error}</p>}
          <Button type="submit" isLoading={isSubmitting} className="w-full">
            Lưu
          </Button>
        </form>
      </Card>
    </div>
  )
}
