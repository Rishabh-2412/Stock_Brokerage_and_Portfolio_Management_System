import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

// Wrap role-restricted routes. Must be used INSIDE RequireAuth
// (so we already know the user is logged in).
//
// Usage:
//   <Route element={<RequireRole roles={["ADMIN"]} />}>
//     <Route path="/admin/users" element={<UserManagement />} />
//   </Route>
export default function RequireRole({ roles }) {
  const { role } = useAuth();

  const isAllowed = roles.includes(role);

  if (!isAllowed) {
    // Send unauthorized users somewhere safe instead of a blank/broken page.
    return <Navigate to="/dashboard" replace />;
  }

  return <Outlet />;
}
