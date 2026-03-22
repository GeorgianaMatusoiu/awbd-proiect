import api from "../api/axiosConfig";

export const getDetaliiRetete = (params) =>
  api.get("/api/detalii-retete", { params });

export const getDetaliuReteta = (retetaId, medicamentId) =>
  api.get(`/api/detalii-retete/${retetaId}/${medicamentId}`);

export const createDetaliuReteta = (data) =>
  api.post("/api/detalii-retete", data);

export const updateDetaliuReteta = (retetaId, medicamentId, data) =>
  api.put(`/api/detalii-retete/${retetaId}/${medicamentId}`, data);

export const deleteDetaliuReteta = (retetaId, medicamentId) =>
  api.delete(`/api/detalii-retete/${retetaId}/${medicamentId}`);