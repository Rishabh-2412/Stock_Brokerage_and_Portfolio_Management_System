import { useState } from "react";
import { useAccount } from "../../context/AccountContext";
import { transferFunds } from "../../api/accountApi";
import { LoadingState, ErrorState } from "../../components/StatusStates";
import { formatCurrency } from "../../utils/format";

const QUICK_AMOUNTS = [1000, 5000, 10000, 25000];

export default function Funds() {
  const { account, isLoading, error, refetch } = useAccount();
  const [tab, setTab] = useState("DEPOSIT"); // "DEPOSIT" | "WITHDRAWAL"
  const [result, setResult] = useState(null);

  if (isLoading) return <LoadingState label="Loading account..." />;
  if (error) return <ErrorState error={error} />;

  const isAccountActive = account.status === "ACTIVE";

  function handleDone(updatedAccount) {
    setResult(updatedAccount);
    refetch();
  }

  return (
    <div className="funds-page">
      <h1 className="page-title">Funds</h1>

      <BalanceHero account={result ?? account} />

      {!isAccountActive && (
        <div className="funds-status-warning">
          <WarningIcon />
          <span>
            This account is <strong>{account.status}</strong>. Fund transfers
            may not be permitted until it's reactivated — contact support if
            a transfer fails.
          </span>
        </div>
      )}

      {result ? (
        <TransferSuccess result={result} onClose={() => setResult(null)} />
      ) : (
        <div className="funds-transfer-panel panel">
          <div className="funds-tabs">
            <button
              className={tab === "DEPOSIT" ? "funds-tab funds-tab-active" : "funds-tab"}
              onClick={() => setTab("DEPOSIT")}
            >
              Add Funds
            </button>
            <button
              className={tab === "WITHDRAWAL" ? "funds-tab funds-tab-active" : "funds-tab"}
              onClick={() => setTab("WITHDRAWAL")}
            >
              Withdraw
            </button>
          </div>

          <TransferForm
            key={tab}
            accountId={account.accountId}
            transferType={tab}
            cashAvailable={Number(account.cashAvailable)}
            onDone={handleDone}
          />
        </div>
      )}
    </div>
  );
}

function BalanceHero({ account }) {
  return (
    <div className="funds-hero">
      <div className="funds-hero-main">
        <div className="funds-hero-label">Total Balance</div>
        <div className="funds-hero-value">{formatCurrency(account.balance)}</div>
        <div className="funds-hero-account">
          {account.accountNumber} · {account.accountType}
        </div>
      </div>
      <div className="funds-hero-divider" />
      <div className="funds-hero-side">
        <div className="funds-hero-label">Cash Available</div>
        <div className="funds-hero-side-value">
          {formatCurrency(account.cashAvailable)}
        </div>
      </div>
    </div>
  );
}

function TransferForm({ accountId, transferType, cashAvailable, onDone }) {
  const isDeposit = transferType === "DEPOSIT";

  const [amount, setAmount] = useState("");
  const [description, setDescription] = useState(
    isDeposit ? "Bank transfer top-up" : "Withdrawal to bank account"
  );
  const [bankAccountNumber, setBankAccountNumber] = useState("");
  const [error, setError] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);

  const numericAmount = Number(amount) || 0;
  const exceedsAvailable = !isDeposit && numericAmount > cashAvailable;

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");

    if (exceedsAvailable) {
      setError("Amount exceeds available cash balance.");
      return;
    }

    setIsSubmitting(true);
    try {
      const updatedAccount = await transferFunds({
        accountId,
        transferType,
        amount: numericAmount,
        description,
        bankAccountNumber: bankAccountNumber || undefined,
      });
      onDone(updatedAccount);
    } catch (err) {
      setError(err.response?.data?.message || "Transfer failed. Please try again.");
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <form onSubmit={handleSubmit} className="funds-form">
      <label htmlFor="amount">Amount</label>
      <div className="funds-amount-input-wrap">
        <span className="funds-currency-prefix">₹</span>
        <input
          id="amount"
          type="number"
          step="0.01"
          min="0.01"
          placeholder="0.00"
          value={amount}
          onChange={(e) => setAmount(e.target.value)}
          className="funds-amount-input"
          required
        />
      </div>

      <div className="funds-quick-amounts">
        {QUICK_AMOUNTS.map((qa) => (
          <button
            type="button"
            key={qa}
            className="funds-quick-chip"
            onClick={() => setAmount(String(qa))}
          >
            +{formatCurrency(qa)}
          </button>
        ))}
      </div>

      {!isDeposit && (
        <div className="funds-available-hint">
          Available to withdraw: <strong>{formatCurrency(cashAvailable)}</strong>
        </div>
      )}

      <label htmlFor="description">Description</label>
      <input
        id="description"
        value={description}
        onChange={(e) => setDescription(e.target.value)}
        required
      />

      <label htmlFor="bankAccountNumber">Bank Account Number (optional)</label>
      <input
        id="bankAccountNumber"
        value={bankAccountNumber}
        onChange={(e) => setBankAccountNumber(e.target.value)}
        placeholder="e.g. BANK-001"
      />

      {error && <div className="error-message">{error}</div>}

      <button
        type="submit"
        className={isDeposit ? "funds-submit-btn funds-submit-deposit" : "funds-submit-btn funds-submit-withdraw"}
        disabled={isSubmitting || exceedsAvailable || numericAmount <= 0}
      >
        {isSubmitting
          ? "Processing..."
          : isDeposit
          ? `Add ${amount ? formatCurrency(numericAmount) : "Funds"}`
          : `Withdraw ${amount ? formatCurrency(numericAmount) : "Funds"}`}
      </button>
    </form>
  );
}

function TransferSuccess({ result, onClose }) {
  return (
    <div className="panel funds-success">
      <div className="funds-success-icon">
        <CheckIcon />
      </div>
      <h2>Transfer Complete</h2>
      <p className="muted">Your account balance has been updated.</p>
      <div className="funds-success-balances">
        <div>
          <div className="funds-hero-label">New Balance</div>
          <div className="funds-success-amount">{formatCurrency(result.balance)}</div>
        </div>
        <div>
          <div className="funds-hero-label">Cash Available</div>
          <div className="funds-success-amount">{formatCurrency(result.cashAvailable)}</div>
        </div>
      </div>
      <button className="primary-btn" onClick={onClose}>
        Make Another Transfer
      </button>
    </div>
  );
}

function CheckIcon() {
  return (
    <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
      <polyline points="20 6 9 17 4 12" />
    </svg>
  );
}

function WarningIcon() {
  return (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <path d="M10.29 3.86 1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0Z" />
      <line x1="12" y1="9" x2="12" y2="13" />
      <line x1="12" y1="17" x2="12.01" y2="17" />
    </svg>
  );
}
