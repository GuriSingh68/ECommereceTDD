import React, { useEffect, useState } from "react";
import api from "@/lib/api";
import { Product } from "./ProductList";
import Input from "@/components/shared/Input";
import Button from "../shared/Button";

interface CreateProductProps {
  onProductChange: (product: Product) => void;
  selectedProduct: Product | null;
}

export default function CreateProduct({ onProductChange, selectedProduct }: CreateProductProps) {
  const [adminId, setAdminId] = useState<string | null>(null);
  const [formData, setFormData] = useState({
    name: "",
    description: "",
    price: "",
    stock: "",
    category: "",
    imageUrl: null as string | null,
  });
  const [isUpdating, setIsUpdating] = useState(false);

  useEffect(() => {
    const user = localStorage.getItem("user");
    if (user) {
      const parsedUser  = JSON.parse(user);
      if (parsedUser .role === "ADMIN") {
        setAdminId(parsedUser ._id);
      }
    }
  }, []);

  useEffect(() => {
    if (selectedProduct) {
      setFormData({
        name: selectedProduct.name,
        description: selectedProduct.description,
        price: selectedProduct.price.toString(),
        stock: selectedProduct.stock.toString(),
        category: selectedProduct.category,
        imageUrl: selectedProduct.imageUrl,
      });
      setIsUpdating(true);
    } else {
      setFormData({
        name: "",
        description: "",
        price: "",
        stock: "",
        category: "",
        imageUrl: null,
      });
      setIsUpdating(false);
    }
  }, [selectedProduct]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files[0]) {
      const file = e.target.files[0];
      uploadImage(file); // Call the upload function
    }
  };

  const uploadImage = async (file: File) => {
    const formData = new FormData();
    formData.append("file", file);

    try {
      const API_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080";
      const response = await fetch(`${API_URL}/upload/image`, {
        method: "POST",
        body: formData, // No need to set Content-Type manually
      });

      if (!response.ok) {
        throw new Error("Failed to upload image");
      }

      const imageUrl = await response.text(); // Backend returns the image URL as plain text
      setFormData((prevData) => ({
        ...prevData,
        imageUrl: imageUrl.trim(), // Remove any accidental spaces/new lines
      }));
    } catch (error) {
      console.error("Error uploading image:", error);
      alert("Failed to upload image. Please try again.");
    }
  };


  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!formData.imageUrl) {
      alert("Please upload an image!");
      return;
    }

    const productData = {
      name: formData.name,
      description: formData.description,
      price: parseFloat(formData.price),
      stock: parseInt(formData.stock, 10), // Ensure stock is an integer
      category: formData.category,
      imageUrl: formData.imageUrl, // Use the URL obtained from the upload
      admin: adminId, // Ensure this is the correct field name
    };

    try {
      let newProduct: Product;

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

      if (isUpdating && selectedProduct) {
        if (!selectedProduct.productId) {
          alert("Product ID is not available.");
          return;
        }

        newProduct = await api(`/products/update/${selectedProduct.productId}/${userEmail}`, {
          method: "PATCH",
          body: productData, // Ensure the body is stringified
          headers: {
            'Content-Type': 'application/json', // Set the content type
          },
        });
        alert("Product updated successfully!");
      } else {
        newProduct = await api(`/products/add/${userEmail}`, {
          method: "POST",
          body: productData, // Ensure the body is stringified
          headers: {
            'Content-Type': 'application/json', // Set the content type
          },
        });
        alert("Product created successfully!");
      }

      onProductChange(newProduct);

      setFormData({
        name: "",
        description: "",
        price: "",
        stock: "",
        category: "",
        imageUrl: null,
      });

      setIsUpdating(false);
    } catch (error) {
      console.error("Error saving product:", error);
      alert("Failed to save product. Please try again.");
    }
  };

  return (
      <div className="container mx-auto p-4">
        <h1 className="text-2xl font-bold mb-4">{isUpdating ? "Update Product" : "Create Product"}</h1>
        <form onSubmit={handleSubmit} className="space-y-4">
          <Input
              type="text"
              name="name"
              placeholder="Product Name"
              value={formData.name}
              onChange={handleChange}
              required
          />

          <textarea
              name="description"
              placeholder="Product Description"
              value={formData.description}
              onChange={handleChange}
              required
              className="border p-2 w-full"
          />

          <Input
              type="number"
              name="price"
              placeholder="Price"
              value={formData.price}
              onChange={handleChange}
              required
          />

          <Input
              type="number"
              name="stock"
              placeholder="Stock"
              value={formData.stock}
              onChange={handleChange}
              required
          />

          <Input
              type="text"
              name="category"
              placeholder="Category"
              value={formData.category}
              onChange={handleChange}
              required
          />

          <Input
              type="file"
              name="imageUrl"
              onChange={handleFileChange}
          />

          <Button type="submit" className="bg-green-600 text-white py-2 px-4 rounded hover:bg-green-800">
            {isUpdating ? "Update" : "Submit"}
          </Button>
        </form>
      </div>
  );
}