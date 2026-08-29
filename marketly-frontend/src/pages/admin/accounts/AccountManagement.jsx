import { useState } from "react";
import { useFetch } from "../../../hooks/useFetch";
import { getAllAccounts, updateAccountStatus } from "../../../api/accountApi";
import { LoadingState, ErrorState } from "../../../components/StatusStates";
import { formatCurrency } from "../../../utils/format";

const STATUSES = ["ACTIVE", "INACTIVE", "SUSPENDED"];

export default function AccountManagement() {
  const { data, isLoading, error, refetch } = useFetch(() => getAllAccounts(), []);
  const [updatingId, setUpdatingId] = useState(null);

  const accounts = data ?? [];

  async function handleStatusChange(account, newStatus) {
    setUpdatingId(account.accountId);
    try {
      await updateAccountStatus(account, newStatus);
      refetch();
    } catch (err) {
      alert(err.response?.data?.message || "Failed to update account status.");
    } finally {
      setUpdatingId(null);
    }
  }

  return (
    <div>
      <h1 className="page-title">Account Management</h1>

      <div className="panel">
        {isLoading && <LoadingState label="Loading accounts..." />}
        {error && <ErrorState error={error} onRetry={refetch} />}

        {!isLoading && !error && accounts.length === 0 && (
          <p className="muted">No accounts found.</p>
        )}

        {!isLoading && accounts.length > 0 && (
          <table className="simple-table">
            <thead>
              <tr>
                <th>Account #</th>
                <th>User ID</th>
                <th>Type</th>
                <th>Balance</th>
                <th>Cash Available</th>
                <th>Status</th>
                <th>Change Status</th>
              </tr>
            </thead>
            <tbody>
              {accounts.map((acc) => (
                <tr key={acc.accountId}>
                  <td>{acc.accountNumber}</td>
                  <td>{acc.userId}</td>
                  <td>{acc.accountType}</td>
                  <td>{formatCurrency(acc.balance)}</td>
                  <td>{formatCurrency(acc.cashAvailable)}</td>
                  <td>
                    <StatusBadge status={acc.status} />
                  </td>
                  <td>
                    <select
                      value={acc.status}
                      disabled={updatingId === acc.accountId}
                      onChange={(e) => handleStatusChange(acc, e.target.value)}
                    >
                      {STATUSES.map((s) => (
                        <option key={s} value={s}>
                          {s}
                        </option>
                      ))}
                    </select>
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

function StatusBadge({ status }) {
  const className =
    status === "ACTIVE"
      ? "status-badge status-active"
      : status === "SUSPENDED"
      ? "status-badge status-suspended"
      : "status-badge status-inactive";
  return <span className={className}>{status}</span>;
}
