import api from "../api/axiosConfig";

export const getFurnizori = (params) =>
  api.get("/api/furnizori", { params });

export const getFurnizor = (id) => api.get(`/api/furnizori/${id}`);

export const createFurnizor = (data) => api.post("/api/furnizori", data);

export const updateFurnizor = (id, data) =>
  api.put(`/api/furnizori/${id}`, data);

export const deleteFurnizor = (id) =>
  api.delete(`/api/furnizori/${id}`);