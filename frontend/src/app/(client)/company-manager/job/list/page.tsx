"use client"

import { JobCard2 } from "@/app/components/card/JobCard2";
import { getCurrentUser } from "@/app/lib/auth";
import { getJobs } from "@/app/services/jobService";
import { Job } from "@/app/types/domain";
import Link from "next/link";
import { useEffect, useState } from "react";
import toast from "react-hot-toast";

export default function Page() {
  const [jobs, setJobs] = useState<Job[]>([]);

  useEffect(() => {
    const user = getCurrentUser();
    getJobs()
      .then((all) => {
        const mine = user?.profileId
          ? all.filter((j) => j.clientId === user.profileId)
          : all;
        setJobs(mine);
      })
      .catch(() => toast.error("Không tải được danh sách công việc"));
  }, []);

  return (
    <>
      <div className="py-[60px]">
        <div className="contain">
          <div className="mb-[20px] flex justify-between items-center flex-wrap gap-[20px]">
            <h1 className="font-[700] sm:text-[28px] text-[24px] text-[#121212]">
              Quản lý công việc
            </h1>
            <Link href="/company-manager/job/create" className="px-[20px] py-[8px] font-[400] text-[14px] text-white bg-[#0088FF] rounded-[4px] inline-block">
              Thêm mới
            </Link>
          </div>
          {jobs.length === 0 && <p className="text-gray-500">Bạn chưa đăng công việc nào.</p>}
          <div className="grid lg:grid-cols-3 sm:grid-cols-2 gap-[20px]">
            {jobs.map((job) => (
              <Link key={job.jobId} href={`/jobs/${job.jobId}`}>
                <JobCard2
                  id={job.jobId}
                  title={job.title}
                  positionRequirement={job.positionRequirement}
                  budgetMin={job.budgetMin}
                  budgetMax={job.budgetMax}
                  experience={job.minExperienceYears}
                  technologies={job.jobSkills}
                  deadline={job.deadline}
                />
              </Link>
            ))}
          </div>
        </div>
      </div>
    </>
  )
}
