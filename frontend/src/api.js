const BASE = '/api';

async function request(path, options = {}) {
  const res = await fetch(BASE + path, {
    headers: { 'Content-Type': 'application/json' },
    ...options,
  });
  if (!res.ok) {
    let message = `Request failed (${res.status})`;
    try {
      const data = await res.json();
      message = data.errors ? Object.values(data.errors).join('. ') : data.message || message;
    } catch {
      /* ignore non-JSON error bodies */
    }
    throw new Error(message);
  }
  return res.json();
}

export const registerPatient = (payload) =>
  request('/patients/register', { method: 'POST', body: JSON.stringify(payload) });

export const getReport = (id) => request(`/patients/${id}/report`);

export const getFoodSuggestions = (region) =>
  request(`/food-suggestions?region=${encodeURIComponent(region)}`);