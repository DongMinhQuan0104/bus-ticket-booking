import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { accountApi } from '@/api/accountApi'
import type { AccountViewModel } from '@/types/dto'
import { Card } from '@/components/ui/Card'
import { Badge } from '@/components/ui/Badge'
import { Button } from '@/components/ui/Button'

export function AccountList() {
  const [accounts, setAccounts] = useState<AccountViewModel[]>([])
  const [isLoading, setIsLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const load = () => {
    setIsLoading(true)
    accountApi
      .list()
      .then(setAccounts)
      .catch((err) => setError(err instanceof Error ? err.message : 'Không tải được danh sách tài khoản'))
      .finally(() => setIsLoading(false))
  }

  useEffect(load, [])

  const handleDelete = async (id: string) => {
    if (!confirm('Xóa tài khoản này?')) return
    await accountApi.remove(id).catch((err) => setError(err instanceof Error ? err.message : 'Xóa thất bại'))
    load()
  }

  return (
    <div className="space-y-4">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold text-slate-800">Tài khoản</h1>
        <Link to="/admin/accounts/new">
          <Button>+ Thêm tài khoản</Button>
        </Link>
      </div>

      {isLoading && <p className="text-slate-500">Đang tải...</p>}
      {error && <p className="text-red-600">{error}</p>}

      <Card className="divide-y divide-slate-100">
        {accounts.map((acc) => (
          <div key={acc.id} className="flex items-center justify-between p-4">
            <div>
              <p className="font-medium text-slate-800">{acc.fullName || acc.email}</p>
              <p className="text-sm text-slate-500">{acc.email} · {acc.phoneNumber}</p>
            </div>
            <div className="flex items-center gap-3">
              <Badge tone="blue">{acc.role}</Badge>
              <Badge tone={acc.status === 'AVAILABLE' ? 'green' : 'red'}>{acc.status}</Badge>
              <Link to={`/admin/accounts/${acc.id}/edit`}>
                <Button variant="secondary">Sửa</Button>
              </Link>
              <Button variant="danger" onClick={() => handleDelete(acc.id)}>
                Xóa
              </Button>
            </div>
          </div>
        ))}
        {!isLoading && accounts.length === 0 && <p className="p-4 text-sm text-slate-500">Chưa có tài khoản nào.</p>}
      </Card>
    </div>
  )
}
