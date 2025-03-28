import React, { useEffect, useState } from "react";
import api from "@/lib/api";
import ProductCard from "./ProductCard";

export interface Product {
  productId: string;
  name: string;
  price: number;
  stock: number;
  description: string;
  imageUrl: string | null;
  users: {
    user_id: string;
    firstName: string;
  } | null;
  category: string;
}

interface ProductListProps {
  products: Product[];
  onEditProduct: (product: Product) => void;
}

export default function ProductList({ products, onEditProduct }: ProductListProps) {
  const [filteredProducts, setFilteredProducts] = useState<Product[]>(products); // Initialize with all products

  useEffect(() => {
    setFilteredProducts(products); // Update filtered products whenever products prop changes
  }, [products]);

  const handleDelete = async (id: string) => {
    setFilteredProducts((prevProducts) => prevProducts.filter(product => product.productId !== id));

    const user = localStorage.getItem("user"); // Get the user object from local storage
    if (!user) {
      alert("User  not found in local storage.");
      return;
    }

    const userEmail = JSON.parse(user).email; // Parse the user object and retrieve the email

    if (!userEmail) {
      alert("User  email is not available.");
      return;
    }

    try {
      await api(`/products/delete/${id}/${userEmail}`, {
        method: "DELETE",
      });
    } catch (error) {
      console.error("Error deleting product:", error);
    }
  };

  const handleEditProduct = (product: Product) => {
    onEditProduct(product);
  };

  return (
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        {filteredProducts.map((product) => (
            <ProductCard
                key={product.productId}
                product={product}
                onDelete={handleDelete}
                onEdit={handleEditProduct}
            />
        ))}
      </div>
  );
}