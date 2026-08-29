import { useState } from "react";
import { useAuth } from "../../context/AuthContext";
import { useFetch } from "../../hooks/useFetch";
import { getAllResearchNotes, createResearchNote } from "../../api/researchNoteApi";
import { getAllSecurities } from "../../api/securityApi";
import { LoadingState, ErrorState } from "../../components/StatusStates";

// Write access is RESEARCH_ANALYST/ADMIN only (enforced server-side too —
// this check just controls whether we SHOW the compose form).
const CAN_WRITE_ROLES = ["RESEARCH_ANALYST", "ADMIN"];

export default function ResearchNotes() {
  const { role } = useAuth();
  const canWrite = CAN_WRITE_ROLES.includes(role);

  const { data, isLoading, error, refetch } = useFetch(() => getAllResearchNotes(), []);
  const [showForm, setShowForm] = useState(false);

  const notes = data ?? [];

  return (
    <div>
      <div className="page-header-row">
        <h1 className="page-title">Research & Reports</h1>
        {canWrite && (
          <button className="primary-btn" onClick={() => setShowForm((v) => !v)}>
            {showForm ? "Cancel" : "+ Publish New Report"}
          </button>
        )}
      </div>

      {showForm && (
        <CreateNoteForm
          onCreated={() => {
            setShowForm(false);
            refetch();
          }}
        />
      )}

      <div className="panel">
        {isLoading && <LoadingState label="Loading research notes..." />}
        {error && <ErrorState error={error} onRetry={refetch} />}

        {!isLoading && !error && notes.length === 0 && (
          <p className="muted">No research notes published yet.</p>
        )}

        {!isLoading && notes.length > 0 && (
          <div className="notes-list">
            {notes.map((note) => (
              <article key={note.noteId} className="note-card">
                <div className="note-card-header">
                  <h3>{note.title}</h3>
                  {note.symbol && <span className="note-symbol-tag">{note.symbol}</span>}
                </div>
                <p className="note-content">{note.content}</p>
                <div className="note-meta">
                  {note.authorName} • {new Date(note.createdAt).toLocaleDateString()}
                </div>
              </article>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}

function CreateNoteForm({ onCreated }) {
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [securityId, setSecurityId] = useState("");
  const [error, setError] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);

  // Securities dropdown is optional context (note can be general market
  // commentary with no security attached), so it's fine if this list is
  // still loading — the form doesn't block on it.
  const { data: securities } = useFetch(() => getAllSecurities(), []);

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");
    setIsSubmitting(true);
    try {
      await createResearchNote({
        title,
        content,
        securityId: securityId ? Number(securityId) : undefined,
      });
      setTitle("");
      setContent("");
      setSecurityId("");
      onCreated();
    } catch (err) {
      setError(err.response?.data?.message || "Failed to publish note.");
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <form className="panel create-note-form" onSubmit={handleSubmit}>
      <h3>Publish New Report</h3>

      <label htmlFor="title">Title</label>
      <input
        id="title"
        value={title}
        onChange={(e) => setTitle(e.target.value)}
        placeholder="e.g. TCS Q1 earnings beat expectations"
        required
      />

      <label htmlFor="securityId">Related Security (optional)</label>
      <select id="securityId" value={securityId} onChange={(e) => setSecurityId(e.target.value)}>
        <option value="">General market commentary</option>
        {(securities ?? []).map((s) => (
          <option key={s.securityId} value={s.securityId}>
            {s.symbol} — {s.name}
          </option>
        ))}
      </select>

      <label htmlFor="content">Content</label>
      <textarea
        id="content"
        rows={5}
        value={content}
        onChange={(e) => setContent(e.target.value)}
        placeholder="Write your analysis..."
        required
      />

      {error && <div className="error-message">{error}</div>}

      <button type="submit" className="primary-btn" disabled={isSubmitting}>
        {isSubmitting ? "Publishing..." : "Publish"}
      </button>
    </form>
  );
}
