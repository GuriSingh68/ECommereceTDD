'use client';

import React, { useState } from 'react';
import { useCart } from '@/lib/CartContext';
import { useRouter } from 'next/navigation';
import api from '@/lib/api';
import toast from 'react-hot-toast';
import Button from '../shared/Button';

export default function MyCart() {
    const API_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080";
  const { cart, updateQuantity, removeFromCart, clearCart } = useCart();
  const [isLoading, setIsLoading] = useState(false);
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [paymentMethod, setPaymentMethod] = useState('cashondelivery');
  const [error, setError] = useState<string | null>(null);
  const router = useRouter();

  const totalPrice = cart.reduce((total, item) => total + item.product.price * item.quantity, 0);

  const paymentoptions = [
    {value: "cashondelivery", label: "Cash On Delivery"},
    {value: "creditcard", label: "Credit Card"},
    {value: "debitcard", label: "Debit Card"},
    {value: "paypal", label:"Pay Pal"}
  ]

    const handlePlaceOrder = async () => {
        setIsLoading(true);
        setError(null);

        // Ensure that the orderData includes orderItems
        const orderItems = cart.map(({ product, quantity }) => ({
            productId: product.productId,
            quantity,
        }));
    console.log(paymentMethod);

        const orderData = {
            totalPrice,
            paymentMethod,
            paymentStatus: paymentMethod === 'cashondelivery' ? 'PENDING' : 'COMPLETED',
            orderItems,
            status: 'PENDING',

        };

        const user = localStorage.getItem("user");
        const parsedUser  = JSON.parse(user!);
        const userId = parsedUser .user_id;

        try {
            const createdOrder = await api(`/orders/create?userId=${userId}`, {
                method: 'POST',
                body: orderData,
            });

            toast.success('Your Order has been placed!', {
                style: {
                    borderRadius: '8px',
                    background: '#16a34a',
                    color: '#fff',
                },
            });
            clearCart();
            setIsDialogOpen(false);
            router.push('/myorder');
        } catch (err: any) {
            console.error('Error placing order:', err);
            setError(err.message || 'Failed to place order. Please try again.');
        } finally {
            setIsLoading(false);
        }
    };

  return (
    <div className="p-6 bg-gray-50 shadow-xl rounded-lg max-w-3xl mx-auto mt-24">
      <h2 className="text-3xl font-extrabold text-gray-800 mb-6 text-center">
        Your <span className="text-green-600">Cart</span>
      </h2>

      {error && <p className="text-red-500 bg-red-100 p-4 rounded mb-4">{error}</p>}

      {cart.length === 0 ? (
        <p className="text-gray-600 text-lg text-center">Your cart is empty.</p>
      ) : (
        <>
          <div className="divide-y divide-gray-200">
            {cart.map(({ product, quantity }) => (
              <div
                key={product.productId}
                className="flex items-center justify-between py-4 transition hover:bg-gray-100 rounded-lg"
              >
                <div className="flex items-center gap-4">
                  <img
                    src={product.imageUrl ? `${API_URL}${product.imageUrl}` : "/pexels-shvetsa-5187579.jpg"}
                    alt={product.name}
                    className="w-16 h-16 rounded-lg object-cover border border-gray-200"
                  />
                  <div>
                    <h3 className="text-lg font-semibold text-gray-800">{product.name}</h3>
                    <p className="text-gray-500 text-sm">${product.price.toFixed(2)}</p>
                  </div>
                </div>
                <div className="flex items-center gap-2">
                  <Button
                    onClick={() => updateQuantity(product.productId, Math.max(1, quantity - 1))}
                    className="w-8 h-8 flex items-center justify-center text-white bg-gray-700 hover:bg-gray-900 rounded-md font-bold shadow-md transition-all"
                  >
                    -
                  </Button>
                  <span className="text-lg font-medium text-gray-800 bg-gray-200 px-3 py-1 rounded-md shadow-sm">
                    {quantity}
                  </span>
                  <Button
                    onClick={() => updateQuantity(product.productId, quantity + 1)}
                    className="w-8 h-8 flex items-center justify-center text-white bg-gray-700 hover:bg-gray-900 rounded-md font-bold shadow-md transition-all"
                  >
                    +
                  </Button>
                  <Button
                    onClick={() => removeFromCart(product.productId)}
                    className="text-white bg-red-500 hover:bg-red-600 px-3 py-1 rounded-md shadow-md transition-all"
                  >
                    Remove
                  </Button>
                </div>
              </div>
            ))}
          </div>

          <div className="text-right mt-6 border-t border-gray-200 pt-4">
            <p className="text-xl font-bold text-gray-800">
              Total: <span className="text-green-600">${totalPrice.toFixed(2)}</span>
            </p>
          </div>

          <Button
            onClick={() => setIsDialogOpen(true)}
            className="mt-6 w-full text-lg font-medium px-6 py-3 rounded-lg shadow-md bg-green-600 hover:bg-green-700 text-white"
          >
            Place Order
          </Button>
        </>
      )}

      {isDialogOpen && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white p-6 rounded-lg shadow-xl w-96">
            <h3 className="text-lg font-bold text-gray-800 mb-4">Order Summary</h3>
            <ul className="mb-4">
              {cart.map(({ product, quantity }) => (
                <li key={product.productId} className="flex justify-between mb-2">
                  <span>{product.name} x {quantity}</span>
                  <span>${(product.price * quantity).toFixed(2)}</span>
                </li>
              ))}
            </ul>
            <p className="text-lg font-semibold text-gray-800 mb-4">
              Total: <span className="text-green-600">${totalPrice.toFixed(2)}</span>
            </p>
            <div className="mb-4">
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Payment Method
              </label>
              <select
                className="w-full border-gray-300 rounded-md shadow-sm"
                value={paymentMethod}
                onChange={(e) => setPaymentMethod(e.target.value)}
              >
                {paymentoptions.map((option)=>(
                  <option key={option.value} value={option.value}>{option.value}</option>
                ))}             
              </select>
            </div>
            <div className="flex gap-4">
              <Button
                onClick={handlePlaceOrder}
                disabled={isLoading}
                className="w-full text-lg font-medium px-4 py-2 rounded-lg shadow-md bg-green-600 hover:bg-green-700 text-white"
              >
                {isLoading ? 'Placing Order...' : 'Confirm'}
              </Button>
              <Button
                onClick={() => setIsDialogOpen(false)}
                className="w-full text-lg font-medium px-4 py-2 rounded-lg shadow-md bg-gray-200 hover:bg-gray-300"
              >
                Cancel
              </Button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
