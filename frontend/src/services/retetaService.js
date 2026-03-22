import api from "../api/axiosConfig";

export const getRetete = (params) => api.get("/api/retete", { params });

export const getReteta = (id) => api.get(`/api/retete/${id}`);

export const createReteta = (data) => api.post("/api/retete", data);

export const updateReteta = (id, data) => api.put(`/api/retete/${id}`, data);

export const deleteReteta = (id) => api.delete(`/api/retete/${id}`);