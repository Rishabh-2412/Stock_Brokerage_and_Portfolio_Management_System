import { useState } from "react";
import { useFetch } from "../../../hooks/useFetch";
import { getAllSecurities, createSecurity, updateSecurityPrice } from "../../../api/securityApi";
import { LoadingState, ErrorState } from "../../../components/StatusStates";
import { formatCurrency } from "../../../utils/format";

export default function SecurityManagement() {
  const { data, isLoading, error, refetch } = useFetch(() => getAllSecurities(), []);
  const [showForm, setShowForm] = useState(false);
  const [editingSecurity, setEditingSecurity] = useState(null);

  const securities = data ?? [];

  return (
    <div>
      <div className="page-header-row">
        <h1 className="page-title">Securities</h1>
        <button className="primary-btn" onClick={() => setShowForm((v) => !v)}>
          {showForm ? "Cancel" : "+ Add Security"}
        </button>
      </div>

      {showForm && (
        <CreateSecurityForm
          onCreated={() => {
            setShowForm(false);
            refetch();
          }}
        />
      )}

      {editingSecurity && (
        <UpdatePriceForm
          security={editingSecurity}
          onSaved={() => {
            setEditingSecurity(null);
            refetch();
          }}
          onCancel={() => setEditingSecurity(null)}
        />
      )}

      <div className="panel">
        {isLoading && <LoadingState label="Loading securities..." />}
        {error && <ErrorState error={error} onRetry={refetch} />}

        {!isLoading && !error && securities.length === 0 && (
          <p className="muted">No securities yet.</p>
        )}

        {!isLoading && securities.length > 0 && (
          <table className="simple-table">
            <thead>
              <tr>
                <th>Symbol</th>
                <th>Name</th>
                <th>Exchange</th>
                <th>Sector</th>
                <th>Price</th>
                <th>Market Cap</th>
                <th>Last Updated</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {securities.map((s) => (
                <tr key={s.securityId}>
                  <td className="symbol-cell">{s.symbol}</td>
                  <td>{s.name}</td>
                  <td>{s.exchange || "—"}</td>
                  <td>{s.sector || "—"}</td>
                  <td>{formatCurrency(s.currentPrice)}</td>
                  <td>{s.marketCap ? formatCurrency(s.marketCap) : "—"}</td>
                  <td>{s.lastUpdated ? new Date(s.lastUpdated).toLocaleString() : "—"}</td>
                  <td>
                    <button className="link-btn" onClick={() => setEditingSecurity(s)}>
                      Update Price
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}

function CreateSecurityForm({ onCreated }) {
  const [form, setForm] = useState({
    symbol: "",
    name: "",
    exchange: "",
    sector: "",
    currentPrice: "",
    marketCap: "",
  });
  const [error, setError] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);

  function handleChange(e) {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");
    setIsSubmitting(true);
    try {
      await createSecurity({
        ...form,
        currentPrice: Number(form.currentPrice),
        marketCap: form.marketCap ? Number(form.marketCap) : undefined,
      });
      onCreated();
    } catch (err) {
      setError(err.response?.data?.message || "Failed to create security.");
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <form className="panel create-user-form" onSubmit={handleSubmit}>
      <h3>Add New Security</h3>

      <div className="form-grid">
        <div>
          <label htmlFor="symbol">Symbol</label>
          <input id="symbol" name="symbol" value={form.symbol} onChange={handleChange} placeholder="TCS" required />
        </div>
        <div>
          <label htmlFor="name">Name</label>
          <input id="name" name="name" value={form.name} onChange={handleChange} placeholder="Tata Consultancy Services" required />
        </div>
        <div>
          <label htmlFor="exchange">Exchange</label>
          <input id="exchange" name="exchange" value={form.exchange} onChange={handleChange} placeholder="NSE" />
        </div>
        <div>
          <label htmlFor="sector">Sector</label>
          <input id="sector" name="sector" value={form.sector} onChange={handleChange} placeholder="IT Services" />
        </div>
        <div>
          <label htmlFor="currentPrice">Current Price (₹)</label>
          <input
            id="currentPrice"
            name="currentPrice"
            type="number"
            step="0.01"
            min="0.01"
            value={form.currentPrice}
            onChange={handleChange}
            required
          />
        </div>
        <div>
          <label htmlFor="marketCap">Market Cap (₹, optional)</label>
          <input
            id="marketCap"
            name="marketCap"
            type="number"
            step="0.01"
            min="0"
            value={form.marketCap}
            onChange={handleChange}
          />
        </div>
      </div>

      {error && <div className="error-message">{error}</div>}

      <button type="submit" className="primary-btn" disabled={isSubmitting}>
        {isSubmitting ? "Creating..." : "Create Security"}
      </button>
    </form>
  );
}

function UpdatePriceForm({ security, onSaved, onCancel }) {
  const [currentPrice, setCurrentPrice] = useState(String(security.currentPrice ?? ""));
  const [marketCap, setMarketCap] = useState(
    security.marketCap != null ? String(security.marketCap) : ""
  );
  const [error, setError] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");
    setIsSubmitting(true);
    try {
      await updateSecurityPrice(
        security,
        Number(currentPrice),
        marketCap ? Number(marketCap) : undefined
      );
      onSaved();
    } catch (err) {
      setError(err.response?.data?.message || "Failed to update price.");
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <form className="panel create-user-form" onSubmit={handleSubmit}>
      <h3>
        Update Price — {security.symbol} ({security.name})
      </h3>

      <div className="form-grid">
        <div>
          <label htmlFor="currentPrice">New Price (₹)</label>
          <input
            id="currentPrice"
            type="number"
            step="0.01"
            min="0.01"
            value={currentPrice}
            onChange={(e) => setCurrentPrice(e.target.value)}
            required
          />
        </div>
        <div>
          <label htmlFor="marketCap">Market Cap (₹, optional)</label>
          <input
            id="marketCap"
            type="number"
            step="0.01"
            min="0"
            value={marketCap}
            onChange={(e) => setMarketCap(e.target.value)}
          />
        </div>
      </div>

      {error && <div className="error-message">{error}</div>}

      <div className="profile-edit-actions">
        <button type="submit" className="primary-btn" disabled={isSubmitting}>
          {isSubmitting ? "Saving..." : "Save Price"}
        </button>
        <button type="button" className="cancel-btn" onClick={onCancel} disabled={isSubmitting}>
          Cancel
        </button>
      </div>
    </form>
  );
}
