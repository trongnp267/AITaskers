"use client"

import { BoxGroup1 } from "@/app/components/form/BoxGroup1";
import { BoxGroup3 } from "@/app/components/form/BoxGroup3";
import { ButtonGroup1 } from "@/app/components/form/ButtonGroup1";
import { getCurrentUser } from "@/app/lib/auth";
import { createJob } from "@/app/services/jobService";
import Link from "next/link";
import { useEffect, useState } from "react";
import toast from "react-hot-toast";

export default function Page() {
  const [clientId, setClientId] = useState<number | null>(null);
  const [formData, setFormData] = useState({
    title: "",
    description: "",
    positionRequirement: "",
    minExperienceYears: 0,
    budgetMin: 0,
    budgetMax: 0,
    deadline: "",
    jobSkills: [] as string[],
  });
  const [skillInput, setSkillInput] = useState("");

  useEffect(() => {
    const user = getCurrentUser();
    if (!user) {
      window.location.href = "/login";
      return;
    }
    if (user.role !== "CLIENT" || !user.profileId) {
      toast.error("Chỉ tài khoản Client mới đăng công việc");
      return;
    }
    setClientId(user.profileId);
  }, []);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]:
        name === "minExperienceYears" || name === "budgetMin" || name === "budgetMax"
          ? Number(value)
          : value,
    }));
  };

  const addSkill = () => {
    if (!skillInput.trim()) return;
    setFormData({ ...formData, jobSkills: [...formData.jobSkills, skillInput.trim()] });
    setSkillInput("");
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!clientId) {
      toast.error("Không xác định được hồ sơ Client");
      return;
    }
    if (formData.jobSkills.length === 0) {
      toast.error("Vui lòng thêm ít nhất 1 skill");
      return;
    }
    try {
      await createJob({ clientId, ...formData });
      toast.success("Tạo công việc thành công");
      window.location.href = "/jobs";
    } catch (err: unknown) {
      const message =
        (err as { response?: { data?: { message?: string } } })?.response?.data?.message ||
        "Tạo công việc thất bại";
      toast.error(message);
    }
  };

  return (
    <>
      <div className="py-[60px]">
        <div className="contain">
          <div className="bg-white rounded-[8px] border border-[#DEDEDE] p-[20px]">
            <div className="flex justify-between items-center mb-[20px] gap-[20px] flex-wrap">
              <h1 className="font-[700] text-[20px] text-black">Thêm mới công việc</h1>
              <Link href={"/jobs"} className="font-[400] text-[14px] text-[#0088FF] underline">
                Quay lại danh sách
              </Link>
            </div>
            <form onSubmit={handleSubmit} className="grid sm:grid-cols-2 gap-x-[20px] gap-y-[15px]">
              <BoxGroup1 name="title" label="Tên công việc *" id="title" type="text" className="sm:col-span-2" values={formData.title} onChange={handleChange} />
              <BoxGroup1 name="budgetMin" label="Ngân sách tối thiểu (đ)" id="budgetMin" type="number" values={formData.budgetMin} onChange={handleChange} />
              <BoxGroup1 name="budgetMax" label="Ngân sách tối đa (đ)" id="budgetMax" type="number" values={formData.budgetMax} onChange={handleChange} />
              <BoxGroup1 name="deadline" label="Ngày hết hạn" id="deadline" type="date" values={formData.deadline} onChange={handleChange} />
              <BoxGroup1 name="positionRequirement" label="Yêu cầu vị trí" id="positionRequirement" type="text" values={formData.positionRequirement} onChange={handleChange} />
              <BoxGroup1 name="minExperienceYears" label="Số năm kinh nghiệm" id="minExperienceYears" type="number" className="sm:col-span-2" values={formData.minExperienceYears} onChange={handleChange} />
              <div className="sm:col-span-2">
                <label className="block mb-2">Skills *</label>
                <div className="flex gap-2">
                  <input type="text" value={skillInput} onChange={(e) => setSkillInput(e.target.value)}
                    className="flex-1 bg-white border border-[#DEDEDE] rounded-[4px] py-[14px] px-[20px] font-[500] text-[14px] text-black" placeholder="Nhập skill" />
                  <button type="button" onClick={addSkill} className="px-4 py-2 bg-blue-500 text-white rounded">Add</button>
                </div>
                <div className="flex flex-wrap gap-2 mt-3">
                  {formData.jobSkills.map((skill, index) => (
                    <span key={index} className="px-3 py-1 bg-blue-100 rounded-full">{skill}</span>
                  ))}
                </div>
              </div>
              <BoxGroup3 label="Mô tả chi tiết" name="description" id="description" className="sm:col-span-2" value={formData.description} onChange={handleChange} />
              <ButtonGroup1 className="sm:col-span-2" label="Tạo mới" />
            </form>
          </div>
        </div>
      </div>
    </>
  )
}
