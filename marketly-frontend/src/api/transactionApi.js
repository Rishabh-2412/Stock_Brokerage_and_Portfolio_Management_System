import apiClient from "./client";

// All filters optional. Omit all three for the full unfiltered history.
export async function getTransactionsForAccount(accountId, filters = {}) {
  const params = {};
  if (filters.type) params.type = filters.type; // "BUY" | "SELL" | "DIVIDEND"
  if (filters.startDate) params.startDate = toIsoDate(filters.startDate);
  if (filters.endDate) params.endDate = toIsoDate(filters.endDate);

  const response = await apiClient.get(`/transactions/account/${accountId}`, {
    params,
  });
  return response.data; // array of TransactionDTO
}

export async function getTransactionById(transactionId) {
  const response = await apiClient.get(`/transactions/${transactionId}`);
  return response.data;
}

// ADMIN/DEALER/COMPLIANCE_OFFICER/RISK_MANAGER only.
export async function getAllTransactions() {
  const response = await apiClient.get("/transactions");
  return response.data;
}

// Executes a PENDING order: fills it, creates a Transaction, updates
// Holdings and Account cash. Takes no request body.
export async function executeOrder(orderId) {
  const response = await apiClient.post(`/transactions/execute/${orderId}`);
  return response.data; // TransactionDTO
}

function toIsoDate(date) {
  if (typeof date === "string") return date;
  return date.toISOString().split("T")[0];
}
