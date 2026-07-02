import { apiClient } from './client'
import type { CreateStationForm, StationViewModel, UpdateStationForm } from '@/types/dto'

// Backend today (AnChu branch): plain @Controller returning Thymeleaf
// views under /stations, form-encoded create/update, no JSON at all.
// Modeled here as JSON under /api per the REST proposal in
// docs/API_INTEGRATION.md#station-rest-contract-proposed.
export const stationApi = {
  list: () => apiClient.get<StationViewModel[]>('/stations').then((res) => res.data),

  get: (id: string) => apiClient.get<StationViewModel>(`/stations/${id}`).then((res) => res.data),

  create: (payload: CreateStationForm) =>
    apiClient.post<StationViewModel>('/stations', payload).then((res) => res.data),

  update: (id: string, payload: UpdateStationForm) =>
    apiClient.put<StationViewModel>(`/stations/${id}`, payload).then((res) => res.data),

  remove: (id: string) => apiClient.delete<void>(`/stations/${id}`).then((res) => res.data),
}
