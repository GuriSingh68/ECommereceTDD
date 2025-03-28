'use client'

import React, { useEffect, useState } from "react";
import ProductCard from "@/components/user/ProductCard";
import Hero from "@/components/shared/Hero";
import api from "@/lib/api";
import Link from "next/link";
import { Product } from "@/lib/CartContext";
import { useUser } from "@/lib/userContext";

async function fetchProducts(): Promise<Product[]> {
  try {
    return await api("/products", { method: "GET" }); // Ensure the endpoint matches your backend
  } catch (error) {
    console.error("Failed to fetch products:", error);
    return [];
  }
}

export default function Landing() {
  const [products, setProducts] = useState<Product[]>([]);
  const { user } = useUser();

  useEffect(() => {
    const fetchData = async () => {
      const fetchedProducts = await fetchProducts();
      setProducts(fetchedProducts);
    };

    fetchData();
  }, []);

  return (
      <>
        <section>
          {/* Hero Section */}
          <div>
            <Hero />
          </div>

          {user?.role !== 'ADMIN' && (
              <div className="container mx-auto px-6 lg:px-12 py-12">
                <h1 className="text-4xl font-extrabold text-center text-green-700 mb-4 tracking-tight">
                  <span className="text-black">Featured</span> Products
                </h1>
                <p className="text-center text-lg text-gray-600 mb-10">
                  Explore our top picks for fresh and organic farm products!
                </p>

                {products.length === 0 ? (
                    <p className="text-center text-xl text-gray-500">
                      No products available
                    </p>
                ) : (
                    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
                      {products.slice(0, 8).map((product) => (
                          <ProductCard key={product.productId} product={product} />
                      ))}
                    </div>
                )}
              </div>
          )}

          <div className="bg-gray-100 py-12">
            <div className="container mx-auto px-6 lg:px-12 text-center">
              <h2 className="text-3xl font-bold text-gray-800 mb-4">
                Ready to experience fresh and organic products?
              </h2>
              <p className="text-lg text-gray-600 mb-6">
                Discover the best of local farming with our hand-picked selection.
              </p>
              <Link href="/product">
                <button className="bg-green-600 text-white px-6 py-3 rounded-lg shadow hover:bg-green-700 transition">
                  Shop Now
                </button>
              </Link>
            </div>
          </div>
        </section>
      </>
  );
}