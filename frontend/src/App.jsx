import { useState } from 'react';
import RegistrationForm from './components/RegistrationForm.jsx';
import DiagnosticCard from './components/DiagnosticCard.jsx';
import ReportView from './components/ReportView.jsx';
import { registerPatient, getReport } from './api.js';

export default function App() {
  const [result, setResult] = useState(null);
  const [report, setReport] = useState(null);
  const [submitting, setSubmitting] = useState(false);
  const [loadingReport, setLoadingReport] = useState(false);
  const [error, setError] = useState('');

  const handleRegister = async (payload) => {
    setSubmitting(true);
    setError('');
    setReport(null);
    try {
      setResult(await registerPatient(payload));
    } catch (e) {
      setError(e.message);
    } finally {
      setSubmitting(false);
    }
  };

  const handleReport = async () => {
    setLoadingReport(true);
    setError('');
    try {
      setReport(await getReport(result.patientId));
    } catch (e) {
      setError(e.message);
    } finally {
      setLoadingReport(false);
    }
  };

  return (
    <div className="min-h-screen">
      <header className="bg-gradient-to-r from-rose-600 to-red-500 text-white">
        <div className="mx-auto max-w-6xl px-6 py-5">
          <h1 className="text-2xl font-bold tracking-tight">AnemiaGuard</h1>
          <p className="text-sm text-rose-100">Health camp anemia screening, regional nutrition guidance and AI reports</p>
        </div>
      </header>

      <main className="mx-auto grid max-w-6xl gap-6 px-6 py-8 lg:grid-cols-2">
        <RegistrationForm onSubmit={handleRegister} submitting={submitting} />

        <div className="space-y-6">
          {error && (
            <div role="alert" className="rounded-lg border border-red-300 bg-red-50 px-4 py-3 text-sm text-red-700">
              {error}
            </div>
          )}
          {!result && !error && (
            <div className="rounded-xl border-2 border-dashed border-slate-300 p-10 text-center text-slate-500">
              Register a patient to see the instant diagnostic result.
            </div>
          )}
          {result && (
            <DiagnosticCard result={result} onGenerateReport={handleReport} loadingReport={loadingReport} />
          )}
          {report && <ReportView report={report} />}
        </div>
      </main>
    </div>
  );
}