import axios from "axios";

const API_URL = process.env.REACT_APP_API_URL;

const apiFetch = axios.create({
  baseURL: API_URL,
  headers: { "Content-Type": "application/json" },
  withCredentials: true
});

export default apiFetch;
