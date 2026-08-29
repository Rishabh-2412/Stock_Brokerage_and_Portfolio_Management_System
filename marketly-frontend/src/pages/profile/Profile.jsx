import { useState } from "react";
import { useFetch } from "../../hooks/useFetch";
import { getCurrentUser, updateMyProfile } from "../../api/authApi";
import { LoadingState, ErrorState } from "../../components/StatusStates";
import { useAuth } from "../../context/AuthContext";

export default function Profile() {
  const { refreshUser } = useAuth();
  const { data: profile, isLoading, error, refetch } = useFetch(
    () => getCurrentUser(),
    []
  );

  const [isEditing, setIsEditing] = useState(false);
  const [fullName, setFullName] = useState("");
  const [phone, setPhone] = useState("");
  const [saveError, setSaveError] = useState("");
  const [isSaving, setIsSaving] = useState(false);

  if (isLoading) return <LoadingState label="Loading profile..." />;
  if (error) return <ErrorState error={error} onRetry={refetch} />;

  function startEditing() {
    setFullName(profile.fullName || "");
    setPhone(profile.phone || "");
    setSaveError("");
    setIsEditing(true);
  }

  async function handleSave(e) {
    e.preventDefault();
    setSaveError("");
    setIsSaving(true);
    try {
      await updateMyProfile({ fullName, phone });
      await refetch();
      await refreshUser(); // keep sidebar/topbar username in sync if it ever changes
      setIsEditing(false);
    } catch (err) {
      const message =
        err.response?.data?.message || "Failed to update profile. Please try again.";
      setSaveError(message);
    } finally {
      setIsSaving(false);
    }
  }

  return (
    <div>
      <h1 className="page-title">User Profile</h1>

      <div className="panel profile-panel">
        {!isEditing ? (
          <>
            <div className="profile-header">
              <h3>Personal Information</h3>
              <button className="link-btn" onClick={startEditing}>
                Edit
              </button>
            </div>

            <div className="profile-grid">
              <ProfileField label="Full Name" value={profile.fullName} />
              <ProfileField label="Username" value={profile.username} />
              <ProfileField label="Email" value={profile.email} />
              <ProfileField label="Phone Number" value={profile.phone || "—"} />
              <ProfileField label="Role" value={profile.role} />
              <ProfileField label="Account Type" value={profile.accountType} />
              <ProfileField label="KYC Status" value={profile.kycStatus} />
              <ProfileField
                label="Member Since"
                value={
                  profile.createdAt
                    ? new Date(profile.createdAt).toLocaleDateString()
                    : "—"
                }
              />
            </div>
          </>
        ) : (
          <form onSubmit={handleSave} className="profile-edit-form">
            <h3>Edit Personal Information</h3>

            <label htmlFor="fullName">Full Name</label>
            <input
              id="fullName"
              type="text"
              value={fullName}
              onChange={(e) => setFullName(e.target.value)}
              required
            />

            <label htmlFor="phone">Phone Number</label>
            <input
              id="phone"
              type="tel"
              placeholder="+91-9876543210"
              value={phone}
              onChange={(e) => setPhone(e.target.value)}
            />

            {saveError && <div className="error-message">{saveError}</div>}

            <div className="profile-edit-actions">
              <button type="submit" disabled={isSaving}>
                {isSaving ? "Saving..." : "Save Changes"}
              </button>
              <button
                type="button"
                className="cancel-btn"
                onClick={() => setIsEditing(false)}
                disabled={isSaving}
              >
                Cancel
              </button>
            </div>
          </form>
        )}
      </div>
    </div>
  );
}

function ProfileField({ label, value }) {
  return (
    <div className="profile-field">
      <div className="profile-field-label">{label}</div>
      <div className="profile-field-value">{value || "—"}</div>
    </div>
  );
}
