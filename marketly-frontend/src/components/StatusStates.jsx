export function LoadingState({ label = "Loading..." }) {
  return <p className="muted">{label}</p>;
}

export function ErrorState({ error, onRetry }) {
  const message =
    error?.response?.data?.message || error?.message || "Something went wrong.";
  return (
    <div className="error-message">
      {message}
      {onRetry && (
        <button className="retry-btn" onClick={onRetry}>
          Retry
        </button>
      )}
    </div>
  );
}
