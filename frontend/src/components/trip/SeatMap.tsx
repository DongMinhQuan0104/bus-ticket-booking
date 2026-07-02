import type { SeatAvailabilityViewModel } from '@/types/dto'

interface SeatMapProps {
  seats: SeatAvailabilityViewModel[]
  selected: string[]
  onToggle: (seatCode: string) => void
}

export function SeatMap({ seats, selected, onToggle }: SeatMapProps) {
  return (
    <div>
      <div className="grid grid-cols-4 gap-3 sm:grid-cols-6">
        {seats.map((seat) => {
          const isBooked = seat.status === 'BOOKED'
          const isSelected = selected.includes(seat.seatCode)
          return (
            <button
              key={seat.id}
              type="button"
              disabled={isBooked}
              onClick={() => onToggle(seat.seatCode)}
              className={`rounded-lg border px-3 py-2 text-sm font-medium transition-colors ${
                isBooked
                  ? 'cursor-not-allowed border-slate-200 bg-slate-100 text-slate-400'
                  : isSelected
                    ? 'border-brand-600 bg-brand-600 text-white'
                    : 'border-slate-300 bg-white text-slate-700 hover:border-brand-400'
              }`}
            >
              {seat.seatCode}
            </button>
          )
        })}
      </div>

      <div className="mt-4 flex flex-wrap gap-4 text-xs text-slate-500">
        <span className="flex items-center gap-1">
          <span className="h-3 w-3 rounded border border-slate-300 bg-white" /> Còn trống
        </span>
        <span className="flex items-center gap-1">
          <span className="h-3 w-3 rounded bg-brand-600" /> Đang chọn
        </span>
        <span className="flex items-center gap-1">
          <span className="h-3 w-3 rounded bg-slate-100" /> Đã đặt
        </span>
      </div>
    </div>
  )
}
