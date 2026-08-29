import { createContext, useContext, useState, useEffect } from "react";
import { login as loginApi, getCurrentUser } from "../api/authApi";

// AuthContext lets any component in the app ask:
// "who is logged in?" and "what is their role?"
// without passing props down through every level.
const AuthContext = createContext(null);

const TOKEN_KEY = "marketly_token";
const USER_KEY = "marketly_user";

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [isLoading, setIsLoading] = useState(true); // true while we check localStorage on first load

  // On first app load, check if we already have a saved session.
  useEffect(() => {
    const savedUser = localStorage.getItem(USER_KEY);
    const savedToken = localStorage.getItem(TOKEN_KEY);

    if (savedUser && savedToken) {
      setUser(JSON.parse(savedUser));
    }
    setIsLoading(false);
  }, []);

  async function login(emailOrUsername, password) {
    const data = await loginApi(emailOrUsername, password);

    // AuthResponse (confirmed from backend source) only contains these
    // fields — email/fullName are NOT returned on login. We store what
    // we have now; call refreshUser() elsewhere if you need the rest
    // right after login (e.g. Profile page will fetch it separately).
    const token = data.token;
    const loggedInUser = {
      userId: data.userId,
      username: data.username,
      role: data.role,
    };

    localStorage.setItem(TOKEN_KEY, token);
    localStorage.setItem(USER_KEY, JSON.stringify(loggedInUser));
    setUser(loggedInUser);

    return loggedInUser;
  }

  function logout() {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
    setUser(null);
  }

  // Optional: re-sync user data from the server (call after profile edits, etc.)
  async function refreshUser() {
    const freshUser = await getCurrentUser();
    localStorage.setItem(USER_KEY, JSON.stringify(freshUser));
    setUser(freshUser);
  }

  const value = {
    user,
    role: user?.role ?? null,
    isAuthenticated: !!user,
    isLoading,
    login,
    logout,
    refreshUser,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

// Custom hook so components just do: const { user, login, logout } = useAuth();
export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used inside an <AuthProvider>");
  }
  return context;
}
