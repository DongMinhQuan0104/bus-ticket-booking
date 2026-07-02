import { apiClient } from './client'
import type { PaymentRequest, PaymentResponse } from '@/types/dto'

// The one endpoint that is a real, stable JSON contract on the backend
// today, unchanged: POST /api/payments -> PaymentController (identical on
// every branch). See docs/API_INTEGRATION.md#payment-rest-contract-existing.
export const paymentApi = {
  pay: (payload: PaymentRequest) =>
    apiClient.post<PaymentResponse>('/payments', payload).then((res) => res.data),
}
