/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  darkMode: "class",
  theme: {
    extend: {
      colors: {
        primary: "#6500cd",
        "primary-container": "#8218ff",
        "surface-container-lowest": "#ffffff",
        "surface-container-low": "#f4f3f6",
        surface: "#faf9fc",
        "on-primary": "#ffffff",
        "on-surface": "#1a1c1e",
        outline: "#7e7386",
        "outline-variant": "#cfc2d7",
        "on-surface-variant": "#4c4354",
        secondary: "#5c5c73"
      },
      fontFamily: {
        headline: ["Manrope", "sans-serif"],
        body: ["Inter", "sans-serif"]
      }
    }
  },
  plugins: [],
}

