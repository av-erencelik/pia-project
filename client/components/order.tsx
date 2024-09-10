import { format } from "date-fns";
import { tr } from "date-fns/locale";
import { cn, formatOrderStatus } from "@/lib/utils";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from "@/components/ui/card";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { cancelOrder } from "../lib/api/api-service";

type CardProps = React.ComponentProps<typeof Card> & {
  order: Order;
};

const Order = ({ className, order, ...props }: CardProps) => {
  const { mutateAsync } = useMutation({
    mutationFn: cancelOrder,
  });

  const queryClient = useQueryClient();

  const handleCancel = async () => {
    try {
      await mutateAsync(order.id);
      queryClient.invalidateQueries({ queryKey: ["orders"] });
      queryClient.invalidateQueries({ queryKey: ["deliveries"] });
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <Card className={cn("w-full mb-5", className)} {...props}>
      <CardHeader>
        <CardTitle>{format(new Date(order.createdAt), "PPpp", { locale: tr })} Tarihli Sipariş</CardTitle>
        <CardDescription>{order.id}</CardDescription>
      </CardHeader>
      <CardContent className="grid gap-4">
        <div className="flex-1 space-y-1">
          <p className="text-sm font-semibold leading-none">
            Toplam:{" "}
            <span className="font-normal">
              {order.totalPrice} TL {order.refunded && "- Gecikme iadesi"}
            </span>
          </p>
          <p className="text-sm text-muted-foreground font-semibold">
            Durum: <span className="font-normal">{formatOrderStatus(order.status)}</span>
          </p>
        </div>
        <div>
          <h4 className="text-sm font-semibold">Ürünler</h4>
          <ul className="text-sm text-muted-foreground">
            {order.orderItems.map((items) => (
              <li key={items.productId + items.orderId}>
                {items.productName} - {items.price} TL
              </li>
            ))}
          </ul>
        </div>
      </CardContent>
      <CardFooter>
        <Button
          className="w-full"
          disabled={order.status === "DELIVERED" || order.status === "CANCELED"}
          onClick={handleCancel}
        >
          İptal Et
        </Button>
      </CardFooter>
    </Card>
  );
};

export default Order;
