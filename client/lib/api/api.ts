import axios from "axios";

const api = axios.create({
  baseURL: "http://api.piaproject.com",
  withCredentials: true,
});

api.defaults.headers.common["Content-Type"] = "application/json";

export default api;
