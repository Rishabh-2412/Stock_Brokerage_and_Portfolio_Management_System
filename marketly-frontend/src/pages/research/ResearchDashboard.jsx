import {
  ResponsiveContainer,
  PieChart,
  Pie,
  Cell,
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
} from "recharts";
import { useAuth } from "../../context/AuthContext";
import { useFetch } from "../../hooks/useFetch";
import { getAllResearchNotes } from "../../api/researchNoteApi";
import { LoadingState, ErrorState } from "../../components/StatusStates";
import { countByDay } from "../../utils/dashboardHelpers";

// Authors are arbitrary usernames, not a fixed enum like Role/OrderStatus,
// so colors are assigned by cycling through this palette rather than a
// fixed name->color map.
const AUTHOR_PALETTE = ["#2f3ee0", "#059669", "#db2777", "#d97706", "#7c3aed", "#0891b2", "#dc2626", "#0d9488"];

/**
 * RESEARCH_ANALYST has no backend access to /accounts, /orders,
 * /transactions or /users (all ADMIN/DEALER/COMPLIANCE_OFFICER/
 * RISK_MANAGER-only) - so unlike Admin/OversightDashboard, none of that
 * data is available here. What this role DOES have is full read access to
 * research notes (GET /research-notes is open to any logged-in user) plus
 * write access to their own - so this dashboard is built entirely from that.
 */
export default function ResearchDashboard() {
  const { user } = useAuth();
  const notes = useFetch(() => getAllResearchNotes(), []);

  if (notes.isLoading) return <LoadingState label="Loading research notes..." />;
  if (notes.error) return <ErrorState error={notes.error} onRetry={notes.refetch} />;

  const noteList = notes.data ?? [];
  const myNotes = noteList.filter((n) => n.authorId === user?.userId);

  const authorData = countByAuthor(noteList);
  const notesTrend = countByDay(noteList, "createdAt");
  const uniqueAuthors = new Set(noteList.map((n) => n.authorName)).size;

  const recentNotes = [...noteList]
    .sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
    .slice(0, 6);

  return (
    <div>
      <h1 className="page-title">Research Dashboard</h1>

      <div className="stat-cards">
        <StatCard label="My Notes Published" value={myNotes.length} />
        <StatCard label="Total Notes (Platform)" value={noteList.length} />
        <StatCard label="Contributing Analysts" value={uniqueAuthors} />
      </div>

      <div className="dashboard-grid">
        <div className="panel chart-panel">
          <h3>Notes by Author</h3>
          {authorData.length === 0 ? (
            <p className="muted">No research notes yet.</p>
          ) : (
            <ResponsiveContainer width="100%" height={240}>
              <PieChart>
                <Pie data={authorData} dataKey="value" nameKey="name" innerRadius={45} outerRadius={80}>
                  {authorData.map((entry) => (
                    <Cell key={entry.name} fill={entry.color} />
                  ))}
                </Pie>
                <Tooltip />
                <Legend />
              </PieChart>
            </ResponsiveContainer>
          )}
        </div>

        <div className="panel chart-panel">
          <h3>Notes Published Over Time</h3>
          {notesTrend.length === 0 ? (
            <p className="muted">No research notes yet.</p>
          ) : (
            <ResponsiveContainer width="100%" height={240}>
              <BarChart data={notesTrend}>
                <CartesianGrid strokeDasharray="3 3" stroke="var(--border-color)" />
                <XAxis dataKey="date" tick={{ fontSize: 11 }} />
                <YAxis allowDecimals={false} tick={{ fontSize: 12 }} />
                <Tooltip />
                <Bar dataKey="value" name="Notes" fill="#0d9488" isAnimationActive={false} />
              </BarChart>
            </ResponsiveContainer>
          )}
        </div>

        <div className="panel chart-panel-wide">
          <h3>Recent Research Notes (All Analysts)</h3>
          {recentNotes.length === 0 ? (
            <p className="muted">No research notes yet.</p>
          ) : (
            <table className="simple-table">
              <thead>
                <tr>
                  <th>Title</th>
                  <th>Symbol</th>
                  <th>Author</th>
                  <th>Posted</th>
                </tr>
              </thead>
              <tbody>
                {recentNotes.map((note) => (
                  <tr key={note.noteId}>
                    <td>{note.title}</td>
                    <td>{note.symbol ?? "General"}</td>
                    <td>{note.authorName}</td>
                    <td>{note.createdAt ? String(note.createdAt).slice(0, 10) : "—"}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      </div>
    </div>
  );
}

function StatCard({ label, value }) {
  return (
    <div className="stat-card">
      <div className="stat-label">{label}</div>
      <div className="stat-value">{value}</div>
    </div>
  );
}

function countByAuthor(notes) {
  const counts = {};
  for (const note of notes) {
    const key = note.authorName ?? "Unknown";
    counts[key] = (counts[key] ?? 0) + 1;
  }
  return Object.entries(counts).map(([name, value], i) => ({
    name,
    value,
    color: AUTHOR_PALETTE[i % AUTHOR_PALETTE.length],
  }));
}
