"use client"

import { useEffect, useState } from "react";
import Link from "next/link";
import { getJobs } from "@/app/services/jobService";
import { getProposalsByJob } from "@/app/services/proposalService";
import { Proposal } from "@/app/types/domain";
import { getCurrentUser } from "@/app/lib/auth";

const statusStyle = (s: string) => {
  switch ((s || "").toUpperCase()) {
    case "ACCEPTED": return "bg-green-100 text-green-700";
    case "REJECTED": return "bg-red-100 text-red-600";
    default: return "bg-yellow-100 text-yellow-700";
  }
};

export default function Page() {
  const [proposals, setProposals] = useState<Proposal[]>([]);
  const [loading, setLoading] = useState(true);
  const [notClient, setNotClient] = useState(false);

  useEffect(() => {
    const user = getCurrentUser();
    if (!user || user.role !== "CLIENT" || !user.profileId) {
      setNotClient(true);
      setLoading(false);
      return;
    }
    const load = async () => {
      try {
        const jobs = (await getJobs()).filter((j) => j.clientId === user.profileId);
        const lists = await Promise.all(
          jobs.map((j) => getProposalsByJob(j.jobId).catch(() => [] as Proposal[]))
        );
        setProposals(lists.flat());
      } finally {
        setLoading(false);
      }
    };
    load();
  }, []);

  return (
    <div className="py-[60px]">
      <div className="contain">
        <h1 className="font-[700] sm:text-[28px] text-[24px] text-[#121212] mb-[20px]">
          Báo giá nhận được
        </h1>
        {loading && <p className="text-gray-500">Đang tải...</p>}
        {notClient && (
          <p className="text-gray-500">
            Vui lòng <Link href="/login" className="text-blue-600 underline">đăng nhập</Link> bằng tài khoản Client để xem báo giá.
          </p>
        )}
        {!loading && !notClient && proposals.length === 0 && (
          <p className="text-gray-500">Chưa có báo giá nào cho các công việc của bạn.</p>
        )}
        <div className="grid lg:grid-cols-3 sm:grid-cols-2 gap-[20px]">
          {proposals.map((p) => (
            <div key={p.proposalId} className="bg-white border border-[#DEDEDE] rounded-[8px] p-[20px]">
              <div className="flex justify-between items-start gap-[8px]">
                <h2 className="font-[700] text-[16px] text-black line-clamp-1">{p.job?.title}</h2>
                <span className={`text-[11px] px-[8px] py-[2px] rounded-full whitespace-nowrap ${statusStyle(p.proposalStatus)}`}>
                  {p.proposalStatus}
                </span>
              </div>
              <p className="text-[13px] text-[#414042] mt-[8px]">
                Chuyên gia:{" "}
                {p.expert ? (
                  <Link href={`/experts/${p.expert.expertId}`} className="text-blue-600 underline">
                    {p.expert.user?.username || `Expert #${p.expert.expertId}`}
                  </Link>
                ) : "—"}
              </p>
              <p className="text-[13px] text-gray-500 mt-[6px] line-clamp-2">{p.coverLetter}</p>
              <div className="mt-[10px] text-[13px] text-[#414042]">
                Giá: <span className="text-blue-600 font-[600]">{p.proposalPrice?.toLocaleString("vi-VN")} đ</span>
                {" · "}{p.estimatedDays} ngày
              </div>
              <Link
                href={`/jobs/${p.job?.jobId}`}
                className="inline-block mt-[12px] text-[13px] font-[600] text-white bg-[#0088FF] rounded-[4px] px-[14px] py-[8px]"
              >
                Xem &amp; xử lý
              </Link>
            </div>
          ))}
        </div>
      </div>
    </div>
  )
}
