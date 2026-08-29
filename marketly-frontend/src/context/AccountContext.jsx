import { createContext, useContext, useState, useEffect } from "react";
import { getMyAccount } from "../api/accountApi";
import { useAuth } from "./AuthContext";

// Almost every page (Portfolio, Orders, Watchlist, Dashboard) needs
// accountId to call its endpoint. Rather than each page fetching
// /accounts/me separately, we fetch it once here and share it.
const AccountContext = createContext(null);

export function AccountProvider({ children }) {
  const { isAuthenticated } = useAuth();
  const [account, setAccount] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);

  const [refetchIndex, setRefetchIndex] = useState(0);

  useEffect(() => {
    if (!isAuthenticated) {
      setAccount(null);
      setIsLoading(false);
      return;
    }

    let isCancelled = false;

    async function fetchAccount() {
      setIsLoading(true);
      setError(null);
      try {
        const data = await getMyAccount();
        if (!isCancelled) {
          if (!data) {
            setError({ message: "No trading account found for this user." });
          } else {
            setAccount(data);
          }
        }
      } catch (err) {
        if (!isCancelled) setError(err);
      } finally {
        if (!isCancelled) setIsLoading(false);
      }
    }

    fetchAccount();

    return () => {
      isCancelled = true;
    };
  }, [isAuthenticated, refetchIndex]);

  function refetch() {
    setRefetchIndex((prev) => prev + 1);
  }

  const value = {
    account,
    accountId: account?.accountId ?? null,
    isLoading,
    error,
    refetch,
  };

  return (
    <AccountContext.Provider value={value}>{children}</AccountContext.Provider>
  );
}

export function useAccount() {
  const context = useContext(AccountContext);
  if (!context) {
    throw new Error("useAccount must be used inside an <AccountProvider>");
  }
  return context;
}
