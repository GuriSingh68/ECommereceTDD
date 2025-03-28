import React, { useState } from "react";
import ProductList from "./ProductList";
import { Product } from "./ProductList";
import CreateProduct from './CreateProduct';

const ProductManagement = () => {
  const [selectedProduct, setSelectedProduct] = useState<Product | null>(null);
  const [products, setProducts] = useState<Product[]>([]);

  const handleProductChange = (newProduct: Product) => {
    setProducts((prevProducts) => {
      const existingProductIndex = prevProducts.findIndex(p => p.productId === newProduct.productId);
      if (existingProductIndex > -1) {

        const updatedProducts = [...prevProducts];
        updatedProducts[existingProductIndex] = newProduct;
        return updatedProducts;
      } else {

        return [...prevProducts, newProduct];
      }
    });
    setSelectedProduct(null);
  };

  const handleEditProduct = (product: Product) => {
    setSelectedProduct(product);
  };

  return (
    <div>
      <CreateProduct onProductChange={handleProductChange} selectedProduct={selectedProduct} />
      <ProductList products={products} onEditProduct={handleEditProduct} />
    </div>
  );
};

export default ProductManagement;