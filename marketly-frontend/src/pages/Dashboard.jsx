import { useAccount } from "../context/AccountContext";
import { useAuth } from "../context/AuthContext";
import { useFetch } from "../hooks/useFetch";
import { getPortfolio } from "../api/portfolioApi";
import { getOrdersForAccount } from "../api/orderApi";
import { getWatchlist } from "../api/watchlistApi";
import { LoadingState, ErrorState } from "../components/StatusStates";
import { formatCurrency, formatPercent } from "../utils/format";
import OpenAccount from "./account/OpenAccount";

export default function Dashboard() {
  const { role } = useAuth();
  const { account, accountId, isLoading: accountLoading, error: accountError } =
    useAccount();

  // Each of these only runs once accountId is available (the "enabled" flag).
  const portfolio = useFetch(
    () => getPortfolio(accountId),
    [accountId],
    !!accountId
  );
  const orders = useFetch(
    () => getOrdersForAccount(accountId),
    [accountId],
    !!accountId
  );
  const watchlist = useFetch(
    () => getWatchlist(accountId),
    [accountId],
    !!accountId
  );

  if (accountLoading) return <LoadingState label="Loading account..." />;

  if (accountError) {
    // CLIENT with no account yet: let them open one right here — this is
    // the actual fix for the missing-account gap (registration/admin
    // user-creation never auto-opens a trading account).
    if (role === "CLIENT") {
      return <OpenAccount />;
    }

    // Other roles (ADMIN, RESEARCH_ANALYST, etc.) typically have no
    // trading account at all — that's expected, not a real error.
    return (
      <div>
        <h1 className="page-title">Dashboard</h1>
        <div className="panel">
          <p className="muted">
            No trading account is associated with this login. Trading and
            portfolio features aren't applicable to this role.
          </p>
        </div>
      </div>
    );
  }

  // Real field names confirmed from /portfolio/account/{id} response.
  const portfolioValue = portfolio.data?.totalCurrentValue ?? 0;

  // todaysPL/todaysPLPercent = actual day change (vs yesterday's close).
  // unrealizedPL/unrealizedPLPercent = total gain/loss since purchase -
  // kept as a separate card since it answers a different question.
  const todaysPL = portfolio.data?.totalTodaysPL ?? 0;
  const todaysPLPercent = portfolio.data?.totalTodaysPLPercent ?? 0;
  const totalPL = portfolio.data?.totalUnrealizedPL ?? 0;
  const totalPLPercent = portfolio.data?.totalUnrealizedPLPercent ?? 0;

  const recentOrders = (orders.data ?? []).slice(0, 5);
  const watchlistItems = (watchlist.data ?? []).slice(0, 5);

  return (
    <div>
      <h1 className="page-title">Dashboard</h1>

      <div className="stat-cards">
        <StatCard label="Portfolio Value" value={formatCurrency(portfolioValue)} />
        <StatCard
          label="Today's P&L"
          value={formatCurrency(todaysPL)}
          sub={formatPercent(todaysPLPercent)}
          positive={todaysPL >= 0}
        />
        <StatCard
          label="Total P&L"
          value={formatCurrency(totalPL)}
          sub={formatPercent(totalPLPercent)}
          positive={totalPL >= 0}
        />
        <StatCard
          label="Available Margin"
          value={formatCurrency(account?.cashAvailable)}
        />
        <StatCard label="Total Investment" value={formatCurrency(account?.balance)} />
      </div>

      <div className="dashboard-grid">
        <div className="panel">
          <h3>Recent Orders</h3>
          {orders.isLoading && <LoadingState />}
          {orders.error && <ErrorState error={orders.error} onRetry={orders.refetch} />}
          {!orders.isLoading && !orders.error && recentOrders.length === 0 && (
            <p className="muted">No orders yet.</p>
          )}
          {!orders.isLoading && recentOrders.length > 0 && (
            <table className="simple-table">
              <thead>
                <tr>
                  <th>Symbol</th>
                  <th>Type</th>
                  <th>Qty</th>
                  <th>Price</th>
                  <th>Status</th>
                </tr>
              </thead>
              <tbody>
                {recentOrders.map((order) => (
                  <tr key={order.orderId}>
                    <td>{order.symbol}</td>
                    <td className={order.orderType === "BUY" ? "text-green" : "text-red"}>
                      {order.orderType}
                    </td>
                    <td>{order.quantity}</td>
                    <td>{formatCurrency(order.price)}</td>
                    <td>{order.orderStatus}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>

        <div className="panel">
          <h3>My Watchlist</h3>
          {watchlist.isLoading && <LoadingState />}
          {watchlist.error && (
            <ErrorState error={watchlist.error} onRetry={watchlist.refetch} />
          )}
          {!watchlist.isLoading && !watchlist.error && watchlistItems.length === 0 && (
            <p className="muted">Your watchlist is empty.</p>
          )}
          {!watchlist.isLoading && watchlistItems.length > 0 && (
            <ul className="simple-list">
              {watchlistItems.map((item) => (
                <li key={item.watchlistId}>
                  <span>{item.symbol}</span>
                  <span>{formatCurrency(item.currentPrice)}</span>
                </li>
              ))}
            </ul>
          )}
        </div>

        <div className="panel">
          <h3>Portfolio Summary</h3>
          {portfolio.isLoading && <LoadingState />}
          {portfolio.error && (
            <ErrorState error={portfolio.error} onRetry={portfolio.refetch} />
          )}
          {!portfolio.isLoading && !portfolio.error && (
            <p className="muted">
              {(portfolio.data?.holdings ?? []).length} holdings in your portfolio.
              See the Portfolio page for details.
            </p>
          )}
        </div>

        <div className="panel">
          <h3>Account</h3>
          <p className="muted">Account #: {account?.accountNumber}</p>
          <p className="muted">Type: {account?.accountType}</p>
          <p className="muted">Status: {account?.status}</p>
        </div>
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
