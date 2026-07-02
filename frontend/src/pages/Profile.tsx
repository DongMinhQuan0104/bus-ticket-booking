import { useState } from 'react'
import type { FormEvent } from 'react'
import { useAuth } from '@/context/AuthContext'
import { accountApi } from '@/api/accountApi'
import { Card } from '@/components/ui/Card'
import { Input } from '@/components/ui/Input'
import { Button } from '@/components/ui/Button'
import { Badge } from '@/components/ui/Badge'

export function Profile() {
  const { account } = useAuth()
  const [fullName, setFullName] = useState(account?.fullName ?? '')
  const [phoneNumber, setPhoneNumber] = useState(account?.phoneNumber ?? '')
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [message, setMessage] = useState<string | null>(null)
  const [error, setError] = useState<string | null>(null)

  if (!account) return null

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault()
    setError(null)
    setMessage(null)
    setIsSubmitting(true)
    try {
      await accountApi.updateSelf(account.id, { fullName, phoneNumber, email: account.email })
      setMessage('Cập nhật thông tin thành công')
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Cập nhật thất bại')
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
    <div className="mx-auto max-w-lg">
      <Card className="p-8">
        <div className="flex items-center justify-between">
          <h1 className="text-xl font-bold text-slate-800">Thông tin cá nhân</h1>
          <Badge tone="blue">{account.role}</Badge>
        </div>

        <form onSubmit={handleSubmit} className="mt-6 space-y-4">
          <Input label="Email" value={account.email} disabled />
          <Input label="Họ tên" value={fullName} onChange={(e) => setFullName(e.target.value)} />
          <Input
            label="Số điện thoại"
            value={phoneNumber}
            pattern="^0\d{9}$"
            title="Số điện thoại phải bắt đầu bằng 0 và có 10 chữ số"
            onChange={(e) => setPhoneNumber(e.target.value)}
          />

          {message && <p className="text-sm text-green-600">{message}</p>}
          {error && <p className="text-sm text-red-600">{error}</p>}

          <Button type="submit" isLoading={isSubmitting} className="w-full">
            Lưu thay đổi
          </Button>
        </form>
      </Card>
    </div>
  )
}
