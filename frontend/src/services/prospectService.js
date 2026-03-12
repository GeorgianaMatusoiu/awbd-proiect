import api from "../api/axiosConfig";

export const getProspecte = () => api.get("/api/prospecte");

export const getProspect = (id) => api.get(`/api/prospecte/${id}`);

export const createProspect = (data) => api.post("/api/prospecte", data);

export const updateProspect = (id, data) =>
  api.put(`/api/prospecte/${id}`, data);

export const deleteProspect = (id) =>
  api.delete(`/api/prospecte/${id}`);