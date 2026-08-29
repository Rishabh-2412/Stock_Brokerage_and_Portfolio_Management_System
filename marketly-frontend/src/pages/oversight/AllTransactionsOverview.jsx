import { useMemo, useState } from "react";
import { useFetch } from "../../hooks/useFetch";
import { getAllTransactions } from "../../api/transactionApi";
import { LoadingState, ErrorState } from "../../components/StatusStates";
import { formatCurrency } from "../../utils/format";

// GET /api/transactions (all-accounts) has no filter query params on the
// backend — filtering here is client-side only, unlike the per-account
// history page which uses real server-side filters.
export default function AllTransactionsOverview() {
  const { data, isLoading, error, refetch } = useFetch(() => getAllTransactions(), []);
  const [type, setType] = useState("");

  const transactions = data ?? [];

  const filtered = useMemo(() => {
    if (!type) return transactions;
    return transactions.filter((tx) => tx.transactionType === type);
  }, [transactions, type]);

  return (
    <div>
      <h1 className="page-title">All Transactions</h1>

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
      </div>

      <div className="panel">
        {isLoading && <LoadingState label="Loading transactions..." />}
        {error && <ErrorState error={error} onRetry={refetch} />}

        {!isLoading && !error && filtered.length === 0 && (
          <p className="muted">No transactions found.</p>
        )}

        {!isLoading && filtered.length > 0 && (
          <table className="simple-table">
            <thead>
              <tr>
                <th>Account #</th>
                <th>Symbol</th>
                <th>Type</th>
                <th>Qty</th>
                <th>Price</th>
                <th>Commission</th>
                <th>Total</th>
                <th>Status</th>
                <th>Date</th>
              </tr>
            </thead>
            <tbody>
              {filtered.map((tx) => (
                <tr key={tx.transactionId}>
                  <td>{tx.accountId}</td>
                  <td>{tx.symbol}</td>
                  <td className={tx.transactionType === "BUY" ? "text-green" : "text-red"}>
                    {tx.transactionType}
                  </td>
                  <td>{tx.quantity}</td>
                  <td>{formatCurrency(tx.price)}</td>
                  <td>{formatCurrency(tx.commission)}</td>
                  <td>{formatCurrency(tx.totalAmount)}</td>
                  <td>{tx.status}</td>
                  <td>{new Date(tx.transactionDate).toLocaleString()}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}
