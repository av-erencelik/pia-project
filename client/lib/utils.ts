import { clsx, type ClassValue } from "clsx";
import { twMerge } from "tailwind-merge";

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

export function formatOrderStatus(status: OrderStatus) {
  switch (status) {
    case "CREATED":
      return "Oluşturuldu";
    case "DELIVERED":
      return "Teslim Edildi";
    case "CANCELED":
      return "İptal Edildi";
    case "ON_DELIVERY":
      return "Yolda";
  }
}

export function getRemainingSeconds(endDate: Date) {
  const now = new Date();
  const diff = endDate.getTime() - now.getTime();
  return Math.max(Math.floor(diff / 1000), 0);
}

export const formatDeliveryStatus = (status: DeliveryStatus) => {
  switch (status) {
    case "PREPARING":
      return "Hazırlanıyor";
    case "ON_DELIVERY":
      return "Yolda";
    case "DELIVERED":
      return "Teslim Edildi";
    case "CANCELED":
      return "İptal Edildi";
  }
};
