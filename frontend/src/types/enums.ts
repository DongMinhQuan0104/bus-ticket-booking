// Mirrors backend enums under Domain/Enum on the Java side.
// See docs/API_INTEGRATION.md#enums for the source of truth and branch notes.

export const Role = {
  ADMIN: 'ADMIN',
  DRIVER: 'DRIVER',
  CUSTOMER: 'CUSTOMER',
} as const
export type Role = (typeof Role)[keyof typeof Role]

export const Status = {
  AVAILABLE: 'AVAILABLE',
  NOT_AVAILABLE: 'NOT_AVAILABLE',
} as const
export type Status = (typeof Status)[keyof typeof Status]

// Richer trip lifecycle (Phong branch). Kept alongside Status because the
// backend has not yet unified Trip's status field across branches.
export const TripStatus = {
  SCHEDULED: 'SCHEDULED',
  FULL: 'FULL',
  CANCELLED: 'CANCELLED',
  COMPLETED: 'COMPLETED',
} as const
export type TripStatus = (typeof TripStatus)[keyof typeof TripStatus]

export const SeatStatus = {
  AVAILABLE: 'AVAILABLE',
  BOOKED: 'BOOKED',
} as const
export type SeatStatus = (typeof SeatStatus)[keyof typeof SeatStatus]

export const PaymentMethod = {
  COD: 'COD',
  CASH: 'CASH',
} as const
export type PaymentMethod = (typeof PaymentMethod)[keyof typeof PaymentMethod]

export const TransactionStatus = {
  PENDING: 'PENDING',
  PAID: 'PAID',
} as const
export type TransactionStatus = (typeof TransactionStatus)[keyof typeof TransactionStatus]

export const BookingType = {
  ONEWAY: 'ONEWAY',
  ROUNDTRIP: 'ROUNDTRIP',
} as const
export type BookingType = (typeof BookingType)[keyof typeof BookingType]
