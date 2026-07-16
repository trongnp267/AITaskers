"use client"

import { JobCard2 } from "@/app/components/card/JobCard2";
import { Select1 } from "@/app/components/select/Select1";
import Link from "next/link";
import { useEffect, useState } from "react";

export default function Page() {
  interface Job {
    id: number;
    title: string;
    description: string;
    positionRequirement: string;
    requiredTechnologies: string;
    minExperienceYears: number;
    budgetMin: number;
    budgetMax: number;
    deadline: string;
    jobSkills: {
      jobSkillId: number;
      skillName: string;
      skillId: number;
      skill?: {}
    }[];
  }

  const [jobs, setJobs] = useState<Job[]>([]);

  useEffect(() => {
    fetchJobs();
  }, []);

  const fetchJobs = async () => {
    try {
      const response = await fetch(
        "http://localhost:8081/jobs"
      );

      const data = await response.json();

      setJobs(data);
    } catch (error) {
      console.log(error);
    }
  };
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
          <div className="grid lg:grid-cols-3 sm:grid-cols-2 gap-[20px]">
            {jobs.map((job, index) => (
              <JobCard2
                key={index}
                id={job.id}
                title={job.title}
                positionRequirement={job.positionRequirement}
                budgetMin={job.budgetMin}
                budgetMax={job.budgetMax}
                experience={job.minExperienceYears}
                technologies={job.jobSkills}
                deadline={job.deadline}
              />
            ))}
          </div>
          <Select1 totalPage={4} />
        </div>
      </div>
    </>
  )
}