"use client"

import { useEffect, useState } from "react";
import Link from "next/link";
import toast from "react-hot-toast";
import { BoxGroup1 } from "@/app/components/form/BoxGroup1";
import { BoxGroup3 } from "@/app/components/form/BoxGroup3";
import { getClient, updateClient } from "@/app/services/clientService";
import { getCurrentUser } from "@/app/lib/auth";

export default function Page() {
  const [profileId, setProfileId] = useState<number | null>(null);
  const [username, setUsername] = useState("");
  const [companyName, setCompanyName] = useState("");
  const [description, setDescription] = useState("");
  const [notClient, setNotClient] = useState(false);

  useEffect(() => {
    const user = getCurrentUser();
    if (!user || user.role !== "CLIENT" || !user.profileId) {
      setNotClient(true);
      return;
    }
    setProfileId(user.profileId);
    getClient(user.profileId)
      .then((c) => {
        setUsername(c.username || "");
        setCompanyName(c.companyName || "");
        setDescription(c.description || "");
      })
      .catch(() => {});
  }, []);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!profileId) return;
    try {
      await updateClient(profileId, { companyName, description });
      toast.success("Cập nhật thông tin công ty thành công");
    } catch {
      toast.error("Cập nhật thất bại");
    }
  };

  if (notClient) {
    return (
      <div className="contain py-[60px] text-gray-500">
        Vui lòng <Link href="/login" className="text-blue-600 underline">đăng nhập</Link> bằng tài khoản Client.
      </div>
    );
  }

  return (
    <div className="py-[60px]">
      <div className="contain">
        <div className="bg-white rounded-[8px] border border-[#DEDEDE] px-[20px] py-[30px] max-w-[700px] mx-auto">
          <h1 className="font-[700] text-[20px] text-black mb-[10px] text-center">
            Thông tin công ty
          </h1>
          <p className="text-center text-[14px] text-gray-500 mb-[20px]">
            Tài khoản: <span className="font-[600] text-[#121212]">{username}</span>
          </p>
          <form onSubmit={handleSubmit} className="grid gap-y-[15px]">
            <BoxGroup1
              name="companyName"
              label="Tên công ty *"
              id="companyName"
              type="text"
              values={companyName}
              onChange={(e) => setCompanyName(e.target.value)}
            />
            <BoxGroup3
              name="description"
              label="Giới thiệu công ty"
              id="description"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
            />
            <button
              type="submit"
              className="bg-[#0088FF] text-white rounded-[4px] h-[48px] font-[600] text-[15px]"
            >
              Lưu thông tin
            </button>
          </form>
        </div>
      </div>
    </div>
  )
}
