import axios from "axios";
import config from "./Config/config";

const API_URL = config.API_BASE_URL;

const apiFetch = axios.create({
  baseURL: API_URL,
  headers: { "Content-Type": "application/json" },
  withCredentials: true
});

export default apiFetch;
