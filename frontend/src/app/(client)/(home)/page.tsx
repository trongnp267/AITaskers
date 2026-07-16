"use client"

import { useEffect, useState } from "react";
import Link from "next/link";
import { ExpertCard } from "@/app/components/card/ExpertCard";
import { Section1 } from "@/app/components/section/Section1";
import { getExperts, ExpertSummary } from "@/app/services/expertService";
import { getJobs } from "@/app/services/jobService";
import { Job } from "@/app/types/domain";

export default function Page() {
  const [experts, setExperts] = useState<ExpertSummary[]>([]);
  const [jobs, setJobs] = useState<Job[]>([]);

  useEffect(() => {
    getExperts().then(setExperts).catch(() => {});
    getJobs()
      .then((all) => setJobs(all.filter((j) => j.jobStatus === "OPEN").slice(0, 6)))
      .catch(() => {});
  }, []);

  return (
    <>
      <Section1 />

      <div className="py-[60px]">
        <div className="contain">
          <h2 className="font-[700] sm:text-[28px] text-[24px] text-[#121212] text-center mb-[30px]">
            Các chuyên gia AI hàng đầu
          </h2>
          {experts.length === 0 && (
            <p className="text-center text-gray-500">Chưa có chuyên gia nào đăng ký.</p>
          )}
          <div className="grid lg:grid-cols-3 sm:grid-cols-2 grid-cols-1 lg:gap-x-[20px] gap-x-[10px] gap-y-[20px]">
            {experts.slice(0, 9).map((e) => (
              <ExpertCard key={e.expertId} expert={e} />
            ))}
          </div>
        </div>
      </div>

      <div className="pb-[60px]">
        <div className="contain">
          <h2 className="font-[700] sm:text-[28px] text-[24px] text-[#121212] text-center mb-[30px]">
            Công việc đang tuyển
          </h2>
          {jobs.length === 0 && (
            <p className="text-center text-gray-500">Chưa có công việc nào đang mở.</p>
          )}
          <div className="grid lg:grid-cols-3 sm:grid-cols-2 grid-cols-1 gap-[20px]">
            {jobs.map((job) => (
              <Link
                key={job.jobId}
                href={`/jobs/${job.jobId}`}
                className="bg-white rounded-[8px] border border-[#DEDEDE] p-[20px] hover:shadow-md transition block"
              >
                <h3 className="font-[700] text-[16px] text-black line-clamp-1">{job.title}</h3>
                <p className="text-[13px] text-gray-500 mt-[8px] line-clamp-2">{job.description}</p>
                <p className="text-[13px] text-blue-600 font-[600] mt-[10px]">
                  {job.budgetMin?.toLocaleString("vi-VN")} - {job.budgetMax?.toLocaleString("vi-VN")} đ
                </p>
              </Link>
            ))}
          </div>
        </div>
      </div>
    </>
  );
}
