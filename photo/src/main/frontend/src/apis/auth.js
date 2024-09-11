import api from './api'

export const login = (userId,password) => api.post(`/login?userId=${userId}&password=${password}`);

export const userInfo = () => api.get(`/users/info`);

export const createUser = (data) => api.post(`/users/create`,data);