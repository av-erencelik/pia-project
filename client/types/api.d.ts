type Product = {
  id: string;
  name: string;
  price: number;
  stock: number;
  createdAt: Date;
};

type OrderItem = {
  productId: string;
  productName: string;
  price: number;
  orderId: string;
  createdAt: Date;
};

type Order = {
  id: string;
  orderItems: OrderItem[];
  totalPrice: number;
  createdAt: Date;
  status: OrderStatus;
  refunded: boolean;
};

type Driver = {
  id: string;
  name: string;
  status: DriverStatus;
};

type Delivery = {
  id: string;
  driver?: Driver;
  orderId: string;
  status: DeliveryStatus;
  expirationDate?: Date;
  onTime: boolean;
};

type CreateOrder = {
  productIds: string[];
};

enum OrderStatus {
  Created = "CREATED",
  Delivered = "DELIVERED",
  Canceled = "CANCELED",
  OnDelivery = "ON_DELIVERY",
}

enum DriverStatus {
  Available = "AVAILABLE",
  OnDelivery = "ON_DELIVERY",
}

enum DeliveryStatus {
  Preparing = "PREPARING",
  OnDelivery = "ON_DELIVERY",
  Delivered = "DELIVERED",
  Canceled = "CANCELED",
}
