"use client";

import { type ReactNode, createContext, useRef, useContext } from "react";
import { useStore } from "zustand";
import { type CartStore, createCartStore } from "@/stores/cart-store";

export type CartStoreApi = ReturnType<typeof createCartStore>;

const CartStoreContext = createContext<CartStoreApi | undefined>(undefined);

export interface CartStoreProviderProps {
  children: ReactNode;
  initialState?: CartStore;
}

export const CartStoreProvider = ({ children, initialState }: CartStoreProviderProps) => {
  const store = useRef(createCartStore(initialState));
  if (!store.current) {
    store.current = createCartStore(initialState);
  }
  return <CartStoreContext.Provider value={store.current}>{children}</CartStoreContext.Provider>;
};

export const useCartStore = <T,>(selector: (store: CartStore) => T): T => {
  const cartStoreContext = useContext(CartStoreContext);

  if (!cartStoreContext) {
    throw new Error("useCartStore must be used within a CartStoreProvider");
  }

  return useStore(cartStoreContext, selector);
};
