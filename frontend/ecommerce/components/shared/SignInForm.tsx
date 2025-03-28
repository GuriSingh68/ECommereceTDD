import React, { useState } from "react";
import { useForm } from "react-hook-form";
import { useRouter } from "next/navigation";
import api from "@/lib/api";
import Input from "../shared/Input";
import Button from "../shared/Button";
import { useUser  } from "@/lib/userContext";
import toast from "react-hot-toast";

interface LoginDto {
    email: string;
    password: string;
}

const SignInForm: React.FC = () => {
    const { register, handleSubmit, formState: { errors } } = useForm<LoginDto>();
    const router = useRouter();
    const { setUser  } = useUser (); // Get setUser  from context
    const [errorMessage, setErrorMessage] = useState<string | null>(null);

    const onSubmit = async (data: LoginDto) => {
        setErrorMessage(null);
        try {
            const response = await api("/login", {
                method: "POST",
                body: data,
            });

            // Assuming the response contains user details
            if (response.user) {
                localStorage.setItem("user", JSON.stringify(response.user));
                setUser (response.user); // Update user context

                toast.success("Signin Successful", {
                    style: {
                        borderRadius: "8px",
                        background: "#16a34a",
                        color: "#fff",
                    }
                });

                const redirectPath = response.user.role === "ADMIN" ? "/dashboard" : "/home";
                router.push(redirectPath);
            } else {
                setErrorMessage("Login failed. Please check your credentials and try again.");
            }
        } catch (error) {
            console.error("Login failed:", error);
            setErrorMessage("Login failed. Please check your credentials and try again.");
        }
    };

    return (
        <div className="max-w-md mx-auto mt-10 p-8 border border-gray-300 rounded-lg shadow-lg bg-white transition-all duration-300 hover:shadow-xl">
            <h2 className="text-3xl font-bold text-center text-gray-800 mb-6">Welcome Back!</h2>
            <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
                <Input
                    type="email"
                    id="email"
                    placeholder="Email"
                    {...register("email", { required: "Email is required" })}
                    error={errors.email?.message}
                />
                <Input
                    type="password"
                    id="password"
                    placeholder="Password"
                    {...register("password", { required: "Password is required" })}
                    error={errors.password?.message}
                />
                {errorMessage && <p className="text-sm text-red-500 mt-2">{errorMessage}</p>}
                <Button
                    type="submit"
                    className="w-full py-3 rounded-lg font-bold text-white bg-gradient-to-r from-green-600 to-green-700 shadow-lg hover:shadow-xl hover:bg-gradient-to-l focus:outline-none transition-all duration-300"
                    style={{
                        background: 'linear-gradient(to right, #727543, #797142)',
                    }}
                >
                    Sign In
                </Button>
            </form>
        </div>
    );
};

export default SignInForm;