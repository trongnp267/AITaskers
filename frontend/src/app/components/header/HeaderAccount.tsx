"use client";

import Link from "next/link"
import { useEffect, useState } from "react";

export const HeaderAccount = () => {

  const [user, setUser] = useState<any>(null);

  useEffect(() => {
    const userData = localStorage.getItem("user");
    if (userData) {
      setUser(JSON.parse(userData));
    }
  }, []);

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    window.location.reload();
  }

  console.log(user);

  return (
    <>
      <div className="sm:text-[16px] text-[12px] font-[600] text-[#FFFFFF] flex gap-[5px]">
        {
          !user ? (
            <>
              {/* Chưa đăng nhập */}
              <Link href="/login">Đăng nhập</Link>
              <span>/</span> 
              <Link href="/register">Đăng ký</Link>
            </>
          ) : (
            <>
              {/* Đã đăng nhập */}
              <nav className="menu">
                <ul>
                  <li>
                    <Link href={"#"}>{user.username}</Link>
                    <ul className="inner-right">
                      {user?.role === "CLIENT" && (
                        <>
                          <li>
                            <Link href="#">
                              Thông tin công ty
                            </Link>
                          </li>

                          <li>
                            <Link href="#">
                              Quản lý CV
                            </Link>
                          </li>

                          <li>
                            <Link href="/company-manager/job/list">
                              Quản lý bài đăng
                            </Link>
                          </li>

                          <li>
                            <button onClick={handleLogout}>
                              Đăng xuất
                            </button>
                          </li>
                        </>
                      )}
                      {user?.role === "EXPERT" && (
                        <>
                          <li>
                            <Link href="#">
                              Thông tin AI Expert
                            </Link>
                          </li>

                          <li>
                            <Link href="#">
                              Quản lý CV đã gửi
                            </Link>
                          </li>

                          <li>
                            <button onClick={handleLogout}>
                              Đăng xuất
                            </button>
                          </li>
                        </>
                      )}
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