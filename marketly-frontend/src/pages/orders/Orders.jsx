import { useAccount } from "../../context/AccountContext";
import { useFetch } from "../../hooks/useFetch";
import { getOrdersForAccount, cancelOrder } from "../../api/orderApi";
import { executeOrder } from "../../api/transactionApi";
import { LoadingState, ErrorState } from "../../components/StatusStates";
import { formatCurrency } from "../../utils/format";
import { useState } from "react";

export default function Orders() {
  const { accountId, isLoading: accountLoading } = useAccount();
  const [cancellingId, setCancellingId] = useState(null);
  const [executingId, setExecutingId] = useState(null);

  const { data, isLoading, error, refetch } = useFetch(
    () => getOrdersForAccount(accountId),
    [accountId],
    !!accountId
  );

  if (accountLoading || isLoading) return <LoadingState label="Loading orders..." />;
  if (error) return <ErrorState error={error} onRetry={refetch} />;

  const orders = data ?? [];

  async function handleCancel(orderId) {
    setCancellingId(orderId);
    try {
      await cancelOrder(orderId);
      refetch();
    } catch (err) {
      alert(err.response?.data?.message || "Failed to cancel order.");
    } finally {
      setCancellingId(null);
    }
  }

  // Orders are placed as PENDING and don't fill automatically — this
  // simulates a broker/exchange filling the order (confirmed from
  // backend source: POST /transactions/execute/{orderId}).
  async function handleExecute(orderId) {
    setExecutingId(orderId);
    try {
      await executeOrder(orderId);
      refetch();
    } catch (err) {
      alert(err.response?.data?.message || "Failed to execute order.");
    } finally {
      setExecutingId(null);
    }
  }

  return (
    <div>
      <h1 className="page-title">Orders</h1>

      <div className="panel">
        {orders.length === 0 ? (
          <p className="muted">You haven't placed any orders yet.</p>
        ) : (
          <table className="simple-table">
            <thead>
              <tr>
                <th>Symbol</th>
                <th>Type</th>
                <th>Qty</th>
                <th>Filled</th>
                <th>Price</th>
                <th>Status</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {orders.map((order) => (
                <tr key={order.orderId}>
                  <td>{order.symbol}</td>
                  <td className={order.orderType === "BUY" ? "text-green" : "text-red"}>
                    {order.orderType}
                  </td>
                  <td>{order.quantity}</td>
                  <td>{order.filledQuantity}</td>
                  <td>{formatCurrency(order.price)}</td>
                  <td>{order.orderStatus}</td>
                  <td>
                    {order.orderStatus === "PENDING" && (
                      <div className="order-row-actions">
                        <button
                          className="link-btn"
                          disabled={executingId === order.orderId}
                          onClick={() => handleExecute(order.orderId)}
                        >
                          {executingId === order.orderId ? "Executing..." : "Execute"}
                        </button>
                        <button
                          className="link-btn link-btn-danger"
                          disabled={cancellingId === order.orderId}
                          onClick={() => handleCancel(order.orderId)}
                        >
                          {cancellingId === order.orderId ? "Cancelling..." : "Cancel"}
                        </button>
                      </div>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}
