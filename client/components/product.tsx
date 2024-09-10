import { format } from "date-fns";
import { tr } from "date-fns/locale";
import { cn } from "@/lib/utils";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from "@/components/ui/card";
import { useCartStore } from "../providers/cart-store-provider";

type CardProps = React.ComponentProps<typeof Card> & {
  product: Product;
};

export function Product({ className, product, ...props }: CardProps) {
  const { products, addToCart, removeFromCart } = useCartStore((state) => state);
  console.log(products);
  function checkProductInCart(id: string): boolean {
    return products.some((product) => product.id === id);
  }
  function handleOnClick(isAdded: boolean) {
    if (isAdded) {
      removeFromCart(product.id);
    } else {
      addToCart({ id: product.id, name: product.name, price: product.price });
    }
  }
  return (
    <Card className={cn("w-full mb-5", className)} {...props}>
      <CardHeader>
        <CardTitle>{product.name}</CardTitle>
        <CardDescription>{format(new Date(product.createdAt), "PPpp", { locale: tr })}</CardDescription>
      </CardHeader>
      <CardContent className="grid gap-4">
        <div className="flex-1 space-y-1">
          <p className="text-sm font-semibold leading-none">
            Ücret: <span className="font-normal">{product.price} TL</span>
          </p>
          <p className="text-sm text-muted-foreground font-semibold">
            Stok: <span className="font-normal">{product.stock}</span>
          </p>
        </div>
      </CardContent>
      <CardFooter>
        <Button className="w-full" onClick={() => handleOnClick(checkProductInCart(product.id))}>
          {checkProductInCart(product.id) ? "Sepetten Çıkar" : "Sepete Ekle"}
        </Button>
      </CardFooter>
    </Card>
  );
}
