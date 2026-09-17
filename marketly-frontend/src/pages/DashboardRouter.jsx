import { useAuth } from "../context/AuthContext";
import Dashboard from "./Dashboard";
import AdminDashboard from "./admin/dashboard/AdminDashboard";
import OversightDashboard from "./oversight/OversightDashboard";
import ResearchDashboard from "./research/ResearchDashboard";

// The "/dashboard" route resolves here first, and picks the right
// dashboard for what each role can actually see on the backend:
// - ADMIN: full platform analytics (users + accounts + orders + transactions)
// - DEALER / COMPLIANCE_OFFICER / RISK_MANAGER: same accounts/orders/
//   transactions access as ADMIN, but no /users access (403 there), so they
//   share OversightDashboard (no user-based widgets)
// - RESEARCH_ANALYST: no access to any of the above - only research notes
//   (read: open to everyone; write: RESEARCH_ANALYST/ADMIN) - so their
//   dashboard is built from that instead
// - everyone else (CLIENT): the original dashboard, unchanged
const OVERSIGHT_ROLES = ["DEALER", "COMPLIANCE_OFFICER", "RISK_MANAGER"];

export default function DashboardRouter() {
  const { role } = useAuth();

  if (role === "ADMIN") return <AdminDashboard />;
  if (OVERSIGHT_ROLES.includes(role)) return <OversightDashboard role={role} />;
  if (role === "RESEARCH_ANALYST") return <ResearchDashboard />;
  return <Dashboard />;
}
