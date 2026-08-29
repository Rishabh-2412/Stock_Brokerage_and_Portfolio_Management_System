import { useState } from "react";
import { createAccount } from "../../api/accountApi";
import { useAccount } from "../../context/AccountContext";

const ACCOUNT_TYPES = ["CASH", "MARGIN", "DEMO"];

// Shown when a logged-in CLIENT has zero trading accounts. Matches
// POST /api/accounts exactly: only accountType and balance (= initial
// deposit) are read; the backend infers the owner from the JWT.
export default function OpenAccount() {
  const { refetch } = useAccount();
  const [accountType, setAccountType] = useState("CASH");
  const [initialDeposit, setInitialDeposit] = useState("10000");
  const [error, setError] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");
    setIsSubmitting(true);
    try {
      await createAccount(accountType, Number(initialDeposit));
      await refetch(); // AccountContext re-fetches /accounts/me, unlocking the rest of the app
    } catch (err) {
      setError(err.response?.data?.message || "Failed to open account.");
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <div className="open-account-wrap">
      <form className="panel open-account-form" onSubmit={handleSubmit}>
        <h2>Open Your Trading Account</h2>
        <p className="muted">
          You're logged in but don't have a trading account yet. Set one up
          to start trading.
        </p>

        <label htmlFor="accountType">Account Type</label>
        <select
          id="accountType"
          value={accountType}
          onChange={(e) => setAccountType(e.target.value)}
        >
          {ACCOUNT_TYPES.map((t) => (
            <option key={t} value={t}>
              {t}
            </option>
          ))}
        </select>

        <label htmlFor="initialDeposit">Initial Deposit (₹)</label>
        <input
          id="initialDeposit"
          type="number"
          min="0"
          step="0.01"
          value={initialDeposit}
          onChange={(e) => setInitialDeposit(e.target.value)}
          required
        />

        {error && <div className="error-message">{error}</div>}

        <button type="submit" className="primary-btn" disabled={isSubmitting}>
          {isSubmitting ? "Opening account..." : "Open Account"}
        </button>
      </form>
    </div>
  );
}
