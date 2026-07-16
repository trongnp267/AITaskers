"use client"

import { useCallback, useEffect, useState } from "react";
import { useParams } from "next/navigation";
import Link from "next/link";
import toast from "react-hot-toast";
import { getCurrentUser, CurrentUser } from "@/app/lib/auth";
import { getJob } from "@/app/services/jobService";
import {
  getProposalsByJob,
  createProposal,
  acceptProposal,
} from "@/app/services/proposalService";
import {
  getEscrowByJob,
  releaseEscrow,
  refundEscrow,
} from "@/app/services/escrowService";
import { analyzeJob, matchExpertsForJob, ExpertMatch, AnalyzeResult } from "@/app/services/aiService";
import { createReview } from "@/app/services/reviewService";
import { Job, Proposal, Escrow } from "@/app/types/domain";

export default function JobDetailPage() {
  const params = useParams();
  const jobId = Number(params.id);

  const [user, setUser] = useState<CurrentUser | null>(null);
  const [job, setJob] = useState<Job | null>(null);
  const [proposals, setProposals] = useState<Proposal[]>([]);
  const [escrow, setEscrow] = useState<Escrow | null>(null);
  const [loading, setLoading] = useState(true);

  const [propForm, setPropForm] = useState({ coverLetter: "", proposalPrice: 0, estimatedDays: 0 });
  const [analysis, setAnalysis] = useState<AnalyzeResult | null>(null);
  const [matches, setMatches] = useState<ExpertMatch[]>([]);
  const [review, setReview] = useState({ rating: 5, comment: "" });

  const load = useCallback(async () => {
    try {
      const j = await getJob(jobId);
      setJob(j);
    } catch {
      toast.error("Không tải được công việc");
    } finally {
      setLoading(false);
    }
    getProposalsByJob(jobId).then(setProposals).catch(() => {});
    getEscrowByJob(jobId).then(setEscrow).catch(() => {});
  }, [jobId]);

  useEffect(() => {
    setUser(getCurrentUser());
    load();
  }, [load]);

  const isExpert = user?.role === "EXPERT";
  const isClientOwner = user?.role === "CLIENT" && !!job && job.clientId === user.profileId;
  const acceptedExpertId = proposals.find((p) => p.proposalStatus === "ACCEPTED")?.expert.expertId;

  const submitProposal = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!user?.profileId) {
      toast.error("Không xác định được hồ sơ Expert");
      return;
    }
    try {
      await createProposal({
        expertId: user.profileId,
        jobId,
        coverLetter: propForm.coverLetter,
        proposalPrice: propForm.proposalPrice,
        estimatedDays: propForm.estimatedDays,
      });
      toast.success("Gửi báo giá thành công");
      setPropForm({ coverLetter: "", proposalPrice: 0, estimatedDays: 0 });
      load();
    } catch (err: unknown) {
      toast.error(apiError(err, "Gửi báo giá thất bại"));
    }
  };

  const doAccept = async (id: number) => {
    try {
      await acceptProposal(id);
      toast.success("Đã chấp nhận báo giá & ký quỹ");
      load();
    } catch (err: unknown) {
      toast.error(apiError(err, "Thao tác thất bại"));
    }
  };

  const doRelease = async () => {
    if (!escrow) return;
    try {
      await releaseEscrow(escrow.escrowId);
      toast.success("Đã giải ngân cho Expert");
      load();
    } catch (err: unknown) {
      toast.error(apiError(err, "Giải ngân thất bại"));
    }
  };

  const doRefund = async () => {
    if (!escrow) return;
    try {
      await refundEscrow(escrow.escrowId);
      toast.success("Đã hoàn tiền cho Client");
      load();
    } catch (err: unknown) {
      toast.error(apiError(err, "Hoàn tiền thất bại"));
    }
  };

  const doAnalyze = async () => {
    if (!job) return;
    try {
      setAnalysis(await analyzeJob(job.description));
    } catch {
      toast.error("Phân tích AI thất bại");
    }
  };

  const doMatch = async () => {
    try {
      const result = await matchExpertsForJob(jobId);
      setMatches(result);
      if (result.length === 0) {
        toast.error("AI chưa được cấu hình (cần Gemini API key trong application.properties) hoặc không có gợi ý.");
      } else {
        toast.success(`AI gợi ý ${result.length} expert`);
      }
    } catch {
      toast.error("Gợi ý AI thất bại");
    }
  };

  const doReview = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!user || !acceptedExpertId) return;
    try {
      await createReview({
        jobId,
        reviewerUserId: user.id,
        expertId: acceptedExpertId,
        rating: review.rating,
        comment: review.comment,
      });
      toast.success("Đã gửi đánh giá");
      setReview({ rating: 5, comment: "" });
    } catch (err: unknown) {
      toast.error(apiError(err, "Đánh giá thất bại"));
    }
  };

  if (loading) return <div className="contain py-[40px] text-gray-500">Đang tải...</div>;
  if (!job) return <div className="contain py-[40px] text-gray-500">Không tìm thấy công việc.</div>;

  return (
    <div className="py-[40px]">
      <div className="contain grid gap-[20px]">
        {/* Job info */}
        <div className="bg-white rounded-[8px] border border-[#DEDEDE] p-[24px]">
          <div className="flex justify-between items-start">
            <h1 className="font-[700] text-[24px] text-black">{job.title}</h1>
            <span className="text-[12px] font-[600] bg-gray-100 rounded-[4px] px-[10px] py-[4px]">
              {job.jobStatus}
            </span>
          </div>
          <p className="text-[14px] text-gray-600 mt-[10px]">{job.description}</p>
          <div className="flex flex-wrap gap-[6px] mt-[12px]">
            {job.jobSkills?.map((s) => (
              <span key={s.jobSkillId} className="text-[12px] bg-gray-100 rounded-[4px] px-[8px] py-[2px]">
                {s.skillName}
              </span>
            ))}
          </div>
          <p className="text-[14px] text-blue-600 font-[600] mt-[12px]">
            Ngân sách: {job.budgetMin?.toLocaleString("vi-VN")} - {job.budgetMax?.toLocaleString("vi-VN")} đ
          </p>
          {(isClientOwner || isExpert) && (
            <Link
              href={`/jobs/${jobId}/milestones`}
              className="inline-block mt-[14px] text-[14px] font-[600] text-blue-600"
            >
              → Quản lý giai đoạn (milestones)
            </Link>
          )}
        </div>

        {/* Huong dan khi chua dang nhap hoac sai vai */}
        {!user && (
          <div className="bg-blue-50 border border-blue-200 rounded-[8px] p-[20px] text-[14px] text-[#414042]">
            <p className="font-[600] text-[15px] text-black mb-[8px]">
              Bạn muốn thao tác với công việc này?
            </p>
            <p className="mb-[4px]">• Là <b>Chuyên gia (Expert)</b>: đăng nhập để gửi báo giá cho công việc này.</p>
            <p className="mb-[12px]">• Là <b>Chủ tin đăng (Client)</b>: đăng nhập để duyệt báo giá, ký quỹ và nghiệm thu.</p>
            <Link href="/login" className="inline-block bg-blue-600 text-white rounded-[6px] px-[18px] py-[9px] font-[600] mr-[10px]">
              Đăng nhập
            </Link>
            <Link href="/register" className="inline-block border border-blue-600 text-blue-600 rounded-[6px] px-[18px] py-[9px] font-[600]">
              Đăng ký
            </Link>
          </div>
        )}
        {user && !isExpert && !isClientOwner && (
          <div className="bg-yellow-50 border border-yellow-200 rounded-[8px] p-[20px] text-[14px] text-[#414042]">
            Bạn đang đăng nhập bằng <b>{user.username}</b> ({user.role}).
            {user.role === "CLIENT"
              ? " Chỉ chủ tin đăng (người tạo công việc này) mới thấy phần duyệt báo giá và ký quỹ."
              : " Tài khoản này không thao tác trực tiếp với công việc — Expert gửi báo giá, Client chủ tin duyệt báo giá."}
          </div>
        )}
        {isExpert && job.jobStatus !== "OPEN" && (
          <div className="bg-gray-50 border border-[#DEDEDE] rounded-[8px] p-[20px] text-[14px] text-gray-600">
            Công việc đang ở trạng thái <b>{job.jobStatus}</b> nên không nhận thêm báo giá.
          </div>
        )}

        {/* Expert: submit proposal */}
        {isExpert && job.jobStatus === "OPEN" && (
          <Section title="Gửi báo giá (Proposal)">
            <form onSubmit={submitProposal} className="grid gap-[12px] max-w-[560px]">
              <textarea
                placeholder="Thư ngỏ / cover letter"
                value={propForm.coverLetter}
                onChange={(e) => setPropForm({ ...propForm, coverLetter: e.target.value })}
                className="border border-[#DEDEDE] rounded-[4px] p-[12px] h-[100px]"
              />
              <div className="grid grid-cols-2 gap-[12px]">
                <input type="number" placeholder="Giá báo (đ)" value={propForm.proposalPrice || ""}
                  onChange={(e) => setPropForm({ ...propForm, proposalPrice: Number(e.target.value) })}
                  className="border border-[#DEDEDE] rounded-[4px] p-[12px]" />
                <input type="number" placeholder="Số ngày" value={propForm.estimatedDays || ""}
                  onChange={(e) => setPropForm({ ...propForm, estimatedDays: Number(e.target.value) })}
                  className="border border-[#DEDEDE] rounded-[4px] p-[12px]" />
              </div>
              <button className="bg-blue-600 text-white rounded-[6px] py-[10px] font-[600] hover:bg-blue-700">
                Gửi báo giá
              </button>
            </form>
          </Section>
        )}

        {/* Client owner: proposals + accept */}
        {isClientOwner && (
          <Section title={`Báo giá nhận được (${proposals.length})`}>
            {proposals.length === 0 && <p className="text-gray-500 text-[14px]">Chưa có báo giá.</p>}
            <div className="grid gap-[12px]">
              {proposals.map((p) => (
                <div key={p.proposalId} className="border border-[#DEDEDE] rounded-[8px] p-[16px]">
                  <div className="flex justify-between items-start">
                    <div>
                      <p className="font-[600] text-[14px]">{p.expert.user?.username || `Expert #${p.expert.expertId}`}
                        <span className="text-gray-400 font-[400]"> · ⭐ {p.expert.rating}</span></p>
                      <p className="text-[13px] text-gray-600 mt-[4px]">{p.coverLetter}</p>
                      <p className="text-[13px] text-blue-600 font-[600] mt-[6px]">
                        {p.proposalPrice?.toLocaleString("vi-VN")} đ · {p.estimatedDays} ngày
                      </p>
                    </div>
                    <div className="text-right">
                      <span className="text-[11px] font-[600] bg-gray-100 rounded-[4px] px-[8px] py-[2px]">
                        {p.proposalStatus}
                      </span>
                      {job.jobStatus === "OPEN" && p.proposalStatus === "SUBMITTED" && (
                        <button onClick={() => doAccept(p.proposalId)}
                          className="block mt-[8px] bg-green-600 text-white rounded-[6px] px-[14px] py-[6px] text-[13px] font-[600] hover:bg-green-700">
                          Chấp nhận
                        </button>
                      )}
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </Section>
        )}

        {/* Escrow (client owner) */}
        {isClientOwner && escrow && (
          <Section title="Ký quỹ (Escrow)">
            <p className="text-[14px]">Số tiền: <b>{escrow.amount?.toLocaleString("vi-VN")} đ</b> ·
              Trạng thái: <b>{escrow.escrowStatus}</b></p>
            {escrow.escrowStatus === "HELD" && (
              <div className="flex gap-[10px] mt-[12px]">
                <button onClick={doRelease} className="bg-green-600 text-white rounded-[6px] px-[16px] py-[8px] text-[14px] font-[600] hover:bg-green-700">
                  Giải ngân cho Expert
                </button>
                <button onClick={doRefund} className="bg-red-600 text-white rounded-[6px] px-[16px] py-[8px] text-[14px] font-[600] hover:bg-red-700">
                  Hoàn tiền
                </button>
              </div>
            )}
          </Section>
        )}

        {/* AI (client owner) */}
        {isClientOwner && (
          <Section title="Trợ lý AI">
            <div className="flex gap-[10px] mb-[12px]">
              <button onClick={doAnalyze} className="bg-purple-600 text-white rounded-[6px] px-[16px] py-[8px] text-[14px] font-[600] hover:bg-purple-700">
                Phân tích mô tả
              </button>
              <button onClick={doMatch} className="bg-purple-600 text-white rounded-[6px] px-[16px] py-[8px] text-[14px] font-[600] hover:bg-purple-700">
                Gợi ý Expert
              </button>
            </div>
            {analysis && (
              <pre className="bg-gray-50 border border-[#DEDEDE] rounded-[6px] p-[12px] text-[12px] whitespace-pre-wrap">
                {JSON.stringify(analysis, null, 2)}
              </pre>
            )}
            {matches.length > 0 && (
              <div className="mt-[10px] text-[13px]">
                {matches.map((m) => (
                  <p key={m.id}>Expert #{m.id}: {m.matchScore}đ — {m.reasoning}</p>
                ))}
              </div>
            )}
          </Section>
        )}

        {/* Review (client owner, completed) */}
        {isClientOwner && job.jobStatus === "COMPLETED" && acceptedExpertId && (
          <Section title="Đánh giá Expert">
            <form onSubmit={doReview} className="grid gap-[12px] max-w-[560px]">
              <select value={review.rating} onChange={(e) => setReview({ ...review, rating: Number(e.target.value) })}
                className="border border-[#DEDEDE] rounded-[4px] p-[10px] w-[120px]">
                {[5, 4, 3, 2, 1].map((r) => <option key={r} value={r}>{r} sao</option>)}
              </select>
              <textarea placeholder="Nhận xét" value={review.comment}
                onChange={(e) => setReview({ ...review, comment: e.target.value })}
                className="border border-[#DEDEDE] rounded-[4px] p-[12px] h-[80px]" />
              <button className="bg-blue-600 text-white rounded-[6px] py-[10px] font-[600] hover:bg-blue-700 w-[160px]">
                Gửi đánh giá
              </button>
            </form>
          </Section>
        )}
      </div>
    </div>
  );
}

function Section({ title, children }: { title: string; children: React.ReactNode }) {
  return (
    <div className="bg-white rounded-[8px] border border-[#DEDEDE] p-[24px]">
      <h2 className="font-[700] text-[18px] text-black mb-[14px]">{title}</h2>
      {children}
    </div>
  );
}

function apiError(err: unknown, fallback: string): string {
  return (
    (err as { response?: { data?: { message?: string } } })?.response?.data?.message ||
    fallback
  );
}
