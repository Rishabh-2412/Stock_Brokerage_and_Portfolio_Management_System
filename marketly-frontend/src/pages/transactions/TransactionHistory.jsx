import { useState } from "react";
import { useAccount } from "../../context/AccountContext";
import { useFetch } from "../../hooks/useFetch";
import { getTransactionsForAccount } from "../../api/transactionApi";
import { LoadingState, ErrorState } from "../../components/StatusStates";
import { formatCurrency } from "../../utils/format";

export default function TransactionHistory() {
  const { accountId, isLoading: accountLoading } = useAccount();

  // Filter state - all optional, matching the backend's optional query params.
  const [type, setType] = useState(""); // "" | "BUY" | "SELL" | "DIVIDEND"
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");

  const { data, isLoading, error, refetch } = useFetch(
    () =>
      getTransactionsForAccount(accountId, {
        type: type || undefined,
        startDate: startDate || undefined,
        endDate: endDate || undefined,
      }),
    [accountId, type, startDate, endDate],
    !!accountId
  );

  if (accountLoading) return <LoadingState label="Loading account..." />;

  const transactions = data ?? [];

  function clearFilters() {
    setType("");
    setStartDate("");
    setEndDate("");
  }

  return (
    <div>
      <h1 className="page-title">Transaction History</h1>

      <div className="filter-bar">
        <div className="filter-field">
          <label htmlFor="type">Type</label>
          <select id="type" value={type} onChange={(e) => setType(e.target.value)}>
            <option value="">All</option>
            <option value="BUY">Buy</option>
            <option value="SELL">Sell</option>
            <option value="DIVIDEND">Dividend</option>
          </select>
        </div>

        <div className="filter-field">
          <label htmlFor="startDate">From</label>
          <input
            id="startDate"
            type="date"
            value={startDate}
            onChange={(e) => setStartDate(e.target.value)}
          />
        </div>

        <div className="filter-field">
          <label htmlFor="endDate">To</label>
          <input
            id="endDate"
            type="date"
            value={endDate}
            onChange={(e) => setEndDate(e.target.value)}
          />
        </div>

        {(type || startDate || endDate) && (
          <button className="link-btn clear-filters-btn" onClick={clearFilters}>
            Clear filters
          </button>
        )}
      </div>

      <div className="panel">
        {isLoading && <LoadingState label="Loading transactions..." />}
        {error && <ErrorState error={error} onRetry={refetch} />}

        {!isLoading && !error && transactions.length === 0 && (
          <p className="muted">No transactions found for the selected filters.</p>
        )}

        {!isLoading && transactions.length > 0 && (
          <table className="simple-table">
            <thead>
              <tr>
                <th>Date</th>
                <th>Symbol</th>
                <th>Type</th>
                <th>Qty</th>
                <th>Price</th>
                <th>Commission</th>
                <th>Total</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
              {transactions.map((tx) => (
                <tr key={tx.transactionId}>
                  <td>{new Date(tx.transactionDate).toLocaleString()}</td>
                  <td>{tx.symbol}</td>
                  <td className={tx.transactionType === "BUY" ? "text-green" : "text-red"}>
                    {tx.transactionType}
                  </td>
                  <td>{tx.quantity}</td>
                  <td>{formatCurrency(tx.price)}</td>
                  <td>{formatCurrency(tx.commission)}</td>
                  <td>{formatCurrency(tx.totalAmount)}</td>
                  <td>{tx.status}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}
