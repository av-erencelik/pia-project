import React, { useEffect, useState } from "react";
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from "@/components/ui/card";
import { cn, formatDeliveryStatus, getRemainingSeconds } from "../lib/utils";
import { Button } from "./ui/button";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { deliverDelivery } from "../lib/api/api-service";

type CardProps = React.ComponentProps<typeof Card> & {
  delivery: Delivery;
};

const Delivery = ({ className, delivery, ...props }: CardProps) => {
  const [timeRemaining, setTimeRemaining] = useState(
    delivery.expirationDate ? getRemainingSeconds(new Date(delivery.expirationDate)) : null
  );

  const queryClient = useQueryClient();

  const { mutateAsync } = useMutation({
    mutationFn: deliverDelivery,
  });

  const handleDeliver = async () => {
    try {
      await mutateAsync(delivery.id);
      queryClient.invalidateQueries({ queryKey: ["deliveries"] });
      queryClient.invalidateQueries({ queryKey: ["orders"] });
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    const timerInterval = setInterval(() => {
      if (timeRemaining === null) {
        return () => clearInterval(timerInterval);
      }
      setTimeRemaining((prevTime) => {
        if (prevTime === 0) {
          clearInterval(timerInterval);
          // Perform actions when the timer reaches zero
          console.log("Countdown complete");
          return 0;
        } else {
          return prevTime! - 1;
        }
      });
    }, 1000);

    // Cleanup the interval when the component unmounts
    return () => clearInterval(timerInterval);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return (
    <Card className={cn("w-full mb-5", className)} {...props}>
      <CardHeader>
        <CardTitle className="flex justify-between items-center">
          Teslimat {!delivery.onTime && <span className="text-red-500 p-1 bg-red-200 rounded text-sm">Gecikti</span>}
        </CardTitle>
        <CardDescription>{delivery.orderId} sipariş</CardDescription>
      </CardHeader>
      <CardContent className="grid gap-4">
        <div className="flex-1 space-y-1">
          <p className="text-sm font-semibold leading-none">
            Kalan süre:{" "}
            <span className="font-normal">
              {!delivery.onTime
                ? "Geciken Teslimat"
                : delivery.status === "DELIVERED"
                ? "Zamanında Teslim"
                : timeRemaining !== null
                ? timeRemaining
                : "Belirsiz"}
            </span>
          </p>
          <p className="text-sm text-muted-foreground font-semibold">
            Durum: <span className="font-normal">{formatDeliveryStatus(delivery.status)}</span>
          </p>
          {/* add driver name */}
          <p className="text-sm text-muted-foreground font-semibold">
            Sürücü: <span className="font-normal">{delivery.driver?.name || "Atanmamış"}</span>
          </p>
        </div>
      </CardContent>
      <CardFooter>
        <Button className="w-full" disabled={delivery.status !== "ON_DELIVERY"} onClick={handleDeliver}>
          Teslim Et
        </Button>
      </CardFooter>
    </Card>
  );
};

export default Delivery;
