"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";

import {
  FaHome,
  FaUsers,
  FaClipboardCheck,
  FaProjectDiagram,
  FaCog,
  FaSignOutAlt,
} from "react-icons/fa";

interface MenuItem {
  title: string;
  href: string;
  icon: React.ReactNode;
}

const menus: MenuItem[] = [
  {
    title: "Dashboard",
    href: "/admin/dashboard",
    icon: <FaHome />,
  },
  {
    title: "Pending Accounts",
    href: "/admin/approvals",
    icon: <FaClipboardCheck />,
  },
  {
    title: "Users",
    href: "/admin/users",
    icon: <FaUsers />,
  },
  {
    title: "Projects",
    href: "/admin/projects",
    icon: <FaProjectDiagram />,
  },
  {
    title: "Settings",
    href: "/admin/settings",
    icon: <FaCog />,
  },
];

export default function AdminSidebar() {
  const pathname = usePathname();

  return (
    <aside className="w-64 bg-slate-900 text-white flex flex-col">

      <div className="border-b border-slate-700 p-6">

        <h1 className="text-2xl font-bold">
          AITasker
        </h1>

        <p className="text-sm text-slate-400">
          Admin Dashboard
        </p>

      </div>

      <nav className="flex-1 p-4 space-y-2">

        {menus.map((menu) => {

          const active = pathname === menu.href;

          return (
            <Link
              key={menu.href}
              href={menu.href}
              className={`flex items-center gap-3 rounded-lg px-4 py-3 transition

                ${
                  active
                    ? "bg-indigo-600 text-white"
                    : "hover:bg-slate-800 text-slate-300"
                }`}
            >
              <span className="text-lg">
                {menu.icon}
              </span>

              {menu.title}
            </Link>
          );
        })}
      </nav>

      <div className="border-t border-slate-700 p-4">

        <button
          className="flex w-full items-center gap-3 rounded-lg px-4 py-3 hover:bg-red-600 transition"
        >
          <FaSignOutAlt />

          Logout
        </button>

      </div>

    </aside>
  );
}