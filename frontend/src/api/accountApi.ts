import { apiClient } from './client'
import type {
  AccountViewModel,
  AdminUpdateAccountForm,
  CreateAccountForm,
  UpdateAccountForm,
} from '@/types/dto'

// Account CRUD services (Admin/Customer/DriverServiceImpl) exist on the
// backend but are not wired to any controller yet. Proposed contract in
// docs/API_INTEGRATION.md#account-rest-contract-proposed.
export const accountApi = {
  list: () => apiClient.get<AccountViewModel[]>('/accounts').then((res) => res.data),

  get: (id: string) => apiClient.get<AccountViewModel>(`/accounts/${id}`).then((res) => res.data),

  create: (payload: CreateAccountForm) =>
    apiClient.post<AccountViewModel>('/accounts', payload).then((res) => res.data),

  updateSelf: (id: string, payload: UpdateAccountForm) =>
    apiClient.put<AccountViewModel>(`/accounts/${id}`, payload).then((res) => res.data),

  updateAsAdmin: (id: string, payload: AdminUpdateAccountForm) =>
    apiClient.put<AccountViewModel>(`/accounts/${id}/admin`, payload).then((res) => res.data),

  remove: (id: string) => apiClient.delete<void>(`/accounts/${id}`).then((res) => res.data),
}
