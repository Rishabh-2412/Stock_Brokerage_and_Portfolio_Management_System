import apiClient from "./client";

// startDate/endDate are optional JS Date objects or "YYYY-MM-DD" strings.
// Provide both to filter, or omit both for the full history.
export async function getPriceHistory(securityId, startDate, endDate) {
  const params = {};
  if (startDate) params.startDate = toIsoDate(startDate);
  if (endDate) params.endDate = toIsoDate(endDate);

  const response = await apiClient.get(`/market/price-history/${securityId}`, {
    params,
  });
  return response.data; // array of PriceHistoryDTO
}

function toIsoDate(date) {
  if (typeof date === "string") return date;
  return date.toISOString().split("T")[0]; // "YYYY-MM-DD"
}

// ---- ADMIN only ----

export async function addPriceHistoryRecord(record) {
  // All fields required and actually read by the backend (unlike some
  // other endpoints in this API, this DTO has no unused/ignored fields
  // on add besides historyId/symbol).
  const response = await apiClient.post("/market/price-history", {
    securityId: Number(record.securityId),
    openPrice: Number(record.openPrice),
    highPrice: Number(record.highPrice),
    lowPrice: Number(record.lowPrice),
    closePrice: Number(record.closePrice),
    volume: Number(record.volume),
    date: toIsoDate(record.date),
  });
  return response.data;
}
