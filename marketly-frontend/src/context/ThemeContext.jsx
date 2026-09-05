import { createContext, useContext, useEffect, useState } from "react";

// Persists the choice so it survives a refresh/new tab, same pattern as
// AuthContext's token storage elsewhere in this app.
const STORAGE_KEY = "marketly-theme";

const ThemeContext = createContext(null);

export function ThemeProvider({ children }) {
  const [theme, setTheme] = useState(() => {
    try {
      return localStorage.getItem(STORAGE_KEY) === "dark" ? "dark" : "light";
    } catch {
      return "light"; // e.g. storage blocked in a private/incognito context
    }
  });

  // Setting data-theme on <html> is what makes App.css's
  // `[data-theme="dark"] { ... }` overrides take effect app-wide.
  useEffect(() => {
    document.documentElement.setAttribute("data-theme", theme);
    try {
      localStorage.setItem(STORAGE_KEY, theme);
    } catch {
      // ignore - theme just won't persist across a refresh
    }
  }, [theme]);

  function toggleTheme() {
    setTheme((prev) => (prev === "light" ? "dark" : "light"));
  }

  return (
    <ThemeContext.Provider value={{ theme, toggleTheme }}>
      {children}
    </ThemeContext.Provider>
  );
}

export function useTheme() {
  const context = useContext(ThemeContext);
  if (!context) {
    throw new Error("useTheme must be used inside a <ThemeProvider>");
  }
  return context;
}
