"use client"

import Link from "next/link"
import { FaAngleDown, FaAngleRight} from "react-icons/fa6"
import { useEffect, useState } from "react"
import { getCurrentUser, logout, CurrentUser } from "@/app/lib/auth"

export const HeaderMenu = (props: any) => {
  const {showMenu} = props;
  const [user, setUser] = useState<CurrentUser | null>(null);
  useEffect(() => { setUser(getCurrentUser()); }, []);

  const accountItems = !user
    ? [
        { name: "Đăng nhập", link: "/login", children: [] },
        { name: "Đăng ký", link: "/register", children: [] },
      ]
    : [
        ...(user.role === "CLIENT"
          ? [
              { name: "Thông tin công ty", link: "/company-manager/profile", children: [] },
              { name: "Quản lý bài đăng", link: "/company-manager/job/list", children: [] },
              { name: "Báo giá nhận được", link: "/company-manager/cv/list", children: [] },
            ]
          : []),
        ...(user.role === "EXPERT"
          ? [
              { name: "Hồ sơ AI Expert", link: "/user-manager/profile", children: [] },
              { name: "Báo giá đã gửi", link: "/user-manager/cv/list", children: [] },
            ]
          : []),
        ...(user.role === "ADMIN"
          ? [{ name: "Trang quản trị", link: "/admin/dashboard", children: [] }]
          : []),
      ];

  const menuList = [
    {
      name: "Công việc",
      link: "/jobs",
      children: []
    },
    {
      name: "Ví",
      link: "/wallet",
      children: []
    },
    {
      name: "Thông báo",
      link: "/notifications",
      children: []
    },
    {
      name: "Tìm chuyên gia",
      link: "/search",
      children: [
        {
          name: "AI Engineer",
          link: "/search?q=AI",
          children: []
        },
        {
          name: "Data Scientist",
          link: "/search?q=Data",
          children: []
        },
        {
          name: "NLP / Chatbot",
          link: "/search?q=NLP",
          children: []
        },
      ]
    },
    {
      name: "Tìm việc",
      link: "/search/job",
      children: [
        {
          name: "Dự Án AI",
          link: "/search/job?q=AI",
          children: []
        },
        {
          name: "Dự Án Website",
          link: "/search/job?q=Website",
          children: []
        },
        {
          name: "Dự Án Mobile App",
          link: "/search/job?q=Mobile",
          children: []
        },
      ]
    },
    {
      name: "Doanh nghiệp",
      link: "/company/list",
      children: []
    },
  ]

  return (
    <>
      <nav className={"menu " + (showMenu ? "show" : "")}>
        <ul>
          {
            menuList.map((item: any, index: number) => (
              <li key={index}>
                <Link href={item.link}>{item.name}</Link>
                {item.children.length > 0 && (
                  <>
                    <FaAngleDown />
                    <ul>
                      {item.children.map((itemChild1: any, indexChild1: number) => (
                        <li key={indexChild1}>
                          <Link href={itemChild1.link}>{itemChild1.name}</Link>
                          {itemChild1.children.length > 0 && (
                            <>
                              <FaAngleRight />
                              <ul>
                                {itemChild1.children.map((itemChild2: any, indexChild2: number) => (
                                  <li key={indexChild2}>
                                    <Link href={itemChild2.link}>{itemChild2.name}</Link>
                                  </li>
                                ))}
                              </ul>
                            </>
                          )}
                        </li>
                      ))}
                    </ul>
                  </>
                )}
              </li>
            ))
          }
          {accountItems.map((item, index) => (
            <li key={"acc-" + index} className="lg:hidden">
              <Link href={item.link}>{item.name}</Link>
            </li>
          ))}
          {user && (
            <li className="lg:hidden">
              <a href="#" onClick={(e) => { e.preventDefault(); logout(); }}>Đăng xuất</a>
            </li>
          )}
        </ul>
      </nav>
    </>
  )
}