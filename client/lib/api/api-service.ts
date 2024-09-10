import api from "./api";

export const getProducts = async (page: number) => {
  const response = await api.get<Product[]>("/products?page=" + page);
  return response.data;
};

export const getOrders = async (page: number) => {
  const response = await api.get<Order[]>("/orders?page=" + page);
  return response.data;
};

export const getDeliveries = async (page: number) => {
  const response = await api.get<Delivery[]>("/deliveries?page=" + page);
  return response.data;
};

export const getDrivers = async () => {
  const response = await api.get<Driver[]>("/deliveries/drivers/all");
  return response.data;
};

export const createOrder = async (order: CreateOrder) => {
  const response = await api.post<Order>("/orders", order);
  return response.data;
};

export const deliverDelivery = async (id: string) => {
  const response = await api.patch("/deliveries/" + id + "/deliver");
  return response.data;
};

export const cancelOrder = async (id: string) => {
  const response = await api.patch("/orders/" + id + "/cancel");
  return response.data;
};
