import apiClient from "./client";

export async function getOrdersForAccount(accountId) {
  const response = await apiClient.get(`/orders/account/${accountId}`);
  return response.data; // expected: array of orders
}

// ADMIN/DEALER/COMPLIANCE_OFFICER/RISK_MANAGER only.
export async function getAllOrders() {
  const response = await apiClient.get("/orders");
  return response.data;
}

export async function placeOrder(orderData) {
  // orderData shape matches PlaceOrderRequest DTO exactly (confirmed from backend source):
  // { accountId, securityId, orderType: "BUY"|"SELL", priceType: "MARKET"|"LIMIT",
  //   quantity, price }  -- price only required/used when priceType is "LIMIT"
  const response = await apiClient.post("/orders", orderData);
  return response.data;
}

export async function cancelOrder(orderId) {
  const response = await apiClient.post(`/orders/${orderId}/cancel`);
  return response.data;
}

export async function getOrderById(orderId) {
  const response = await apiClient.get(`/orders/${orderId}`);
  return response.data;
}
