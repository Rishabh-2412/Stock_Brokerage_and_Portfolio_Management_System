import axios from "axios";

// Change this if your backend runs somewhere else.
const BASE_URL = "http://localhost:8080/api";

const apiClient = axios.create({
  baseURL: BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

// --- Attach the JWT to every request automatically ---
// We read it fresh from localStorage on every call, so login/logout
// take effect immediately without needing to re-create the client.
apiClient.interceptors.request.use((config) => {
  const token = localStorage.getItem("marketly_token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// --- Handle expired/invalid tokens globally ---
// If the backend ever says 401 (unauthorized), we clear the stored
// session and send the user back to login. This means individual
// pages don't need to handle "token expired" themselves.
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      localStorage.removeItem("marketly_token");
      localStorage.removeItem("marketly_user");
      // Full page redirect (not react-router) so all app state resets cleanly.
      if (window.location.pathname !== "/login") {
        window.location.href = "/login";
      }
    }
    return Promise.reject(error);
  }
);

export default apiClient;
