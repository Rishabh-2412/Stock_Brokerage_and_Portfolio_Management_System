// Decorative illustrations for the auth pages' side panel. Built as SVG
// (not raster images) so they stay crisp at any size and match the app's
// indigo brand palette exactly.

// Deterministic pseudo-random OHLC-style data so the chart looks like a
// real, slightly noisy market instead of a perfectly smooth fake trend.
function generateCandles(count) {
  const candles = [];
  let price = 120;
  let seed = 7;
  const rand = () => {
    // simple seeded generator so the chart is stable across re-renders
    seed = (seed * 9301 + 49297) % 233280;
    return seed / 233280;
  };

  for (let i = 0; i < count; i++) {
    const drift = (rand() - 0.42) * 22; // slight upward bias overall
    const open = price;
    const close = Math.max(20, open + drift);
    const high = Math.max(open, close) + rand() * 10;
    const low = Math.min(open, close) - rand() * 10;
    candles.push({ open, close, high, low });
    price = close;
  }
  return candles;
}

export function CandlestickIllustration() {
  const width = 400;
  const height = 560;
  const padding = { top: 60, right: 24, bottom: 60, left: 24 };
  const chartWidth = width - padding.left - padding.right;
  const chartHeight = height - padding.top - padding.bottom;

  const candles = generateCandles(14);
  const allValues = candles.flatMap((c) => [c.high, c.low]);
  const minVal = Math.min(...allValues);
  const maxVal = Math.max(...allValues);
  const range = maxVal - minVal || 1;

  const slotWidth = chartWidth / candles.length;
  const bodyWidth = slotWidth * 0.45;

  const yFor = (value) =>
    padding.top + chartHeight - ((value - minVal) / range) * chartHeight;

  const gridLines = [0, 0.25, 0.5, 0.75, 1].map((t) => padding.top + chartHeight * t);

  return (
    <svg
      viewBox={`0 0 ${width} ${height}`}
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
      className="auth-illustration auth-illustration-chart"
    >
      {/* subtle horizontal grid */}
      {gridLines.map((y, i) => (
        <line key={i} x1={padding.left} y1={y} x2={width - padding.right} y2={y} stroke="#E6E4F5" strokeWidth="1" />
      ))}

      {/* candles */}
      {candles.map((c, i) => {
        const x = padding.left + i * slotWidth + slotWidth / 2;
        const up = c.close >= c.open;
        const color = up ? "#00C48C" : "#E5484D";
        const bodyTop = yFor(Math.max(c.open, c.close));
        const bodyBottom = yFor(Math.min(c.open, c.close));
        const bodyHeight = Math.max(bodyBottom - bodyTop, 3);

        return (
          <g key={i}>
            {/* wick */}
            <line x1={x} y1={yFor(c.high)} x2={x} y2={yFor(c.low)} stroke={color} strokeWidth="2" />
            {/* body */}
            <rect
              x={x - bodyWidth / 2}
              y={bodyTop}
              width={bodyWidth}
              height={bodyHeight}
              rx="2"
              fill={color}
            />
          </g>
        );
      })}
    </svg>
  );
}

export function BarChartIllustration() {
  return (
    <svg viewBox="0 0 400 400" fill="none" xmlns="http://www.w3.org/2000/svg" className="auth-illustration">
      {/* Ground shadow */}
      <ellipse cx="200" cy="330" rx="140" ry="18" fill="#E6E4F5" />

      {/* Ascending bars */}
      <rect x="80" y="260" width="34" height="60" rx="6" fill="#C7C2FF" />
      <rect x="130" y="230" width="34" height="90" rx="6" fill="#9E97FF" />
      <rect x="180" y="195" width="34" height="125" rx="6" fill="#7C71FF" />
      <rect x="230" y="150" width="34" height="170" rx="6" fill="#4A3AFF" />
      <rect x="280" y="110" width="34" height="210" rx="6" fill="#3626E0" />

      {/* Upward trend arrow across the tops of the bars */}
      <polyline
        points="90,250 145,215 195,180 245,135 290,95"
        stroke="#14142B"
        strokeWidth="6"
        fill="none"
        strokeLinecap="round"
        strokeLinejoin="round"
      />
      <path d="M270 90 L300 88 L302 118 Z" fill="#14142B" />
    </svg>
  );
}
