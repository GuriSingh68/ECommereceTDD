'use client';

import React, { useEffect, useState } from 'react';
import { useUser  } from '@/lib/userContext';
import api from '@/lib/api';

export type Product = {
    id: number; // Assuming you're using an integer ID for MySQL
    name: string;
    price: number;
    quantity: number; // Include quantity in the product type
};

export type Order = {
    id: number; // Assuming you're using an integer ID for MySQL
    customer: string;
    totalPrice: number;
    estimatedDeliveryDate: Date;
    paymentMethod: string;
    status: string;
    paymentStatus: string;
    transactionId?: string;
    products: Product[];
};

const ManageOrders: React.FC = () => {
    const { user } = useUser ();
    const [orders, setOrders] = useState<Order[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchOrders = async () => {
            if (user && user.role === 'ADMIN') { // Check if the user is an admin
                try {
                    const response = await api('/orders', { // Adjust the endpoint as necessary
                        method: 'GET',
                    });
                    setOrders(response); // Update the orders state
                } catch (err) {
                    setError('Failed to fetch orders');
                } finally {
                    setLoading(false);
                }
            } else {
                setError('You are not authorized to view this page.');
                setLoading(false);
            }
        };

        fetchOrders(); // Fetch orders on component mount
    }, [user]);

    if (loading) return <div>Loading...</div>; // Loading message
    if (error) return <div>{error}</div>; // Error message

    return (
        <div className="flex justify-center py-6">
            <div className="w-full max-w-4xl bg-white rounded-lg shadow-md overflow-hidden">
                <table className="min-w-full">
                    <thead className="bg-gradient-to-r from-green-500 to-green-600 text-white">
                    <tr>
                        <th className="px-4 py-2 text-left text-sm font-semibold">Order ID</th>
                        <th className="px-4 py-2 text-left text-sm font-semibold">Products</th>
                        <th className="px-4 py-2 text-left text-sm font-semibold">Total Quantity</th>
                        <th className="px-4 py-2 text-left text-sm font-semibold">Total Price</th>
                        <th className="px-4 py-2 text-left text-sm font-semibold">Status</th>
                        <th className="px-4 py-2 text-left text-sm font-semibold">Estimated Delivery</th>
                        <th className="px-4 py-2 text-left text-sm font-semibold">Payment Status</th>
                        <th className="px-4 py-2 text-left text-sm font-semibold">Transaction ID</th>
                    </tr>
                    </thead>
                    <tbody className="divide-y divide-gray-200">
                    {orders.map((order, index) => {
                        const totalQuantity = order.products.reduce((sum, product) => sum + product.quantity, 0);

                        return (
                            <tr
                                key={order.id.toString()} // Use id for MySQL
                                className={`hover:bg-gray-100 transition ${index % 2 === 0 ? "bg-gray-50" : "bg-white"}`}
                            >
                                <td className="px-4 py-2 text-sm text-gray-800 font-medium">{order.id.toString()}</td>
                                <td className="px-4 py-2 text-sm text-gray-600">
                                    <ul className="list-disc pl-5">
                                        {order.products.map((product) => (
                                            <li key={product.id.toString() || "Unknown"}>
                                                {product.name || "Unknown"} (Quantity: {product.quantity || "unknown"})
                                            </li>
                                        ))}
                                    </ul>
                                </td>
                                <td className="px-4 py-2 text-sm text-gray-700">{totalQuantity}</td>
                                <td className="px-4 py-2 text-sm font-bold text-green-600">${order.totalPrice.toFixed(2)}</td>
                                <td className="px-4 py-2 text-sm capitalize text-gray-700">{order.status}</td>
                                <td className="px-4 py-2 text-sm text-gray-700">
                                    {new Date(order.estimatedDeliveryDate).toLocaleDateString()}
                                </td>
                                <td className={`px-4 py-2 text-sm capitalize font-medium ${order.paymentStatus === "completed" ? "text-green-600" : "text-red-600"}`}>
                                    {order.paymentStatus}
                                </td>
                                <td className="px-4 py-2 text-sm text-gray-800 font-medium">{order.transactionId}</td>
                            </tr>
                        );
                    })}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default ManageOrders;