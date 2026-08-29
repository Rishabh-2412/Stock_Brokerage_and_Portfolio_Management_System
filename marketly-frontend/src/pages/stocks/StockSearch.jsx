import { useState, useMemo } from "react";
import { useNavigate } from "react-router-dom";
import { useFetch } from "../../hooks/useFetch";
import { getAllSecurities } from "../../api/securityApi";
import { getWatchlist, addToWatchlist, removeFromWatchlist } from "../../api/watchlistApi";
import { useAccount } from "../../context/AccountContext";
import { LoadingState, ErrorState } from "../../components/StatusStates";
import { formatCurrency } from "../../utils/format";

// GET /api/securities doesn't take a search query param (confirmed from
// backend source — it's a plain list-all), so we filter client-side.
// Fine for a small securities list; would need a backend search param
// if the list grows large.
export default function StockSearch() {
  const [query, setQuery] = useState("");
  const navigate = useNavigate();
  const { accountId } = useAccount(); // only CLIENT has one — gates the star column

  const { data, isLoading, error, refetch } = useFetch(() => getAllSecurities(), []);

  // Fetch current watchlist so we know which stars should show as "filled".
  const watchlistQuery = useFetch(
    () => getWatchlist(accountId),
    [accountId],
    !!accountId
  );
  const [pendingId, setPendingId] = useState(null);

  const securities = data ?? [];
  const watchlist = watchlistQuery.data ?? [];

  // Map securityId -> that watchlist entry's own id (needed to DELETE it).
  const watchlistBySecurityId = useMemo(() => {
    const map = new Map();
    for (const entry of watchlist) {
      map.set(entry.securityId, entry.watchlistId);
    }
    return map;
  }, [watchlist]);

  const filtered = useMemo(() => {
    if (!query.trim()) return securities;
    const q = query.toLowerCase();
    return securities.filter(
      (s) =>
        s.symbol?.toLowerCase().includes(q) || s.name?.toLowerCase().includes(q)
    );
  }, [securities, query]);

  async function handleToggleWatchlist(e, security) {
    e.stopPropagation();
    setPendingId(security.securityId);
    try {
      const existingWatchlistId = watchlistBySecurityId.get(security.securityId);
      if (existingWatchlistId) {
        await removeFromWatchlist(existingWatchlistId);
      } else {
        await addToWatchlist(accountId, security.securityId);
      }
      watchlistQuery.refetch();
    } catch (err) {
      alert(err.response?.data?.message || "Failed to update watchlist.");
    } finally {
      setPendingId(null);
    }
  }

  return (
    <div>
      <h1 className="page-title">Stock Search</h1>

      <input
        className="search-input"
        type="text"
        placeholder="Search by symbol or name..."
        value={query}
        onChange={(e) => setQuery(e.target.value)}
      />

      <div className="panel">
        {isLoading && <LoadingState label="Loading securities..." />}
        {error && <ErrorState error={error} onRetry={refetch} />}

        {!isLoading && !error && filtered.length === 0 && (
          <p className="muted">No securities match your search.</p>
        )}

        {!isLoading && filtered.length > 0 && (
          <table className="simple-table">
            <thead>
              <tr>
                {accountId && <th></th>}
                <th>Symbol</th>
                <th>Name</th>
                <th>Exchange</th>
                <th>Sector</th>
                <th>Price</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {filtered.map((s) => {
                const isWatched = watchlistBySecurityId.has(s.securityId);
                return (
                  <tr
                    key={s.securityId}
                    className="clickable-row"
                    onClick={() => navigate(`/stocks/${s.securityId}`)}
                  >
                    {accountId && (
                      <td>
                        <button
                          className={isWatched ? "star-btn star-btn-active" : "star-btn"}
                          disabled={pendingId === s.securityId}
                          onClick={(e) => handleToggleWatchlist(e, s)}
                          title={isWatched ? "Remove from watchlist" : "Add to watchlist"}
                        >
                          {isWatched ? "★" : "☆"}
                        </button>
                      </td>
                    )}
                    <td className="symbol-cell">{s.symbol}</td>
                    <td>{s.name}</td>
                    <td>{s.exchange}</td>
                    <td>{s.sector}</td>
                    <td>{formatCurrency(s.currentPrice)}</td>
                    <td>
                      <button
                        className="link-btn"
                        onClick={(e) => {
                          e.stopPropagation();
                          navigate(`/stocks/${s.securityId}`);
                        }}
                      >
                        View →
                      </button>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}
