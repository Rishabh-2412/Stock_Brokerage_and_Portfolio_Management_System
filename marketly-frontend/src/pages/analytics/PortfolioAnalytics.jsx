import { useMemo } from "react";
import {
  PieChart,
  Pie,
  Cell,
  Tooltip,
  Legend,
  ResponsiveContainer,
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  AreaChart,
  Area,
} from "recharts";
import { useAccount } from "../../context/AccountContext";
import { useFetch } from "../../hooks/useFetch";
import { getPortfolio } from "../../api/portfolioApi";
import { getTransactionsForAccount } from "../../api/transactionApi";
import { LoadingState, ErrorState } from "../../components/StatusStates";
import { formatCurrency, formatPercent } from "../../utils/format";

// A cheerful, distinct palette that cycles for however many holdings exist.
const COLORS = [
  "#6366f1", // indigo
  "#22c55e", // green
  "#f59e0b", // amber
  "#ec4899", // pink
  "#06b6d4", // cyan
  "#a855f7", // purple
  "#ef4444", // red
  "#84cc16", // lime
];

export default function PortfolioAnalytics() {
  const { accountId, isLoading: accountLoading } = useAccount();

  const portfolio = useFetch(
    () => getPortfolio(accountId),
    [accountId],
    !!accountId
  );
  const transactions = useFetch(
    () => getTransactionsForAccount(accountId),
    [accountId],
    !!accountId
  );

  const holdings = portfolio.data?.holdings ?? [];

  // --- Allocation data: each holding's share of total current value ---
  const allocationData = useMemo(
    () =>
      holdings.map((h) => ({
        name: h.symbol,
        value: Number(h.currentValue) || 0,
      })),
    [holdings]
  );

  // --- P&L bar data: unrealized P&L per holding ---
  const pnlData = useMemo(
    () =>
      holdings.map((h) => ({
        name: h.symbol,
        pnl: Number(h.unrealizedPL) || 0,
      })),
    [holdings]
  );

  // --- Value trend: cumulative invested amount over time, derived from
  // real Transaction History (BUY adds, SELL subtracts). This is an
  // approximation of portfolio growth, not a true daily mark-to-market
  // history, since no such endpoint exists. ---
  const trendData = useMemo(() => {
    const txs = transactions.data ?? [];
    if (txs.length === 0) return [];

    const sorted = [...txs].sort(
      (a, b) => new Date(a.transactionDate) - new Date(b.transactionDate)
    );

    let running = 0;
    return sorted.map((tx) => {
      const amount = Number(tx.totalAmount) || 0;
      running += tx.transactionType === "BUY" ? amount : -amount;
      return {
        date: new Date(tx.transactionDate).toLocaleDateString(),
        invested: Math.max(running, 0),
      };
    });
  }, [transactions.data]);

  if (accountLoading || portfolio.isLoading)
    return <LoadingState label="Loading portfolio analytics..." />;
  if (portfolio.error) return <ErrorState error={portfolio.error} onRetry={portfolio.refetch} />;

  const totalCurrentValue = portfolio.data?.totalCurrentValue ?? 0;
  const totalUnrealizedPL = portfolio.data?.totalUnrealizedPL ?? 0;
  const totalUnrealizedPLPercent = portfolio.data?.totalUnrealizedPLPercent ?? 0;

  return (
    <div>
      <h1 className="page-title">Portfolio Analytics</h1>

      <div className="stat-cards">
        <StatCard label="Current Value" value={formatCurrency(totalCurrentValue)} />
        <StatCard
          label="Total P&L"
          value={formatCurrency(totalUnrealizedPL)}
          sub={formatPercent(totalUnrealizedPLPercent)}
          positive={totalUnrealizedPL >= 0}
        />
        <StatCard label="Holdings" value={holdings.length} />
      </div>

      {holdings.length === 0 ? (
        <div className="panel">
          <p className="muted">
            You don't have any holdings yet — analytics will appear once you
            own at least one security.
          </p>
        </div>
      ) : (
        <div className="analytics-grid">
          <div className="panel chart-panel">
            <h3>Allocation by Holding</h3>
            <ResponsiveContainer width="100%" height={300}>
              <PieChart>
                <Pie
                  data={allocationData}
                  dataKey="value"
                  nameKey="name"
                  cx="50%"
                  cy="50%"
                  innerRadius={70}
                  outerRadius={110}
                  paddingAngle={3}
                  isAnimationActive={true}
                  animationDuration={800}
                  animationEasing="ease-out"
                >
                  {allocationData.map((entry, index) => (
                    <Cell
                      key={entry.name}
                      fill={COLORS[index % COLORS.length]}
                      stroke="#fff"
                      strokeWidth={2}
                    />
                  ))}
                </Pie>
                <Tooltip formatter={(value) => formatCurrency(value)} />
                <Legend verticalAlign="bottom" height={36} />
              </PieChart>
            </ResponsiveContainer>
          </div>

          <div className="panel chart-panel">
            <h3>P&L by Holding</h3>
            <ResponsiveContainer width="100%" height={300}>
              <BarChart data={pnlData}>
                <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0" />
                <XAxis dataKey="name" tick={{ fontSize: 12 }} />
                <YAxis tick={{ fontSize: 12 }} />
                <Tooltip formatter={(value) => formatCurrency(value)} />
                <Bar
                  dataKey="pnl"
                  radius={[6, 6, 0, 0]}
                  isAnimationActive={true}
                  animationDuration={800}
                  animationEasing="ease-out"
                >
                  {pnlData.map((entry) => (
                    <Cell
                      key={entry.name}
                      fill={entry.pnl >= 0 ? "#22c55e" : "#ef4444"}
                    />
                  ))}
                </Bar>
              </BarChart>
            </ResponsiveContainer>
          </div>

          <div className="panel chart-panel chart-panel-wide">
            <h3>Invested Value Over Time</h3>
            {transactions.isLoading && <LoadingState />}
            {!transactions.isLoading && trendData.length === 0 && (
              <p className="muted">No transaction history yet to chart a trend.</p>
            )}
            {!transactions.isLoading && trendData.length > 0 && (
              <ResponsiveContainer width="100%" height={280}>
                <AreaChart data={trendData}>
                  <defs>
                    <linearGradient id="investedGradient" x1="0" y1="0" x2="0" y2="1">
                      <stop offset="5%" stopColor="#6366f1" stopOpacity={0.4} />
                      <stop offset="95%" stopColor="#6366f1" stopOpacity={0} />
                    </linearGradient>
                  </defs>
                  <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0" />
                  <XAxis dataKey="date" tick={{ fontSize: 11 }} />
                  <YAxis tick={{ fontSize: 12 }} />
                  <Tooltip formatter={(value) => formatCurrency(value)} />
                  <Area
                    type="monotone"
                    dataKey="invested"
                    stroke="#6366f1"
                    strokeWidth={2.5}
                    fill="url(#investedGradient)"
                    isAnimationActive={true}
                    animationDuration={1000}
                    animationEasing="ease-out"
                  />
                </AreaChart>
              </ResponsiveContainer>
            )}
            <p className="chart-note">
              Approximate — derived from your transaction history, not a
              true daily valuation (no history endpoint exists yet).
            </p>
          </div>
        </div>
      )}
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
