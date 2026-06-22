"use client"

import { BoxGroup1 } from "@/app/components/form/BoxGroup1";
import { BoxGroup2 } from "@/app/components/form/BoxGroup2";
import { BoxGroup3 } from "@/app/components/form/BoxGroup3";
import { ButtonGroup1 } from "@/app/components/form/ButtonGroup1";
import Link from "next/link";
import { useState } from "react";

export default function Page() {
  const id = JSON.parse(localStorage.getItem("user") || "{}").id;

  const [formData, setFormData] = useState({
    clientId: id,
    title: "",
    description: "",
    positionRequirement: "",
    requiredTechnologies: "",
    minExperienceYears: 0,
    budgetMin: 0,
    budgetMax: 0,
    deadline: "",
    jobSkills: [] as string[],
  });

  const [skillInput, setSkillInput] = useState("");

  const handleChange = (e: any) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
    ...prev,
    [name]:
      name === "minExperienceYears" ||
      name === "budgetMin" ||
      name === "budgetMax"
        ? Number(value)
        : value,
  }));
  };

  const addSkill = () => {
    if (!skillInput.trim()) return;

    setFormData({
      ...formData,
      jobSkills: [...formData.jobSkills, skillInput.trim()],
    });

    setSkillInput("");
  };

  const handleSubmit = async (e:any) => {
    e.preventDefault();

    try {
      const token = localStorage.getItem("token");

      const response = await fetch(
        "http://localhost:8081/jobs",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify(formData),
        }
      );

      const data = await response.json();

      if (!response.ok) {
        throw new Error(data.message);
      }

      alert("Create job successfully!");

    } catch (error) {
      console.error(error);
    }
  };
  return (
    <>
      <div className="py-[60px]">
        <div className="contain">
          <div className="bg-white rounded-[8px] border border-[#DEDEDE] p-[20px]">
            <div className="flex justify-between items-center mb-[20px] gap-[20px] flex-wrap">
              <h1 className="font-[700] text-[20px] text-black">
                Thêm mới công việc
              </h1>
              <Link 
                href={"/company-manager/job/list"}
                className="font-[400] text-[14px] text-[#0088FF] underline"
              >
                Quay lại danh sách
              </Link>
            </div>
            <form onSubmit={handleSubmit} action="" className="grid sm:grid-cols-2 gap-x-[20px] gap-y-[15px]">
              <BoxGroup1
                name="title"
                label="Tên công việc *"
                id="title"
                type="text"
                className="sm:col-span-2"
                values={formData.title}
                onChange={handleChange}
              />
              <BoxGroup1
                name="budgetMin"
                label="Mức lương tối thiểu ($)"
                id="budgetMin"
                type="text"
                values={formData.budgetMin}
                onChange={handleChange}
              />
              <BoxGroup1
                name="budgetMax"
                label="Mức lương tối đa ($)"
                id="budgetMax"
                type="text"
                values={formData.budgetMax}
                onChange={handleChange}
              />
              <BoxGroup1
                name="deadline"
                label="Ngày hết hạn"
                id="deadline"
                type="text"
                values={formData.deadline}
                onChange={handleChange}
              />
              <BoxGroup1
                name="positionRequirement"
                label="Yêu cầu vị trí"
                id="positionRequirement"
                type="text"
                values={formData.positionRequirement}
                onChange={handleChange}
              />
              <BoxGroup1
                name="minExperienceYears"
                label="Số năm kinh nghiệm"
                id="minExperienceYears"
                type="text"
                values={formData.minExperienceYears}
                onChange={handleChange}
              />
              <BoxGroup1
                name="requiredTechnologies"
                label="Yêu cầu công nghệ"
                id="requiredTechnologies"
                type="text"
                values={formData.requiredTechnologies}
                onChange={handleChange}
              />
              <div className="sm:col-span-2">
                <label className="block mb-2">
                  Skills *
                </label>

                <div className="flex gap-2">
                  <input
                    type="text"
                    value={skillInput}
                    onChange={(e) => setSkillInput(e.target.value)}
                    className="flex-1 bg-white border border-[#DEDEDE] rounded-[4px] py-[14px] px-[20px] font-[500] text-[14px] text-black"
                    placeholder="Nhập skill"
                  />

                  <button
                    type="button"
                    onClick={addSkill}
                    className="px-4 py-2 bg-blue-500 text-white rounded"
                  >
                    Add
                  </button>
                </div>

                <div className="flex flex-wrap gap-2 mt-3">
                  {formData.jobSkills.map((skill, index) => (
                    <span
                      key={index}
                      className="px-3 py-1 bg-blue-100 rounded-full"
                    >
                      {skill}
                    </span>
                  ))}
                </div>
              </div>
              <BoxGroup3
                label="Mô tả chi tiết"
                name="description"
                id="description"
                className="sm:col-span-2"
                value={formData.description}
                onChange={handleChange}
              />
              <ButtonGroup1 
                className="sm:col-span-2"
                label="Tạo mới"
              />
            </form>
          </div>
        </div>
      </div>
    </>
  )
}