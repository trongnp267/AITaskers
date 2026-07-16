"use client"

import { Suspense, useEffect, useState } from "react";
import { useSearchParams } from "next/navigation";
import { ExpertCard } from "@/app/components/card/ExpertCard";
import { Section1 } from "@/app/components/section/Section1";
import { getExperts, ExpertSummary } from "@/app/services/expertService";

function SearchExperts() {
  const searchParams = useSearchParams();
  const q = (searchParams.get("q") || "").toLowerCase();
  const [experts, setExperts] = useState<ExpertSummary[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getExperts()
      .then(setExperts)
      .catch(() => {})
      .finally(() => setLoading(false));
  }, []);

  const filtered = q
    ? experts.filter(
        (e) =>
          (e.username || "").toLowerCase().includes(q) ||
          (e.description || "").toLowerCase().includes(q)
      )
    : experts;

  return (
    <div className="py-[60px]">
      <div className="contain">
        <h2 className="mb-[30px] font-[700] text-[28px] text-[#121212]">
          {filtered.length} chuyên gia{" "}
          {q && <span className="text-[#0088FF]">&quot;{q}&quot;</span>}
        </h2>
        {loading && <p className="text-gray-500">Đang tải...</p>}
        {!loading && filtered.length === 0 && (
          <p className="text-gray-500">Không tìm thấy chuyên gia phù hợp.</p>
        )}
        <div className="grid lg:grid-cols-3 sm:grid-cols-2 grid-cols-1 gap-[20px]">
          {filtered.map((e) => (
            <ExpertCard key={e.expertId} expert={e} />
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
        <SearchExperts />
      </Suspense>
    </>
  )
}
