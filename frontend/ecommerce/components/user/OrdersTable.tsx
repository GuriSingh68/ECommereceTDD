'use client';

import React, { useState } from 'react';
import Button from "@/components/shared/Button";
import api from "@/lib/api";

interface Order {
    id: number;
    totalPrice: number;
    status: string;
    payment: {
        paymentMethod: string;
        status: string;
    };
    orderDate: string;
    estimatedDeliveryDate: string;
    orderItems: {
        product: { name: string; price: number };
        quantity: number;
    }[];
}

const OrdersTable: React.FC<{ orders: Order[] }> = ({ orders: initialOrders }) => {
    const [orders, setOrders] = useState<Order[]>(initialOrders);
    const [error, setError] = useState<string | null>(null);

    const handleDeleteOrder = async (orderId: number) => {
        try {
            const confirmDelete = window.confirm('Are you sure you want to delete this order?');
            if (!confirmDelete) return;

            await api(`/orders/deleteOrder?orderId=${orderId}`, {
                method: 'DELETE',
            });

            // Update the orders list after deletion
            setOrders(orders.filter(order => order.id !== orderId));
        } catch (error) {
            console.error('Error deleting order:', error);
            setError('Failed to delete order');
        }
    };

    if (!orders || orders.length === 0) {
        return <div className="text-center text-gray-600 py-6">No orders available</div>;
    }

    return (
        <div className="flex justify-center py-6">
            <div className="w-full max-w-4xl bg-white rounded-lg shadow-md overflow-hidden">
                {error && (
                    <div className="bg-red-100 border-l-4 border-red-500 text-red-700 p-4 mb-4">
                        <p>{error}</p>
                    </div>
                )}
                <table className="min-w-full">
                    <thead className="bg-gradient-to-r from-green-500 to-green-600 text-white">
                    <tr>
                        <th className="px-4 py-2 text-left text-sm font-semibold">#</th>
                        <th className="px-4 py-2 text-left text-sm font-semibold">Total Price</th>
                        <th className="px-4 py-2 text-left text-sm font-semibold">Status</th>
                        <th className="px-4 py-2 text-left text-sm font-semibold">Payment Status</th>
                        <th className="px-4 py-2 text-left text-sm font-semibold">Payment Method</th>
                        <th className="px-4 py-2 text-left text-sm font-semibold">Order Date</th>
                        <th className="px-4 py-2 text-left text-sm font-semibold">Products</th>
                        <th className="px-4 py-2 text-left text-sm font-semibold">Est. Delivery</th>
                        <th className="px-4 py-2 text-left text-sm font-semibold">Actions</th>
                    </tr>
                    </thead>
                    <tbody className="divide-y divide-gray-200">
                    {orders.map((order, index) => (
                        <tr
                            key={order.id}
                            className={`hover:bg-gray-100 transition ${
                                index % 2 === 0 ? "bg-gray-50" : "bg-white"
                            }`}
                        >
                            <td className="px-4 py-2 text-sm text-gray-800 font-medium">{index + 1}</td>
                            <td className="px-4 py-2 text-sm font-bold text-green-600">
                                ${order.totalPrice.toFixed(2)}
                            </td>
                            <td className="px-4 py-2 text-sm capitalize text-gray-700">{order.status}</td>
                            <td
                                className={`px-4 py-2 text-sm capitalize font-medium ${
                                    order.payment.status === "COMPLETED"
                                        ? "text-green-600"
                                        : "text-red-600"
                                }`}
                            >
                                {order.payment.status}
                            </td>
                            <td className="px-4 py-2 text-sm capitalize text-gray-700">{order.payment.paymentMethod}</td>
                            <td className="px-4 py-2 text-sm text-gray-700">
                                {new Date(order.orderDate).toLocaleDateString()}
                            </td>
                            <td className="px-4 py-2 text-sm text-gray-600">
                                <ul className="list-disc pl-5">
                                    {order.orderItems && order.orderItems.length > 0 ? (
                                        order.orderItems.map((item, idx) => (
                                            <li key={idx}>
                                                {item.quantity} x {item.product?.name || "Unknown"} ($
                                                {item.product?.price?.toFixed(2) || "0.00"})
                                            </li>
                                        ))
                                    ) : (
                                        <li>No products available</li>
                                    )}
                                </ul>
                            </td>
                            <td className="px-4 py-2 text-sm capitalize text-gray-700">
                                {order.estimatedDeliveryDate ?
                                    new Date(order.estimatedDeliveryDate).toLocaleDateString() :
                                    'Calculating...'}
                            </td>
                            <td className="px-4 py-2">
                                {order.status === "COMPLETED" && (
                                    <Button
                                        onClick={() => handleDeleteOrder(order.id)}
                                        className="text-white bg-red-500 hover:bg-red-600 px-3 py-1 rounded-md shadow-md transition-all"
                                    >
                                        Delete
                                    </Button>
                                )}
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default OrdersTable;