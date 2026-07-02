import { apiClient } from './client'
import type { AccountViewModel, LoginForm, RegisterForm } from '@/types/dto'

// NOTE: On the backend today, /auth/* is server-rendered Spring MVC (form
// POST + redirect + HttpSession), not JSON. These calls target the JSON
// contract proposed in docs/API_INTEGRATION.md#auth-rest-contract-proposed
// (under /api/auth/**), which the backend needs to expose via a small
// @RestController wrapper around AuthenticationService.
export const authApi = {
  login: (payload: LoginForm) =>
    apiClient.post<AccountViewModel>('/auth/login', payload).then((res) => res.data),

  register: (payload: RegisterForm) =>
    apiClient.post<AccountViewModel>('/auth/register', payload).then((res) => res.data),

  logout: () => apiClient.post<void>('/auth/logout').then((res) => res.data),

  me: () => apiClient.get<AccountViewModel>('/auth/me').then((res) => res.data),
}
