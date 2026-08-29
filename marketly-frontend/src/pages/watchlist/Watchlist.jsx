import { useAccount } from "../../context/AccountContext";
import { useFetch } from "../../hooks/useFetch";
import { getWatchlist, removeFromWatchlist } from "../../api/watchlistApi";
import { LoadingState, ErrorState } from "../../components/StatusStates";
import { formatCurrency } from "../../utils/format";
import { useState } from "react";

export default function Watchlist() {
  const { accountId, isLoading: accountLoading } = useAccount();
  const [removingId, setRemovingId] = useState(null);

  const { data, isLoading, error, refetch } = useFetch(
    () => getWatchlist(accountId),
    [accountId],
    !!accountId
  );

  if (accountLoading || isLoading) return <LoadingState label="Loading watchlist..." />;
  if (error) return <ErrorState error={error} onRetry={refetch} />;

  const items = data ?? [];

  async function handleRemove(watchlistId) {
    setRemovingId(watchlistId);
    try {
      await removeFromWatchlist(watchlistId);
      refetch();
    } catch (err) {
      alert(err.response?.data?.message || "Failed to remove from watchlist.");
    } finally {
      setRemovingId(null);
    }
  }

  return (
    <div>
      <h1 className="page-title">Watchlist</h1>

      <div className="panel">
        {items.length === 0 ? (
          <p className="muted">
            Your watchlist is empty. Add stocks from the Stock Search page.
          </p>
        ) : (
          <table className="simple-table">
            <thead>
              <tr>
                <th>Symbol</th>
                <th>Name</th>
                <th>Price</th>
                <th>Added</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {items.map((item) => (
                <tr key={item.watchlistId}>
                  <td>{item.symbol}</td>
                  <td>{item.securityName}</td>
                  <td>{formatCurrency(item.currentPrice)}</td>
                  <td>{new Date(item.addedAt).toLocaleDateString()}</td>
                  <td>
                    <button
                      className="link-btn"
                      disabled={removingId === item.watchlistId}
                      onClick={() => handleRemove(item.watchlistId)}
                    >
                      {removingId === item.watchlistId ? "Removing..." : "Remove"}
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
