import Image from "next/image";
import MainSection from "../components/mainSection";
import Cart from "../components/cart";

export default function Home() {
  return (
    <div className="flex min-h-screen flex-col font-[family-name:var(--font-geist-sans)]">
      <header className="sticky top-0 flex justify-center bg-amber-50 z-50">
        <div className="z-40 sm:container px-4 flex items-center justify-between">
          <div className="flex items-center justify-center py-4">
            <Image src="/logo.png" alt="Logo" width={200} height={300} />
          </div>
          <Cart />
        </div>
      </header>
      <section id="hero" className="w-full flex justify-center bg-amber-50">
        <div className="sm:container px-4 py-5 mt-10">
          <div className="flex justify-center items-center gap-60">
            <div>
              <h1 className="text-4xl font-bold text-red-800">Zamanla Yarışıyoruz!</h1>
              <p className="text-lg mt-2 text-red-800">Gecikmek yok, mutluluk var.</p>
            </div>
            <Image src="/hero.png" alt="Hero" width={400} height={400} className="rounded-full w-[400px] h-[400px]" />
          </div>
        </div>
      </section>
      <MainSection />
      <footer className="bg-amber-50 py-5 mt-10 flex justify-center">
        <div className="sm:container px-4 flex justify-center">
          <p className="text-center text-red-800">© 2024 - Mehmet Eren Çelik</p>
        </div>
      </footer>
    </div>
  );
}
