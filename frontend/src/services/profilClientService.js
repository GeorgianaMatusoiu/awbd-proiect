import api from "../api/axiosConfig";

export const getProfiluri = (params) => api.get("/api/profiluri", { params });

export const getProfil = (id) => api.get(`/api/profiluri/${id}`);

export const createProfil = (data) => api.post("/api/profiluri", data);

export const updateProfil = (id, data) => api.put(`/api/profiluri/${id}`, data);

export const deleteProfil = (id) => api.delete(`/api/profiluri/${id}`);