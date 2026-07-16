"use client";

import { useState } from "react";
import { Eye, EyeOff, ShieldCheck } from "lucide-react";
import { useRouter } from "next/navigation";
import { login } from "@/app/services/authService";


export default function AdminLoginPage() {
  const router = useRouter();

  const [username, setUsername] = useState("");

  const [password, setPassword] = useState("");

  const [loading, setLoading] = useState(false);

  const [error, setError] = useState("");
  const [showPassword, setShowPassword] = useState(false);

  const handleLogin = async (
      e: React.FormEvent<HTMLFormElement>
  ) => {

      e.preventDefault();

      try {

          setLoading(true);

          setError("");

          const response = await login({

              username,

              password,

          });

          if (response.user.role !== "ADMIN") {
              setError("You do not have permission to access the admin panel.");
              return;
          }

          localStorage.setItem("token", response.token);
          localStorage.setItem("user", JSON.stringify(response.user));

          router.push("/admin/dashboard");

      } catch (error: any) {

          setError(
              error.response?.data?.message ??
              "Login failed"
          );

      } finally {

          setLoading(false);

      }

  };

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

          <form onSubmit={handleLogin} className="space-y-6">

            <div>
              <label className="block mb-2 font-medium">
                username
              </label>

              <input
                type="text"
                className="w-full rounded-xl border p-3 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
              />
            </div>

            <div>
              <label className="block mb-2 font-medium">
                Password
              </label>

              <div className="relative">

                <input
                  type={showPassword ? "text" : "password"}
                  className="w-full rounded-xl border p-3 pr-12 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
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

            {
                error && (

                    <p className="text-red-500 text-sm">

                        {error}

                    </p>

                )
            }

            <button
              className="w-full bg-indigo-600 hover:bg-indigo-700 transition text-white py-3 rounded-xl font-semibold"
            >
              {
                  loading
                      ? "Signing in..."
                      : "Login"
              }
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