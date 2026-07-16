"use client"

import { useCallback, useEffect, useState } from "react";
import Link from "next/link";
import toast from "react-hot-toast";
import { getCurrentUser, CurrentUser } from "@/app/lib/auth";
import {
  getMilestonesByProject,
  createMilestone,
  submitMilestone,
  approveMilestone,
  rejectMilestone,
} from "@/app/services/milestoneService";
import { Milestone } from "@/app/types/domain";

const statusColor: Record<string, string> = {
  PENDING: "bg-gray-100 text-gray-700",
  WAITING_FOR_APPROVAL: "bg-yellow-100 text-yellow-700",
  APPROVED: "bg-green-100 text-green-700",
  REJECTED: "bg-red-100 text-red-700",
};

export default function MilestonesPage() {
  const [jobId, setJobId] = useState(0);
  const [user, setUser] = useState<CurrentUser | null>(null);
  const [items, setItems] = useState<Milestone[]>([]);
  const [loading, setLoading] = useState(true);
  const [form, setForm] = useState({ title: "", amount: 0, dueDate: "" });

  useEffect(() => {
    setJobId(Number(new URLSearchParams(window.location.search).get("job")) || 0);
    setUser(getCurrentUser());
  }, []);

  const load = useCallback(async () => {
    if (!jobId) return;
    try {
      setItems(await getMilestonesByProject(jobId));
    } catch {
      toast.error("Không tải được milestone");
    } finally {
      setLoading(false);
    }
  }, [jobId]);

  useEffect(() => {
    load();
  }, [load]);

  const isClient = user?.role === "CLIENT";
  const isExpert = user?.role === "EXPERT";

  const doCreate = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await createMilestone({
        projectId: jobId,
        title: form.title,
        amount: form.amount,
        dueDate: form.dueDate ? `${form.dueDate}T00:00:00` : undefined,
      });
      toast.success("Đã tạo giai đoạn");
      setForm({ title: "", amount: 0, dueDate: "" });
      load();
    } catch (err: unknown) {
      toast.error(apiError(err, "Tạo thất bại"));
    }
  };

  const act = async (fn: () => Promise<unknown>, ok: string) => {
    try {
      await fn();
      toast.success(ok);
      load();
    } catch (err: unknown) {
      toast.error(apiError(err, "Thao tác thất bại"));
    }
  };

  return (
    <div className="py-[40px]">
      <div className="contain">
        <Link href={`/job-detail?id=${jobId}`} className="text-[14px] text-blue-600 font-[600]">← Về công việc</Link>
        <h1 className="font-[700] text-[24px] text-black my-[16px]">Giai đoạn dự án (Milestones)</h1>

        {isClient && (
          <form onSubmit={doCreate} className="bg-white rounded-[8px] border border-[#DEDEDE] p-[20px] mb-[20px] grid sm:grid-cols-4 gap-[10px] items-end">
            <div className="sm:col-span-2">
              <label className="text-[13px] font-[500]">Tiêu đề</label>
              <input value={form.title} onChange={(e) => setForm({ ...form, title: e.target.value })}
                className="border border-[#DEDEDE] rounded-[4px] p-[10px] w-full mt-[4px]" />
            </div>
            <div>
              <label className="text-[13px] font-[500]">Số tiền (đ)</label>
              <input type="number" value={form.amount || ""} onChange={(e) => setForm({ ...form, amount: Number(e.target.value) })}
                className="border border-[#DEDEDE] rounded-[4px] p-[10px] w-full mt-[4px]" />
            </div>
            <div>
              <label className="text-[13px] font-[500]">Hạn</label>
              <input type="date" value={form.dueDate} onChange={(e) => setForm({ ...form, dueDate: e.target.value })}
                className="border border-[#DEDEDE] rounded-[4px] p-[10px] w-full mt-[4px]" />
            </div>
            <button className="bg-blue-600 text-white rounded-[6px] py-[10px] font-[600] hover:bg-blue-700 sm:col-span-4">
              + Tạo giai đoạn
            </button>
          </form>
        )}

        {loading && <p className="text-gray-500">Đang tải...</p>}
        {!loading && items.length === 0 && <p className="text-gray-500">Chưa có giai đoạn nào.</p>}
        <div className="grid gap-[12px]">
          {items.map((m) => (
            <div key={m.id} className="bg-white rounded-[8px] border border-[#DEDEDE] p-[16px] flex justify-between items-center">
              <div>
                <p className="font-[600] text-[15px]">{m.title}</p>
                <p className="text-[13px] text-blue-600 font-[600]">{m.amount?.toLocaleString("vi-VN")} đ</p>
                {m.dueDate && <p className="text-[12px] text-gray-400">Hạn: {new Date(m.dueDate).toLocaleDateString("vi-VN")}</p>}
              </div>
              <div className="flex items-center gap-[10px]">
                <span className={`text-[11px] font-[600] rounded-[4px] px-[8px] py-[3px] ${statusColor[m.status] || "bg-gray-100"}`}>
                  {m.status}
                </span>
                {isExpert && (m.status === "PENDING" || m.status === "REJECTED") && (
                  <button onClick={() => act(() => submitMilestone(m.id), "Đã nộp")}
                    className="bg-blue-600 text-white rounded-[6px] px-[12px] py-[6px] text-[12px] font-[600]">Nộp</button>
                )}
                {isClient && m.status === "WAITING_FOR_APPROVAL" && (
                  <>
                    <button onClick={() => act(() => approveMilestone(m.id), "Đã duyệt & giải ngân")}
                      className="bg-green-600 text-white rounded-[6px] px-[12px] py-[6px] text-[12px] font-[600]">Duyệt</button>
                    <button onClick={() => act(() => rejectMilestone(m.id), "Đã từ chối")}
                      className="bg-red-600 text-white rounded-[6px] px-[12px] py-[6px] text-[12px] font-[600]">Từ chối</button>
                  </>
                )}
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

function apiError(err: unknown, fallback: string): string {
  return (
    (err as { response?: { data?: { message?: string } } })?.response?.data?.message ||
    fallback
  );
}
