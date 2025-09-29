import axios from "axios";

const runtimeApiUrl = typeof window !== "undefined" && window.__ENV__ && window.__ENV__.API_URL;
const API_URL = process.env.REACT_APP_API_URL || runtimeApiUrl;

const apiFetch = axios.create({
  baseURL: API_URL,
  headers: { "Content-Type": "application/json" },
  withCredentials: true
});

export default apiFetch;
