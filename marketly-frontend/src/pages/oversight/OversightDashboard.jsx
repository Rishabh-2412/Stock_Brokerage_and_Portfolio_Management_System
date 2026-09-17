import {
  ResponsiveContainer,
  PieChart,
  Pie,
  Cell,
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
} from "recharts";
import { useFetch } from "../../hooks/useFetch";
import { getAllAccounts } from "../../api/accountApi";
import { getAllOrders } from "../../api/orderApi";
import { getAllTransactions } from "../../api/transactionApi";
import { LoadingState, ErrorState } from "../../components/StatusStates";
import { formatCurrency } from "../../utils/format";
import {
  ACCOUNT_STATUS_COLORS,
  ORDER_STATUS_COLORS,
  countBy,
  sumByDay,
} from "../../utils/dashboardHelpers";

const TITLE_BY_ROLE = {
  DEALER: "Dealer Dashboard",
  COMPLIANCE_OFFICER: "Compliance Dashboard",
  RISK_MANAGER: "Risk Dashboard",
};

/**
 * DEALER, COMPLIANCE_OFFICER and RISK_MANAGER all have IDENTICAL backend
 * read access: GET /accounts, /orders, /transactions (all four roles share
 * that @PreAuthorize list) but NOT GET /users (ADMIN only) - so this page
 * reuses AdminDashboard's account/order/transaction widgets, just without
 * the two user-based ones (Users by Role, New Signups), which would 403.
 */
export default function OversightDashboard({ role }) {
  const accounts = useFetch(() => getAllAccounts(), []);
  const orders = useFetch(() => getAllOrders(), []);
  const transactions = useFetch(() => getAllTransactions(), []);

  const isLoading = accounts.isLoading || orders.isLoading || transactions.isLoading;
  const firstError = accounts.error || orders.error || transactions.error;

  if (isLoading) return <LoadingState label="Loading platform data..." />;
  if (firstError) return <ErrorState error={firstError} onRetry={accounts.refetch} />;

  const accountList = accounts.data ?? [];
  const orderList = orders.data ?? [];
  const transactionList = transactions.data ?? [];

  const totalAUM = accountList.reduce((sum, a) => sum + Number(a.balance ?? 0), 0);
  const totalVolume = transactionList.reduce(
    (sum, t) => sum + Number(t.totalAmount ?? 0),
    0
  );

  const accountStatusData = countBy(accountList, "status", ACCOUNT_STATUS_COLORS);
  const orderStatusData = countBy(orderList, "orderStatus", ORDER_STATUS_COLORS);
  const transactionVolumeTrend = sumByDay(transactionList, "transactionDate", "totalAmount");

  const recentOrders = [...orderList]
    .sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
    .slice(0, 6);

  return (
    <div>
      <h1 className="page-title">{TITLE_BY_ROLE[role] ?? "Dashboard"}</h1>

      <div className="stat-cards">
        <StatCard label="Total Accounts" value={accountList.length} />
        <StatCard label="Total AUM" value={formatCurrency(totalAUM)} />
        <StatCard label="Total Orders" value={orderList.length} />
        <StatCard label="Total Transaction Volume" value={formatCurrency(totalVolume)} />
      </div>

      <div className="dashboard-grid">
        <div className="panel chart-panel">
          <h3>Account Status</h3>
          {accountStatusData.length === 0 ? (
            <p className="muted">No accounts yet.</p>
          ) : (
            <ResponsiveContainer width="100%" height={240}>
              <PieChart>
                <Pie data={accountStatusData} dataKey="value" nameKey="name" innerRadius={45} outerRadius={80}>
                  {accountStatusData.map((entry) => (
                    <Cell key={entry.name} fill={entry.color} />
                  ))}
                </Pie>
                <Tooltip />
                <Legend />
              </PieChart>
            </ResponsiveContainer>
          )}
        </div>

        <div className="panel chart-panel">
          <h3>Orders by Status</h3>
          {orderStatusData.length === 0 ? (
            <p className="muted">No orders yet.</p>
          ) : (
            <ResponsiveContainer width="100%" height={240}>
              <BarChart data={orderStatusData}>
                <CartesianGrid strokeDasharray="3 3" stroke="var(--border-color)" />
                <XAxis dataKey="name" tick={{ fontSize: 11 }} />
                <YAxis allowDecimals={false} tick={{ fontSize: 12 }} />
                <Tooltip />
                <Bar dataKey="value" isAnimationActive={false}>
                  {orderStatusData.map((entry) => (
                    <Cell key={entry.name} fill={entry.color} />
                  ))}
                </Bar>
              </BarChart>
            </ResponsiveContainer>
          )}
        </div>

        <div className="panel chart-panel chart-panel-wide">
          <h3>Transaction Volume</h3>
          {transactionVolumeTrend.length === 0 ? (
            <p className="muted">No completed transactions yet.</p>
          ) : (
            <ResponsiveContainer width="100%" height={240}>
              <BarChart data={transactionVolumeTrend}>
                <CartesianGrid strokeDasharray="3 3" stroke="var(--border-color)" />
                <XAxis dataKey="date" tick={{ fontSize: 11 }} />
                <YAxis tick={{ fontSize: 12 }} tickFormatter={(v) => `₹${v}`} />
                <Tooltip formatter={(value) => formatCurrency(value)} />
                <Bar dataKey="value" name="Volume" fill="#7c3aed" isAnimationActive={false} />
              </BarChart>
            </ResponsiveContainer>
          )}
        </div>

        <div className="panel">
          <h3>Recent Orders (All Accounts)</h3>
          {recentOrders.length === 0 ? (
            <p className="muted">No orders placed yet.</p>
          ) : (
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
