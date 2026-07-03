"use client"

import { BoxGroup1 } from "@/app/components/form/BoxGroup1";
import { BoxGroup3 } from "@/app/components/form/BoxGroup3";
import { ButtonGroup2 } from "@/app/components/form/ButtonGroup2";
import { useState } from "react";

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

  const handleChange = (e:any) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]:
        name === "hourlyRate"
          ? Number(value)
          : value,
    }));
  };

  const handleSubmit = async (e:any) => {
    e.preventDefault();

    try {
      const response = await fetch(
        "http://localhost:8081/api/register",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(formData),
        }
      );

      const data = await response.json();

      if (!response.ok) {
        throw new Error(data.message);
      }

      console.log(data);
      alert("Register successfully!");
    } catch (error:any) {
      console.error(error);
      alert(error.message);
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