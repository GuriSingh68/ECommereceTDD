import React, { forwardRef } from 'react';

// Define the props interface
interface InputFieldProps {
    type?: string;
    id?: string;
    name?: string;
    placeholder?: string;
    error?: string;
    value?: string;
    onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void;
    required?: boolean;
}

const Input = forwardRef<HTMLInputElement, InputFieldProps>(({ type = 'text', id, name, placeholder, error, value, required, onChange, ...rest }, ref) => (
    <div>
        <input
            type={type}
            id={id}
            name={name}
            placeholder={placeholder}
            ref={ref}
            value={value}
            required={required}
            onChange={onChange}
            className="block w-full border border-gray-300 rounded-lg p-3 focus:outline-none focus:ring-4 focus:ring-blue-400"
            {...rest}
        />
        {error && <p className="text-red-500">{error}</p>}
    </div>
));

Input.displayName = 'Input';

export default Input;