import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import PasswordInput from "../../components/PasswordInput";
import { CandlestickIllustration } from "../../components/AuthIllustrations";

export default function Login() {
  const [emailOrUsername, setEmailOrUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);

  const { login } = useAuth();
  const navigate = useNavigate();

  async function handleSubmit(e) {
    e.preventDefault(); // stop the browser from reloading the page
    setError("");
    setIsSubmitting(true);

    try {
      await login(emailOrUsername, password);
      navigate("/dashboard");
    } catch (err) {
      // err.response is the axios error response from the backend, if any
      const message =
        err.response?.data?.message || "Login failed. Check your credentials.";
      setError(message);
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <div className="auth-page">
      <div className="auth-card auth-card-split">
        <div className="auth-card-form">
          <h1 className="brand">
            <img src="/marketly-logo.svg" alt="" width="20" height="21" />
            Marketly
          </h1>
          <h2>Welcome Back!</h2>
          <p className="subtitle">Login to your account</p>

          <form onSubmit={handleSubmit}>
            <label htmlFor="emailOrUsername">Email or Username</label>
            <input
              id="emailOrUsername"
              type="text"
              placeholder="Enter email or username"
              value={emailOrUsername}
              onChange={(e) => setEmailOrUsername(e.target.value)}
              required
            />

            <label htmlFor="password">Password</label>
            <PasswordInput
              id="password"
              placeholder="Enter your password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              autoComplete="current-password"
              required
            />

            {error && <div className="error-message">{error}</div>}

            <button type="submit" disabled={isSubmitting}>
              {isSubmitting ? "Logging in..." : "Login"}
            </button>
          </form>

          <p className="auth-footer">
            Don't have an account? <Link to="/register">Register</Link>
          </p>
        </div>

        <div className="auth-card-visual auth-card-visual-chart">
          <CandlestickIllustration />
        </div>
      </div>
    </div>
  );
}
