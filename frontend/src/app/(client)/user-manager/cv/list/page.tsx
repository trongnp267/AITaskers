"use client"

import { useEffect, useState } from "react";
import Link from "next/link";
import { getProposalsByExpert } from "@/app/services/proposalService";
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
  const [notExpert, setNotExpert] = useState(false);

  useEffect(() => {
    const user = getCurrentUser();
    if (!user || user.role !== "EXPERT" || !user.profileId) {
      setNotExpert(true);
      setLoading(false);
      return;
    }
    getProposalsByExpert(user.profileId)
      .then(setProposals)
      .catch(() => {})
      .finally(() => setLoading(false));
  }, []);

  return (
    <div className="py-[60px]">
      <div className="contain">
        <h1 className="font-[700] sm:text-[28px] text-[24px] text-[#121212] mb-[20px]">
          Báo giá đã gửi
        </h1>
        {loading && <p className="text-gray-500">Đang tải...</p>}
        {notExpert && (
          <p className="text-gray-500">
            Vui lòng <Link href="/login" className="text-blue-600 underline">đăng nhập</Link> bằng tài khoản Expert để xem báo giá đã gửi.
          </p>
        )}
        {!loading && !notExpert && proposals.length === 0 && (
          <p className="text-gray-500">
            Bạn chưa gửi báo giá nào. <Link href="/jobs" className="text-blue-600 underline">Tìm việc ngay</Link>.
          </p>
        )}
        <div className="grid lg:grid-cols-3 sm:grid-cols-2 gap-[20px]">
          {proposals.map((p) => (
            <Link
              key={p.proposalId}
              href={`/job-detail?id=${p.job?.jobId}`}
              className="bg-white border border-[#DEDEDE] rounded-[8px] p-[20px] hover:shadow-md transition block"
            >
              <div className="flex justify-between items-start gap-[8px]">
                <h2 className="font-[700] text-[16px] text-black line-clamp-1">{p.job?.title}</h2>
                <span className={`text-[11px] px-[8px] py-[2px] rounded-full whitespace-nowrap ${statusStyle(p.proposalStatus)}`}>
                  {p.proposalStatus}
                </span>
              </div>
              <p className="text-[13px] text-gray-500 mt-[8px] line-clamp-2">{p.coverLetter}</p>
              <div className="mt-[10px] text-[13px] text-[#414042]">
                Giá đề xuất: <span className="text-blue-600 font-[600]">{p.proposalPrice?.toLocaleString("vi-VN")} đ</span>
                {" · "}{p.estimatedDays} ngày
              </div>
              {p.submittedAt && (
                <p className="text-[12px] text-gray-400 mt-[6px]">
                  Gửi lúc {new Date(p.submittedAt).toLocaleString("vi-VN")}
                </p>
              )}
            </Link>
          ))}
        </div>
      </div>
    </div>
  )
}
