import { useState } from "react";
import { useFetch } from "../../../hooks/useFetch";
import { getAllSecurities } from "../../../api/securityApi";
import { getPriceHistory, addPriceHistoryRecord } from "../../../api/marketApi";
import { LoadingState, ErrorState } from "../../../components/StatusStates";
import { formatCurrency } from "../../../utils/format";

const todayIso = () => new Date().toISOString().split("T")[0];

export default function MarketDataManagement() {
  const { data: securities, isLoading, error, refetch: refetchSecurities } = useFetch(
    () => getAllSecurities(),
    []
  );
  const [selectedSecurityId, setSelectedSecurityId] = useState("");

  const selectedSecurity = (securities ?? []).find(
    (s) => String(s.securityId) === String(selectedSecurityId)
  );

  return (
    <div>
      <h1 className="page-title">Market Data</h1>
      <p className="muted" style={{ marginBottom: "1rem" }}>
        Add daily OHLCV price records for a security, used to chart its price
        history.
      </p>

      <div className="panel" style={{ marginBottom: "1.25rem" }}>
        {isLoading && <LoadingState label="Loading securities..." />}
        {error && <ErrorState error={error} onRetry={refetchSecurities} />}

        {!isLoading && !error && (
          <>
            <label htmlFor="securitySelect" style={{ display: "block", fontSize: "0.85rem", fontWeight: 600, marginBottom: "0.4rem" }}>
              Select a Security
            </label>
            <select
              id="securitySelect"
              value={selectedSecurityId}
              onChange={(e) => setSelectedSecurityId(e.target.value)}
              style={{ maxWidth: "420px", width: "100%" }}
            >
              <option value="">Choose a security...</option>
              {(securities ?? []).map((s) => (
                <option key={s.securityId} value={s.securityId}>
                  {s.symbol} — {s.name}
                </option>
              ))}
            </select>
          </>
        )}
      </div>

      {selectedSecurity && (
        <SecurityPriceHistoryPanel security={selectedSecurity} />
      )}
    </div>
  );
}

function SecurityPriceHistoryPanel({ security }) {
  const { data: history, isLoading, error, refetch } = useFetch(
    () => getPriceHistory(security.securityId),
    [security.securityId],
    true
  );
  const [showForm, setShowForm] = useState(false);

  const records = history ?? [];

  return (
    <div>
      <div className="page-header-row">
        <h3 style={{ margin: 0 }}>
          Price History — {security.symbol} ({security.name})
        </h3>
        <button className="primary-btn" onClick={() => setShowForm((v) => !v)}>
          {showForm ? "Cancel" : "+ Add Record"}
        </button>
      </div>

      {showForm && (
        <AddRecordForm
          security={security}
          onCreated={() => {
            setShowForm(false);
            refetch();
          }}
        />
      )}

      <div className="panel">
        {isLoading && <LoadingState label="Loading price history..." />}
        {error && <p className="muted">No price history recorded yet for this security.</p>}

        {!isLoading && !error && records.length === 0 && (
          <p className="muted">No records yet. Add the first one above.</p>
        )}

        {!isLoading && records.length > 0 && (
          <table className="simple-table">
            <thead>
              <tr>
                <th>Date</th>
                <th>Open</th>
                <th>High</th>
                <th>Low</th>
                <th>Close</th>
                <th>Volume</th>
              </tr>
            </thead>
            <tbody>
              {[...records]
                .sort((a, b) => new Date(b.date) - new Date(a.date))
                .map((row) => (
                  <tr key={row.historyId}>
                    <td>{row.date}</td>
                    <td>{formatCurrency(row.openPrice)}</td>
                    <td>{formatCurrency(row.highPrice)}</td>
                    <td>{formatCurrency(row.lowPrice)}</td>
                    <td>{formatCurrency(row.closePrice)}</td>
                    <td>{row.volume?.toLocaleString()}</td>
                  </tr>
                ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}

function AddRecordForm({ security, onCreated }) {
  const [form, setForm] = useState({
    date: todayIso(),
    openPrice: security.currentPrice ?? "",
    highPrice: security.currentPrice ?? "",
    lowPrice: security.currentPrice ?? "",
    closePrice: security.currentPrice ?? "",
    volume: "",
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
      await addPriceHistoryRecord({ ...form, securityId: security.securityId });
      onCreated();
    } catch (err) {
      setError(err.response?.data?.message || "Failed to add price record.");
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <form className="panel create-user-form" onSubmit={handleSubmit}>
      <h3>Add OHLCV Record</h3>

      <div className="form-grid">
        <div>
          <label htmlFor="date">Date</label>
          <input id="date" name="date" type="date" value={form.date} onChange={handleChange} required />
        </div>
        <div>
          <label htmlFor="openPrice">Open</label>
          <input
            id="openPrice"
            name="openPrice"
            type="number"
            step="0.01"
            min="0"
            value={form.openPrice}
            onChange={handleChange}
            required
          />
        </div>
        <div>
          <label htmlFor="highPrice">High</label>
          <input
            id="highPrice"
            name="highPrice"
            type="number"
            step="0.01"
            min="0"
            value={form.highPrice}
            onChange={handleChange}
            required
          />
        </div>
        <div>
          <label htmlFor="lowPrice">Low</label>
          <input
            id="lowPrice"
            name="lowPrice"
            type="number"
            step="0.01"
            min="0"
            value={form.lowPrice}
            onChange={handleChange}
            required
          />
        </div>
        <div>
          <label htmlFor="closePrice">Close</label>
          <input
            id="closePrice"
            name="closePrice"
            type="number"
            step="0.01"
            min="0"
            value={form.closePrice}
            onChange={handleChange}
            required
          />
        </div>
        <div>
          <label htmlFor="volume">Volume</label>
          <input
            id="volume"
            name="volume"
            type="number"
            step="1"
            min="0"
            value={form.volume}
            onChange={handleChange}
            required
          />
        </div>
      </div>

      {error && <div className="error-message">{error}</div>}

      <button type="submit" className="primary-btn" disabled={isSubmitting}>
        {isSubmitting ? "Adding..." : "Add Record"}
      </button>
    </form>
  );
}
