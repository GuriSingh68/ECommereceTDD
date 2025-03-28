'use client'

import Link from "next/link";

export default function Hero () {
    return (
      <section
          className="relative w-full h-screen bg-cover bg-center bg-no-repeat"
          style={{
            backgroundImage: "url('/pexels-loifotos-1660898.jpg')",
          }}
      >
        {/* Overlay */}
        <div className="absolute inset-0 bg-black/50" />
  
        {/* Content */}
        <div className="relative z-10 flex flex-col items-center justify-center h-full px-4 text-center text-white">
          <h1 className="text-4xl font-extrabold md:text-6xl">
            <span className="text-green-500">Fresh</span> From The Farm To Your Table.
          </h1>
          <p className="mt-4 text-lg md:text-2xl">
            Quality Farm Products Directly From Trusted Farmers.
          </p>
          <Link href="/product">
            <button className="mt-8 px-6 py-3 text-lg font-semibold bg-green-600 text-white rounded-md relative overflow-hidden group">
                <span
                className="absolute inset-0 bg-green-500 translate-x-full group-hover:translate-x-0 transition-transform duration-500 ease-in-out"
                 />
                 <span className="relative group-hover:text-gray-100 transition-colors duration-300 flex justify-center">
                     Explore Products
                      <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" className="size-6 ml-2">
                        <path fillRule="evenodd" d="M10.5 3.75a6.75 6.75 0 1 0 0 13.5 6.75 6.75 0 0 0 0-13.5ZM2.25 10.5a8.25 8.25 0 1 1 14.59 5.28l4.69 4.69a.75.75 0 1 1-1.06 1.06l-4.69-4.69A8.25 8.25 0 0 1 2.25 10.5Z" clipRule="evenodd" />
                      </svg>
                 </span>
            </button>
          </Link>
        </div>
      </section>  
    )
}
