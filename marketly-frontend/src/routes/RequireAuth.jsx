import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

// Wrap any route(s) that should only be visible to logged-in users.
// Usage in App.jsx:
//   <Route element={<RequireAuth />}>
//     <Route path="/dashboard" element={<Dashboard />} />
//   </Route>
export default function RequireAuth() {
  const { isAuthenticated, isLoading } = useAuth();

  if (isLoading) {
    // Still checking localStorage for a saved session — avoid flashing the login page.
    return <div style={{ padding: "2rem" }}>Loading...</div>;
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  return <Outlet />;
}
