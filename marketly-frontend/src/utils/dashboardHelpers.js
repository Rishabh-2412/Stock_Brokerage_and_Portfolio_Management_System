// Shared by every role-specific dashboard (Admin/Oversight/Research) so the
// same grouping logic and color choices aren't copy-pasted four times.

export const ROLE_COLORS = {
  ADMIN: "#c026d3",
  CLIENT: "#2f3ee0",
  DEALER: "#059669",
  RESEARCH_ANALYST: "#0d9488",
  COMPLIANCE_OFFICER: "#0369a1",
  RISK_MANAGER: "#dc2626",
};

// Matches the .status-active/.status-suspended/.status-inactive badge
// colors already used elsewhere in the app, for visual consistency.
export const ACCOUNT_STATUS_COLORS = {
  ACTIVE: "#16a34a",
  SUSPENDED: "#dc2626",
  INACTIVE: "#6b7280",
};

export const ORDER_STATUS_COLORS = {
  PENDING: "#d97706",
  FILLED: "#16a34a",
  PARTIALLY_FILLED: "#0891b2",
  CANCELLED: "#6b7280",
  REJECTED: "#dc2626",
};

// Groups `items` by `field`, returns [{ name, value, color }] for Pie/Bar
// charts. colorMap is optional - falls back to a neutral gray for any
// value it doesn't recognize (e.g. a future enum value added later).
export function countBy(items, field, colorMap = {}) {
  const counts = {};
  for (const item of items) {
    const key = item[field] ?? "UNKNOWN";
    counts[key] = (counts[key] ?? 0) + 1;
  }
  return Object.entries(counts).map(([name, value]) => ({
    name,
    value,
    color: colorMap[name] ?? "#9ca3af",
  }));
}

// Groups `items` by the calendar day portion of `dateField`, counting
// occurrences per day. Returns [{ date, value }] sorted chronologically -
// feeds directly into a Recharts LineChart/BarChart.
export function countByDay(items, dateField) {
  const counts = {};
  for (const item of items) {
    if (!item[dateField]) continue;
    const day = String(item[dateField]).slice(0, 10); // "YYYY-MM-DD"
    counts[day] = (counts[day] ?? 0) + 1;
  }
  return Object.entries(counts)
    .map(([date, value]) => ({ date, value }))
    .sort((a, b) => (a.date < b.date ? -1 : 1));
}

// Same day-bucketing as countByDay, but SUMS `sumField` (e.g. totalAmount)
// per day instead of counting rows - used for the transaction volume chart.
export function sumByDay(items, dateField, sumField) {
  const sums = {};
  for (const item of items) {
    if (!item[dateField]) continue;
    const day = String(item[dateField]).slice(0, 10);
    sums[day] = (sums[day] ?? 0) + Number(item[sumField] ?? 0);
  }
  return Object.entries(sums)
    .map(([date, value]) => ({ date, value: Math.round(value * 100) / 100 }))
    .sort((a, b) => (a.date < b.date ? -1 : 1));
}
