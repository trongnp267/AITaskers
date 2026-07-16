"use client"

import { BoxGroup1 } from "@/app/components/form/BoxGroup1";
import { ButtonGroup2 } from "@/app/components/form/ButtonGroup2";
import { login } from "@/app/services/authService";
import { saveSession } from "@/app/lib/auth";
import Link from "next/link";
import { useState } from "react";
import toast from "react-hot-toast";

export default function Page() {
  const [formData, setFormData] = useState({
    username: "",
    password: "",
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData((prev) => ({
      ...prev,
      [e.target.name]: e.target.value,
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const data = await login(formData);
      saveSession(data.token, data.user);
      toast.success("Đăng nhập thành công");
      window.location.href = "/";
    } catch (error: unknown) {
      const message =
        (error as { response?: { data?: { message?: string } } })?.response?.data
          ?.message || "Đăng nhập thất bại";
      toast.error(message);
    }
  };

  return (
    <>
      <div className="py-[60px] sm:min-h-[calc(100vh-121px-72px)] min-h-[calc(100vh-121px-60px)]">
        <div className="contain">
          <div className="bg-white rounded-[8px] border border-[#DEDEDE] px-[20px] py-[50px] max-w-[602px] w-full mx-[auto]">
            <h1 className="font-[700] text-[20px] text-black mb-[20px] text-center">
              Đăng nhập
            </h1>
            <form onSubmit={handleSubmit} className="grid gap-x-[20px] gap-y-[15px]">
              <BoxGroup1
                name="username"
                label="Tên đăng nhập *"
                id="username"
                type="text"
                values={formData.username}
                onChange={handleChange}
              />
              <BoxGroup1
                name="password"
                label="Mật khẩu *"
                id="password"
                type="password"
                values={formData.password}
                onChange={handleChange}
              />
              <ButtonGroup2 label="Đăng nhập" />
            </form>
            <p className="text-center text-[14px] mt-[15px] text-gray-600">
              Chưa có tài khoản?{" "}
              <Link href="/register" className="text-blue-600 font-[600]">
                Đăng ký
              </Link>
            </p>
          </div>
        </div>
      </div>
    </>
  )
}
