import apiClient from "./client";

export async function getWatchlist(accountId) {
  const response = await apiClient.get(`/watchlist/account/${accountId}`);
  return response.data; // expected: array of watchlist entries
}

export async function addToWatchlist(accountId, securityId) {
  const response = await apiClient.post("/watchlist", { accountId, securityId });
  return response.data;
}

export async function removeFromWatchlist(watchlistId) {
  const response = await apiClient.delete(`/watchlist/${watchlistId}`);
  return response.data;
}
