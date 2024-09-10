import React from "react";
import { ScrollArea } from "./ui/scroll-area";
import { ReloadIcon } from "@radix-ui/react-icons";
import { getDrivers } from "../lib/api/api-service";
import { useQuery } from "@tanstack/react-query";
import Driver from "./driver";
import { Button } from "./ui/button";

const Drivers = () => {
  const { data, error, isLoading, isError, refetch, isRefetching } = useQuery({
    queryKey: ["drivers"],
    queryFn: getDrivers,
    retry: false,
    refetchOnWindowFocus: true,
  });

  return (
    <div className="flex flex-col gap-2 flex-1">
      <h3 className="text-xl text-red-800 font-semibold text-center border-2 p-1 border-amber-400 bg-amber-400 rounded">
        Sürücüler
      </h3>
      <div className="flex justify-end w-full gap-2 items-center">
        <Button variant={"outline"} size={"icon"} onClick={() => refetch()} disabled={isRefetching}>
          <ReloadIcon className="w-5 h-5 text-red-800" />
        </Button>
      </div>
      <ScrollArea className="h-[50vh] w-full border-y border-y-amber-500 pb-4">
        {(isLoading || isRefetching) && (
          <div className="w-full flex justify-center items-center p-3">
            <ReloadIcon className="animate-spin w-12 h-12 text-red-800" />
          </div>
        )}
        {isError && <div className="w-full h-[50vh] flex justify-center items-center p-3">Hata: {error.message}</div>}
        {data?.length === 0 && (
          <div className="w-full h-[50vh] flex justify-center items-center p-3">Sürücü bulunamadı.</div>
        )}
        <div className="p-3">
          {data?.map((driver) => (
            <Driver key={driver.id} driver={driver} />
          ))}
        </div>
      </ScrollArea>
    </div>
  );
};

export default Drivers;
