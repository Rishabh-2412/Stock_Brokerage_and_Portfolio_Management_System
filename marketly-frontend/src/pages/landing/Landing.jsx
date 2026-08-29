import { Link } from "react-router-dom";
import { useEffect, useState } from "react";
import "./Landing.css";

// Live-look ticker values — purely decorative on the landing page (no API
// call here; this is marketing surface, not the authenticated app).
const TICKER = [
  { symbol: "RELIANCE", price: "2,736.80", change: "+1.08%", up: true },
  { symbol: "TCS", price: "3,568.95", change: "+1.34%", up: true },
  { symbol: "HDFCBANK", price: "1,630.45", change: "-0.75%", up: false },
  { symbol: "INFY", price: "1,366.15", change: "-0.22%", up: false },
  { symbol: "ICICIBANK", price: "1,030.20", change: "+0.43%", up: true },
];

const FEATURES = [
  {
    title: "Real portfolio tracking",
    body: "See your holdings, cost basis, and unrealized P&L update the moment a trade fills — not an end-of-day snapshot.",
  },
  {
    title: "Built for limit and market orders",
    body: "Place, review, and cancel orders with the same order types professional desks use.",
  },
  {
    title: "Watchlists that mean something",
    body: "Track the names you care about and jump straight from a quote to a trade.",
  },
  {
    title: "Research from real analysts",
    body: "Read notes published by our research desk before you decide, not after.",
  },
];

export default function Landing() {
  return (
    <div className="landing">
      <LandingNav />
      <Hero />
      <TrustStrip />
      <Features />
      <StatBand />
      <FinalCta />
      <LandingFooter />
    </div>
  );
}

function LandingNav() {
  return (
    <header className="l-nav">
      <div className="l-nav-inner">
        <div className="l-brand">
          <img src="/marketly-logo.svg" alt="" width="28" height="29" />
          <span>Marketly</span>
        </div>
        <nav className="l-nav-links">
          <a href="#features">Features</a>
          <a href="#trust">Exchanges</a>
        </nav>
        <div className="l-nav-actions">
          <Link to="/login" className="l-btn-ghost">
            Log in
          </Link>
          <Link to="/register" className="l-btn-primary">
            Get started
          </Link>
        </div>
      </div>
    </header>
  );
}

function Hero() {
  const [tickIndex, setTickIndex] = useState(0);

  useEffect(() => {
    const id = setInterval(() => {
      setTickIndex((i) => (i + 1) % TICKER.length);
    }, 2200);
    return () => clearInterval(id);
  }, []);

  return (
    <section className="l-hero">
      <div className="l-hero-inner">
        <div className="l-hero-copy">
          <div className="l-eyebrow">Built on real market data</div>
          <h1>
            Investing that
            <br />
            reads like <span className="l-accent">a clean chart</span>,
            <br />
            not a spreadsheet.
          </h1>
          <p className="l-hero-sub">
            Marketly brings your portfolio, orders, and research into one
            place — priced in rupees, built for the way Indian investors
            actually trade.
          </p>
          <div className="l-hero-actions">
            <Link to="/register" className="l-btn-primary l-btn-lg">
              Open a free account
            </Link>
            <Link to="/login" className="l-btn-ghost l-btn-lg">
              I already have one
            </Link>
          </div>
          <div className="l-hero-live">
            <span className="l-live-dot" />
            Live quote — {TICKER[tickIndex].symbol}
            <strong>₹{TICKER[tickIndex].price}</strong>
            <span className={TICKER[tickIndex].up ? "l-up" : "l-down"}>
              {TICKER[tickIndex].change}
            </span>
          </div>
        </div>

        <div className="l-hero-visual" aria-hidden="true">
          <BarsMark />
          <div className="l-ticker-card">
            {TICKER.map((t, i) => (
              <div
                key={t.symbol}
                className={i === tickIndex ? "l-ticker-row l-ticker-row-active" : "l-ticker-row"}
              >
                <span className="l-ticker-symbol">{t.symbol}</span>
                <span className="l-ticker-price">₹{t.price}</span>
                <span className={t.up ? "l-up" : "l-down"}>{t.change}</span>
              </div>
            ))}
          </div>
        </div>
      </div>
    </section>
  );
}

// The logo is four ascending bars — this echoes that exact geometry as a
// full-size ambient mark behind the ticker card, rather than a generic
// stock-photo candlestick illustration.
function BarsMark() {
  return (
    <svg
      className="l-bars-mark"
      viewBox="0 0 380 390"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
    >
      <rect y="231" width="97" height="151" rx="48" fill="#4A3AFF" opacity="0.12">
        <animate attributeName="height" values="151;190;151" dur="3.2s" repeatCount="indefinite" />
        <animate attributeName="y" values="231;192;231" dur="3.2s" repeatCount="indefinite" />
      </rect>
      <rect width="97" height="97" rx="48" fill="#9E97FF" opacity="0.18" />
      <rect x="141" y="112" width="97" height="270" rx="48" fill="#4A3AFF" opacity="0.14">
        <animate attributeName="height" values="270;310;270" dur="3.2s" begin="0.3s" repeatCount="indefinite" />
        <animate attributeName="y" values="112;72;112" dur="3.2s" begin="0.3s" repeatCount="indefinite" />
      </rect>
      <rect x="283" width="97" height="382" rx="48" fill="#4A3AFF" opacity="0.16" />
    </svg>
  );
}

function TrustStrip() {
  const items = ["NSE", "BSE", "CDSL", "NSDL", "SEBI Registered"];
  return (
    <section id="trust" className="l-trust">
      <p className="l-trust-label">Trusted infrastructure across</p>
      <div className="l-trust-row">
        {items.map((item) => (
          <span key={item} className="l-trust-item">
            {item}
          </span>
        ))}
      </div>
    </section>
  );
}

function Features() {
  return (
    <section id="features" className="l-features">
      <div className="l-section-head">
        <div className="l-eyebrow">What you get</div>
        <h2>Everything between a quote and a filled order.</h2>
      </div>
      <div className="l-feature-grid">
        {FEATURES.map((f) => (
          <div key={f.title} className="l-feature-card">
            <h3>{f.title}</h3>
            <p>{f.body}</p>
          </div>
        ))}
      </div>
    </section>
  );
}

function StatBand() {
  return (
    <section className="l-stats">
      <div className="l-stats-inner">
        <div className="l-stat">
          <div className="l-stat-num">₹4.5L Cr+</div>
          <div className="l-stat-label">in assets tracked</div>
        </div>
        <div className="l-stat">
          <div className="l-stat-num">50,000+</div>
          <div className="l-stat-label">active investors</div>
        </div>
        <div className="l-stat">
          <div className="l-stat-num">99.98%</div>
          <div className="l-stat-label">order execution uptime</div>
        </div>
      </div>
    </section>
  );
}

function FinalCta() {
  return (
    <section className="l-final-cta">
      <h2>Your portfolio, one login away.</h2>
      <p>No paperwork today — open your account in under two minutes.</p>
      <Link to="/register" className="l-btn-primary l-btn-lg">
        Create your account
      </Link>
    </section>
  );
}

function LandingFooter() {
  return (
    <footer className="l-footer">
      <div className="l-brand l-brand-footer">
        <img src="/marketly-logo.svg" alt="" width="22" height="23" />
        <span>Marketly</span>
      </div>
      <p>© {new Date().getFullYear()} Marketly. All prices are illustrative.</p>
    </footer>
  );
}
