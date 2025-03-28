'use client';

import { useState, useEffect } from "react";
import ProductCard from "@/components/user/ProductCard";
import ProductFilter from "@/components/user/ProductFilter";
import api from "@/lib/api";
import { Product } from "@/lib/CartContext";

export default function ProductsPage() {
  const [products, setProducts] = useState<Product[]>([]);
  const [filteredProducts, setFilteredProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      setError(null);

      try {
        const fetchedProducts = await api("/products", { method: "GET" });
        setProducts(fetchedProducts);
        setFilteredProducts(fetchedProducts);
      } catch (err) {
        console.error("Failed to fetch products:", err);
        setError("Failed to load products. Please try again later.");
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  const handleFilter = (filtered: Product[]) => {
    setFilteredProducts(filtered);
  };

  // Group products by category
  const groupedProducts = filteredProducts.reduce((acc, product) => {
    acc[product.category] = acc[product.category] || [];
    acc[product.category].push(product);
    return acc;
  }, {} as Record<string, Product[]>);

  return (
    <section className="bg-gray-50">
      {/* Adjust for fixed navbar */}
      <div className="container mx-auto px-6 lg:px-12 py-12 mt-[80px] lg:mt-[80px]">
        {/* Page Header */}
        <div className="mb-8 text-center">
          <h1 className="text-3xl font-bold text-gray-800">
            Explore Our <span className="text-green-700">Products</span>
          </h1>
          <p className="text-lg text-gray-600 mt-2">
            Find the best products tailored to your needs.
          </p>
        </div>

        {/* Loading State */}
        {loading && (
          <div className="text-center mt-10">
            <p className="text-lg font-medium text-gray-600">
              Loading products...
            </p>
          </div>
        )}

        {/* Error State */}
        {error && (
          <div className="text-center mt-10">
            <p className="text-red-500 text-lg font-medium">{error}</p>
          </div>
        )}

        {/* Content */}
        {!loading && !error && (
          <>
            {/* Filter Section */}
            <div className="flex flex-col items-center mb-6 gap-4">
              <ProductFilter products={products} onFilter={handleFilter} />
            </div>

            {/* Product Categories */}
            {Object.entries(groupedProducts).map(([category, categoryProducts]) => (
              <div key={category} className="mb-12">
                {/* Category Heading */}
                <h2 className="text-2xl font-semibold text-gray-800 mb-4">
                  {category}
                </h2>
                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
                  {categoryProducts.map((product) => (
                    <ProductCard key={product.productId} product={product} />
                  ))}
                </div>
              </div>
            ))}
          </>
        )}
      </div>
    </section>
  );
}