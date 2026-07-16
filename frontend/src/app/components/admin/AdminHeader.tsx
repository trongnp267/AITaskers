"use client";

import {
  FaBell,
  FaUserCircle,
  FaSearch,
} from "react-icons/fa";

export default function AdminHeader() {
  return (
    <header className="h-20 bg-white shadow-sm flex items-center justify-between px-8">

      <div className="flex items-center bg-gray-100 rounded-lg px-4 py-2 w-96">

        <FaSearch className="text-gray-400" />

        <input
          type="text"
          placeholder="Search..."
          className="ml-3 w-full bg-transparent outline-none"
        />

      </div>

      <div className="flex items-center gap-6">

        <button className="relative">

          <FaBell
            size={20}
            className="text-gray-600"
          />

          <span
            className="absolute -top-2 -right-2
            h-5 w-5 rounded-full bg-red-500
            text-xs flex items-center justify-center text-white"
          >
            5
          </span>

        </button>

        <div className="flex items-center gap-3">

          <FaUserCircle
            size={36}
            className="text-gray-500"
          />

          <div>

            <p className="font-semibold">
              Admin
            </p>

            <p className="text-sm text-gray-500">
              Administrator
            </p>

          </div>

        </div>

      </div>

    </header>
  );
}