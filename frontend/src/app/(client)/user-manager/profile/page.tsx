"use client"

import { useEffect, useState } from "react";
import Link from "next/link";
import toast from "react-hot-toast";
import { BoxGroup1 } from "@/app/components/form/BoxGroup1";
import { BoxGroup3 } from "@/app/components/form/BoxGroup3";
import { getExpert, updateExpert } from "@/app/services/expertService";
import { getCurrentUser } from "@/app/lib/auth";
import { FaStar } from "react-icons/fa6";

export default function Page() {
  const [profileId, setProfileId] = useState<number | null>(null);
  const [username, setUsername] = useState("");
  const [rating, setRating] = useState<number | null>(null);
  const [completedJobs, setCompletedJobs] = useState(0);
  const [description, setDescription] = useState("");
  const [experienceYears, setExperienceYears] = useState("0");
  const [notExpert, setNotExpert] = useState(false);

  useEffect(() => {
    const user = getCurrentUser();
    if (!user || user.role !== "EXPERT" || !user.profileId) {
      setNotExpert(true);
      return;
    }
    setProfileId(user.profileId);
    getExpert(user.profileId)
      .then((e) => {
        setUsername(e.username || "");
        setRating(e.rating);
        setCompletedJobs(e.completedJobs ?? 0);
        setDescription(e.description || "");
        setExperienceYears(String(e.experienceYears ?? 0));
      })
      .catch(() => {});
  }, []);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!profileId) return;
    try {
      await updateExpert(profileId, {
        description,
        experienceYears: Number(experienceYears) || 0,
      });
      toast.success("Cập nhật hồ sơ thành công");
    } catch {
      toast.error("Cập nhật hồ sơ thất bại");
    }
  };

  if (notExpert) {
    return (
      <div className="contain py-[60px] text-gray-500">
        Vui lòng <Link href="/login" className="text-blue-600 underline">đăng nhập</Link> bằng tài khoản Expert.
      </div>
    );
  }

  return (
    <div className="py-[60px]">
      <div className="contain">
        <div className="bg-white rounded-[8px] border border-[#DEDEDE] px-[20px] py-[30px] max-w-[700px] mx-auto">
          <h1 className="font-[700] text-[20px] text-black mb-[10px] text-center">
            Hồ sơ AI Expert
          </h1>
          <p className="text-center text-[14px] text-gray-500 mb-[20px] flex items-center justify-center gap-[10px]">
            <span className="font-[600] text-[#121212]">{username}</span>
            <span className="flex items-center gap-[4px]"><FaStar className="text-[#df7606]" /> {rating ?? "—"}</span>
            <span>{completedJobs} việc hoàn thành</span>
          </p>
          <form onSubmit={handleSubmit} className="grid gap-y-[15px]">
            <BoxGroup1
              name="experienceYears"
              label="Số năm kinh nghiệm"
              id="experienceYears"
              type="number"
              values={experienceYears}
              onChange={(e) => setExperienceYears(e.target.value)}
            />
            <BoxGroup3
              name="description"
              label="Giới thiệu bản thân / kỹ năng"
              id="description"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
            />
            <button
              type="submit"
              className="bg-[#0088FF] text-white rounded-[4px] h-[48px] font-[600] text-[15px]"
            >
              Lưu hồ sơ
            </button>
          </form>
        </div>
      </div>
    </div>
  )
}
