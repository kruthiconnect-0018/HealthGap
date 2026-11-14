import { useState } from 'react';
import { REGIONS } from '../regions.js';

const initial = { fullName: '', age: '', gender: 'Female', region: REGIONS[0], hemoglobinLevel: '' };

const inputCls =
  'mt-1 w-full rounded-lg border border-slate-300 px-3 py-2 text-sm focus:border-rose-500 focus:outline-none focus:ring-2 focus:ring-rose-200';

export default function RegistrationForm({ onSubmit, submitting }) {
  const [form, setForm] = useState(initial);

  const set = (key) => (e) => setForm({ ...form, [key]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    await onSubmit({
      fullName: form.fullName,
      age: Number(form.age),
      gender: form.gender,
      region: form.region,
      hemoglobinLevel: Number(form.hemoglobinLevel),
    });
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4 rounded-xl bg-white p-6 shadow-sm ring-1 ring-slate-200">
      <h2 className="text-lg font-semibold text-slate-800">Patient Camp Registration</h2>

      <label className="block text-sm font-medium text-slate-700">
        Full name
        <input className={inputCls} value={form.fullName} onChange={set('fullName')} required placeholder="e.g. Lakshmi Devi" />
      </label>

      <div className="grid grid-cols-2 gap-4">
        <label className="block text-sm font-medium text-slate-700">
          Age (years)
          <input type="number" min="0" max="120" className={inputCls} value={form.age} onChange={set('age')} required />
        </label>
        <label className="block text-sm font-medium text-slate-700">
          Gender
          <select className={inputCls} value={form.gender} onChange={set('gender')}>
            <option>Female</option>
            <option>Male</option>
            <option>Other</option>
          </select>
        </label>
      </div>

      <label className="block text-sm font-medium text-slate-700">
        Region
        <select className={inputCls} value={form.region} onChange={set('region')}>
          {REGIONS.map((r) => (
            <option key={r}>{r}</option>
          ))}
        </select>
      </label>

      <label className="block text-sm font-medium text-slate-700">
        Hemoglobin (Hb) level, g/dL
        <input
          type="number" step="0.1" min="1" max="25"
          className={inputCls} value={form.hemoglobinLevel} onChange={set('hemoglobinLevel')}
          required placeholder="e.g. 9.8"
        />
      </label>

      <button
        type="submit"
        disabled={submitting}
        className="w-full rounded-lg bg-rose-600 px-4 py-2.5 text-sm font-semibold text-white transition hover:bg-rose-700 disabled:opacity-60"
      >
        {submitting ? 'Registering...' : 'Register & Analyse'}
      </button>
    </form>
  );
}