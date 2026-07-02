# API Integration Guide — Frontend ↔ Backend

This document is the contract between the `frontend/` app (React + Vite +
TypeScript, branch `Viet-FE`) and the Spring Boot backend
(`com.group8.hsf302.bus_ticket_booking`). It covers two things:

1. **What the backend actually exposes today**, across every branch that
   has been pushed (`main`, `Viet`, `AnChu`, `Phong`,
   `feature/AccountService`, `feature/authentication`).
2. **The normalized `/api/**` JSON contract the frontend is built
   against**, so backend work can converge on one shape instead of the
   fragmented state described in (1).

If you're a backend dev picking this up: search this file for **"BE
TODO"** to find every endpoint the frontend calls that does not exist yet
on any branch.

---

## 1. Current backend reality (read this first)

- **No branch has a complete REST API.** Endpoints were built in parallel
  across branches and never merged: `AuthenticationController` (MVC) is on
  `main`/`AnChu`/`Phong`/etc but **not** on `Viet`; `StationController`
  only exists on `AnChu`; `TripController` only exists on `Phong`;
  `PaymentController` is the only controller present and identical on
  every branch.
- **Most controllers are `@Controller`, not `@RestController`.** Auth and
  Station return Thymeleaf view names and read/write `HttpSession`, not
  JSON. Trip uses `@ResponseBody`/`ResponseEntity` so it is JSON, but its
  create endpoint still binds a form (`@ModelAttribute`), not
  `@RequestBody`.
- **There is no JWT/session-token auth.** No Spring Security dependency,
  no `SecurityConfig`, no CORS configuration anywhere. "Login" sets
  `HttpSession.setAttribute("LOGGED_IN_USER", accountViewModel)`; "logout"
  calls `session.invalidate()`. Passwords are BCrypt-hashed
  (`Infrastructure/Security/BCryptPasswordHasher`).
- **No role-based route protection exists server-side.** `/admin/trips/*`
  is not actually restricted to ADMIN by any filter/interceptor.
- **Booking, BookingDetail, Route, RouteStation, SeatAvailability,
  Transaction, and full Account CRUD have services + repositories but zero
  controllers** on any branch — no way to create a booking, list seats, or
  manage accounts over HTTP today.
- `spring.jpa.hibernate.ddl-auto=create` — the schema (and all data) is
  wiped every time the backend restarts.
- No `server.servlet.context-path`; backend runs at `http://localhost:8080/`.

Given this, the frontend cannot be built against "what exists" alone —
half the screens a bus ticket booking app needs (search trips, pick
seats, book, pay, manage my bookings) have no backing endpoint. The
frontend is instead built against the **target contract in section 3**,
which is a normalization of what already exists plus the minimum new
endpoints needed to complete the booking flow. Sections 2 and 3 both list
every field so nothing is ambiguous when these are implemented for real.

---

## 2. Endpoints that exist today, unmodified

### 2.1 `POST /api/payments` — stable, used as-is

`PaymentController` (`@RestController @RequestMapping("/api/payments")`),
identical on `main`, `Viet`, `AnChu`, `feature/AccountService`,
`feature/authentication`.

Request body (`PaymentRequest`):
```json
{ "bookingId": "uuid", "paymentMethod": "COD" }
```
`paymentMethod` is one of `COD | CASH`.

Response `200 OK` (`PaymentResponse`):
```json
{
  "paymentId": "uuid",
  "createPayment": "2026-07-02T10:15:00",
  "paymentMethod": "COD",
  "bookingId": "uuid"
}
```
`400 Bad Request` with an empty body if the booking isn't found, or on any
`RuntimeException`.

Frontend usage: `src/api/paymentApi.ts` → `paymentApi.pay()`, called from
`src/pages/Payment.tsx`.

### 2.2 `GET /admin/trips`, `GET /admin/trips/{tripId}` — JSON, Phong branch only

`TripController` (`@Controller`, methods use `@ResponseBody`/`ResponseEntity`).
Returns `TripViewModel` / `List<TripViewModel>` — see section 3.3 for the
field list (identical shape to what the frontend expects, just at a
different path and without `/api`).

**BE TODO**: move this controller to `@RestController
@RequestMapping("/api/trips")` (or add a thin wrapper) so path and prefix
match section 3.3, and merge it onto whichever branch becomes the base for
future work — it currently only exists on `Phong`.

### 2.3 Everything else (`/auth/**`, `/stations/**`)

Exists as server-rendered MVC, not JSON. Do not call these paths directly
from the frontend fetch layer; they will return HTML, not JSON. Sections
3.1 and 3.2 describe the JSON versions the frontend actually calls.

---

## 3. Target contract (what `src/api/*.ts` calls)

All frontend requests go through `src/api/client.ts`, an axios instance
with `baseURL: '/api'` (overridable via `VITE_API_BASE_URL`) and
`withCredentials: true` (the session cookie, `JSESSIONID`, is the auth
mechanism — see section 4). In dev, Vite proxies `/api/**` to
`http://localhost:8080` (see `frontend/vite.config.ts`).

Every backend enum below must serialize as its Java `name()` string
(Jackson default) — the frontend's `src/types/enums.ts` mirrors
`Domain/Enum/*.java` exactly:

| Enum | Values |
|---|---|
| `Role` | `ADMIN`, `DRIVER`, `CUSTOMER` |
| `Status` | `AVAILABLE`, `NOT_AVAILABLE` |
| `TripStatus` (Phong branch; not yet on `Trip` everywhere) | `SCHEDULED`, `FULL`, `CANCELLED`, `COMPLETED` |
| `SeatStatus` | `AVAILABLE`, `BOOKED` |
| `PaymentMethod` | `COD`, `CASH` |
| `TransactionStatus` | `PENDING`, `PAID` |
| `BookingType` | `ONEWAY`, `ROUNDTRIP` |

> **BE TODO**: `Trip.status` is `Status` (AVAILABLE/NOT_AVAILABLE) on
> `main`/`Viet`/`AnChu` but `TripStatus` (SCHEDULED/FULL/CANCELLED/COMPLETED)
> on `Phong`. Pick one before merging — the frontend's `TripViewModel.status`
> is typed as `TripStatus` since that's the richer, more useful model for a
> booking UI.

### 3.1 Auth REST contract (proposed) {#auth-rest-contract-proposed}

**BE TODO**: wrap `AuthenticationService` in a small `@RestController` at
`/api/auth/**`, replacing the `@ModelAttribute` form binding with
`@RequestBody` JSON, and replying with the account JSON instead of a
`redirect:` view. Keep `HttpSession` as the underlying mechanism (see
section 4) — just stop rendering Thymeleaf.

| Method | Path | Request body | Response |
|---|---|---|---|
| POST | `/api/auth/login` | `LoginForm` | `200` `AccountViewModel`, `401` on `InvalidCredentialsException` |
| POST | `/api/auth/register` | `RegisterForm` | `200` `AccountViewModel`, `409` on `EmailAlreadyExistsException`, `400` on `PasswordConfirmNotMatchException` |
| POST | `/api/auth/logout` | — | `204`, invalidates the session |
| GET | `/api/auth/me` | — | `200` `AccountViewModel` if a session exists, `401` otherwise |

```ts
// LoginForm
{ email: string; password: string }

// RegisterForm
{ email: string; password: string; confirmPassword: string }

// AccountViewModel
{
  id: string;          // UUID
  fullName: string;
  role: 'ADMIN' | 'DRIVER' | 'CUSTOMER';
  email: string;
  phoneNumber: string;
  status: 'AVAILABLE' | 'NOT_AVAILABLE';
}
```

Error responses today (`GlobalExceptionHandler`) re-render a Thymeleaf
view with an `errorMessage` model attribute. The REST wrapper should
instead return `{ "message": "..." }` with an appropriate 4xx status so
`src/api/client.ts`'s response interceptor (which reads
`error.response.data.message`) surfaces it correctly.

Frontend usage: `src/api/authApi.ts`, `src/context/AuthContext.tsx`
(caches the account in `localStorage` and re-syncs against `GET
/api/auth/me` on load — the cookie is still the real source of truth).

### 3.2 Station REST contract (proposed) {#station-rest-contract-proposed}

**BE TODO**: convert `StationController` (currently `@Controller`,
`AnChu` branch only) to `@RestController @RequestMapping("/api/stations")`,
JSON in/out instead of form binding + redirects.

| Method | Path | Request body | Response |
|---|---|---|---|
| GET | `/api/stations` | — | `200` `StationViewModel[]` |
| GET | `/api/stations/{id}` | — | `200` `StationViewModel` |
| POST | `/api/stations` | `CreateStationForm` | `201` `StationViewModel` |
| PUT | `/api/stations/{id}` | `UpdateStationForm` | `200` `StationViewModel` |
| DELETE | `/api/stations/{id}` | — | `204` (soft-delete, same as today's `POST /stations/delete/{id}`) |

```ts
// CreateStationForm / UpdateStationForm
{ name: string; address: string }

// StationViewModel
{ id: string; name: string; address: string; status: 'AVAILABLE' | 'NOT_AVAILABLE' }
```

Frontend usage: `src/api/stationApi.ts`, `src/pages/admin/StationList.tsx`,
`src/pages/admin/StationForm.tsx`.

### 3.3 Trip REST contract (proposed) {#trip-rest-contract-proposed}

**BE TODO**: move `TripController` (`Phong` branch) to
`@RestController @RequestMapping("/api/trips")`; change `create` to
`@RequestBody CreateTripForm`; add search filters and a seat-map endpoint
(the latter needs `SeatAvailabilityService`, which exists but is unwired).

| Method | Path | Query/body | Response |
|---|---|---|---|
| GET | `/api/trips?from=&to=&date=` | query params, all optional | `200` `TripViewModel[]` |
| GET | `/api/trips/{tripId}` | — | `200` `TripViewModel` |
| POST | `/api/trips` | `CreateTripForm` | `200`/`201` `TripViewModel`, `400` + validation errors |
| GET | `/api/trips/{tripId}/seats` | — | `200` `SeatAvailabilityViewModel[]` — **BE TODO: does not exist today; needs a controller over `SeatAvailabilityService`** |

```ts
// CreateTripForm
{
  destinationFrom: string;
  destinationTo: string;
  busCode: string;
  busLicensePlate: string;
  vehicleType: string;
  driverName: string;
  departureTime: string; // "yyyy-MM-dd'T'HH:mm"
  arrivalTime: string;   // "yyyy-MM-dd'T'HH:mm"
  price: number;         // >= 0
}

// TripViewModel
{
  id: string;
  destinationFrom: string;
  destinationTo: string;
  departureTime: string; // ISO LocalDateTime
  arrivalTime: string;
  busCode: string;
  busLicensePlate: string;
  vehicleType: string;
  driverName: string;
  price: number;
  totalSeats: number;
  availableSeats: number;
  status: 'SCHEDULED' | 'FULL' | 'CANCELLED' | 'COMPLETED';
}

// SeatAvailabilityViewModel (BE TODO — no view model exists yet;
// this is the minimum the seat map needs from the SeatAvailability entity)
{ id: string; seatCode: string; status: 'AVAILABLE' | 'BOOKED' }
```

Frontend usage: `src/api/tripApi.ts`, `src/pages/TripSearchResults.tsx`,
`src/pages/TripDetail.tsx` (seat map), `src/pages/admin/TripList.tsx`,
`src/pages/admin/TripForm.tsx`.

### 3.4 Booking REST contract (proposed, BE TODO — no controller exists at all) {#booking-rest-contract-proposed}

Needs a new `BookingController` over the existing `BookingService` +
`BookingDetailService` + `SeatAvailabilityService` (to mark seats
`BOOKED` atomically when a booking is created).

| Method | Path | Request body | Response |
|---|---|---|---|
| POST | `/api/bookings` | `CreateBookingRequest` | `201` `BookingViewModel` |
| GET | `/api/bookings/{id}` | — | `200` `BookingViewModel` |
| GET | `/api/bookings/me` | — | `200` `BookingViewModel[]` for the logged-in account |

```ts
// CreateBookingRequest
{
  tripId: string;
  bookingType: 'ONEWAY' | 'ROUNDTRIP';
  note?: string;
  passengers: Array<{
    passengerName: string;
    seatCode: string;       // must match a SeatAvailability.seatCode on this trip
    luggageWeightKg: number;
  }>;
}

// BookingViewModel
{
  id: string;
  dateBooked: string;
  totalPrice: number;
  note: string;
  bookingType: 'ONEWAY' | 'ROUNDTRIP';
  tripId: string;
  details: Array<{
    id: string;
    passengerName: string;
    ticketPrice: number;
    luggageWeightKg: number;
    luggageFee: number;
    subTotal: number;
    seatCode: string;
  }>;
}
```

`totalPrice` / `subTotal` / `luggageFee` calculation is a backend
responsibility (matches `Booking`/`BookingDetail` entity fields) — the
frontend never computes authoritative prices, only an optimistic estimate
for display (`BookingCartContext.totalPrice`, `luggageWeightKg * 5000`
placeholder rate) before the real `BookingViewModel` comes back.

Frontend usage: `src/api/bookingApi.ts`, `src/pages/BookingCheckout.tsx`,
`src/pages/MyBookings.tsx`.

### 3.5 Account REST contract (proposed, BE TODO — no controller exists at all) {#account-rest-contract-proposed}

Needs a new `AccountController` (or three role-specific ones) over the
existing `AdminService` / `CustomerService` / `DriverService`.

| Method | Path | Request body | Response |
|---|---|---|---|
| GET | `/api/accounts` | — | `200` `AccountViewModel[]` (ADMIN only) |
| GET | `/api/accounts/{id}` | — | `200` `AccountViewModel` |
| POST | `/api/accounts` | `CreateAccountForm` | `201` `AccountViewModel` (ADMIN only) |
| PUT | `/api/accounts/{id}` | `UpdateAccountForm` | `200` `AccountViewModel` (self-service: fullName/email/phone) |
| PUT | `/api/accounts/{id}/admin` | `AdminUpdateAccountForm` | `200` `AccountViewModel` (ADMIN only: also sets role/status/password) |
| DELETE | `/api/accounts/{id}` | — | `204` (ADMIN only) |

```ts
// CreateAccountForm
{ role: 'ADMIN' | 'DRIVER' | 'CUSTOMER'; email: string; password: string }

// UpdateAccountForm
{ fullName: string; email: string; phoneNumber: string } // phoneNumber must match ^0\d{9}$

// AdminUpdateAccountForm
{
  fullName: string;
  role: 'ADMIN' | 'DRIVER' | 'CUSTOMER';
  email: string;
  password: string;
  phoneNumber: string;
  status: 'AVAILABLE' | 'NOT_AVAILABLE';
}
```

Frontend usage: `src/api/accountApi.ts`, `src/pages/Profile.tsx` (self
update), `src/pages/admin/AccountList.tsx` /
`src/pages/admin/AccountForm.tsx` (admin CRUD).

---

## 4. CORS & session {#cors--session}

The backend has no CORS configuration and no context-path today. Two
things need to happen for the frontend (served from a different origin in
production, e.g. `http://localhost:5173` in dev) to talk to it over a
session cookie:

1. **Dev**: already solved — `frontend/vite.config.ts` proxies `/api/**`
   to `http://localhost:8080`, so the browser only ever talks to the Vite
   origin and no CORS request is made.
2. **Prod / any setup where FE and BE are on different origins**: the
   backend needs a `WebMvcConfigurer` CORS bean (or
   `@CrossOrigin(origins = "...", allowCredentials = "true")`) that
   allowlists the frontend origin and sets `allowCredentials(true)` — a
   wildcard `*` origin does not work together with credentials. The
   frontend already sends `withCredentials: true` on every request
   (`src/api/client.ts`) so the `JSESSIONID` cookie round-trips.

**BE TODO**: add this CORS config; it does not exist on any branch.

No JWT is used anywhere in this app. If the backend later adds Spring
Security + JWT, the only frontend change needed is in
`src/api/client.ts`: attach `Authorization: Bearer <token>` via a request
interceptor instead of relying on `withCredentials`, and store the token
in `AuthContext` instead of/alongside the cached `AccountViewModel`.

---

## 5. Running both sides locally

```bash
# Backend (from repo root)
./mvnw spring-boot:run   # http://localhost:8080, SQL Server on localhost:1433

# Frontend (from frontend/)
npm install
npm run dev               # http://localhost:5173, proxies /api -> :8080
```

Because `spring.jpa.hibernate.ddl-auto=create`, every backend restart
wipes the database — expect to re-register a test account and recreate
stations/trips each time during manual FE/BE integration testing.

## 6. Where things live in the frontend

```
frontend/src/
  api/            one file per backend concern (client.ts is the shared axios instance)
  types/          enums.ts + dto.ts, field-for-field mirrors of Domain/Enum and Application/Dto
  context/        AuthContext (session-derived login state), BookingCartContext (client-only cart)
  components/ui/  Button, Input, Card, Badge
  components/     layout/ (Navbar, Footer, MainLayout, AdminLayout, ProtectedRoute), trip/ (TripCard, SeatMap)
  pages/          one file per route; pages/admin/ for the ADMIN-only dashboard
```

Every `api/*.ts` file has an inline comment stating whether the endpoint
it calls exists today, on which branch, and what differs from the target
contract in this document — check there first if a call 404s against a
real backend.
