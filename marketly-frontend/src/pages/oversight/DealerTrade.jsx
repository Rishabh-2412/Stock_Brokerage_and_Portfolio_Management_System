import { useState } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";
import { useFetch } from "../../hooks/useFetch";
import { getAllSecurities } from "../../api/securityApi";
import { placeOrder } from "../../api/orderApi";
import { LoadingState, ErrorState } from "../../components/StatusStates";
import { formatCurrency } from "../../utils/format";

// DEALER (and ADMIN) can place orders on ANY account, not just their own
// (confirmed: POST /orders is hasAnyRole('CLIENT','DEALER','ADMIN'), and
// the service layer allows DEALER/ADMIN to target any accountId).
// accountId arrives via ?accountId= from the "Trade →" link on All Accounts.
export default function DealerTrade() {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const initialAccountId = searchParams.get("accountId") || "";

  const [accountId, setAccountId] = useState(initialAccountId);
  const [securityId, setSecurityId] = useState("");
  const [orderType, setOrderType] = useState("BUY");
  const [priceType, setPriceType] = useState("LIMIT");
  const [quantity, setQuantity] = useState(1);
  const [price, setPrice] = useState("");
  const [error, setError] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [successOrder, setSuccessOrder] = useState(null);

  const { data: securities, isLoading, error: loadError } = useFetch(
    () => getAllSecurities(),
    []
  );

  const selectedSecurity = (securities ?? []).find(
    (s) => String(s.securityId) === String(securityId)
  );

  const isBuy = orderType === "BUY";
  const effectivePrice =
    priceType === "MARKET" ? selectedSecurity?.currentPrice ?? 0 : Number(price) || 0;
  const totalAmount = effectivePrice * Number(quantity || 0);

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");
    setIsSubmitting(true);

    const orderData = {
      accountId: Number(accountId),
      securityId: Number(securityId),
      orderType,
      priceType,
      quantity: Number(quantity),
      ...(priceType === "LIMIT" ? { price: Number(price) } : {}),
    };

    try {
      const result = await placeOrder(orderData);
      setSuccessOrder(result);
    } catch (err) {
      setError(err.response?.data?.message || "Failed to place order.");
    } finally {
      setIsSubmitting(false);
    }
  }

  if (isLoading) return <LoadingState label="Loading securities..." />;
  if (loadError) return <ErrorState error={loadError} />;

  if (successOrder) {
    return (
      <div className="panel order-success">
        <h2>Order Placed ✅</h2>
        <p className="muted">
          {successOrder.orderType} order for {successOrder.quantity} share(s) of{" "}
          {successOrder.symbol} placed on account #{successOrder.accountId}. Status:{" "}
          {successOrder.orderStatus}.
        </p>
        <div className="order-success-actions">
          <button onClick={() => navigate("/dealer/orders")}>View All Orders</button>
          <button onClick={() => setSuccessOrder(null)}>Place Another</button>
        </div>
      </div>
    );
  }

  return (
    <div>
      <h1 className="page-title">Trade Desk</h1>
      <p className="muted" style={{ marginBottom: "1rem" }}>
        Place an order on behalf of any client account.
      </p>

      <div className="trade-layout">
        <form className="panel trade-form" onSubmit={handleSubmit}>
          <label htmlFor="accountId">Account ID</label>
          <input
            id="accountId"
            type="number"
            placeholder="e.g. 2"
            value={accountId}
            onChange={(e) => setAccountId(e.target.value)}
            required
          />

          <label htmlFor="securityId">Security</label>
          <select
            id="securityId"
            value={securityId}
            onChange={(e) => setSecurityId(e.target.value)}
            required
          >
            <option value="">Select a security...</option>
            {(securities ?? []).map((s) => (
              <option key={s.securityId} value={s.securityId}>
                {s.symbol} — {s.name} ({formatCurrency(s.currentPrice)})
              </option>
            ))}
          </select>

          <div className="side-toggle">
            <button
              type="button"
              className={isBuy ? "side-btn side-btn-buy-active" : "side-btn"}
              onClick={() => setOrderType("BUY")}
            >
              Buy
            </button>
            <button
              type="button"
              className={!isBuy ? "side-btn side-btn-sell-active" : "side-btn"}
              onClick={() => setOrderType("SELL")}
            >
              Sell
            </button>
          </div>

          <label htmlFor="priceType">Order Type</label>
          <select
            id="priceType"
            value={priceType}
            onChange={(e) => setPriceType(e.target.value)}
          >
            <option value="LIMIT">Limit</option>
            <option value="MARKET">Market</option>
          </select>

          {priceType === "LIMIT" && (
            <>
              <label htmlFor="price">Price (₹)</label>
              <input
                id="price"
                type="number"
                step="0.01"
                min="0.01"
                value={price}
                onChange={(e) => setPrice(e.target.value)}
                required
              />
            </>
          )}

          <label htmlFor="quantity">Quantity</label>
          <input
            id="quantity"
            type="number"
            min="1"
            step="1"
            value={quantity}
            onChange={(e) => setQuantity(e.target.value)}
            required
          />

          <div className="order-summary">
            <div className="order-summary-row">
              <span>Total Amount</span>
              <strong>{formatCurrency(totalAmount)}</strong>
            </div>
          </div>

          {error && <div className="error-message">{error}</div>}

          <button
            type="submit"
            className={isBuy ? "submit-buy-btn" : "submit-sell-btn"}
            disabled={isSubmitting || !accountId || !securityId}
          >
            {isSubmitting ? "Placing order..." : `Place ${orderType} Order`}
          </button>
        </form>
      </div>
    </div>
  );
}
