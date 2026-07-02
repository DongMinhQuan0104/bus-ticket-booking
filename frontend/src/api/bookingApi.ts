import { apiClient } from './client'
import type { BookingViewModel, CreateBookingRequest } from '@/types/dto'

// Not present on the backend on any branch today. Proposed contract lives
// in docs/API_INTEGRATION.md#booking-rest-contract-proposed. Building the
// FE against this now so the checkout flow is ready once BE lands it.
export const bookingApi = {
  create: (payload: CreateBookingRequest) =>
    apiClient.post<BookingViewModel>('/bookings', payload).then((res) => res.data),

  get: (id: string) => apiClient.get<BookingViewModel>(`/bookings/${id}`).then((res) => res.data),

  myBookings: () => apiClient.get<BookingViewModel[]>('/bookings/me').then((res) => res.data),
}
