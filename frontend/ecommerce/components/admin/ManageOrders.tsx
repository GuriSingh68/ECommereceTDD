'use client';

import React, { useEffect, useState } from 'react';
import api from '@/lib/api';
import Button from "@/components/shared/Button";

export type UserInfo = {
    userId: number;
    firstName: string;
    lastName: string;
    email: string;
};

export type Product = {
    id: number;
    name: string;
    price: number;
    quantity: number;
};

export type Order = {
    id: number;
    totalPrice: number;
    status: string;
    payment: {
        paymentMethod: string;
        status: string;
    };
    orderDate: string;
    estimatedDeliveryDate: string;
    user: UserInfo;
    orderItems: {
        product: {
            id: number;
            name: string;
            price: number
        };
        quantity: number;
    }[];
};

const ManageOrders: React.FC = () => {
    const [orders, setOrders] = useState<Order[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {

        fetchOrders();
    }, []);

    const fetchOrders = async () => {
        try {
            const user = localStorage.getItem("user");
            if (user) {
                const parsedUser = JSON.parse(user);
                if (parsedUser.role === "ADMIN") {
                    const response = await api('/orders/admin/allOrders', {
                        method: 'GET',
                    });
                    setOrders(response);
                } else {
                    setError('You are not authorized to view this page.');
                }
            } else {
                setError('Please login to view this page.');
            }
        } catch (err) {
            setError('Failed to fetch orders');
        } finally {
            setLoading(false);
        }
    };

    const handleDeleteOrder = async (orderId: number) => {
        try {
            const confirmDelete = window.confirm('Are you sure you want to delete this order?');
            if (!confirmDelete) return;

            await api(`/orders/deleteOrder?orderId=${orderId}`, {
                method: 'DELETE',
            });

            // Refresh the orders list after deletion
            fetchOrders();
        } catch (error) {
            console.error('Error deleting order:', error);
            setError('Failed to delete order');
        }
    };


    if (loading) return <div className="flex justify-center items-center h-64">Loading...</div>;
    if (error) return <div className="text-red-500 text-center p-4">{error}</div>;

    return (
        <div className="container mx-auto px-4 py-8">
            <h1 className="text-3xl font-bold text-gray-800 mb-8">Manage Orders</h1>

            <div className="overflow-x-auto shadow-md rounded-lg">
                <table className="min-w-full bg-white">
                    <thead className="bg-gradient-to-r from-green-500 to-green-600 text-white">
                    <tr>
                        <th className="px-6 py-3 text-left text-sm font-semibold">Order ID</th>
                        <th className="px-6 py-3 text-left text-sm font-semibold">User</th>
                        <th className="px-6 py-3 text-left text-sm font-semibold">User Email</th>
                        <th className="px-6 py-3 text-left text-sm font-semibold">Products</th>
                        {/*<th className="px-6 py-3 text-left text-sm font-semibold">Total Quantity</th>*/}
                        <th className="px-6 py-3 text-left text-sm font-semibold">Total Price</th>
                        <th className="px-6 py-3 text-left text-sm font-semibold">Status</th>
                        <th className="px-6 py-3 text-left text-sm font-semibold">Order Date</th>
                        <th className="px-6 py-3 text-left text-sm font-semibold">Payment Status</th>
                        <th className="px-6 py-3 text-left text-sm font-semibold">Payment Method</th>
                        <th className="px-6 py-3 text-left text-sm font-semibold">Est. Delivery</th>

                    </tr>
                    </thead>
                    <tbody className="divide-y divide-gray-200">
                    {orders.map((order) => {
                        const totalQuantity = order.orderItems.reduce(
                            (sum, item) => sum + item.quantity, 0
                        );
                        const userName = `${order.user?.firstName || ''} ${order.user?.lastName || ''}`.trim() || 'N/A';

                        return (
                            <tr key={order.id} className="hover:bg-gray-50">
                                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                                    {order.id}
                                </td>

                                {/*<td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">*/}
                                {/*    {order.user?.userId || 'N/A'}*/}
                                {/*</td>*/}
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                    {userName}
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                    {order.user?.email || 'N/A'}
                                </td>
                                <td className="px-6 py-4 text-sm text-gray-500">
                                    <ul className="list-disc pl-5">
                                        {order.orderItems.map((item, idx) => (
                                            <li key={idx}>
                                                {item.quantity} x {item.product?.name || 'Unknown'}
                                                (${item.product?.price?.toFixed(2) || '0.00'})
                                            </li>
                                        ))}
                                    </ul>
                                </td>
                                {/*<td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">*/}
                                {/*    {totalQuantity}*/}
                                {/*</td>*/}
                                <td className="px-6 py-4 whitespace-nowrap text-sm font-bold text-green-600">
                                    ${order.totalPrice.toFixed(2)}
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm capitalize text-gray-500">
                                    {order.status}
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                    {new Date(order.orderDate).toLocaleDateString()}
                                </td>
                                <td className={`px-6 py-4 whitespace-nowrap text-sm font-medium ${
                                    order.payment.status === 'COMPLETED'
                                        ? 'text-green-600'
                                        : 'text-red-600'
                                }`}>
                                    {order.payment.status}
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                    {order.payment.paymentMethod || 'N/A'}
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                    {order.estimatedDeliveryDate ?
                                        new Date(order.estimatedDeliveryDate).toLocaleDateString() :
                                        'Calculating...'}
                                </td>
                                <Button
                                    onClick={() => handleDeleteOrder(order.id)}
                                    className="text-white bg-red-500 hover:bg-red-600 px-3 py-1 rounded-md shadow-md transition-all"
                                >
                                    Delete
                                </Button>
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