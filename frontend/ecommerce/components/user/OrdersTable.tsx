'use client';

import React from 'react';

interface Order {
    id: string;
    totalPrice: number;
    status: string;
    paymentStatus: string;
    estimatedDeliveryDate: string;
    orderItems: {
        product: { name: string; price: number };
        quantity: number;
    }[];
}

const OrdersTable: React.FC<{ orders: Order[] }> = ({ orders }) => {
    if (!orders || orders.length === 0) {
        return <div className="text-center text-gray-600 py-6">No orders available</div>;
    }

    return (
        <div className="flex justify-center py-6">
            <div className="w-full max-w-4xl bg-white rounded-lg shadow-md overflow-hidden">
                <table className="min-w-full">
                    <thead className="bg-gradient-to-r from-green-500 to-green-600 text-white">
                    <tr>
                        <th className="px-4 py-2 text-left text-sm font-semibold">#</th>
                        <th className="px-4 py-2 text-left text-sm font-semibold">Total Price</th>
                        <th className="px-4 py-2 text-left text-sm font-semibold">Status</th>
                        <th className="px-4 py-2 text-left text-sm font-semibold">Payment Status</th>
                        <th className="px-4 py-2 text-left text-sm font-semibold">Delivery Date</th>
                        <th className="px-4 py-2 text-left text-sm font-semibold">Products</th>
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
                                    order.paymentStatus === "completed"
                                        ? "text-green-600"
                                        : "text-red-600"
                                }`}
                            >
                                {order.paymentStatus}
                            </td>
                            <td className="px-4 py-2 text-sm text-gray-700">
                                {order.estimatedDeliveryDate
                                    ? new Date(order.estimatedDeliveryDate).toLocaleDateString()
                                    : "N/A"}
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
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default OrdersTable;