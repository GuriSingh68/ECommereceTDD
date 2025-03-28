import type { Metadata } from "next";
import { Toaster } from "react-hot-toast";
import localFont from "next/font/local";
import "./globals.css";

// components
import Navbar from "@/components/shared/Navbar";
import Footer from "@/components/shared/Footer";
import { UserProvider } from "@/lib/userContext";
import { CartProvider } from "@/lib/CartContext";
import NavbarWrapper from "@/app/NavbarWrapper";

const geistSans = localFont({
  src: "./fonts/GeistVF.woff",
  variable: "--font-geist-sans",
  weight: "100 900",
});
const geistMono = localFont({
  src: "./fonts/GeistMonoVF.woff",
  variable: "--font-geist-mono",
  weight: "100 900",
});

export const metadata: Metadata = {
  title: "FarmKart",
  description: "Marketplace for Farmers to sell fresh produce",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
    return (
    <html lang="en">
      <body className={`${geistSans.variable} ${geistMono.variable} antialiased flex flex-col min-h-screen`} >
        <UserProvider>
        <CartProvider>
            {/* Navbar */}
            <NavbarWrapper />

            {/* Main content */}
            <main className="flex-grow pt-16">
              <Toaster />
              {children}

            </main>

          <Footer />
          </CartProvider>
        </UserProvider>
      </body>
    </html>
  );
}
