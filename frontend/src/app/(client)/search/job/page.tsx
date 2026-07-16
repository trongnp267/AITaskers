"use client"

import { Suspense, useEffect, useRef, useState } from "react";
import { useSearchParams } from "next/navigation";
import Link from "next/link";
import { Section1 } from "@/app/components/section/Section1";
import { getJobs } from "@/app/services/jobService";
import { Job } from "@/app/types/domain";

function SearchJobs() {
  const searchParams = useSearchParams();
  const q = (searchParams.get("q") || "").toLowerCase();
  const [jobs, setJobs] = useState<Job[]>([]);
  const [loading, setLoading] = useState(true);
  const resultRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    getJobs()
      .then(setJobs)
      .catch(() => {})
      .finally(() => setLoading(false));
  }, []);

  useEffect(() => {
    if (q) {
      resultRef.current?.scrollIntoView({ behavior: "smooth", block: "start" });
    }
  }, [q]);

  const filtered = jobs.filter((j) => {
    if (!q) return true;
    const skills = (j.jobSkills || []).map((s) => (s.skillName || "").toLowerCase()).join(" ");
    return (
      (j.title || "").toLowerCase().includes(q) ||
      (j.description || "").toLowerCase().includes(q) ||
      skills.includes(q)
    );
  });

  return (
    <div ref={resultRef} className="py-[60px] scroll-mt-[70px]">
      <div className="contain">
        <h2 className="mb-[30px] font-[700] text-[28px] text-[#121212]">
          {filtered.length} việc làm{" "}
          {q && <span className="text-[#0088FF]">&quot;{q}&quot;</span>}
        </h2>
        {loading && <p className="text-gray-500">Đang tải...</p>}
        {!loading && filtered.length === 0 && (
          <p className="text-gray-500">Không tìm thấy công việc phù hợp.</p>
        )}
        <div className="grid lg:grid-cols-3 sm:grid-cols-2 grid-cols-1 gap-[20px]">
          {filtered.map((job) => (
            <Link
              key={job.jobId}
              href={`/job-detail?id=${job.jobId}`}
              className="bg-white rounded-[8px] border border-[#DEDEDE] p-[20px] hover:shadow-md transition block"
            >
              <div className="flex justify-between items-start gap-[8px]">
                <h3 className="font-[700] text-[16px] text-black line-clamp-1">{job.title}</h3>
                <span className={`text-[11px] px-[8px] py-[2px] rounded-full whitespace-nowrap ${job.jobStatus === "OPEN" ? "bg-green-100 text-green-700" : "bg-gray-100 text-gray-600"}`}>
                  {job.jobStatus}
                </span>
              </div>
              <p className="text-[13px] text-gray-500 mt-[8px] line-clamp-2">{job.description}</p>
              <div className="flex gap-[6px] mt-[10px] flex-wrap">
                {(job.jobSkills || []).slice(0, 4).map((s) => (
                  <span key={s.jobSkillId} className="text-[11px] bg-blue-50 text-blue-700 px-[8px] py-[2px] rounded-full">
                    {s.skillName}
                  </span>
                ))}
              </div>
              <p className="text-[13px] text-blue-600 font-[600] mt-[10px]">
                {job.budgetMin?.toLocaleString("vi-VN")} - {job.budgetMax?.toLocaleString("vi-VN")} đ
              </p>
            </Link>
          ))}
        </div>
      </div>
    </div>
  );
}

export default function Page() {
  return (
    <>
      <Section1 />
      <Suspense fallback={<div className="contain py-[40px] text-gray-500">Đang tải...</div>}>
        <SearchJobs />
      </Suspense>
    </>
  )
}
