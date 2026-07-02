import { useEffect, useState } from 'react'
import type { FormEvent } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { accountApi } from '@/api/accountApi'
import { Role, Status } from '@/types/enums'
import { Card } from '@/components/ui/Card'
import { Input } from '@/components/ui/Input'
import { Button } from '@/components/ui/Button'

export function AccountForm() {
  const { accountId } = useParams<{ accountId: string }>()
  const isEditing = Boolean(accountId)
  const navigate = useNavigate()

  const [fullName, setFullName] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [phoneNumber, setPhoneNumber] = useState('')
  const [role, setRole] = useState<Role>(Role.CUSTOMER)
  const [status, setStatus] = useState<Status>(Status.AVAILABLE)
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    if (!accountId) return
    accountApi.get(accountId).then((acc) => {
      setFullName(acc.fullName)
      setEmail(acc.email)
      setPhoneNumber(acc.phoneNumber)
      setRole(acc.role)
      setStatus(acc.status)
    })
  }, [accountId])

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault()
    setError(null)
    setIsSubmitting(true)
    try {
      if (isEditing && accountId) {
        await accountApi.updateAsAdmin(accountId, { fullName, email, password, phoneNumber, role, status })
      } else {
        await accountApi.create({ role, email, password })
      }
      navigate('/admin/accounts')
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Lưu tài khoản thất bại')
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
    <div className="mx-auto max-w-lg">
      <Card className="p-8">
        <h1 className="text-xl font-bold text-slate-800">{isEditing ? 'Sửa tài khoản' : 'Thêm tài khoản'}</h1>
        <form onSubmit={handleSubmit} className="mt-6 space-y-4">
          {isEditing && (
            <Input label="Họ tên" value={fullName} onChange={(e) => setFullName(e.target.value)} />
          )}
          <Input label="Email" type="email" required value={email} onChange={(e) => setEmail(e.target.value)} />
          <Input
            label={isEditing ? 'Mật khẩu mới (để trống nếu không đổi)' : 'Mật khẩu'}
            type="password"
            required={!isEditing}
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
          {isEditing && (
            <Input label="Số điện thoại" value={phoneNumber} onChange={(e) => setPhoneNumber(e.target.value)} />
          )}

          <div className="flex flex-col gap-1">
            <label className="text-sm font-medium text-slate-700">Vai trò</label>
            <select
              className="rounded-lg border border-slate-300 px-3 py-2 text-sm"
              value={role}
              onChange={(e) => setRole(e.target.value as Role)}
            >
              {Object.values(Role).map((r) => (
                <option key={r} value={r}>
                  {r}
                </option>
              ))}
            </select>
          </div>

          {isEditing && (
            <div className="flex flex-col gap-1">
              <label className="text-sm font-medium text-slate-700">Trạng thái</label>
              <select
                className="rounded-lg border border-slate-300 px-3 py-2 text-sm"
                value={status}
                onChange={(e) => setStatus(e.target.value as Status)}
              >
                {Object.values(Status).map((s) => (
                  <option key={s} value={s}>
                    {s}
                  </option>
                ))}
              </select>
            </div>
          )}

          {error && <p className="text-sm text-red-600">{error}</p>}

          <Button type="submit" isLoading={isSubmitting} className="w-full">
            Lưu
          </Button>
        </form>
      </Card>
    </div>
  )
}
