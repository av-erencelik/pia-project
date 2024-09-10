"use client";

import React from "react";

import { ScrollArea } from "@/components/ui/scroll-area";
import { Product } from "./product";
import { useQuery } from "@tanstack/react-query";
import { getProducts } from "../lib/api/api-service";
import { ChevronLeftIcon, ChevronRightIcon, ReloadIcon } from "@radix-ui/react-icons";
import { Button } from "./ui/button";

const Products = () => {
  const [page, setPage] = React.useState(0);
  const { data, error, isLoading, isError, refetch, isRefetching } = useQuery({
    queryKey: ["products", page],
    queryFn: () => getProducts(page),
    retry: false,
    refetchOnWindowFocus: true,
  });

  return (
    <div className="flex flex-col gap-2 flex-1">
      <h3 className="text-xl text-red-800 font-semibold text-center border-2 p-1 border-amber-400 bg-amber-400 rounded">
        Yemekler
      </h3>
      <div className="flex justify-end w-full gap-2 items-center">
        <div className="flex items-center">
          <Button
            variant={"outline"}
            size={"icon"}
            onClick={() => setPage((prev) => prev - 1)}
            className="rounded-none border-r-0"
            disabled={page === 0}
          >
            <ChevronLeftIcon className="w-5 h-5 text-red-800" />
          </Button>
          <span className="text-sm w-9 h-9 flex justify-center items-center rounded-none border-r-0 text-red-800 border-red-200 border">
            {page + 1}
          </span>
          <Button
            variant={"outline"}
            size={"icon"}
            onClick={() => setPage((prev) => prev + 1)}
            className="rounded-none"
            disabled={data?.length != 10}
          >
            <ChevronRightIcon className="w-5 h-5 text-red-800" />
          </Button>
        </div>
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
        {isError && <div className="w-full h-[20vh] flex justify-center items-center p-3">Hata: {error.message}</div>}
        <div className="p-3">
          {data?.map((product) => (
            <Product key={product.id} product={product} />
          ))}
        </div>
        {data?.length === 0 && (
          <div className="w-full h-[20vh] flex justify-center items-center p-3">Ürün bulunamadı.</div>
        )}
      </ScrollArea>
    </div>
  );
};

export default Products;
