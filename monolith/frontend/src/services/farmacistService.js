import api from "../api/axiosConfig";

export const getFarmacisti = (params) =>
  api.get("/api/farmacisti", { params });

export const getFarmacist = (id) => api.get(`/api/farmacisti/${id}`);

export const createFarmacist = (data) => api.post("/api/farmacisti", data);

export const updateFarmacist = (id, data) =>
  api.put(`/api/farmacisti/${id}`, data);

export const deleteFarmacist = (id) => api.delete(`/api/farmacisti/${id}`);