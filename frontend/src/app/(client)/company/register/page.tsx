"use client"

import { BoxGroup1 } from "@/app/components/form/BoxGroup1";
import { BoxGroup2 } from "@/app/components/form/BoxGroup2";
import { BoxGroup3 } from "@/app/components/form/BoxGroup3";
import { ButtonGroup2 } from "@/app/components/form/ButtonGroup2";
import { register } from "@/app/services/authService";
import { useState } from "react";
import toast from "react-hot-toast";

export default function Page() {
  const [formData, setFormData] = useState({
    username: "",
    password: "",
    confirmPassword: "",
    role: "CLIENT",
    companyName: "",
    industry: "",
    companySize: "",
    description: "",
  });

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>
  ) => {
    setFormData((prev) => ({
      ...prev,
      [e.target.name]: e.target.value,
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const data = await register({
        username: formData.username,
        password: formData.password,
        confirmPassword: formData.confirmPassword,
        role: "CLIENT",
        companyName: formData.companyName,
        description: formData.description,
      });
      if (data.message?.includes("Success")) {
        toast.success("Đăng ký thành công. Vui lòng chờ admin duyệt.");
        window.location.href = "/login";
      } else {
        toast.error(data.message);
      }
    } catch (error: unknown) {
      const message =
        (error as { response?: { data?: { message?: string } } })?.response?.data?.message ||
        "Đăng ký thất bại";
      toast.error(message);
    }
  };

  return (
    <>
      <div className="py-[60px] sm:min-h-[calc(100vh-121px-72px)] min-h-[calc(100vh-121px-60px)]">
        <div className="contain">
          <div className="bg-white rounded-[8px] border border-[#DEDEDE] px-[20px] py-[50px] max-w-[602px] w-full mx-[auto]">
            <h1 className="font-[700] text-[20px] text-black mb-[20px] text-center">
              Đăng ký (Nhà tuyển dụng)
            </h1>
            <form onSubmit={handleSubmit} action="" className="grid gap-x-[20px] gap-y-[15px]">
              <BoxGroup1
                name="username"
                label="Email *"
                id="emai"
                type="email"
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
              <BoxGroup1
                name="confirmPassword"
                label="Xác nhận lại mật khẩu *"
                id="confirmPassword"
                type="password"
                values={formData.confirmPassword}
                onChange={handleChange}
              />
              <BoxGroup1
                name="companyName"
                label="Tên công ty *"
                id="companyName"
                type="text"
                values={formData.companyName}
                onChange={handleChange}
              />
              <BoxGroup1
                name="industry"
                label="Industry"
                id="industry"
                type="text"
                values={formData.industry}
                onChange={handleChange}
              />
              <BoxGroup2
                name="companySize"
                label="Quy mô công ty"
                id="companySize"
                value={formData.companySize}
                onChange={handleChange}
                options={[
                  {value:"", label:"Select company size"},
                  {value:"1-10", label:"1 - 10"},
                  {value:"11-50", label:"11 - 50"},
                  {value:"51-100", label:"51 - 100"},
                  {value:"100-500", label:"100 - 500"},
                  {value:"500+", label:"500+"},
                ]}
              />
              <BoxGroup3
                name="description"
                label="Nhập mô tả công ty"
                id="description"
                value={formData.description}
                onChange={handleChange}
              />
              <ButtonGroup2
                label="Đăng ký"
              />
            </form>
          </div>
        </div>
      </div>
    </>
  )
}