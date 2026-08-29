import { useState } from "react";
import { useFetch } from "../../../hooks/useFetch";
import { getAllUsers, createUser } from "../../../api/authApi";
import { LoadingState, ErrorState } from "../../../components/StatusStates";

const ROLES = ["ADMIN", "CLIENT", "DEALER", "RESEARCH_ANALYST", "COMPLIANCE_OFFICER", "RISK_MANAGER"];
const CATEGORIES = ["INDIVIDUAL", "INSTITUTIONAL"];

export default function UserManagement() {
  const { data, isLoading, error, refetch } = useFetch(() => getAllUsers(), []);
  const [showForm, setShowForm] = useState(false);

  const users = data ?? [];

  return (
    <div>
      <div className="page-header-row">
        <h1 className="page-title">User Management</h1>
        <button className="primary-btn" onClick={() => setShowForm((v) => !v)}>
          {showForm ? "Cancel" : "+ Add User"}
        </button>
      </div>

      {showForm && (
        <CreateUserForm
          onCreated={() => {
            setShowForm(false);
            refetch();
          }}
        />
      )}

      <div className="panel">
        {isLoading && <LoadingState label="Loading users..." />}
        {error && <ErrorState error={error} onRetry={refetch} />}

        {!isLoading && !error && users.length === 0 && (
          <p className="muted">No users found.</p>
        )}

        {!isLoading && users.length > 0 && (
          <table className="simple-table">
            <thead>
              <tr>
                <th>Name</th>
                <th>Username</th>
                <th>Email</th>
                <th>Role</th>
                <th>KYC Status</th>
              </tr>
            </thead>
            <tbody>
              {users.map((u) => (
                <tr key={u.userId}>
                  <td>{u.fullName}</td>
                  <td>{u.username}</td>
                  <td>{u.email}</td>
                  <td>
                    <span className="role-pill">{u.role}</span>
                  </td>
                  <td>{u.kycStatus || "—"}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}

function CreateUserForm({ onCreated }) {
  const [form, setForm] = useState({
    username: "",
    email: "",
    password: "",
    fullName: "",
    phone: "",
    role: "CLIENT",
    accountType: "INDIVIDUAL",
  });
  const [error, setError] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);

  function handleChange(e) {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");
    setIsSubmitting(true);
    try {
      await createUser(form);
      onCreated();
    } catch (err) {
      setError(err.response?.data?.message || "Failed to create user.");
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <form className="panel create-user-form" onSubmit={handleSubmit}>
      <h3>Create New User</h3>

      <div className="form-grid">
        <div>
          <label htmlFor="username">Username</label>
          <input id="username" name="username" value={form.username} onChange={handleChange} required />
        </div>
        <div>
          <label htmlFor="email">Email</label>
          <input id="email" name="email" type="email" value={form.email} onChange={handleChange} required />
        </div>
        <div>
          <label htmlFor="password">Password</label>
          <input id="password" name="password" type="password" value={form.password} onChange={handleChange} required />
        </div>
        <div>
          <label htmlFor="fullName">Full Name</label>
          <input id="fullName" name="fullName" value={form.fullName} onChange={handleChange} required />
        </div>
        <div>
          <label htmlFor="phone">Phone</label>
          <input id="phone" name="phone" value={form.phone} onChange={handleChange} />
        </div>
        <div>
          <label htmlFor="role">Role</label>
          <select id="role" name="role" value={form.role} onChange={handleChange}>
            {ROLES.map((r) => (
              <option key={r} value={r}>
                {r}
              </option>
            ))}
          </select>
        </div>
        <div>
          <label htmlFor="accountType">Account Category</label>
          <select id="accountType" name="accountType" value={form.accountType} onChange={handleChange}>
            {CATEGORIES.map((c) => (
              <option key={c} value={c}>
                {c}
              </option>
            ))}
          </select>
        </div>
      </div>

      {error && <div className="error-message">{error}</div>}

      <button type="submit" className="primary-btn" disabled={isSubmitting}>
        {isSubmitting ? "Creating..." : "Create User"}
      </button>
    </form>
  );
}
