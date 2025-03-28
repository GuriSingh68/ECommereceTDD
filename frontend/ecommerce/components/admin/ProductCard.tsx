'use client'

import Button from "../shared/Button";

interface ProductCardProps {
  product: {
    productId: string;
    name: string;
    price: number;
    stock: number;
    description: string;
    imageUrl: string | null;
    users: {
      user_id: string;
    } | null;
  };
  onDelete: (id: string) => Promise<void>;
  onEdit: (product: any) => void;
}

export default function ProductCard({ product, onDelete, onEdit }: ProductCardProps) {
  const { productId, name, price, imageUrl, description } = product;

  const API_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080";

  const handleDelete = () => {
    onDelete(productId);
  };

  const handleUpdate = () => {
    onEdit(product);
  };

  return (
    <div className="border rounded-lg p-4 shadow hover:shadow-lg">
      {/* Product Image */}
      <img
        src={imageUrl ? `${API_URL}${imageUrl}`: "./pexels-shvetsa-5187579.jpg"}
        alt={name}
        className="h-48 w-full object-cover rounded"
      />

      {/* Product Details */}
      <h2 className="font-bold mt-2 text-lg">{name}</h2>
      <p className="text-gray-600 text-sm mt-1">{description}</p>
      <p className="text-lg font-semibold mt-2 text-green-600">${price}</p>

      {/* Update and Delete Buttons */}
      <div className="mt-2 flex justify-between">
        <Button
          onClick={handleUpdate}
          className="px-4 py-2 rounded bg-green-600 text-white hover:bg-green-800"
        >
          Update
        </Button>
        <Button
          onClick={handleDelete}
          className="px-4 py-2 rounded bg-red-600 text-white hover:bg-red-800"
        >
          Delete
        </Button>
      </div>
    </div>
  );
}