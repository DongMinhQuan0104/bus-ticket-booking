# BusTicketBooking — Frontend

React + TypeScript + Vite + Tailwind CSS v4 frontend for the
BusTicketBooking project (backend: `com.group8.hsf302.bus_ticket_booking`,
Spring Boot).

## Getting started

```bash
npm install
npm run dev      # http://localhost:5173, proxies /api/** to http://localhost:8080
```

## Backend integration

See [`../docs/API_INTEGRATION.md`](../docs/API_INTEGRATION.md) for the
full contract: which endpoints exist today (and on which branch), which
are proposed/not-yet-built, request/response shapes, auth (session
cookie, not JWT), and CORS notes.

## Project layout

```
src/
  api/            axios client + one module per backend concern
  types/          enums.ts + dto.ts mirroring the backend's Domain/Enum and Application/Dto
  context/        AuthContext, BookingCartContext
  components/     ui/ (Button, Input, Card, Badge), layout/, trip/
  pages/          one file per route; pages/admin/ for the ADMIN dashboard
```

## Scripts

- `npm run dev` — start the dev server
- `npm run build` — type-check (`tsc -b`) and build for production
- `npm run lint` — run oxlint
- `npm run preview` — preview the production build locally
