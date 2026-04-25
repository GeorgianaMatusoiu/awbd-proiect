import api from "../api/axiosConfig";

export const registerUser = (data) => api.post("/auth/register", data);

export const loginUser = async (data) => {
  const response = await api.post("/auth/login", data);

  localStorage.setItem("user", JSON.stringify(response.data));

  return response.data;
};

export const logoutUser = async () => {
  await api.post("/auth/logout");
  localStorage.removeItem("user");
};

export const getCurrentUser = () => {
  const user = localStorage.getItem("user");
  return user ? JSON.parse(user) : null;
};