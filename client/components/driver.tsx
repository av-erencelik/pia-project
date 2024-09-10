import { cn } from "@/lib/utils";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";

import React from "react";

type CardProps = React.ComponentProps<typeof Card> & {
  driver: Driver;
};

const Driver = ({ className, driver, ...props }: CardProps) => {
  return (
    <Card className={cn("w-full mb-5", className)} {...props}>
      <CardHeader>
        <CardTitle>{driver.name}</CardTitle>
      </CardHeader>
      <CardContent className="grid gap-4">
        <div className="flex-1 space-y-1">
          <p className="text-sm text-muted-foreground font-semibold">
            Durum: <span className="font-normal">{driver.status === "AVAILABLE" ? "Boşta" : "Teslimatta"}</span>
          </p>
        </div>
      </CardContent>
    </Card>
  );
};

export default Driver;
