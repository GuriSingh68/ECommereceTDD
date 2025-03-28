import React from 'react';
import Button from './Button';

interface TogglePanelProps {
    title: string;
    description: string;
    onToggle: () => void;
}

const TogglePanel: React.FC<TogglePanelProps> = ({ title, description, onToggle }) => {
    return (
        <div className="text-center mt-8 p-6 border border-gray-200 rounded-lg shadow-lg bg-white transition-transform hover:scale-105 hover:shadow-2xl">
            <h1 className="text-2xl font-bold text-gray-800 tracking-wide mb-2">
                {title}
            </h1>
            <p className="text-gray-600 mb-4 text-sm leading-relaxed">
                {description}
            </p>
            <Button
                className="relative w-full text-white font-semibold rounded-lg py-3 overflow-hidden transition-transform transform"
                style={{
                    background: 'linear-gradient(to right, #727543, #797142)',
                }}
                onClick={onToggle}
            >
                {title.includes('Welcome Back!') ? 'Sign In' : 'Sign Up'}
            </Button>
        </div>
    );
};


export default TogglePanel;