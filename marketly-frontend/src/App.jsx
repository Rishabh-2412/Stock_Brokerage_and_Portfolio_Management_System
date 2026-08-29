import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext";
import { AccountProvider } from "./context/AccountContext";
import RequireAuth from "./routes/RequireAuth";
import RequireRole from "./routes/RequireRole";
import AppLayout from "./layouts/AppLayout";

import Login from "./pages/auth/Login";
import Register from "./pages/auth/Register";
import Landing from "./pages/landing/Landing";
import Dashboard from "./pages/Dashboard";
import Portfolio from "./pages/portfolio/Portfolio";
import Watchlist from "./pages/watchlist/Watchlist";
import Orders from "./pages/orders/Orders";
import TradeOrder from "./pages/orders/TradeOrder";
import StockSearch from "./pages/stocks/StockSearch";
import StockDetails from "./pages/stocks/StockDetails";
import PortfolioAnalytics from "./pages/analytics/PortfolioAnalytics";
import TransactionHistory from "./pages/transactions/TransactionHistory";
import Profile from "./pages/profile/Profile";
import Funds from "./pages/funds/Funds";
import ResearchNotes from "./pages/research/ResearchNotes";
import UserManagement from "./pages/admin/users/UserManagement";
import AccountManagement from "./pages/admin/accounts/AccountManagement";
import SecurityManagement from "./pages/admin/securities/SecurityManagement";
import MarketDataManagement from "./pages/admin/market/MarketDataManagement";
import AllAccountsOverview from "./pages/oversight/AllAccountsOverview";
import AllOrdersOverview from "./pages/oversight/AllOrdersOverview";
import AllTransactionsOverview from "./pages/oversight/AllTransactionsOverview";
import DealerTrade from "./pages/oversight/DealerTrade";

import "./App.css";

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          {/* Public routes */}
          <Route path="/" element={<Landing />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />

          {/* Protected routes: must be logged in. */}
          <Route element={<RequireAuth />}>
            {/* Routes needing a trading account (CLIENT-centric).
                AccountProvider fetches accountId once and shares it via
                context, and AppLayout renders the sidebar/topbar once. */}
            <Route
              element={
                <AccountProvider>
                  <AppLayout />
                </AccountProvider>
              }
            >
              <Route path="/dashboard" element={<Dashboard />} />
              <Route path="/stocks" element={<StockSearch />} />
              <Route path="/stocks/:securityId" element={<StockDetails />} />
              <Route path="/trade/:securityId" element={<TradeOrder />} />
              <Route path="/portfolio" element={<Portfolio />} />
              <Route path="/watchlist" element={<Watchlist />} />
              <Route path="/orders" element={<Orders />} />
              <Route path="/analytics" element={<PortfolioAnalytics />} />
              <Route path="/transactions" element={<TransactionHistory />} />
              <Route path="/research" element={<ResearchNotes />} />
              <Route path="/funds" element={<Funds />} />
              <Route path="/profile" element={<Profile />} />

              {/* ADMIN only */}
              <Route element={<RequireRole roles={["ADMIN"]} />}>
                <Route path="/admin/users" element={<UserManagement />} />
                <Route path="/admin/accounts" element={<AccountManagement />} />
                <Route path="/admin/securities" element={<SecurityManagement />} />
                <Route path="/admin/market-data" element={<MarketDataManagement />} />
              </Route>

              {/* Oversight views: ADMIN/DEALER/COMPLIANCE_OFFICER/RISK_MANAGER
                  (matches backend @PreAuthorize on GET /accounts, /orders,
                  /transactions exactly). */}
              <Route
                element={
                  <RequireRole
                    roles={["ADMIN", "DEALER", "COMPLIANCE_OFFICER", "RISK_MANAGER"]}
                  />
                }
              >
                <Route path="/dealer/accounts" element={<AllAccountsOverview />} />
                <Route path="/dealer/orders" element={<AllOrdersOverview />} />
                <Route path="/dealer/transactions" element={<AllTransactionsOverview />} />
              </Route>

              {/* Trade-on-behalf-of: DEALER/ADMIN only (matches backend's
                  POST /orders authorization for placing on any account). */}
              <Route element={<RequireRole roles={["DEALER", "ADMIN"]} />}>
                <Route path="/dealer/trade" element={<DealerTrade />} />
              </Route>
            </Route>
          </Route>

          {/* Unknown paths fall back to the dashboard (RequireAuth will
              redirect to /login if not signed in). */}
          <Route path="*" element={<Navigate to="/dashboard" replace />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;
