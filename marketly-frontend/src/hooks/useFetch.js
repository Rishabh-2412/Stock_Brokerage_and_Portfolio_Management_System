import { useState, useEffect } from "react";

// A small reusable hook so pages don't each rewrite the same
// "loading / error / data" boilerplate around an API call.
//
// Usage:
//   const { data, isLoading, error, refetch } = useFetch(
//     () => getPortfolio(accountId),
//     [accountId],       // dependencies — refetches when these change
//     !!accountId         // enabled — only runs once accountId is available
//   );
export function useFetch(fetchFn, dependencies = [], enabled = true) {
  const [data, setData] = useState(null);
  const [isLoading, setIsLoading] = useState(enabled);
  const [error, setError] = useState(null);
  const [refetchIndex, setRefetchIndex] = useState(0);

  useEffect(() => {
    if (!enabled) {
      setIsLoading(false);
      return;
    }

    let isCancelled = false;

    async function run() {
      setIsLoading(true);
      setError(null);
      try {
        const result = await fetchFn();
        if (!isCancelled) setData(result);
      } catch (err) {
        if (!isCancelled) setError(err);
      } finally {
        if (!isCancelled) setIsLoading(false);
      }
    }

    run();

    return () => {
      isCancelled = true;
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [...dependencies, enabled, refetchIndex]);

  function refetch() {
    setRefetchIndex((prev) => prev + 1);
  }

  return { data, isLoading, error, refetch };
}
