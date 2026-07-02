import axios from 'axios'

// The backend has no context-path today, and only PaymentController lives
// under /api (POST /api/payments) — auth is server-rendered under /auth,
// stations under /stations, trips under /admin/trips. This frontend is
// built against the *target* contract proposed in
// docs/API_INTEGRATION.md, which normalizes every JSON endpoint under
// /api/** so the backend team has one convention to converge on. In dev,
// Vite proxies /api to http://localhost:8080 (see vite.config.ts). In
// prod, set VITE_API_BASE_URL to the backend origin (e.g. https://api.example.com/api).
const baseURL = import.meta.env.VITE_API_BASE_URL ?? '/api'

export const apiClient = axios.create({
  baseURL,
  withCredentials: true, // backend auth is session-cookie based (JSESSIONID), not JWT
  headers: {
    'Content-Type': 'application/json',
  },
})

apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    const message =
      error.response?.data?.errorMessage ??
      error.response?.data?.message ??
      error.message ??
      'Unexpected network error'
    return Promise.reject(new Error(message))
  },
)
