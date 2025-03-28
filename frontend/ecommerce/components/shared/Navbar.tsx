'use client';

import { useState } from "react";
import Link from "next/link";
import { usePathname } from "next/navigation";
import { useUser  } from "@/lib/userContext";
import { useRouter } from "next/navigation";

interface NavLink {
  href: string;
  label: string;
}

function Navbar() {
  const pathname = usePathname();
  const [isMenuOpen, setIsMenuOpen] = useState<boolean>(false);
  const { user, logout } = useUser ();
  const router = useRouter();

  const commonLinks: NavLink[] = [
    { href: "/", label: "Home" },
    { href: "/product", label: "Product" },
    { href: "/about", label: "About Us" },
  ];

  const customerLinks: NavLink[] = [
    { href: "/home", label: "Home" },
    { href: "/product", label: "Product" },
    { href: "/about", label: "About Us" },
    { href: "/myorder", label: "My Orders" },
    { href: "/cart", label: "My Cart" },
    { href: `/customer/profile/${user?._id}`, label: "Profile" }
  ];

  const adminLinks: NavLink[] = [
    { href: "/dashboard", label: "Dashboard" },
    { href: `/admin/profile/${user?._id}`, label: "Profile" },
    { href: "/manageOrders", label: "Orders" }
  ];
  console.log(":::", user?.role);
  const roleBasedLinks = user?.role === "USER" ? customerLinks : user?.role === "ADMIN" ? adminLinks : [];

  const handleLogout = () => {
    logout();
    router.push("/");
  };

  return (
      <nav className="fixed top-0 left-0 right-0 z-50 bg-navbar-bg shadow-md">
        <div className="flex items-center justify-between px-6 py-4">
          {/* LOGO */}
          <div className="flex items-center text-xl font-bold text-green-600">
            <Link href="/">
              <img src="/farmkartLogo.png" alt="FarmKart Logo" className="w-15 h-8 mr-2" />
            </Link>
          </div>

          {/* Menu for mobile */}
          <button
              className="text-gray-700 md:hidden"
              onClick={() => setIsMenuOpen((prev) => !prev)}
              aria-label="Toggle Menu"
          >
            <svg
                xmlns="http://www.w3.org/2000/svg"
                className="w-6 h-6"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
            >
              <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d={
                    isMenuOpen
                        ? "M6 18L18 6M6 6l12 12" // Close icon
                        : "M4 6h16M4 12h16M4 18h16" // Hamburger icon
                  }
              />
            </svg>
          </button>

          {/* Navigation Links */}
          <div
              className={`${
                  isMenuOpen ? "block" : "hidden"
              } absolute top-full left-0 w-full bg-navbar-bg md:static md:flex md:space-x-8 md:bg-transparent md:w-auto md:items-center md:justify-center z-20`}
          >
            {/* Render common links only if user is not logged in */}
            {!user &&
                commonLinks.map((link) => (
                    <Link
                        key={link.href}
                        href={link.href}
                        className={`block px-4 py-2 text-black relative md:inline md:px-0
                  ${
                            pathname === link.href
                                ? "text-white after:scale-x-100"
                                : "hover:text-white hover:after:scale-x-100"
                        }
                  after:content-[''] after:block after:w-full after:h-[2px] after:bg-white after:origin-left after:transition-transform after:duration-300 after:scale-x-0 md:after:mx-auto after:mt-1`}
                        onClick={() => setIsMenuOpen(false)}
                    >
                      {link.label}
                    </Link>
                ))}

            {/* Render role-based links if user is logged in */}
            {user &&
                roleBasedLinks.map((link) => (
                    <Link
                        key={link.href}
                        href={link.href}
                        className={`block px-4 py-2 text-black relative md:inline md:px-0
                  ${
                            pathname === link.href
                                ? "text-white after:scale-x-100"
                                : "hover:text-white hover:after:scale-x-100"
                        }
                  after:content-[''] after:block after:w-full after:h-[2px] after:bg-white after:origin-left after:transition-transform after:duration-300 after:scale-x-0 md:after:mx-auto after:mt-1`}
                        onClick={() => setIsMenuOpen(false)}
                    >
                      {link.label}
                    </Link>
                ))}

            {/* Mobile Login/Logout Button */}
            <div className="block md:hidden px-4 py-2">
              {user ? (
                  <button
                      onClick={handleLogout}
                      className="w-full px-4 py-2 text-white bg-green-800 rounded hover:bg-green-600"
                  >
                <span className="flex justify-between">
                  Log Out
                  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" className="size-6 ml-2">
                    <path fillRule="evenodd" d="M7.5 3.75 A1.5 0 0 0 6 5.25v13.5a1.5 1.5 0 0 0 1.5 1.5h6a1.5 1.5 0 0 0 1.5-1.5V15a.75.75 0 0 1 1.5 0v3.75a3 3 0 0 1-3 3h-6a3 3 0 0 1-3-3V5.25a3 3 0 0 1 3-3h6a3 3 0 0 1 3 3V9A.75.75 0 0 1 15 9V5.25a1.5 1.5 0 0 0-1.5-1.5h-6Zm10.72 4.72a.75.75 0 0 1 1.06 0l3 3a.75.75 0 0 1 0 1.06l-3 3a.75.75 0 1 1-1.06-1.06l1.72-1.72H9a.75.75 0 0 1 0-1.5h10.94l-1.72-1.72a.75.75 0 0 1 0-1.06Z" clipRule="evenodd" />
                  </svg>
                </span>
                  </button>
              ) : (
                  <Link href="/login">
                    <button className="w-full px-4 py-2 text-white bg-green-600 rounded hover:bg-green-800">
                  <span className="flex justify-between ">
                    Log In
                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" className="size-6 ml-2">
                      <path fillRule="evenodd" d="M12 2.25c-5.385 0-9.75 4.365-9.75 9.75s4.365 9.75 9.75 9.75 9.75-4.365 9.75-9.75S17.385 2.25 12 2.25Zm4.28 10.28a.75.75 0 0 0 0-1.06l-3-3a.75.75 0 1 0-1.06 1.06l1.72 1.72H8.25a.75.75 0 0 0 0 1.5h5.69l-1.72 1.72a.75.75 0 1 0 1.06 1.06l3-3Z" clipRule="evenodd" />
                    </svg>
                  </span>
                    </button>
                  </Link>
              )}
            </div>
          </div>

          {/* Desktop Login/Logout Button */}
          <div className="hidden md:block">
            {user ? (
                <button
                    onClick={handleLogout}
                    className="px-4 py-2 text-white bg-green-800 rounded-lg hover:bg-green-600"
                >
              <span className="flex justify-between">
                Log Out
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" className="size-6 ml-2">
                    <path fillRule="evenodd" d="M7.5 3.75A1.5 1.5 0 0 0 6 5.25v13.5a1.5 1.5 0 0 0 1.5 1.5h6a1.5 1.5 0 0 0 1.5-1.5V15a.75.75 0 0 1 1.5 0v3.75a3 3 0 0 1-3 3h-6a3 3 0 0 1-3-3V5.25a3 3 0 0 1 3-3h6a3 3 0 0 1 3 3V9A.75.75 0 0 1 15 9V5.25a1.5 1.5 0 0 0-1.5-1.5h-6Zm10.72 4.72a.75.75 0 0 1 1.06 0l3 3a.75.75 0 0 1 0 1.06l-3 3a.75.75 0 1 1-1.06-1.06l1.72-1.72H9a.75.75 0 0 1 0-1.5h10.94l-1.72-1.72a.75.75 0 0 1 0-1.06Z" clipRule="evenodd" />
                  </svg>
              </span>
                </button>
            ) : (
                <Link href="/login">
                  <button className="px-4 py-2 text-white bg-green-600 rounded-lg hover:bg-green-800">
                <span className="flex justify-between ">
                  Log In
                  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" className="size-6 ml-2">
                    <path fillRule="evenodd" d="M12 2.25c-5.385 0-9.75 4.365-9.75 9.75s4.365 9.75 9.75 9.75 9.75-4.365 9.75-9.75S17.385 2.25 12 2.25Zm4.28 10.28a.75.75 0 0 0 0-1.06l-3-3a.75.75 0 1 0-1.06 1.06l1.72 1.72H8.25a.75.75 0 0 0 0 1.5h5.69l-1.72 1.72a.75.75 0 1 0 1.06 1.06l3-3Z" clipRule="evenodd" />
                  </svg>
                </span>
                  </button>
                </Link>
            )}
          </div>
        </div>
      </nav>
  );
}

export default Navbar;