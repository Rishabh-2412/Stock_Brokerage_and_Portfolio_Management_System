import { NavLink, Outlet, useNavigate } from "react-router-dom";
import { useState } from "react";
import { useAuth } from "../context/AuthContext";
import { useAccount } from "../context/AccountContext";
import { getNavForRole } from "../config/navConfig";
import ThemeToggle from "../components/ThemeToggle";

// This layout renders once and stays mounted while child pages
// (Dashboard, Portfolio, etc.) swap in and out via <Outlet />.
// That means the sidebar/topbar don't re-render on every page navigation.
export default function AppLayout() {
  const { user, role, logout } = useAuth();
  const { account } = useAccount();
  const navigate = useNavigate();
  const [searchTerm, setSearchTerm] = useState("");

  const navItems = getNavForRole(role);

  function handleLogout() {
    logout();
    navigate("/login");
  }

  return (
    <div className="app-shell">
      <aside className="sidebar">
        <div className="sidebar-brand">
          <img src="/marketly-logo.svg" alt="" width="20" height="21" />
          Marketly
        </div>

        <nav className="sidebar-nav">
          {navItems.map((item) => (
            <NavLink
              key={item.path}
              to={item.path}
              className={({ isActive }) =>
                isActive ? "nav-item nav-item-active" : "nav-item"
              }
            >
              <span className="nav-icon" style={{ background: item.color }}>
                {item.icon}
              </span>
              {item.label}
            </NavLink>
          ))}
        </nav>

        <button className="logout-btn" onClick={handleLogout}>
          <LogoutIcon />
          Logout
        </button>
      </aside>

      <div className="main-area">
        <header className="topbar">
          <input
            className="topbar-search"
            type="text"
            placeholder="Search stocks, indices..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            onKeyDown={(e) => {
              if (e.key === "Enter") {
                navigate("/stocks");
              }
            }}
          />
          <div className="topbar-user">
            {account && (
              <span className="account-badge" title="Active trading account">
                {account.accountNumber}
              </span>
            )}
            <span>{user?.username}</span>
            <span className="role-badge">{role}</span>
            <ThemeToggle />
          </div>
        </header>

        <main className="page-content">
          {/* The matched child route (Dashboard, Portfolio, etc.) renders here */}
          <Outlet />
        </main>
      </div>
    </div>
  );
}

// Minimal line-style icon (door + arrow), matching the clean monochrome
// icon style used in Claude's own UI rather than an emoji.
function LogoutIcon() {
  return (
    <svg
      width="16"
      height="16"
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      strokeWidth="2"
      strokeLinecap="round"
      strokeLinejoin="round"
    >
      <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4" />
      <polyline points="16 17 21 12 16 7" />
      <line x1="21" y1="12" x2="9" y2="12" />
    </svg>
  );
}
