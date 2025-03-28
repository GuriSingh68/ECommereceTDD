'use client'

import React, { useEffect, useState } from "react";
import CreateProduct from "@/components/admin/CreateProduct";
import ProductList from "@/components/admin/ProductList";
import api from "@/lib/api";
import { Product } from "@/components/admin/ProductList";

function Page() {
  const [products, setProducts] = useState<Product[]>([]);
  const [selectedProduct, setSelectedProduct] = useState<Product | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const fetchProducts = async () => {
    setLoading(true);
    try {
      const response = await api("/products", {
        method: "GET",
      });
      setProducts(response);
      setError(null);
    } catch (error) {
      console.error("Error fetching products:", error);
      setError("Failed to fetch products. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  const handleProductChange = () => {
    fetchProducts();
  };

  const handleEditProduct = (product: Product) => {
    setSelectedProduct(product);
  };

  useEffect(() => {
    const loadProducts = async () => {
      await fetchProducts();
    };
    loadProducts();
  }, []);

  return (
      <div className="container mx-auto p-4">
        <h1 className="text-3xl font-bold mb-6">Manage Products</h1>
        <CreateProduct
            onProductChange={handleProductChange}
            selectedProduct={selectedProduct}
        />
        {loading && <div>Loading products...</div>}
        {error && <div className="text-red-500">{error}</div>}
        <ProductList
            products={products}
            onEditProduct={handleEditProduct}
        />
      </div>
  );
}

export default Page;