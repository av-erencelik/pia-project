"use client";
import { useState } from "react";
import { Sheet, SheetContent, SheetFooter, SheetHeader, SheetTitle, SheetTrigger } from "./ui/sheet";
import { Button } from "./ui/button";
import { useCartStore } from "../providers/cart-store-provider";
import { Separator } from "./ui/separator";
import { ScrollArea } from "@radix-ui/react-scroll-area";
import { useMutation } from "@tanstack/react-query";
import { createOrder } from "../lib/api/api-service";

const Cart = () => {
  const [isOpen, setIsOpen] = useState(false);
  const { products, removeFromCart } = useCartStore((state) => state);
  const { mutateAsync } = useMutation({
    mutationFn: createOrder,
  });

  const handleOrder = async () => {
    try {
      const productIds = products.map((product) => product.id);
      await mutateAsync({ productIds });
      for (const product of products) {
        removeFromCart(product.id);
      }
      setIsOpen(false);
    } catch (error) {
      console.error(error);
    }
  };
  return (
    <Sheet open={isOpen} onOpenChange={setIsOpen}>
      <SheetTrigger asChild>
        <Button variant={"default"} size={"sm"} className="relative">
          Sepet
          <span className="absolute -top-1 -right-1 text-xs font-semibold text-white bg-amber-500 rounded-full h-4 w-4 flex items-center justify-center">
            {products.length}
          </span>
        </Button>
      </SheetTrigger>
      <SheetContent side="right" className="flex w-full flex-col pr-0 sm:max-w-sm">
        <SheetHeader className="space-y-2.5 pr-6">
          <SheetTitle>Sepet {products.length > 0 && `(${products.length})`}</SheetTitle>
          <Separator />
        </SheetHeader>
        <>
          {products.length > 0 ? (
            <>
              <ScrollArea className="h-full">
                <div className="flex w-full flex-col gap-5 pr-6 flex-1">
                  {products.map((product) => (
                    <div key={product.id} className="space-y-3">
                      <div className="flex items-center justify-between gap-4 flex-col sm:flex-row">
                        <div className="flex items-center space-x-4">
                          <span className="font-semibold">{product.name}</span>
                          <span className="text-red-800 font-semibold">{product.price} TL</span>
                        </div>
                        <div className="flex items-center space-x-4">
                          <Button
                            variant={"default"}
                            size={"sm"}
                            onClick={() => {
                              removeFromCart(product.id);
                            }}
                          >
                            Sil
                          </Button>
                        </div>
                      </div>
                      <Separator />
                    </div>
                  ))}
                </div>
              </ScrollArea>
              <div className="space-y-4 pr-6">
                <Separator />
                <div className="space-y-1.5 text-sm">
                  <div className="flex">
                    <span className="flex-1">Teslimat</span>
                    <span>Ücretsiz</span>
                  </div>
                  <div className="flex">
                    <span className="flex-1">Toplam</span>
                    {/* take total price of products */}
                    <span>{products.reduce((acc, product) => acc + product.price, 0)}</span>
                  </div>
                </div>
                <SheetFooter>
                  <SheetTrigger asChild>
                    <Button variant={"default"} size={"lg"} className="w-full" onClick={handleOrder}>
                      Sipariş Ver
                    </Button>
                  </SheetTrigger>
                </SheetFooter>
              </div>
            </>
          ) : (
            <div className="flex h-full flex-col items-center justify-center space-y-1">
              <div className="text-xl font-medium text-muted-foreground">Sepet boş</div>
              <div className="text-sm text-muted-foreground text-center">Sepetinize ürün ekleyebilirsiniz.</div>
            </div>
          )}
        </>
      </SheetContent>
    </Sheet>
  );
};

export default Cart;
