// Each nav item: { label, path, icon }
// Icons are plain emoji for now to avoid adding an icon library dependency this early.

const CLIENT_NAV = [
  { label: "Dashboard", path: "/dashboard", icon: "🏠" },
  { label: "Stock Search", path: "/stocks", icon: "🔍" },
  { label: "Portfolio", path: "/portfolio", icon: "📊" },
  { label: "Watchlist", path: "/watchlist", icon: "⭐" },
  { label: "Orders", path: "/orders", icon: "📋" },
  { label: "Portfolio Analytics", path: "/analytics", icon: "📈" },
  { label: "Transaction History", path: "/transactions", icon: "🧾" },
  { label: "Research & Reports", path: "/research", icon: "📰" },
  { label: "Funds", path: "/funds", icon: "💰" },
  { label: "Profile", path: "/profile", icon: "👤" },
];

const ADMIN_NAV = [
  { label: "Dashboard", path: "/dashboard", icon: "🏠" },
  { label: "User Management", path: "/admin/users", icon: "👥" },
  { label: "Account Management", path: "/admin/accounts", icon: "🏦" },
  { label: "Securities", path: "/admin/securities", icon: "💹" },
  { label: "Market Data", path: "/admin/market-data", icon: "📉" },
  { label: "Stock Search", path: "/stocks", icon: "🔍" },
  { label: "Research & Reports", path: "/research", icon: "📰" },
  { label: "Profile", path: "/profile", icon: "👤" },
];

const RESEARCH_ANALYST_NAV = [
  { label: "Dashboard", path: "/dashboard", icon: "🏠" },
  { label: "Stock Search", path: "/stocks", icon: "🔍" },
  { label: "Research & Reports", path: "/research", icon: "📰" },
  { label: "Profile", path: "/profile", icon: "👤" },
];

const DEALER_NAV = [
  { label: "Dashboard", path: "/dashboard", icon: "🏠" },
  { label: "Trade Desk", path: "/dealer/trade", icon: "💱" },
  { label: "All Accounts", path: "/dealer/accounts", icon: "🏦" },
  { label: "All Orders", path: "/dealer/orders", icon: "📋" },
  { label: "All Transactions", path: "/dealer/transactions", icon: "🧾" },
  { label: "Stock Search", path: "/stocks", icon: "🔍" },
  { label: "Profile", path: "/profile", icon: "👤" },
];

// COMPLIANCE_OFFICER and RISK_MANAGER have identical read-only backend
// access (no trading, no status changes) — same nav for both.
const OVERSIGHT_NAV = [
  { label: "Dashboard", path: "/dashboard", icon: "🏠" },
  { label: "All Accounts", path: "/dealer/accounts", icon: "🏦" },
  { label: "All Orders", path: "/dealer/orders", icon: "📋" },
  { label: "All Transactions", path: "/dealer/transactions", icon: "🧾" },
  { label: "Profile", path: "/profile", icon: "👤" },
];

const NAV_BY_ROLE = {
  CLIENT: CLIENT_NAV,
  ADMIN: ADMIN_NAV,
  RESEARCH_ANALYST: RESEARCH_ANALYST_NAV,
  DEALER: DEALER_NAV,
  COMPLIANCE_OFFICER: OVERSIGHT_NAV,
  RISK_MANAGER: OVERSIGHT_NAV,
};

// Fallback for roles we haven't built nav for yet, so the app
// doesn't render an empty sidebar for those users.
const DEFAULT_NAV = [{ label: "Dashboard", path: "/dashboard", icon: "🏠" }];

export function getNavForRole(role) {
  return NAV_BY_ROLE[role] || DEFAULT_NAV;
}
