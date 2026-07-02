import type { ReactNode } from 'react'

type Tone = 'green' | 'red' | 'slate' | 'blue' | 'amber'

const toneClasses: Record<Tone, string> = {
  green: 'bg-green-100 text-green-700',
  red: 'bg-red-100 text-red-700',
  slate: 'bg-slate-100 text-slate-600',
  blue: 'bg-brand-100 text-brand-700',
  amber: 'bg-amber-100 text-amber-700',
}

export function Badge({ tone = 'slate', children }: { tone?: Tone; children: ReactNode }) {
  return (
    <span className={`inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-medium ${toneClasses[tone]}`}>
      {children}
    </span>
  )
}
