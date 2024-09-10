"use client";
import Deliveries from "./deliveries";
import Drivers from "./drivers";
import Orders from "./orders";
import Products from "./products";

export default function MainSection() {
  return (
    <main id="main" className="w-full flex justify-center">
      <div className="sm:container px-4 py-5 mt-10">
        <div className="border-b-2 border-amber-300">
          <h2 className="text-2xl text-red-800 mb-2 font-semibold">Dashboard</h2>
        </div>
        <section className="flex gap-5 mt-10 justify-between">
          <Products />
          <Orders />
          <Deliveries />
          <Drivers />
        </section>
      </div>
    </main>
  );
}
