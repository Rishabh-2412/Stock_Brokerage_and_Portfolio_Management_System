import { useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import { useFetch } from "../../hooks/useFetch";
import { getAllAccounts } from "../../api/accountApi";
import { LoadingState, ErrorState } from "../../components/StatusStates";
import { formatCurrency } from "../../utils/format";

// Read-only oversight view for COMPLIANCE_OFFICER/RISK_MANAGER.
// DEALER sees the same table plus a "Trade" action (only DEALER/ADMIN can
// place orders on someone else's account per the backend's @PreAuthorize).
export default function AllAccountsOverview() {
  const { role } = useAuth();
  const navigate = useNavigate();
  const canTrade = role === "DEALER" || role === "ADMIN";

  const { data, isLoading, error, refetch } = useFetch(() => getAllAccounts(), []);
  const accounts = data ?? [];

  return (
    <div>
      <h1 className="page-title">All Accounts</h1>

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
                {canTrade && <th></th>}
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
                  {canTrade && (
                    <td>
                      <button
                        className="link-btn"
                        onClick={() => navigate(`/dealer/trade?accountId=${acc.accountId}`)}
                      >
                        Trade →
                      </button>
                    </td>
                  )}
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
