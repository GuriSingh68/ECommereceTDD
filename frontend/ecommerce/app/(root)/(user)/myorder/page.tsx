'use client';

import React, { useEffect, useState } from 'react';
import api from '@/lib/api';
import OrdersTable from '@/components/user/OrdersTable';

const fetchCustomerOrders = async () => {
    try {
        const user = localStorage.getItem('user');
        const userId = JSON.parse(user!).user_id;
        return await api(`/orders/customer-orders?customerId=${userId}`, {
            method: 'GET',
        });
    } catch (error) {
        console.error('Failed to fetch orders:', error);
        return [];
    }
};

const OrdersPage = () => {
    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const loadOrders = async () => {
            const fetchedOrders = await fetchCustomerOrders();
            setOrders(fetchedOrders);
            setLoading(false);
        };

        loadOrders();
    }, []);

    if (loading) {
        return <p>Loading your orders...</p>;
    }

    if (orders.length === 0) {
        return <p>No orders found.</p>;
    }

    return (
        <div>
            <h1 className="text-3xl font-bold text-gray-800 text-center mb-8 mt-4">My <span className='text-green-600'>Orders</span></h1>
            <OrdersTable orders={orders} />
        </div>
    );
};

export default OrdersPage;