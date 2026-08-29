import { useState } from "react";
import { useParams, useNavigate, Link } from "react-router-dom";
import {
  ResponsiveContainer,
  AreaChart,
  Area,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
} from "recharts";
import { useFetch } from "../../hooks/useFetch";
import { getSecurityById } from "../../api/securityApi";
import { getPriceHistory } from "../../api/marketApi";
import { getWatchlist, addToWatchlist, removeFromWatchlist } from "../../api/watchlistApi";
import { useAccount } from "../../context/AccountContext";
import { LoadingState, ErrorState } from "../../components/StatusStates";
import { formatCurrency } from "../../utils/format";

export default function StockDetails() {
  const { securityId } = useParams();
  const navigate = useNavigate();
  const { accountId } = useAccount(); // only CLIENT has one

  const {
    data: security,
    isLoading: securityLoading,
    error: securityError,
  } = useFetch(() => getSecurityById(securityId), [securityId], !!securityId);

  // History is informational — if it 404s (ADMIN hasn't added any records
  // for this security yet) we show "no data" rather than blocking the page.
  const {
    data: history,
    isLoading: historyLoading,
    error: historyError,
  } = useFetch(() => getPriceHistory(securityId), [securityId], !!securityId);

  const watchlistQuery = useFetch(
    () => getWatchlist(accountId),
    [accountId],
    !!accountId
  );
  const [isTogglingWatchlist, setIsTogglingWatchlist] = useState(false);

  if (securityLoading) return <LoadingState label="Loading security..." />;
  if (securityError) return <ErrorState error={securityError} />;

  const priceHistory = history ?? [];
  const chartData = [...priceHistory]
    .sort((a, b) => new Date(a.date) - new Date(b.date))
    .map((row) => ({
      date: row.date,
      close: Number(row.closePrice),
      high: Number(row.highPrice),
      low: Number(row.lowPrice),
    }));
  const watchlistEntry = (watchlistQuery.data ?? []).find(
    (w) => w.securityId === security.securityId
  );

  async function handleToggleWatchlist() {
    setIsTogglingWatchlist(true);
    try {
      if (watchlistEntry) {
        await removeFromWatchlist(watchlistEntry.watchlistId);
      } else {
        await addToWatchlist(accountId, security.securityId);
      }
      watchlistQuery.refetch();
    } catch (err) {
      alert(err.response?.data?.message || "Failed to update watchlist.");
    } finally {
      setIsTogglingWatchlist(false);
    }
  }

  return (
    <div>
      <Link to="/stocks" className="back-link">
        ← Back to search
      </Link>

      <div className="stock-header">
        <div>
          <h1 className="page-title">{security.name}</h1>
          <p className="muted">
            {security.symbol} • {security.exchange}
          </p>
        </div>
        <div className="stock-actions">
          {accountId && (
            <button
              className={
                watchlistEntry ? "star-btn star-btn-active star-btn-large" : "star-btn star-btn-large"
              }
              onClick={handleToggleWatchlist}
              disabled={isTogglingWatchlist}
              title={watchlistEntry ? "Remove from watchlist" : "Add to watchlist"}
            >
              {watchlistEntry ? "★" : "☆"}
            </button>
          )}
          <button
            className="buy-btn"
            onClick={() => navigate(`/trade/${security.securityId}?side=BUY`)}
          >
            Buy
          </button>
          <button
            className="sell-btn"
            onClick={() => navigate(`/trade/${security.securityId}?side=SELL`)}
          >
            Sell
          </button>
        </div>
      </div>

      <div className="stat-cards">
        <StatCard label="Current Price" value={formatCurrency(security.currentPrice)} />
        <StatCard label="Sector" value={security.sector || "—"} />
        <StatCard
          label="Market Cap"
          value={security.marketCap ? formatCurrency(security.marketCap) : "—"}
        />
        <StatCard
          label="Last Updated"
          value={
            security.lastUpdated
              ? new Date(security.lastUpdated).toLocaleString()
              : "—"
          }
        />
      </div>

      {!historyLoading && !historyError && chartData.length > 1 && (
        <div className="panel chart-panel">
          <h3>Price Chart</h3>
          <ResponsiveContainer width="100%" height={280}>
            <AreaChart data={chartData}>
              <defs>
                <linearGradient id="closeGradient" x1="0" y1="0" x2="0" y2="1">
                  <stop offset="5%" stopColor="#6366f1" stopOpacity={0.4} />
                  <stop offset="95%" stopColor="#6366f1" stopOpacity={0} />
                </linearGradient>
              </defs>
              <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0" />
              <XAxis dataKey="date" tick={{ fontSize: 11 }} />
              <YAxis
                tick={{ fontSize: 12 }}
                domain={["auto", "auto"]}
                tickFormatter={(value) => `₹${value}`}
              />
              <Tooltip formatter={(value) => formatCurrency(value)} />
              <Area
                type="monotone"
                dataKey="close"
                name="Close Price"
                stroke="#6366f1"
                strokeWidth={2.5}
                fill="url(#closeGradient)"
                isAnimationActive={true}
                animationDuration={900}
                animationEasing="ease-out"
              />
            </AreaChart>
          </ResponsiveContainer>
        </div>
      )}

      <div className="panel">
        <h3>Price History</h3>
        {historyLoading && <LoadingState />}
        {historyError && (
          <p className="muted">No price history available for this security yet.</p>
        )}
        {!historyLoading && !historyError && priceHistory.length === 0 && (
          <p className="muted">No price history recorded yet.</p>
        )}
        {!historyLoading && priceHistory.length > 0 && (
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
              {priceHistory.map((row) => (
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

function StatCard({ label, value }) {
  return (
    <div className="stat-card">
      <div className="stat-label">{label}</div>
      <div className="stat-value">{value}</div>
    </div>
  );
}
