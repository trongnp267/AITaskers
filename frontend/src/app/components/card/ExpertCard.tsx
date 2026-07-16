import Link from "next/link"
import { FaStar } from "react-icons/fa6"
import { LuBriefcaseBusiness } from "react-icons/lu"
import { ExpertSummary } from "@/app/services/expertService"

export const ExpertCard = ({ expert }: { expert: ExpertSummary }) => {
  return (
    <Link
      href={`/experts/${expert.expertId}`}
      className="p-[24px] rounded-[16px] border border-[#e9e9e9] block bg-white hover:border-blue-600 hover:shadow-xl transition-all duration-300"
    >
      <div className="flex items-center gap-[16px]">
        <div className="w-[60px] h-[60px] rounded-[50%] bg-blue-600 text-white flex items-center justify-center text-[24px] font-[700]">
          {(expert.username || "?").charAt(0).toUpperCase()}
        </div>
        <div className="flex-1">
          <div className="text-[20px] font-[600] mb-[4px] text-[#121212]">
            {expert.username}
          </div>
          <div className="mt-[4px] flex gap-x-[12px] gap-y-[5px] items-center flex-wrap">
            <div className="flex gap-[4px] items-center text-[14px]">
              <FaStar className="text-[18px] text-[#df7606]" /> {expert.rating ?? "—"}
            </div>
            <div className="flex gap-[4px] items-center text-[14px]">
              <LuBriefcaseBusiness className="text-[18px]" /> {expert.completedJobs ?? 0} jobs
            </div>
            <div className="text-[14px] text-gray-500">
              {expert.experienceYears ?? 0} năm KN
            </div>
          </div>
        </div>
      </div>
      <div className="mt-[16px] line-clamp-3 font-[400] text-[14px] text-[#676767]">
        {expert.description || "Chưa có mô tả."}
      </div>
    </Link>
  )
}
