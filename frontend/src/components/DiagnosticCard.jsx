import { SEVERITY } from '../severity.js';

export default function DiagnosticCard({ result, onGenerateReport, loadingReport }) {
  const s = SEVERITY[result.severity];
  const pct = Math.min(100, Math.round((result.hemoglobinLevel / 16) * 100));

  return (
    <div className={`rounded-xl border p-6 shadow-sm ${s.card}`}>
      <div className="flex items-start justify-between gap-4">
        <div>
          <p className="text-xs uppercase tracking-wide text-slate-500">Diagnostic result</p>
          <h2 className="text-lg font-semibold text-slate-800">{result.fullName}</h2>
          <p className="text-xs text-slate-500">Patient ID #{result.patientId}</p>
        </div>
        <span className={`rounded-full px-3 py-1 text-sm font-semibold ring-1 ${s.badge}`}>
          {s.label} anemia
        </span>
      </div>

      <div className="mt-5">
        <div className="flex items-baseline gap-2">
          <span className="text-4xl font-bold text-slate-900">{result.hemoglobinLevel}</span>
          <span className="text-sm text-slate-500">g/dL hemoglobin</span>
        </div>
        <div className="mt-2 h-2.5 w-full overflow-hidden rounded-full bg-white/70">
          <div className={`h-full rounded-full ${s.bar}`} style={{ width: `${pct}%` }} />
        </div>
      </div>

      <p className="mt-4 text-sm text-slate-700">{result.recommendedAction}</p>

      <button
        onClick={onGenerateReport}
        disabled={loadingReport}
        className="mt-5 rounded-lg bg-slate-900 px-4 py-2 text-sm font-semibold text-white hover:bg-slate-700 disabled:opacity-60"
      >
        {loadingReport ? 'Generating report...' : 'Generate AI Health & Diet Report'}
      </button>
    </div>
  );
}