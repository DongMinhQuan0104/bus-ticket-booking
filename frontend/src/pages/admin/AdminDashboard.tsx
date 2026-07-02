import { Card } from '@/components/ui/Card'

export function AdminDashboard() {
  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold text-slate-800">Tổng quan quản trị</h1>
      <div className="grid grid-cols-1 gap-4 sm:grid-cols-3">
        <Card className="p-6">
          <p className="text-sm text-slate-500">Bến xe</p>
          <p className="mt-2 text-2xl font-bold text-slate-800">Quản lý</p>
        </Card>
        <Card className="p-6">
          <p className="text-sm text-slate-500">Chuyến xe</p>
          <p className="mt-2 text-2xl font-bold text-slate-800">Lập lịch</p>
        </Card>
        <Card className="p-6">
          <p className="text-sm text-slate-500">Tài khoản</p>
          <p className="mt-2 text-2xl font-bold text-slate-800">Phân quyền</p>
        </Card>
      </div>
      <p className="text-sm text-slate-500">
        Chọn một mục ở thanh bên để quản lý bến xe, chuyến xe hoặc tài khoản người dùng.
      </p>
    </div>
  )
}
