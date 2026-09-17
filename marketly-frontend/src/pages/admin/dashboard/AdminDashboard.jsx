import {
  ResponsiveContainer,
  PieChart,
  Pie,
  Cell,
  BarChart,
  Bar,
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
} from "recharts";
import { useFetch } from "../../../hooks/useFetch";
import { getAllUsers } from "../../../api/authApi";
import { getAllAccounts } from "../../../api/accountApi";
import { getAllOrders } from "../../../api/orderApi";
import { getAllTransactions } from "../../../api/transactionApi";
import { LoadingState, ErrorState } from "../../../components/StatusStates";
import { formatCurrency } from "../../../utils/format";
import {
  ROLE_COLORS,
  ACCOUNT_STATUS_COLORS,
  ORDER_STATUS_COLORS,
  countBy,
  countByDay,
  sumByDay,
} from "../../../utils/dashboardHelpers";

export default function AdminDashboard() {
  // Intentionally does NOT use useAccount() anywhere on this page - an
  // admin's own personal trading account isn't relevant here. Every number
  // on this page is a platform-wide aggregate over ALL users/accounts.
  const users = useFetch(() => getAllUsers(), []);
  const accounts = useFetch(() => getAllAccounts(), []);
  const orders = useFetch(() => getAllOrders(), []);
  const transactions = useFetch(() => getAllTransactions(), []);

  const isLoading =
    users.isLoading || accounts.isLoading || orders.isLoading || transactions.isLoading;
  const firstError = users.error || accounts.error || orders.error || transactions.error;

  if (isLoading) return <LoadingState label="Loading platform data..." />;
  if (firstError) return <ErrorState error={firstError} onRetry={users.refetch} />;

  const userList = users.data ?? [];
  const accountList = accounts.data ?? [];
  const orderList = orders.data ?? [];
  const transactionList = transactions.data ?? [];

  const totalAUM = accountList.reduce((sum, a) => sum + Number(a.balance ?? 0), 0);

  const roleData = countBy(userList, "role", ROLE_COLORS);
  const accountStatusData = countBy(accountList, "status", ACCOUNT_STATUS_COLORS);
  const orderStatusData = countBy(orderList, "orderStatus", ORDER_STATUS_COLORS);
  const signupTrend = countByDay(userList, "createdAt");
  const transactionVolumeTrend = sumByDay(transactionList, "transactionDate", "totalAmount");

  const recentOrders = [...orderList]
    .sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
    .slice(0, 6);

  return (
    <div>
      <h1 className="page-title">Admin Dashboard</h1>

      <div className="stat-cards">
        <StatCard label="Total Users" value={userList.length} />
        <StatCard label="Total Accounts" value={accountList.length} />
        <StatCard label="Total AUM" value={formatCurrency(totalAUM)} />
        <StatCard label="Total Orders" value={orderList.length} />
      </div>

      <div className="dashboard-grid">
        <div className="panel chart-panel">
          <h3>Users by Role</h3>
          {roleData.length === 0 ? (
            <p className="muted">No users yet.</p>
          ) : (
            <ResponsiveContainer width="100%" height={240}>
              <PieChart>
                <Pie data={roleData} dataKey="value" nameKey="name" innerRadius={45} outerRadius={80}>
                  {roleData.map((entry) => (
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

        <div className="panel chart-panel">
          <h3>New User Signups</h3>
          {signupTrend.length === 0 ? (
            <p className="muted">No signup data yet.</p>
          ) : (
            <ResponsiveContainer width="100%" height={240}>
              <LineChart data={signupTrend}>
                <CartesianGrid strokeDasharray="3 3" stroke="var(--border-color)" />
                <XAxis dataKey="date" tick={{ fontSize: 11 }} />
                <YAxis allowDecimals={false} tick={{ fontSize: 12 }} />
                <Tooltip />
                <Line
                  type="monotone"
                  dataKey="value"
                  name="New users"
                  stroke="#2f3ee0"
                  strokeWidth={2.5}
                  dot={{ r: 3 }}
                  isAnimationActive={false}
                />
              </LineChart>
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
