"use client"

import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { getCurrentUser } from "@/app/lib/auth";
import { deposit, getBalance } from "@/app/services/walletService";

export default function WalletPage() {
  const [userId, setUserId] = useState<number | null>(null);
  const [balance, setBalance] = useState<number>(0);
  const [amount, setAmount] = useState<number>(0);
  const [loading, setLoading] = useState(true);

  const loadBalance = async (uid: number) => {
    try {
      setBalance(await getBalance(uid));
    } catch {
      toast.error("Không tải được số dư ví");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const user = getCurrentUser();
    if (!user) {
      window.location.href = "/login";
      return;
    }
    setUserId(user.id);
    loadBalance(user.id);
  }, []);

  const handleDeposit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!userId || amount <= 0) {
      toast.error("Nhập số tiền hợp lệ");
      return;
    }
    try {
      const res = await deposit(userId, amount);
      setBalance(res.balance);
      setAmount(0);
      toast.success("Nạp tiền thành công");
    } catch {
      toast.error("Nạp tiền thất bại");
    }
  };

  return (
    <div className="py-[40px]">
      <div className="contain">
        <h1 className="font-[700] text-[24px] text-black mb-[20px]">Ví của tôi</h1>
        <div className="bg-white rounded-[8px] border border-[#DEDEDE] p-[30px] max-w-[560px]">
          <p className="text-[14px] text-gray-500">Số dư khả dụng</p>
          <p className="font-[700] text-[36px] text-blue-600 mb-[24px]">
            {loading ? "..." : balance.toLocaleString("vi-VN")} đ
          </p>
          <form onSubmit={handleDeposit} className="flex gap-[10px] items-end">
            <div className="flex-1">
              <label className="font-[500] text-[14px] text-black">Số tiền nạp</label>
              <input
                type="number"
                value={amount || ""}
                onChange={(e) => setAmount(Number(e.target.value))}
                className="bg-white border border-[#DEDEDE] rounded-[4px] py-[12px] px-[16px] w-full mt-[5px]"
                placeholder="Ví dụ: 1000000"
              />
            </div>
            <button
              type="submit"
              className="bg-blue-600 text-white rounded-[6px] px-[24px] py-[12px] font-[600] hover:bg-blue-700"
            >
              Nạp tiền
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}
