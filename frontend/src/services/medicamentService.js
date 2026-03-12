import api from "../api/axiosConfig";

export const getMedicamente = () => api.get("/api/medicamente");

export const getMedicament = (id) => api.get(`/api/medicamente/${id}`);

export const createMedicament = (data) => api.post("/api/medicamente", data);

export const updateMedicament = (id, data) =>
  api.put(`/api/medicamente/${id}`, data);

export const deleteMedicament = (id) => api.delete(`/api/medicamente/${id}`);