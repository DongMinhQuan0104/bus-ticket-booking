import { createContext, useContext, useMemo, useState } from 'react'
import type { ReactNode } from 'react'
import type { PassengerInput, TripViewModel } from '@/types/dto'

interface BookingCartValue {
  trip: TripViewModel | null
  passengers: PassengerInput[]
  bookingId: string | null
  setTrip: (trip: TripViewModel) => void
  setPassengers: (passengers: PassengerInput[]) => void
  setBookingId: (id: string) => void
  clear: () => void
  totalPrice: number
}

const BookingCartContext = createContext<BookingCartValue | undefined>(undefined)

export function BookingCartProvider({ children }: { children: ReactNode }) {
  const [trip, setTrip] = useState<TripViewModel | null>(null)
  const [passengers, setPassengers] = useState<PassengerInput[]>([])
  const [bookingId, setBookingId] = useState<string | null>(null)

  const totalPrice = useMemo(() => {
    if (!trip) return 0
    return passengers.reduce((sum, p) => sum + trip.price + p.luggageWeightKg * 5000, 0)
  }, [trip, passengers])

  const clear = () => {
    setTrip(null)
    setPassengers([])
    setBookingId(null)
  }

  const value = { trip, passengers, bookingId, setTrip, setPassengers, setBookingId, clear, totalPrice }

  return <BookingCartContext.Provider value={value}>{children}</BookingCartContext.Provider>
}

export function useBookingCart(): BookingCartValue {
  const ctx = useContext(BookingCartContext)
  if (!ctx) throw new Error('useBookingCart must be used within BookingCartProvider')
  return ctx
}
