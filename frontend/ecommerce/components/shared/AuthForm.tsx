'use client';

import React, { useState } from "react";
import SignInForm from "./SignInForm";
import SignUpForm from "./SignUpForm";
import TogglePanel from "./TogglePanel";

const AuthForm: React.FC = () => {
    const [isSignIn, setIsSignIn] = useState(true);

    const toggleForm = () => {
        setIsSignIn(!isSignIn);

    };

    return (
        <div className="flex justify-center items-center min-h-screen p-4">
            <div className="bg-white rounded-2xl shadow-2xl p-8 w-full max-w-4xl flex flex-col lg:flex-row transition-transform duration-500">
                <div className="flex-1 p-6">
                    {isSignIn ? (
                        <SignInForm />
                    ) : (
                        <SignUpForm />
                    )}
                </div>

                {/* Toggle Panel Section */}
                <div className="flex-1 p-6 flex flex-col justify-center items-center bg-gradient-to-br from-white-100 to-white-300 rounded-2xl lg:rounded-none lg:rounded-r-2xl">
                    <TogglePanel
                        title={isSignIn ? 'Hello, Subscriber!' : 'Welcome Back!'}
                        description={
                            isSignIn
                                ? 'Sign up now to unlock a world of vibrant flavors, seasonal delights, and exclusive offers.' : 'Please enter your details to dive back into a world of fresh flavors and exciting features.'
                        }
                        onToggle={toggleForm}
                    />
                </div>
            </div>
        </div>


    );
};

export default AuthForm;
