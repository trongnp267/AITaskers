"use client"

import { BoxGroup1 } from "@/app/components/form/BoxGroup1";
import { ButtonGroup2 } from "@/app/components/form/ButtonGroup2";
import { register } from "@/app/services/authService";
import { useState } from "react";
import toast from "react-hot-toast";

export default function Page() {
  const [formData, setFormData] = useState({
    username: "",
    password: "",
    confirmPassword: "",
    role: "EXPERT",
    skill: "",
    experience: "",
    certificate: "",
    hourlyRate: 0,
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: name === "hourlyRate" ? Number(value) : value,
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const data = await register({
        username: formData.username,
        password: formData.password,
        confirmPassword: formData.confirmPassword,
        role: "EXPERT",
        description: formData.skill,
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
              Đăng ký (Ứng viên)
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
                name="skill"
                label="Kỹ năng"
                id="skill"
                type="text"
                values={formData.skill}
                onChange={handleChange}
              />
              <BoxGroup1
                name="experience"
                label="Kinh nghiệm"
                id="experience"
                values={formData.experience}
                onChange={handleChange}
              />
              <BoxGroup1
                name="certificate"
                label="Chứng chỉ"
                id="certificate"
                values={formData.certificate}
                onChange={handleChange}
              />
              <BoxGroup1
                name="hourlyRate"
                label="Mức lương theo giờ"
                id="hourlyRate"
                values={formData.hourlyRate}
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