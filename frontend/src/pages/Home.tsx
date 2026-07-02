import { useState } from 'react'
import type { FormEvent } from 'react'
import { useNavigate } from 'react-router-dom'
import { Button } from '@/components/ui/Button'
import { Card } from '@/components/ui/Card'
import { Input } from '@/components/ui/Input'

export function Home() {
  const navigate = useNavigate()
  const [from, setFrom] = useState('')
  const [to, setTo] = useState('')
  const [date, setDate] = useState('')

  const handleSearch = (e: FormEvent) => {
    e.preventDefault()
    const params = new URLSearchParams()
    if (from) params.set('from', from)
    if (to) params.set('to', to)
    if (date) params.set('date', date)
    navigate(`/trips?${params.toString()}`)
  }

  return (
    <div className="space-y-10">
      <section className="rounded-2xl bg-gradient-to-br from-brand-600 to-brand-800 px-6 py-12 text-white shadow-lg md:px-12">
        <h1 className="text-3xl font-bold md:text-4xl">Đặt vé xe khách trực tuyến, nhanh &amp; tiện lợi</h1>
        <p className="mt-2 max-w-xl text-brand-100">
          Tìm kiếm chuyến xe, chọn chỗ ngồi và thanh toán chỉ trong vài bước.
        </p>

        <Card className="mt-8 p-4 md:p-6">
          <form onSubmit={handleSearch} className="grid grid-cols-1 gap-4 md:grid-cols-4">
            <Input label="Điểm đi" placeholder="VD: Hà Nội" value={from} onChange={(e) => setFrom(e.target.value)} />
            <Input
              label="Điểm đến"
              placeholder="VD: Đà Nẵng"
              value={to}
              onChange={(e) => setTo(e.target.value)}
            />
            <Input label="Ngày đi" type="date" value={date} onChange={(e) => setDate(e.target.value)} />
            <div className="flex items-end">
              <Button type="submit" className="w-full">
                Tìm chuyến xe
              </Button>
            </div>
          </form>
        </Card>
      </section>

      <section className="grid grid-cols-1 gap-6 md:grid-cols-3">
        <Card className="p-6">
          <h3 className="font-semibold text-slate-800">Đa dạng tuyến đường</h3>
          <p className="mt-1 text-sm text-slate-500">Kết nối các bến xe và tuyến đường trên toàn quốc.</p>
        </Card>
        <Card className="p-6">
          <h3 className="font-semibold text-slate-800">Chọn ghế trực quan</h3>
          <p className="mt-1 text-sm text-slate-500">Xem sơ đồ ghế còn trống theo thời gian thực trước khi đặt.</p>
        </Card>
        <Card className="p-6">
          <h3 className="font-semibold text-slate-800">Thanh toán linh hoạt</h3>
          <p className="mt-1 text-sm text-slate-500">Hỗ trợ thanh toán tiền mặt (CASH) hoặc khi nhận vé (COD).</p>
        </Card>
      </section>
    </div>
  )
}
