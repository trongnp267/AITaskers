"use client"

import { useEffect, useState } from "react";
import Link from "next/link";
import { FaBuilding } from "react-icons/fa6";
import { getClients, ClientSummary } from "@/app/services/clientService";

export default function Page() {
  const [clients, setClients] = useState<ClientSummary[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getClients()
      .then(setClients)
      .catch(() => {})
      .finally(() => setLoading(false));
  }, []);

  return (
    <>
      <div className="py-[60px]">
        <div className="contain">
          <h1 className="text-[#121212] font-[700] sm:text-[28px] text-[24px] mb-[30px] text-center">
            Danh sách công ty
          </h1>
          {loading && <p className="text-center text-gray-500">Đang tải...</p>}
          {!loading && clients.length === 0 && (
            <p className="text-center text-gray-500">Chưa có công ty nào đăng ký.</p>
          )}
          <div className="grid lg:grid-cols-3 sm:grid-cols-2 grid-cols-1 sm:gap-x-[20px] gap-x-[10px] gap-y-[20px]">
            {clients.map((c) => (
              <Link
                key={c.clientId}
                href={`/company/details/${c.clientId}`}
                className="bg-white border border-[#DEDEDE] rounded-[8px] p-[20px] hover:border-blue-600 hover:shadow-lg transition-all block text-center"
              >
                <div className="w-[64px] h-[64px] mx-auto rounded-[8px] bg-[#000065] text-white flex items-center justify-center text-[28px]">
                  <FaBuilding />
                </div>
                <h2 className="font-[700] text-[16px] text-[#121212] mt-[12px] line-clamp-1">
                  {c.companyName || c.username}
                </h2>
                <p className="text-[13px] text-gray-500 mt-[6px] line-clamp-2">
                  {c.description || "Chưa có mô tả."}
                </p>
              </Link>
            ))}
          </div>
        </div>
      </div>
    </>
  )
}
