"use client";

import Link from "next/link"
import { useEffect, useState } from "react";
import { getCurrentUser, logout, CurrentUser } from "@/app/lib/auth";

export const HeaderAccount = () => {

  const [user, setUser] = useState<CurrentUser | null>(null);

  useEffect(() => {
    setUser(getCurrentUser());
  }, []);

  const handleLogout = () => {
    logout();
  }

  return (
    <>
      <div className="sm:text-[16px] text-[12px] font-[600] text-[#FFFFFF] flex gap-[5px]">
        {
          !user ? (
            <>
              <Link href="/login">Đăng nhập</Link>
              <span>/</span>
              <Link href="/register">Đăng ký</Link>
            </>
          ) : (
            <>
              <nav className="menu">
                <ul>
                  <li>
                    <Link href={"#"}>{user.username}</Link>
                    <ul className="inner-right">
                      {user?.role === "CLIENT" && (
                        <>
                          <li>
                            <Link href="/company-manager/profile">
                              Thông tin công ty
                            </Link>
                          </li>

                          <li>
                            <Link href="/company-manager/job/list">
                              Quản lý bài đăng
                            </Link>
                          </li>

                          <li>
                            <Link href="/company-manager/cv/list">
                              Báo giá nhận được
                            </Link>
                          </li>
                        </>
                      )}
                      {user?.role === "EXPERT" && (
                        <>
                          <li>
                            <Link href="/user-manager/profile">
                              Thông tin AI Expert
                            </Link>
                          </li>

                          <li>
                            <Link href="/jobs">
                              Tìm việc làm
                            </Link>
                          </li>

                          <li>
                            <Link href="/user-manager/cv/list">
                              Báo giá đã gửi
                            </Link>
                          </li>
                        </>
                      )}
                      {user?.role === "ADMIN" && (
                        <li>
                          <Link href="/admin/dashboard">
                            Trang quản trị
                          </Link>
                        </li>
                      )}
                      <li>
                        <Link href="/wallet">
                          Ví của tôi
                        </Link>
                      </li>
                      <li>
                        <Link href="/notifications">
                          Thông báo
                        </Link>
                      </li>
                      <li>
                        <button onClick={handleLogout}>
                          Đăng xuất
                        </button>
                      </li>
                    </ul>
                  </li>
                </ul>
              </nav>
            </>
          )
        }
        </div>
    </>
  )
}
