import { useAccount } from "../../context/AccountContext";
import { useFetch } from "../../hooks/useFetch";
import { getPortfolio } from "../../api/portfolioApi";
import { LoadingState, ErrorState } from "../../components/StatusStates";
import { formatCurrency, formatPercent } from "../../utils/format";

export default function Portfolio() {
  const { accountId, isLoading: accountLoading } = useAccount();

  const { data, isLoading, error, refetch } = useFetch(
    () => getPortfolio(accountId),
    [accountId],
    !!accountId
  );

  if (accountLoading || isLoading) return <LoadingState label="Loading portfolio..." />;
  if (error) return <ErrorState error={error} onRetry={refetch} />;

  // Real field names confirmed from /portfolio/account/{id} response.
  const holdings = data?.holdings ?? [];
  const invested = data?.totalCostBasis ?? 0;
  const currentValue = data?.totalCurrentValue ?? 0;
  const pnl = data?.totalUnrealizedPL ?? 0;
  const pnlPercent = data?.totalUnrealizedPLPercent ?? 0;

  return (
    <div>
      <h1 className="page-title">Portfolio</h1>

      <div className="stat-cards">
        <StatCard label="Invested Value" value={formatCurrency(invested)} />
        <StatCard label="Current Value" value={formatCurrency(currentValue)} />
        <StatCard
          label="P&L"
          value={formatCurrency(pnl)}
          sub={formatPercent(pnlPercent)}
          positive={pnl >= 0}
        />
      </div>

      <div className="panel">
        {holdings.length === 0 ? (
          <p className="muted">You don't have any holdings yet.</p>
        ) : (
          <table className="simple-table">
            <thead>
              <tr>
                <th>Symbol</th>
                <th>Name</th>
                <th>Qty</th>
                <th>Avg Price</th>
                <th>Current Price</th>
                <th>Cost Basis</th>
                <th>Current Value</th>
                <th>P&L</th>
                <th>P&L %</th>
              </tr>
            </thead>
            <tbody>
              {holdings.map((h) => (
                <tr key={h.holdingId}>
                  <td>{h.symbol}</td>
                  <td>{h.securityName}</td>
                  <td>{h.quantity}</td>
                  <td>{formatCurrency(h.averageCost)}</td>
                  <td>{formatCurrency(h.currentPrice)}</td>
                  <td>{formatCurrency(h.costBasis)}</td>
                  <td>{formatCurrency(h.currentValue)}</td>
                  <td className={h.unrealizedPL >= 0 ? "text-green" : "text-red"}>
                    {formatCurrency(h.unrealizedPL)}
                  </td>
                  <td className={h.unrealizedPL >= 0 ? "text-green" : "text-red"}>
                    {formatPercent(h.unrealizedPLPercent)}
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

function StatCard({ label, value, sub, positive }) {
  return (
    <div className="stat-card">
      <div className="stat-label">{label}</div>
      <div className="stat-value">{value}</div>
      {sub && (
        <div className={positive ? "stat-sub text-green" : "stat-sub text-red"}>
          {sub}
        </div>
      )}
    </div>
  );
}
