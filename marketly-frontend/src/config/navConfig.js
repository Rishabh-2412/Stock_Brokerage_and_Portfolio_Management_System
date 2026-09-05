// Each nav item: { label, path, icon, color }
// "icon" is a short (1-2 letter) text abbreviation rather than an emoji, so
// it renders as a plain, consistent glyph on every OS/browser and can be
// colored/styled purely with CSS (emoji glyphs ignore `color`). "color" sets
// that badge's background so items stay visually distinguishable at a glance.

const CLIENT_NAV = [
  { label: "Dashboard", path: "/dashboard", icon: "Da", color: "#2f3ee0" },
  { label: "Stock Search", path: "/stocks", icon: "Sr", color: "#0891b2" },
  { label: "Portfolio", path: "/portfolio", icon: "Pf", color: "#7c3aed" },
  { label: "Watchlist", path: "/watchlist", icon: "Wl", color: "#d97706" },
  { label: "Orders", path: "/orders", icon: "Or", color: "#059669" },
  { label: "Portfolio Analytics", path: "/analytics", icon: "An", color: "#db2777" },
  { label: "Transaction History", path: "/transactions", icon: "Tx", color: "#4b5563" },
  { label: "Research & Reports", path: "/research", icon: "Rs", color: "#0d9488" },
  { label: "Funds", path: "/funds", icon: "Fn", color: "#16a34a" },
  { label: "Profile", path: "/profile", icon: "Pr", color: "#6366f1" },
];

const ADMIN_NAV = [
  { label: "Dashboard", path: "/dashboard", icon: "Da", color: "#2f3ee0" },
  { label: "User Management", path: "/admin/users", icon: "Us", color: "#c026d3" },
  { label: "Account Management", path: "/admin/accounts", icon: "Ac", color: "#0369a1" },
  { label: "Securities", path: "/admin/securities", icon: "Sc", color: "#b45309" },
  { label: "Market Data", path: "/admin/market-data", icon: "Md", color: "#dc2626" },
  { label: "Stock Search", path: "/stocks", icon: "Sr", color: "#0891b2" },
  { label: "Research & Reports", path: "/research", icon: "Rs", color: "#0d9488" },
  { label: "Profile", path: "/profile", icon: "Pr", color: "#6366f1" },
];

const RESEARCH_ANALYST_NAV = [
  { label: "Dashboard", path: "/dashboard", icon: "Da", color: "#2f3ee0" },
  { label: "Stock Search", path: "/stocks", icon: "Sr", color: "#0891b2" },
  { label: "Research & Reports", path: "/research", icon: "Rs", color: "#0d9488" },
  { label: "Profile", path: "/profile", icon: "Pr", color: "#6366f1" },
];

const DEALER_NAV = [
  { label: "Dashboard", path: "/dashboard", icon: "Da", color: "#2f3ee0" },
  { label: "Trade Desk", path: "/dealer/trade", icon: "Td", color: "#059669" },
  { label: "All Accounts", path: "/dealer/accounts", icon: "Aa", color: "#0369a1" },
  { label: "All Orders", path: "/dealer/orders", icon: "Ao", color: "#7c3aed" },
  { label: "All Transactions", path: "/dealer/transactions", icon: "At", color: "#4b5563" },
  { label: "Stock Search", path: "/stocks", icon: "Sr", color: "#0891b2" },
  { label: "Profile", path: "/profile", icon: "Pr", color: "#6366f1" },
];

// COMPLIANCE_OFFICER and RISK_MANAGER have identical read-only backend
// access (no trading, no status changes) — same nav for both.
const OVERSIGHT_NAV = [
  { label: "Dashboard", path: "/dashboard", icon: "Da", color: "#2f3ee0" },
  { label: "All Accounts", path: "/dealer/accounts", icon: "Aa", color: "#0369a1" },
  { label: "All Orders", path: "/dealer/orders", icon: "Ao", color: "#7c3aed" },
  { label: "All Transactions", path: "/dealer/transactions", icon: "At", color: "#4b5563" },
  { label: "Profile", path: "/profile", icon: "Pr", color: "#6366f1" },
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
const DEFAULT_NAV = [{ label: "Dashboard", path: "/dashboard", icon: "Da", color: "#2f3ee0" }];

export function getNavForRole(role) {
  return NAV_BY_ROLE[role] || DEFAULT_NAV;
}
