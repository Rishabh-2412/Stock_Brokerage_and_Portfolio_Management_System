import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { register } from "../../api/authApi";
import PasswordInput from "../../components/PasswordInput";
import { BarChartIllustration } from "../../components/AuthIllustrations";

export default function Register() {
  const [formData, setFormData] = useState({
    fullName: "",
    email: "",
    phone: "",
    username: "",
    password: "",
  });
  const [confirmPassword, setConfirmPassword] = useState("");
  const [error, setError] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);

  const navigate = useNavigate();

  function handleChange(e) {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");

    // Client-side only check — confirmPassword is never sent to the
    // backend, since RegisterRequest doesn't have that field.
    if (formData.password !== confirmPassword) {
      setError("Passwords do not match.");
      return;
    }

    setIsSubmitting(true);
    try {
      await register(formData);
      // After successful registration, send them to login rather than
      // auto-logging in, since register response may not include a token.
      navigate("/login");
    } catch (err) {
      const message =
        err.response?.data?.message || "Registration failed. Please try again.";
      setError(message);
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <div className="auth-page">
      <div className="auth-card auth-card-split auth-card-wide">
        <div className="auth-card-form">
          <h1 className="brand">
            <img src="/marketly-logo.svg" alt="" width="20" height="21" />
            Marketly
          </h1>
          <h2>Create Your Account</h2>
          <p className="subtitle">Join Marketly and start your investment journey</p>

          <form onSubmit={handleSubmit}>
            <label htmlFor="fullName">Full Name</label>
            <input
              id="fullName"
              name="fullName"
              type="text"
              placeholder="Enter full name"
              value={formData.fullName}
              onChange={handleChange}
              required
            />

            <label htmlFor="email">Email Address</label>
            <input
              id="email"
              name="email"
              type="email"
              placeholder="Enter email address"
              value={formData.email}
              onChange={handleChange}
              required
            />

            <label htmlFor="phone">Phone Number</label>
            <input
              id="phone"
              name="phone"
              type="tel"
              placeholder="Enter phone number"
              value={formData.phone}
              onChange={handleChange}
            />

            <label htmlFor="username">Username</label>
            <input
              id="username"
              name="username"
              type="text"
              placeholder="Choose a username"
              value={formData.username}
              onChange={handleChange}
              required
            />

            <label htmlFor="password">Password</label>
            <PasswordInput
              id="password"
              name="password"
              placeholder="Choose a password"
              value={formData.password}
              onChange={handleChange}
              autoComplete="new-password"
              required
            />

            <label htmlFor="confirmPassword">Confirm Password</label>
            <PasswordInput
              id="confirmPassword"
              name="confirmPassword"
              placeholder="Re-enter your password"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              autoComplete="new-password"
              required
            />

            {error && <div className="error-message">{error}</div>}

            <button type="submit" disabled={isSubmitting}>
              {isSubmitting ? "Creating account..." : "Create Account"}
            </button>
          </form>

          <p className="auth-footer">
            Already have an account? <Link to="/login">Login</Link>
          </p>
        </div>

        <div className="auth-card-visual">
          <BarChartIllustration />
        </div>
      </div>
    </div>
  );
}
