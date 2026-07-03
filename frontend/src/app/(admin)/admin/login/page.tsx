"use client";

import { useState } from "react";
import { Eye, EyeOff, ShieldCheck } from "lucide-react";

export default function AdminLoginPage() {
  const [showPassword, setShowPassword] = useState(false);

  return (
    <div className="min-h-screen flex">
      <div className="flex flex-1 items-center justify-center bg-gray-100 p-8">
        <div className="bg-white shadow-2xl rounded-3xl p-10 w-full max-w-lg">
          <div className="text-center mb-8">
            <h1 className="text-3xl font-bold text-gray-800">
              Admin Login
            </h1>

            <p className="text-gray-500 mt-2">
              Sign in to continue
            </p>
          </div>

          <form className="space-y-6">

            <div>
              <label className="block mb-2 font-medium">
                Email
              </label>

              <input
                type="email"
                placeholder="admin@example.com"
                className="w-full rounded-xl border p-3 focus:outline-none focus:ring-2 focus:ring-indigo-500"
              />
            </div>

            <div>
              <label className="block mb-2 font-medium">
                Password
              </label>

              <div className="relative">

                <input
                  type={showPassword ? "text" : "password"}
                  placeholder="********"
                  className="w-full rounded-xl border p-3 pr-12 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                />

                <button
                  type="button"
                  onClick={() => setShowPassword(!showPassword)}
                  className="absolute right-4 top-4 text-gray-500"
                >
                  {showPassword ? (
                    <EyeOff size={20} />
                  ) : (
                    <Eye size={20} />
                  )}
                </button>

              </div>
            </div>

            <button
              className="w-full bg-indigo-600 hover:bg-indigo-700 transition text-white py-3 rounded-xl font-semibold"
            >
              Login
            </button>

          </form>

          <p className="text-center text-gray-400 text-sm mt-8">
            © 2026 AI Tasker Admin Panel
          </p>

        </div>
      </div>
    </div>
  );
}