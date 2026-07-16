"use client"

import { useEffect, useState } from "react";
import Link from "next/link";
import toast from "react-hot-toast";
import { getJobs } from "@/app/services/jobService";
import { Job } from "@/app/types/domain";

const statusColor: Record<string, string> = {
  OPEN: "bg-green-100 text-green-700",
  ASSIGNED: "bg-yellow-100 text-yellow-700",
  COMPLETED: "bg-blue-100 text-blue-700",
  CANCELLED: "bg-red-100 text-red-700",
};

export default function JobsPage() {
  const [jobs, setJobs] = useState<Job[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getJobs()
      .then(setJobs)
      .catch(() => toast.error("Không tải được danh sách công việc"))
      .finally(() => setLoading(false));
  }, []);

  return (
    <div className="py-[40px]">
      <div className="contain">
        <div className="flex justify-between items-center mb-[20px]">
          <h1 className="font-[700] text-[24px] text-black">Danh sách công việc</h1>
          <Link
            href="/company-manager/job/create"
            className="bg-blue-600 text-white rounded-[6px] px-[18px] py-[10px] font-[600] text-[14px] hover:bg-blue-700"
          >
            + Đăng công việc
          </Link>
        </div>
        {loading && <p className="text-gray-500">Đang tải...</p>}
        {!loading && jobs.length === 0 && (
          <p className="text-gray-500">Chưa có công việc nào.</p>
        )}
        <div className="grid sm:grid-cols-2 gap-[16px]">
          {jobs.map((job) => (
            <Link
              key={job.jobId}
              href={`/jobs/${job.jobId}`}
              className="bg-white rounded-[8px] border border-[#DEDEDE] p-[20px] hover:shadow-md transition"
            >
              <div className="flex justify-between items-start gap-[10px]">
                <h3 className="font-[700] text-[16px] text-black">{job.title}</h3>
                <span
                  className={`text-[11px] font-[600] rounded-[4px] px-[8px] py-[2px] whitespace-nowrap ${
                    statusColor[job.jobStatus] || "bg-gray-100 text-gray-600"
                  }`}
                >
                  {job.jobStatus}
                </span>
              </div>
              <p className="text-[13px] text-gray-500 mt-[8px] line-clamp-2">
                {job.description}
              </p>
              <div className="flex flex-wrap gap-[6px] mt-[12px]">
                {job.jobSkills?.map((s) => (
                  <span
                    key={s.jobSkillId}
                    className="text-[11px] bg-gray-100 text-gray-700 rounded-[4px] px-[8px] py-[2px]"
                  >
                    {s.skillName}
                  </span>
                ))}
              </div>
              <p className="text-[13px] text-blue-600 font-[600] mt-[12px]">
                {job.budgetMin?.toLocaleString("vi-VN")} - {job.budgetMax?.toLocaleString("vi-VN")} đ
              </p>
            </Link>
          ))}
        </div>
      </div>
    </div>
  );
}
