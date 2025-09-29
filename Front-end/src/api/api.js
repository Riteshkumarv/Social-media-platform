import axios from "axios";

// Prefer runtime config from public/config.js, fallback to build-time env
const runtimeApiUrl = typeof window !== "undefined" && window.__ENV__ && window.__ENV__.API_URL;
const API_URL = process.env.REACT_APP_API_URL || runtimeApiUrl ;

const api = axios.create({
  baseURL: API_URL,
  headers: { "Content-Type": "application/json" },
  withCredentials: true,
});

// ✅ Add access token to all requests
api.interceptors.request.use((config) => {
  const token = localStorage.getItem("accessToken");
  if (token) {
    config.headers["Authorization"] = `Bearer ${token}`;
  }
  return config;
});

// ✅ Refresh token on 401/403
api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    if ((error.response?.status === 401 || error.response?.status === 403) && !originalRequest._retry) {
      originalRequest._retry = true;
      try {
        const refreshToken = localStorage.getItem("refreshToken");
        if (!refreshToken) {
          window.location.href = "/"; // No token → back to login
          return Promise.reject(error);
        }

        const res = await axios.post(`${API_URL}/auth/refresh`, { refreshToken });

        const newAccessToken = res.data.accessToken;
        localStorage.setItem("accessToken", newAccessToken);

        // Update request with new token
        originalRequest.headers["Authorization"] = `Bearer ${newAccessToken}`;
        return api(originalRequest);
      } catch (err) {
        window.location.href = "/";
        return Promise.reject(err);
      }
    }

    return Promise.reject(error);
  }
);

export default api;
