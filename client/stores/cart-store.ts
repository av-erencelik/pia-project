import { createStore } from "zustand/vanilla";

export type CartState = {
  products: CartProduct[];
};

export type CartProduct = {
  id: string;
  name: string;
  price: number;
};

export type CartActions = {
  addToCart: (product: CartProduct) => void;
  removeFromCart: (id: string) => void;
};

export type CartStore = CartState & CartActions;

export const defaultCartState: CartState = {
  products: [],
};

export const createCartStore = (initState: CartState = defaultCartState) => {
  return createStore<CartStore>()((set) => ({
    ...initState,
    addToCart: (product: CartProduct) => {
      // if item already exists in cart, do not add itee again
      set((state) => {
        if (state.products.find((p) => p.id === product.id)) {
          return state;
        }
        return {
          products: [...state.products, product],
        };
      });
    },
    removeFromCart: (id) => {
      set((state) => ({
        products: state.products.filter((product) => product.id !== id),
      }));
    },
  }));
};
