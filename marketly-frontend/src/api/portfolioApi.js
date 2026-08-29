import apiClient from "./client";

export async function getPortfolio(accountId) {
  const response = await apiClient.get(`/portfolio/account/${accountId}`);
  return response.data; // expected: holdings array + summary (invested, currentValue, pnl, etc.)
}
