"use client";

import {
  FaBell,
  FaUserCircle,
  FaSearch,
} from "react-icons/fa";
import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import Link from "next/link";
import { getCurrentUser } from "@/app/lib/auth";
import { getUnread } from "@/app/services/notificationService";

export default function AdminHeader() {
  const router = useRouter();
  const [keyword, setKeyword] = useState("");
  const [username, setUsername] = useState("Admin");
  const [unread, setUnread] = useState(0);

  useEffect(() => {
    const user = getCurrentUser();
    if (user) {
      setUsername(user.username);
      getUnread(user.id)
        .then((list) => setUnread(list.length))
        .catch(() => {});
    }
  }, []);

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    if (keyword.trim()) {
      router.push(`/search/job?q=${encodeURIComponent(keyword.trim())}`);
    }
  };

  return (
    <header className="h-20 bg-white shadow-sm flex items-center justify-between px-8">

      <form onSubmit={handleSearch} className="flex items-center bg-gray-100 rounded-lg px-4 py-2 w-96">

        <FaSearch className="text-gray-400" />

        <input
          type="text"
          value={keyword}
          onChange={(e) => setKeyword(e.target.value)}
          placeholder="Tìm công việc... (Enter để tìm)"
          className="ml-3 w-full bg-transparent outline-none"
        />

      </form>

      <div className="flex items-center gap-6">

        <Link href="/notifications" className="relative" title="Xem thông báo">

          <FaBell
            size={20}
            className="text-gray-600"
          />

          {unread > 0 && (
            <span
              className="absolute -top-2 -right-2
              h-5 w-5 rounded-full bg-red-500
              text-xs flex items-center justify-center text-white"
            >
              {unread}
            </span>
          )}

        </Link>

        <div className="flex items-center gap-3">

          <FaUserCircle
            size={36}
            className="text-gray-500"
          />

          <div>

            <p className="font-semibold">
              {username}
            </p>

            <p className="text-sm text-gray-500">
              Administrator
            </p>

          </div>

        </div>

      </div>

    </header>
  );
}
