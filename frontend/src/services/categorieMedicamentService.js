import api from "../api/axiosConfig";

export const getCategoriiMedicamente = (params) =>
  api.get("/api/categorii", { params });

export const getCategorieMedicament = (id) => api.get(`/api/categorii/${id}`);

export const createCategorieMedicament = (data) =>
  api.post("/api/categorii", data);

export const updateCategorieMedicament = (id, data) =>
  api.put(`/api/categorii/${id}`, data);

export const deleteCategorieMedicament = (id) =>
  api.delete(`/api/categorii/${id}`);