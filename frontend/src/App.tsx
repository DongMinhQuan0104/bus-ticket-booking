import { BrowserRouter, Route, Routes } from 'react-router-dom'
import { AuthProvider } from '@/context/AuthContext'
import { BookingCartProvider } from '@/context/BookingCartContext'
import { MainLayout } from '@/components/layout/MainLayout'
import { AdminLayout } from '@/components/layout/AdminLayout'
import { ProtectedRoute } from '@/components/layout/ProtectedRoute'

import { Home } from '@/pages/Home'
import { Login } from '@/pages/Login'
import { Register } from '@/pages/Register'
import { TripSearchResults } from '@/pages/TripSearchResults'
import { TripDetail } from '@/pages/TripDetail'
import { BookingCheckout } from '@/pages/BookingCheckout'
import { Payment } from '@/pages/Payment'
import { BookingSuccess } from '@/pages/BookingSuccess'
import { MyBookings } from '@/pages/MyBookings'
import { Profile } from '@/pages/Profile'
import { NotFound } from '@/pages/NotFound'

import { AdminDashboard } from '@/pages/admin/AdminDashboard'
import { StationList } from '@/pages/admin/StationList'
import { StationForm } from '@/pages/admin/StationForm'
import { TripList } from '@/pages/admin/TripList'
import { TripForm } from '@/pages/admin/TripForm'
import { AccountList } from '@/pages/admin/AccountList'
import { AccountForm } from '@/pages/admin/AccountForm'

function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <BookingCartProvider>
          <Routes>
            <Route element={<MainLayout />}>
              <Route index element={<Home />} />
              <Route path="login" element={<Login />} />
              <Route path="register" element={<Register />} />
              <Route path="trips" element={<TripSearchResults />} />
              <Route path="trips/:tripId" element={<TripDetail />} />

              <Route element={<ProtectedRoute />}>
                <Route path="checkout" element={<BookingCheckout />} />
                <Route path="payment/:bookingId" element={<Payment />} />
                <Route path="booking-success/:bookingId" element={<BookingSuccess />} />
                <Route path="bookings" element={<MyBookings />} />
                <Route path="profile" element={<Profile />} />
              </Route>

              <Route path="admin" element={<ProtectedRoute allowedRoles={['ADMIN']} />}>
                <Route element={<AdminLayout />}>
                  <Route index element={<AdminDashboard />} />
                  <Route path="stations" element={<StationList />} />
                  <Route path="stations/new" element={<StationForm />} />
                  <Route path="stations/:stationId/edit" element={<StationForm />} />
                  <Route path="trips" element={<TripList />} />
                  <Route path="trips/new" element={<TripForm />} />
                  <Route path="accounts" element={<AccountList />} />
                  <Route path="accounts/new" element={<AccountForm />} />
                  <Route path="accounts/:accountId/edit" element={<AccountForm />} />
                </Route>
              </Route>

              <Route path="*" element={<NotFound />} />
            </Route>
          </Routes>
        </BookingCartProvider>
      </AuthProvider>
    </BrowserRouter>
  )
}

export default App
