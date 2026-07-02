// Request/response shapes mirrored from the backend DTOs.
// See docs/API_INTEGRATION.md for the endpoint each DTO belongs to.
import type { PaymentMethod, Role, Status, TripStatus } from './enums'

export interface LoginForm {
  email: string
  password: string
}

export interface RegisterForm {
  email: string
  password: string
  confirmPassword: string
}

export interface ChangePasswordForm {
  oldPassword: string
  newPassword: string
  confirmNewPassword: string
}

export interface CreateAccountForm {
  role: Role
  email: string
  password: string
}

export interface UpdateAccountForm {
  fullName: string
  email: string
  phoneNumber: string
}

export interface AdminUpdateAccountForm {
  fullName: string
  role: Role
  email: string
  password: string
  phoneNumber: string
  status: Status
}

export interface AccountViewModel {
  id: string
  fullName: string
  role: Role
  email: string
  phoneNumber: string
  status: Status
}

export interface CreateStationForm {
  name: string
  address: string
}

export interface UpdateStationForm {
  name: string
  address: string
}

export interface StationViewModel {
  id: string
  name: string
  address: string
  status: Status
}

export interface CreateTripForm {
  destinationFrom: string
  destinationTo: string
  busCode: string
  busLicensePlate: string
  vehicleType: string
  driverName: string
  departureTime: string // yyyy-MM-dd'T'HH:mm
  arrivalTime: string // yyyy-MM-dd'T'HH:mm
  price: number
}

export interface TripViewModel {
  id: string
  destinationFrom: string
  destinationTo: string
  departureTime: string
  arrivalTime: string
  busCode: string
  busLicensePlate: string
  vehicleType: string
  driverName: string
  price: number
  totalSeats: number
  availableSeats: number
  status: TripStatus
}

export interface PaymentRequest {
  bookingId: string
  paymentMethod: PaymentMethod
}

export interface PaymentResponse {
  paymentId: string
  createPayment: string
  paymentMethod: PaymentMethod
  bookingId: string
}

// --- Not yet exposed by any controller on the backend today. Modeled here
// so the frontend can be built against the target contract proposed in
// docs/API_INTEGRATION.md (section "Endpoints to be added"). ---

export interface PassengerInput {
  passengerName: string
  seatCode: string
  luggageWeightKg: number
}

export interface CreateBookingRequest {
  tripId: string
  bookingType: 'ONEWAY' | 'ROUNDTRIP'
  note?: string
  passengers: PassengerInput[]
}

export interface BookingDetailViewModel {
  id: string
  passengerName: string
  ticketPrice: number
  luggageWeightKg: number
  luggageFee: number
  subTotal: number
  seatCode: string
}

export interface BookingViewModel {
  id: string
  dateBooked: string
  totalPrice: number
  note: string
  bookingType: 'ONEWAY' | 'ROUNDTRIP'
  tripId: string
  details: BookingDetailViewModel[]
}

export interface SeatAvailabilityViewModel {
  id: string
  seatCode: string
  status: 'AVAILABLE' | 'BOOKED'
}
