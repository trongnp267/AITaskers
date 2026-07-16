"use client"

import { use, useCallback, useEffect, useState } from "react";
import { FaStar } from "react-icons/fa6";
import { LuBriefcaseBusiness } from "react-icons/lu";
import toast from "react-hot-toast";
import { getExpert, ExpertSummary } from "@/app/services/expertService";
import { getReviewsForExpert, createReview } from "@/app/services/reviewService";
import { Review } from "@/app/types/domain";
import { getCurrentUser } from "@/app/lib/auth";

export default function Page({ params }: { params: Promise<{ id: string }> }) {
  const { id } = use(params);
  const expertId = Number(id);
  const user = getCurrentUser();

  const [expert, setExpert] = useState<ExpertSummary | null>(null);
  const [reviews, setReviews] = useState<Review[]>([]);
  const [rating, setRating] = useState(5);
  const [comment, setComment] = useState("");
  const [jobId, setJobId] = useState("");

  const load = useCallback(() => {
    getExpert(expertId).then(setExpert).catch(() => {});
    getReviewsForExpert(expertId).then(setReviews).catch(() => {});
  }, [expertId]);

  useEffect(() => { load(); }, [load]);

  const handleReview = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!user) { toast.error("Vui lòng đăng nhập để đánh giá"); return; }
    if (!jobId) { toast.error("Nhập mã công việc đã hợp tác"); return; }
    try {
      await createReview({
        jobId: Number(jobId),
        reviewerUserId: user.id,
        expertId,
        rating,
        comment,
      });
      toast.success("Đã gửi đánh giá");
      setComment(""); setJobId("");
      load();
    } catch (error: unknown) {
      const message = (error as { response?: { data?: { message?: string } } })?.response?.data?.message || "Gửi đánh giá thất bại";
      toast.error(message);
    }
  };

  if (!expert) {
    return <div className="contain py-[60px] text-gray-500">Đang tải...</div>;
  }

  return (
    <div className="py-[40px]">
      <div className="contain max-w-[800px]">
        <div className="bg-white rounded-[8px] border border-[#DEDEDE] p-[24px]">
          <div className="flex items-center gap-[20px]">
            <div className="w-[80px] h-[80px] rounded-full bg-blue-600 text-white flex items-center justify-center text-[32px] font-[700]">
              {(expert.username || "?").charAt(0).toUpperCase()}
            </div>
            <div>
              <h1 className="font-[700] text-[26px] text-[#121212]">{expert.username}</h1>
              <div className="mt-[6px] flex gap-[16px] items-center text-[15px] text-gray-600 flex-wrap">
                <span className="flex items-center gap-[4px]">
                  <FaStar className="text-[#df7606]" /> {expert.rating ?? "—"}
                </span>
                <span className="flex items-center gap-[4px]">
                  <LuBriefcaseBusiness /> {expert.completedJobs ?? 0} việc đã hoàn thành
                </span>
                <span>{expert.experienceYears ?? 0} năm kinh nghiệm</span>
              </div>
            </div>
          </div>
          <p className="mt-[20px] text-[15px] text-[#414042] whitespace-pre-line">
            {expert.description || "Chưa có mô tả."}
          </p>
        </div>

        <div className="bg-white rounded-[8px] border border-[#DEDEDE] p-[24px] mt-[20px]">
          <h2 className="font-[700] text-[20px] text-[#121212] mb-[16px]">
            Đánh giá ({reviews.length})
          </h2>
          {reviews.length === 0 && <p className="text-gray-500 text-[14px]">Chưa có đánh giá nào.</p>}
          <div className="flex flex-col gap-[12px]">
            {reviews.map((r) => (
              <div key={r.reviewId} className="border border-[#EEE] rounded-[8px] p-[14px]">
                <div className="flex items-center gap-[6px] text-[#df7606]">
                  {Array.from({ length: r.rating }).map((_, i) => <FaStar key={i} />)}
                  <span className="text-gray-500 text-[12px] ml-[8px]">
                    Job #{r.jobId} · {r.createdAt ? new Date(r.createdAt).toLocaleDateString("vi-VN") : ""}
                  </span>
                </div>
                <p className="mt-[6px] text-[14px] text-[#414042]">{r.comment}</p>
              </div>
            ))}
          </div>

          {user?.role === "CLIENT" && (
            <form onSubmit={handleReview} className="mt-[24px] border-t pt-[20px] grid gap-[12px]">
              <h3 className="font-[600] text-[16px]">Viết đánh giá</h3>
              <div className="flex gap-[10px] items-center">
                <label className="text-[14px]">Số sao:</label>
                <select value={rating} onChange={(e) => setRating(Number(e.target.value))}
                        className="border border-[#DEDEDE] rounded-[4px] px-[10px] py-[6px] text-[14px]">
                  {[5,4,3,2,1].map(v => <option key={v} value={v}>{v} sao</option>)}
                </select>
                <input value={jobId} onChange={(e) => setJobId(e.target.value)} placeholder="Mã công việc"
                       className="border border-[#DEDEDE] rounded-[4px] px-[10px] py-[6px] text-[14px] w-[140px]" />
              </div>
              <textarea value={comment} onChange={(e) => setComment(e.target.value)} placeholder="Nhận xét..."
                        className="border border-[#DEDEDE] rounded-[4px] px-[12px] py-[8px] text-[14px] h-[80px]" />
              <button type="submit" className="bg-[#0088FF] text-white rounded-[4px] px-[20px] py-[10px] text-[14px] font-[600] w-fit">
                Gửi đánh giá
              </button>
            </form>
          )}
        </div>
      </div>
    </div>
  );
}
