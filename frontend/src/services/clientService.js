import api from "../api/axiosConfig";

export const getClients = (params) => api.get("/api/clienti", { params });

export const getClient = (id) => api.get(`/api/clienti/${id}`);

export const createClient = (data) => api.post("/api/clienti", data);

export const updateClient = (id, data) => api.put(`/api/clienti/${id}`, data);

export const deleteClient = (id) => api.delete(`/api/clienti/${id}`);