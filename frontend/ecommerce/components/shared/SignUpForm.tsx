import React from "react";
import { useForm } from "react-hook-form";
import { useRouter } from "next/navigation";
import api from "@/lib/api";
import Input from "../shared/Input";
import Button from "../shared/Button";
import toast from "react-hot-toast";

interface RegisterUserDto {
    email: string;
    password: string;
    firstName: string;
    lastName: string;
    phoneNumber: string;
    role: "USER" | "ADMIN"; // Adjust based on your roles
}

const SignUpForm: React.FC = () => {
    const { register, handleSubmit, formState: { errors }, reset } = useForm<RegisterUserDto>();
    const router = useRouter();

    const onSubmit = async (data: RegisterUserDto) => {
        try {
            const response = await api("/signup", {
                method: "POST",
                body: data,
            });

            toast.success("Signup Successful", {
                style: {
                    borderRadius: "8px",
                    background: "#16a34a",
                    color: "#fff",
                }
            });

            reset();
            router.push("/login");
        } catch (error) {
            console.error("Registration failed:", error);
        }
    };

    return (
        <div className="max-w-lg mx-auto mt-10 p-6 border border-gray-200 rounded-lg shadow-lg bg-white transition-all duration-300 hover:shadow-xl">
            <h2 className="text-3xl font-extrabold text-gray-800 text-center mb-8">Create an Account</h2>
            <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
                <Input
                    type="text"
                    id="firstName"
                    placeholder="First Name"
                    {...register("firstName", { required: "First name is required" })}
                    error={errors.firstName?.message}
                />
                <Input
                    type="text"
                    id="lastName"
                    placeholder="Last Name"
                    {...register("lastName", { required: "Last name is required" })}
                    error={errors.lastName?.message}
                />
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
                <Input
                    type="tel"
                    id="phoneNumber"
                    placeholder="Mobile Number"
                    {...register("phoneNumber", { required: "Phone number is required" })}
                    error={errors.phoneNumber?.message}
                />
                <div>
                    <label htmlFor="role" className="block text-sm font-medium text-gray-700">Role</label>
                    <select
                        id="role"
                        {...register("role", { required: "Role is required" })}
                        className="mt-1 block w-full p-3 border border-gray-300 rounded-md focus:ring-2 focus:ring-green-600 focus:outline-none"
                    >
                        <option value="">Select role</option>
                        <option value="USER">User </option>
                        <option value="ADMIN">Admin</option>
                    </select>
                    {errors.role && <p className="text-sm text-red-500 mt-1">{errors.role.message}</p>}
                </div>
                <Button
                    type="submit"
                    className="w-full py-3 rounded-lg font-bold text-white bg-gradient-to-r from-green-600 to-green-700 shadow-lg hover:shadow-xl hover:bg-gradient-to-l focus:outline-none transition-all duration-300"
                    style={{
                        background: 'linear-gradient(to right, #727543, #797142)',
                    }}
                >
                    Sign Up
                </Button>
            </form>
        </div>
    );
};

export default SignUpForm;