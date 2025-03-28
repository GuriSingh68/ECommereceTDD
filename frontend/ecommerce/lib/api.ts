const API_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080";

interface RequestOptions extends RequestInit {
    headers?: Record<string, string>;
    body?: any;
}

const api = async (url: string, options: RequestOptions = {}) => {
    try {
        // Prepare headers
        const headers: Record<string, string> = {
            "Content-Type": "application/json",
            ...options.headers,
        };

        const response = await fetch(`${API_URL}${url}`, {
            ...options,
            headers,
            body: options.body ? JSON.stringify(options.body) : undefined,
        });

        if (!response.ok) {
            const errorResponse = await response.json();
            throw new Error(`HTTP error! Status: ${response.status}, Message: ${errorResponse.message}`);
        }

        return await response.json();
    } catch (error: any) {
        console.error(`API error: ${error.message || error}`);
        throw error;
    }
};

export default api;