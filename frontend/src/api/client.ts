import axios from "axios";

const API_BASE_URL =
  import.meta.env.VITE_API_URL || "http://localhost:8081/api";

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Content-type": "application/json",
  },
});

// Add tokens to response
api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer {token}`;
  }
  return config;
});

// Handle response
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.reponse?.status === 401) {
      localStorage.removeItem("token");
      localStorage.removeUser("user");
      window.location.href = "/login";
    }
    return Promise.reject(error);
  },
);

export default api;
