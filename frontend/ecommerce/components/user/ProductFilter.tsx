import { useState, useEffect, useMemo } from "react";
import Input from "../shared/Input";
import { Product } from "@/lib/CartContext";

interface ProductFilterProps {
  products: Product[];
  onFilter: (filteredProducts: Product[]) => void;
}

export default function ProductFilter({ products, onFilter }: ProductFilterProps) {
  const [searchTerm, setSearchTerm] = useState<string>("");
  const [selectedCategory, setSelectedCategory] = useState<string>("All");

  const categories = useMemo(() => {
    return ["All", ...Array.from(new Set(products.map((product) => product.category)))];
  }, [products]);

  const [debouncedSearchTerm, setDebouncedSearchTerm] = useState<string>(searchTerm);

  useEffect(() => {
    const timer = setTimeout(() => {
      setDebouncedSearchTerm(searchTerm);
    }, 500);

    return () => clearTimeout(timer);
  }, [searchTerm]);

  const filteredProducts = useMemo(() => {
    return products.filter((product) => {
      const matchesSearch = product.name
        .toLowerCase()
        .includes(debouncedSearchTerm.toLowerCase());
      const matchesCategory =
        selectedCategory === "All" || product.category === selectedCategory;

      return matchesSearch && matchesCategory;
    });
  }, [debouncedSearchTerm, selectedCategory, products]);

  useEffect(() => {
    onFilter(filteredProducts);
  }, [filteredProducts, onFilter]);

  return (
    <div className="flex flex-col items-center mb-8">
      {/* Filter Controls */}
      <div className="flex w-full max-w-4xl gap-4">
        {/* Search Bar */}
        <div className="flex items-center w-2/3">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            viewBox="0 0 24 24"
            className="w-10 h-10 mr-2 "
          >
            <path d="M8.25 10.875a2.625 2.625 0 1 1 5.25 0 2.625 2.625 0 0 1-5.25 0Z" />
            <path
              fillRule="evenodd"
              d="M12 2.25c-5.385 0-9.75 4.365-9.75 9.75s4.365 9.75 9.75 9.75 9.75-4.365 9.75-9.75S17.385 2.25 12 2.25Zm-1.125 4.5a4.125 4.125 0 1 0 2.338 7.524l2.007 2.006a.75.75 0 1 0 1.06-1.06l-2.006-2.007a4.125 4.125 0 0 0-3.399-6.463Z"
              clipRule="evenodd"
            />
          </svg>
          <Input
            type="text"
            placeholder="Search for products..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>

        {/* Category Filter */}
        <select
          className="w-1/3 px-4 py-2 border border-gray-300 rounded-lg shadow-sm focus:ring-2 focus:ring-green-600 focus:outline-none"
          value={selectedCategory}
          onChange={(e) => setSelectedCategory(e.target.value)}
        >
          {categories.map((category) => (
            <option key={category} value={category}>
              {category}
            </option>
          ))}
        </select>
      </div>
    </div>
  );
}

