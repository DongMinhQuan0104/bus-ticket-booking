import { apiClient } from './client'
import type { CreateTripForm, SeatAvailabilityViewModel, TripViewModel } from '@/types/dto'

export interface TripSearchParams {
  from?: string
  to?: string
  date?: string // yyyy-MM-dd
}

// Backend today (Phong branch): TripController exposes JSON for list
// ("/admin/trips") and detail, but create binds a form (@ModelAttribute)
// rather than @RequestBody JSON, and paths aren't under /api. Search by
// route/date and the seat map don't exist yet. This module targets the
// normalized contract in docs/API_INTEGRATION.md#trip-rest-contract-proposed.
export const tripApi = {
  search: (params: TripSearchParams) =>
    apiClient.get<TripViewModel[]>('/trips', { params }).then((res) => res.data),

  get: (tripId: string) =>
    apiClient.get<TripViewModel>(`/trips/${tripId}`).then((res) => res.data),

  create: (payload: CreateTripForm) =>
    apiClient.post<TripViewModel>('/trips', payload).then((res) => res.data),

  seats: (tripId: string) =>
    apiClient.get<SeatAvailabilityViewModel[]>(`/trips/${tripId}/seats`).then((res) => res.data),
}
