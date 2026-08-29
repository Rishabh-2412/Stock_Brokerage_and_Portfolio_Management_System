import apiClient from "./client";

export async function getAllSecurities() {
  const response = await apiClient.get("/securities");
  return response.data; // array of SecurityDTO
}

export async function getSecurityById(securityId) {
  const response = await apiClient.get(`/securities/${securityId}`);
  return response.data;
}

export async function getSecurityBySymbol(symbol) {
  const response = await apiClient.get(`/securities/symbol/${symbol}`);
  return response.data;
}

// ---- ADMIN only ----

export async function createSecurity(securityData) {
  // symbol/name/currentPrice required; exchange/sector/marketCap optional.
  const response = await apiClient.post("/securities", {
    symbol: securityData.symbol,
    name: securityData.name,
    exchange: securityData.exchange || undefined,
    sector: securityData.sector || undefined,
    currentPrice: securityData.currentPrice,
    marketCap: securityData.marketCap || undefined,
  });
  return response.data;
}

export async function updateSecurityPrice(security, newPrice, newMarketCap) {
  // IMPORTANT: like the account-status endpoint, this only READS
  // currentPrice/marketCap, but @Valid still validates the whole DTO —
  // symbol/name have @NotBlank. So we send the security's existing
  // symbol/name back too, or the request gets rejected before the price
  // update logic ever runs.
  const response = await apiClient.put(`/securities/${security.securityId}/price`, {
    symbol: security.symbol,
    name: security.name,
    currentPrice: newPrice,
    marketCap: newMarketCap ?? security.marketCap ?? undefined,
  });
  return response.data;
}
