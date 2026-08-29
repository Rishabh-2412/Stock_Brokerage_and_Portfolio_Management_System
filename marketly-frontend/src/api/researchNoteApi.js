import apiClient from "./client";

// Read: open to any logged-in user.
export async function getAllResearchNotes() {
  const response = await apiClient.get("/research-notes");
  return response.data; // array of ResearchNoteDTO, most recent first
}

export async function getResearchNotesForSecurity(securityId) {
  const response = await apiClient.get(`/research-notes/security/${securityId}`);
  return response.data;
}

// Write: RESEARCH_ANALYST/ADMIN only (enforced server-side too).
export async function createResearchNote(noteData) {
  // Only title, content, and optional securityId are read by the backend.
  const response = await apiClient.post("/research-notes", {
    title: noteData.title,
    content: noteData.content,
    ...(noteData.securityId ? { securityId: noteData.securityId } : {}),
  });
  return response.data;
}
