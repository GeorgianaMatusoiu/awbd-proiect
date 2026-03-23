import api from "../api/axiosConfig";

export const getCarduri = () => api.get("/api/carduri");

export const getCard = (id) => api.get(`/api/carduri/${id}`);

export const createCard = (data) => api.post("/api/carduri", data);

export const updateCard = (id, data) => api.put(`/api/carduri/${id}`, data);

export const deleteCard = (id) => api.delete(`/api/carduri/${id}`);