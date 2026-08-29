import apiClient from "./client";

// Each function returns response.data directly, so calling code
// doesn't have to unwrap `.data` every time.

export async function login(emailOrUsername, password) {
  const response = await apiClient.post("/auth/login", {
    username: emailOrUsername, // adjust field name if your backend expects "email"
    password,
  });
  return response.data; // expected: { token, userId, username, email, role, ... }
}

export async function register(registerData) {
  // registerData shape matches your RegisterRequest DTO, e.g.:
  // { fullName, email, phoneNumber, username, password }
  const response = await apiClient.post("/auth/register", registerData);
  return response.data;
}

export async function getCurrentUser() {
  const response = await apiClient.get("/users/me");
  return response.data;
}

export async function updateMyProfile(profileData) {
  // Matches UpdateProfileRequest exactly: only fullName and phone are
  // read by the backend. Sending anything else is harmless but ignored.
  const response = await apiClient.put("/users/me", {
    fullName: profileData.fullName,
    phone: profileData.phone,
  });
  return response.data; // UserResponse
}

// ---- ADMIN only ----

export async function getAllUsers() {
  const response = await apiClient.get("/users");
  return response.data; // array of UserResponse
}

export async function getUserById(userId) {
  const response = await apiClient.get(`/users/${userId}`);
  return response.data;
}

export async function createUser(userData) {
  // Matches CreateUserRequest exactly:
  // { username, email, password, fullName, phone, role, accountType }
  const response = await apiClient.post("/users", userData);
  return response.data;
}
