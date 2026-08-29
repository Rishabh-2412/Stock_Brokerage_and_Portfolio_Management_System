import apiClient from "./client";

// GET /accounts/me returns an ARRAY of accounts (a user can have more than
// one — e.g. multiple CASH accounts, or a suspended one alongside an
// active one). We pick the best one to use as "the" account for the app:
// prefer ACTIVE, otherwise fall back to the first one so the app still works.
export async function getMyAccounts() {
  const response = await apiClient.get("/accounts/me");
  return response.data; // array of accounts
}

export async function getMyAccount() {
  const accounts = await getMyAccounts();

  if (!Array.isArray(accounts) || accounts.length === 0) {
    return null;
  }

  const activeAccount = accounts.find((acc) => acc.status === "ACTIVE");
  return activeAccount ?? accounts[0];
}

// Opens a new trading account for the LOGGED-IN user (no userId in the
// request — the backend infers it from the JWT). Only accountType and
// balance are read; balance is treated as the initial deposit.
export async function createAccount(accountType, initialDeposit) {
  const response = await apiClient.post("/accounts", {
    accountType,
    balance: initialDeposit,
  });
  return response.data;
}

export async function getAllAccounts() {
  const response = await apiClient.get("/accounts");
  return response.data; // array of AccountDTO
}

// ---- ADMIN only ----

export async function updateAccountStatus(account, newStatus) {
  // IMPORTANT: the backend's PUT /accounts/{id}/status endpoint only READS
  // the `status` field — but it validates the WHOLE AccountDTO with @Valid,
  // and accountType/balance are @NotNull on that DTO. So we must send the
  // full account object (with its existing accountType/balance) or the
  // request gets rejected with a 400 before status is ever looked at.
  const response = await apiClient.put(`/accounts/${account.accountId}/status`, {
    accountType: account.accountType,
    balance: account.balance,
    status: newStatus,
  });
  return response.data;
}
