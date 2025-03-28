'use client';

import { Card, CardHeader, CardTitle, CardContent, CardFooter, } from "@/components/ui/card";
import Button from "../shared/Button";
import Image from "next/image";
import { useCart } from "@/lib/CartContext";
import toast from "react-hot-toast";
import { useUser } from "@/lib/userContext";
import { useRouter } from "next/navigation";
import { useState } from "react";

interface ProductCardProps {
  product: {
    productId: string;
    name: string;
    price: number;
    stock: number;
    description: string;
    imageUrl?: string;
    category: string;
    users: {
      user_id: string;
    };
  };
}

export default function ProductCard({ product }: ProductCardProps) {
  const { user } = useUser();
  const { addToCart } = useCart();
  const router = useRouter();
  const API_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080";

  // Validate and handle the image URL
  const validImageUrl = product.imageUrl ? `${API_URL}${product.imageUrl}` : "/pexels-shvetsa-5187579.jpg";

  const [imgSrc, setImgSrc] = useState(validImageUrl);

  const handleAddToCart = () => {
    addToCart(product);
    toast.success(`${product.name} has been added to your cart!`);
  };

  return (
      <Card className="w-full md:w-[260px] shadow-md hover:shadow-lg transition-shadow rounded-lg">
        {/* Product Image */}
        <CardHeader className="p-0">
          <div className="relative w-full h-40">
            <Image
                src={imgSrc}
                alt={product.name}
                layout="fill"
                objectFit="cover"
                className="rounded-t-lg"
                onError={() => setImgSrc("/default-fallback.png")}
            />
          </div>
        </CardHeader>

        {/* Product Details */}
        <CardContent className="p-4">
          <CardTitle className="text-base font-semibold truncate">{product.name}</CardTitle>
          <p className="text-sm text-gray-600 truncate">
            {product.users?.user_id || "Unknown Farmer"}
          </p>
          <p className="mt-2 text-green-600 font-bold text-lg">${product.price}</p>
          <p
              className={`text-sm mt-1 ${
                  product.stock > 0 ? "text-gray-600" : "text-red-600"
              }`}
          >
            {product.stock > 0 ? `${product.stock} in stock` : "Out of stock"}
          </p>
        </CardContent>

        {/* Add to Cart Button */}
        <CardFooter className="p-4 pt-0">
          <Button
              onClick={() => (user ? handleAddToCart() : router.push("/login"))}
              className="w-full bg-green-600 hover:bg-green-800 flex justify-center items-center"
              disabled={product.stock === 0}
          >
            <span className="mr-2">Add to Cart</span>
            <svg
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 24 24"
                fill="currentColor"
                className="w-5 h-5"
            >
              <path d="M12 1.5a.75.75 0 0 1 .75.75V7.5h-1.5V2.25A.75.75 0 0 1 12 1.5ZM11.25 7.5v5.69l-1.72-1.72a.75.75 0 0 0-1.06 1.06l3 3a.75.75 0 0 0 1.06 0l3-3a.75.75 0 1 0-1.06-1.06l-1.72 1.72V7.5h3.75a3 3 0 0 1 3 3v9a3 3 0 0 1-3 3h-9a3 3 0 0 1-3-3v-9a3 3 0 0 1 3-3h3.75Z" />
            </svg>
          </Button>
        </CardFooter>
      </Card>
  );
}