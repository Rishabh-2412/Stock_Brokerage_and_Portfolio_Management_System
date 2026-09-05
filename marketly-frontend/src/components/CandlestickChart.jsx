import {
  ResponsiveContainer,
  ComposedChart,
  BarChart,
  Bar,
  Cell,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
} from "recharts";
import { formatCurrency } from "../utils/format";

const UP_COLOR = "#16a34a";
const DOWN_COLOR = "#dc2626";

/**
 * Custom shape for the Bar. Recharts hands us the pixel box (x, y, width,
 * height) that corresponds to the [low, high] value range we gave the Bar's
 * dataKey — NOT open/close. So we re-derive open/close pixel positions
 * ourselves using that box, since price and pixels scale linearly within it.
 */
function Candle(props) {
  const { x, y, width, height, payload } = props;
  const { open, close, high, low } = payload;

  if (high === low || !Number.isFinite(height) || height <= 0) return null;

  const isUp = close >= open;
  const color = isUp ? UP_COLOR : DOWN_COLOR;

  // price -> pixel: at the top of the box (y) the price is `high`,
  // at the bottom (y + height) the price is `low`.
  const pixelsPerUnit = height / (high - low);
  const openY = y + (high - open) * pixelsPerUnit;
  const closeY = y + (high - close) * pixelsPerUnit;

  const bodyTop = Math.min(openY, closeY);
  const bodyHeight = Math.max(Math.abs(closeY - openY), 1.5); // keep doji candles visible
  const bodyWidth = Math.max(width * 0.55, 2);
  const bodyX = x + (width - bodyWidth) / 2;
  const wickX = x + width / 2;

  return (
    <g>
      {/* wick: the full high-low range as a thin line */}
      <line x1={wickX} x2={wickX} y1={y} y2={y + height} stroke={color} strokeWidth={1.4} />
      {/* body: the open-close range as a filled rectangle */}
      <rect x={bodyX} y={bodyTop} width={bodyWidth} height={bodyHeight} fill={color} rx={1} />
    </g>
  );
}

function CandleTooltip({ active, payload, label }) {
  if (!active || !payload || !payload.length) return null;
  const d = payload[0].payload;
  const isUp = d.close >= d.open;
  return (
    <div className="candle-tooltip">
      <div className="candle-tooltip-date">{label}</div>
      <div className={isUp ? "candle-tooltip-up" : "candle-tooltip-down"}>
        {isUp ? "▲" : "▼"} {formatCurrency(d.close)}
      </div>
      <div className="candle-tooltip-row"><span>Open</span><span>{formatCurrency(d.open)}</span></div>
      <div className="candle-tooltip-row"><span>High</span><span>{formatCurrency(d.high)}</span></div>
      <div className="candle-tooltip-row"><span>Low</span><span>{formatCurrency(d.low)}</span></div>
      <div className="candle-tooltip-row"><span>Close</span><span>{formatCurrency(d.close)}</span></div>
      {d.volume != null && (
        <div className="candle-tooltip-row"><span>Volume</span><span>{d.volume.toLocaleString()}</span></div>
      )}
    </div>
  );
}

/**
 * data: array of { date, open, high, low, close, volume }, oldest first.
 * Renders a candlestick price chart, plus a compact volume histogram
 * underneath (colored to match that day's candle) if volume data exists.
 */
export default function CandlestickChart({ data, height = 320 }) {
  const chartData = data.map((d) => ({ ...d, range: [d.low, d.high] }));
  const hasVolume = chartData.some((d) => d.volume != null);

  return (
    <div>
      <ResponsiveContainer width="100%" height={height}>
        <ComposedChart data={chartData} margin={{ top: 10, right: 10, left: 0, bottom: 0 }}>
          <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0" />
          <XAxis dataKey="date" tick={{ fontSize: 11 }} />
          <YAxis
            tick={{ fontSize: 12 }}
            domain={["auto", "auto"]}
            tickFormatter={(value) => `₹${value}`}
            width={70}
          />
          <Tooltip content={<CandleTooltip />} />
          <Bar dataKey="range" shape={Candle} isAnimationActive={false} />
        </ComposedChart>
      </ResponsiveContainer>

      {hasVolume && (
        <ResponsiveContainer width="100%" height={70}>
          <BarChart data={chartData} margin={{ top: 0, right: 10, left: 0, bottom: 0 }}>
            <XAxis dataKey="date" hide />
            <YAxis hide domain={["auto", "auto"]} width={70} />
            <Bar dataKey="volume" isAnimationActive={false}>
              {chartData.map((d, i) => (
                <Cell key={i} fill={d.close >= d.open ? UP_COLOR : DOWN_COLOR} fillOpacity={0.35} />
              ))}
            </Bar>
          </BarChart>
        </ResponsiveContainer>
      )}
    </div>
  );
}
