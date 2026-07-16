"use client"

import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { getCurrentUser } from "@/app/lib/auth";
import { getNotifications, markAsRead } from "@/app/services/notificationService";
import { NotificationItem } from "@/app/types/domain";

export default function NotificationsPage() {
  const [items, setItems] = useState<NotificationItem[]>([]);
  const [loading, setLoading] = useState(true);

  const load = async (uid: number) => {
    try {
      setItems(await getNotifications(uid));
    } catch {
      toast.error("Không tải được thông báo");
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
    load(user.id);
  }, []);

  const handleRead = async (id: number) => {
    try {
      await markAsRead(id);
      setItems((prev) =>
        prev.map((n) => (n.notificationId === id ? { ...n, read: true } : n))
      );
    } catch {
      toast.error("Thao tác thất bại");
    }
  };

  return (
    <div className="py-[40px]">
      <div className="contain">
        <h1 className="font-[700] text-[24px] text-black mb-[20px]">Thông báo</h1>
        {loading && <p className="text-gray-500">Đang tải...</p>}
        {!loading && items.length === 0 && (
          <p className="text-gray-500">Chưa có thông báo nào.</p>
        )}
        <div className="grid gap-[10px] max-w-[720px]">
          {items.map((n) => (
            <div
              key={n.notificationId}
              className={`rounded-[8px] border p-[16px] flex justify-between items-start gap-[12px] ${
                n.read ? "bg-white border-[#DEDEDE]" : "bg-blue-50 border-blue-200"
              }`}
            >
              <div>
                <span className="inline-block text-[11px] font-[600] text-blue-700 bg-blue-100 rounded-[4px] px-[8px] py-[2px] mb-[6px]">
                  {n.type}
                </span>
                <p className="text-[14px] text-black">{n.message}</p>
                <p className="text-[12px] text-gray-400 mt-[4px]">
                  {new Date(n.createdAt).toLocaleString("vi-VN")}
                </p>
              </div>
              {!n.read && (
                <button
                  onClick={() => handleRead(n.notificationId)}
                  className="text-[12px] font-[600] text-blue-600 whitespace-nowrap"
                >
                  Đánh dấu đã đọc
                </button>
              )}
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
