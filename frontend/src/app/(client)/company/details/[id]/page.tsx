"use client"

import { use, useEffect, useState } from "react";
import Link from "next/link";
import { FaBuilding } from "react-icons/fa6";
import { getClient, ClientSummary } from "@/app/services/clientService";
import { getJobs } from "@/app/services/jobService";
import { Job } from "@/app/types/domain";

export default function Page({ params }: { params: Promise<{ id: string }> }) {
  const { id } = use(params);
  const clientId = Number(id);

  const [client, setClient] = useState<ClientSummary | null>(null);
  const [jobs, setJobs] = useState<Job[]>([]);

  useEffect(() => {
    getClient(clientId).then(setClient).catch(() => {});
    getJobs()
      .then((all) => setJobs(all.filter((j) => j.clientId === clientId)))
      .catch(() => {});
  }, [clientId]);

  if (!client) {
    return <div className="contain py-[60px] text-gray-500">Đang tải...</div>;
  }

  return (
    <>
      <div className="pt-[30px] pb-[60px]">
        <div className="contain">
          <div className="p-[20px] border border-[#DEDEDE] bg-white rounded-[8px]">
            <div className="flex gap-[16px] mb-[20px] md:flex-nowrap flex-wrap items-center">
              <div className="w-[100px] h-[100px] rounded-[4px] bg-[#000065] text-white flex items-center justify-center text-[40px]">
                <FaBuilding />
              </div>
              <div className="md:flex-1">
                <h1 className="font-[700] text-[28px] text-[#121212] mb-[6px]">
                  {client.companyName || client.username}
                </h1>
                <p className="text-[14px] text-gray-500">
                  Tài khoản: {client.username}
                  {client.createdAt && <> · Tham gia {new Date(client.createdAt).toLocaleDateString("vi-VN")}</>}
                </p>
              </div>
            </div>
          </div>

          <div className="p-[20px] border border-[#DEDEDE] bg-white rounded-[8px] mt-[20px] text-[15px] text-[#414042] whitespace-pre-line">
            {client.description || "Công ty chưa có mô tả."}
          </div>

          <div className="mt-[30px]">
            <h2 className="font-[700] text-[28px] text-[#121212] mb-[20px]">
              Công ty có {jobs.length} việc làm
            </h2>
            {jobs.length === 0 && <p className="text-gray-500">Công ty chưa đăng việc làm nào.</p>}
            <div className="grid lg:grid-cols-3 sm:grid-cols-2 grid-cols-1 gap-[20px]">
              {jobs.map((job) => (
                <Link
                  key={job.jobId}
                  href={`/jobs/${job.jobId}`}
                  className="bg-white rounded-[8px] border border-[#DEDEDE] p-[20px] hover:shadow-md transition block"
                >
                  <div className="flex justify-between items-start gap-[8px]">
                    <h3 className="font-[700] text-[16px] text-black line-clamp-1">{job.title}</h3>
                    <span className={`text-[11px] px-[8px] py-[2px] rounded-full whitespace-nowrap ${job.jobStatus === "OPEN" ? "bg-green-100 text-green-700" : "bg-gray-100 text-gray-600"}`}>
                      {job.jobStatus}
                    </span>
                  </div>
                  <p className="text-[13px] text-gray-500 mt-[8px] line-clamp-2">{job.description}</p>
                  <p className="text-[13px] text-blue-600 font-[600] mt-[10px]">
                    {job.budgetMin?.toLocaleString("vi-VN")} - {job.budgetMax?.toLocaleString("vi-VN")} đ
                  </p>
                </Link>
              ))}
            </div>
          </div>
        </div>
      </div>
    </>
  )
}
