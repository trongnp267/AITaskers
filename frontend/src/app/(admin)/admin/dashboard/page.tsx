"use client"

import { useEffect, useState } from "react";
import Link from "next/link";
import { getStats, AdminStats } from "@/app/services/adminService";

export default function DashboardPage() {
  const [stats, setStats] = useState<AdminStats | null>(null);

  useEffect(() => {
    getStats().then(setStats).catch(() => {});
  }, []);

  const cards = [
    { label: "Tổng người dùng", value: stats?.totalUsers, color: "bg-blue-50 text-blue-700" },
    { label: "Tài khoản chờ duyệt", value: stats?.pendingAccounts, color: "bg-yellow-50 text-yellow-700" },
    { label: "Công việc", value: stats?.totalJobs, color: "bg-green-50 text-green-700" },
    { label: "Báo giá", value: stats?.totalProposals, color: "bg-purple-50 text-purple-700" },
    { label: "Giao dịch ký quỹ", value: stats?.totalEscrows, color: "bg-red-50 text-red-700" },
  ];

  return (
    <div>
      <h1 className="text-3xl font-bold mb-2">
        Dashboard
      </h1>

      <p className="text-gray-500 mb-8">
        Welcome back, Admin 👋
      </p>

      <div className="grid lg:grid-cols-3 sm:grid-cols-2 gap-4">
        {cards.map((c) => (
          <div key={c.label} className={`rounded-xl p-6 ${c.color}`}>
            <p className="text-sm font-medium">{c.label}</p>
            <p className="text-4xl font-bold mt-2">{c.value ?? "—"}</p>
          </div>
        ))}
      </div>

      {stats && stats.pendingAccounts > 0 && (
        <Link
          href="/admin/approvals"
          className="inline-block mt-8 bg-blue-600 text-white rounded-lg px-5 py-3 font-semibold"
        >
          Duyệt {stats.pendingAccounts} tài khoản đang chờ →
        </Link>
      )}
    </div>
  );
}
