import { createContext, useCallback, useContext, useEffect, useMemo, useState } from 'react'
import type { ReactNode } from 'react'
import { authApi } from '@/api/authApi'
import type { AccountViewModel, LoginForm, RegisterForm } from '@/types/dto'

interface AuthContextValue {
  account: AccountViewModel | null
  isLoading: boolean
  login: (form: LoginForm) => Promise<AccountViewModel>
  register: (form: RegisterForm) => Promise<AccountViewModel>
  logout: () => Promise<void>
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined)

const STORAGE_KEY = 'busticket.account'

export function AuthProvider({ children }: { children: ReactNode }) {
  const [account, setAccount] = useState<AccountViewModel | null>(() => {
    const cached = localStorage.getItem(STORAGE_KEY)
    return cached ? (JSON.parse(cached) as AccountViewModel) : null
  })
  const [isLoading, setIsLoading] = useState(true)

  useEffect(() => {
    // The real session lives in the JSESSIONID cookie; this just re-syncs
    // the cached account on reload in case the session expired server-side.
    authApi
      .me()
      .then((data) => setAccount(data))
      .catch(() => setAccount(null))
      .finally(() => setIsLoading(false))
  }, [])

  useEffect(() => {
    if (account) {
      localStorage.setItem(STORAGE_KEY, JSON.stringify(account))
    } else {
      localStorage.removeItem(STORAGE_KEY)
    }
  }, [account])

  const login = useCallback(async (form: LoginForm) => {
    const data = await authApi.login(form)
    setAccount(data)
    return data
  }, [])

  const register = useCallback(async (form: RegisterForm) => {
    const data = await authApi.register(form)
    setAccount(data)
    return data
  }, [])

  const logout = useCallback(async () => {
    await authApi.logout().catch(() => undefined)
    setAccount(null)
  }, [])

  const value = useMemo(
    () => ({ account, isLoading, login, register, logout }),
    [account, isLoading, login, register, logout],
  )

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth(): AuthContextValue {
  const ctx = useContext(AuthContext)
  if (!ctx) throw new Error('useAuth must be used within AuthProvider')
  return ctx
}
