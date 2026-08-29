import { useState } from "react";
import { useParams, useSearchParams, useNavigate, Link } from "react-router-dom";
import { useAccount } from "../../context/AccountContext";
import { useFetch } from "../../hooks/useFetch";
import { getSecurityById } from "../../api/securityApi";
import { placeOrder } from "../../api/orderApi";
import { LoadingState, ErrorState } from "../../components/StatusStates";
import { formatCurrency } from "../../utils/format";

export default function TradeOrder() {
  const { securityId } = useParams();
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const { accountId } = useAccount();

  // "BUY" or "SELL" — comes from the link on the Stock Details page.
  const initialSide = searchParams.get("side") === "SELL" ? "SELL" : "BUY";

  const [orderType, setOrderType] = useState(initialSide); // BUY | SELL
  const [priceType, setPriceType] = useState("LIMIT"); // MARKET | LIMIT
  const [quantity, setQuantity] = useState(1);
  const [price, setPrice] = useState("");
  const [error, setError] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [successOrder, setSuccessOrder] = useState(null);

  const {
    data: security,
    isLoading,
    error: loadError,
  } = useFetch(() => getSecurityById(securityId), [securityId], !!securityId);

  if (isLoading) return <LoadingState label="Loading security..." />;
  if (loadError) return <ErrorState error={loadError} />;

  const isBuy = orderType === "BUY";
  const effectivePrice =
    priceType === "MARKET" ? security.currentPrice : Number(price) || 0;
  const totalAmount = effectivePrice * Number(quantity || 0);

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");
    setIsSubmitting(true);

    // Matches PlaceOrderRequest exactly. price is omitted for MARKET orders
    // since the backend ignores it there anyway.
    const orderData = {
      accountId,
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
      const message =
        err.response?.data?.message || "Failed to place order. Please try again.";
      setError(message);
    } finally {
      setIsSubmitting(false);
    }
  }

  if (successOrder) {
    return (
      <div className="panel order-success">
        <h2>Order Placed ✅</h2>
        <p className="muted">
          {successOrder.orderType} order for {successOrder.quantity} share(s) of{" "}
          {successOrder.symbol} is now {successOrder.orderStatus}.
        </p>
        <div className="order-success-actions">
          <button onClick={() => navigate("/orders")}>View Orders</button>
          <button onClick={() => navigate(`/stocks/${securityId}`)}>
            Back to Stock
          </button>
        </div>
      </div>
    );
  }

  return (
    <div>
      <Link to={`/stocks/${securityId}`} className="back-link">
        ← Back to {security.symbol}
      </Link>

      <div className="trade-layout">
        <form className="panel trade-form" onSubmit={handleSubmit}>
          <h2 className={isBuy ? "text-green" : "text-red"}>
            {orderType} {security.symbol}
          </h2>
          <p className="muted">
            {security.exchange} • {formatCurrency(security.currentPrice)}
          </p>

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
                placeholder={`e.g. ${security.currentPrice}`}
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
            disabled={isSubmitting}
          >
            {isSubmitting ? "Placing order..." : `Review ${orderType}`}
          </button>
        </form>
      </div>
    </div>
  );
}
