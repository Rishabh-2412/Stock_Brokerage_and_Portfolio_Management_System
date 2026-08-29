import { useFetch } from "../../hooks/useFetch";
import { getAllOrders } from "../../api/orderApi";
import { LoadingState, ErrorState } from "../../components/StatusStates";
import { formatCurrency } from "../../utils/format";

export default function AllOrdersOverview() {
  const { data, isLoading, error, refetch } = useFetch(() => getAllOrders(), []);
  const orders = data ?? [];

  return (
    <div>
      <h1 className="page-title">All Orders</h1>

      <div className="panel">
        {isLoading && <LoadingState label="Loading orders..." />}
        {error && <ErrorState error={error} onRetry={refetch} />}

        {!isLoading && !error && orders.length === 0 && (
          <p className="muted">No orders found.</p>
        )}

        {!isLoading && orders.length > 0 && (
          <table className="simple-table">
            <thead>
              <tr>
                <th>Account #</th>
                <th>Symbol</th>
                <th>Type</th>
                <th>Price Type</th>
                <th>Qty</th>
                <th>Filled</th>
                <th>Price</th>
                <th>Status</th>
                <th>Placed</th>
              </tr>
            </thead>
            <tbody>
              {orders.map((order) => (
                <tr key={order.orderId}>
                  <td>{order.accountId}</td>
                  <td>{order.symbol}</td>
                  <td className={order.orderType === "BUY" ? "text-green" : "text-red"}>
                    {order.orderType}
                  </td>
                  <td>{order.priceType}</td>
                  <td>{order.quantity}</td>
                  <td>{order.filledQuantity}</td>
                  <td>{formatCurrency(order.price)}</td>
                  <td>{order.orderStatus}</td>
                  <td>{new Date(order.createdAt).toLocaleString()}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}
