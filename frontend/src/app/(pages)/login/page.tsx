import Link from "next/link";
import { FaBuilding, FaUserTie } from "react-icons/fa6";

export default function Page() {
  return (
    <>
      <div className="py-[60px] sm:min-h-[calc(100vh-121px-72px)] min-h-[calc(100vh-121px-60px)]">
        <div className="contain">
          <div className="bg-white rounded-[8px] border border-[#DEDEDE] px-[20px] py-[50px] max-w-[602px] w-full mx-[auto]">
            <h1 className="font-[700] text-[20px] text-black mb-[20px] text-center">
              Vui lòng chọn vai trò để tiếp tục đăng nhập
            </h1>
            <div className="grid gap-8 mt-10">
              <Link
                href="/user/login"
                className="
                  group
                  border-2 border-gray-200
                  rounded-2xl
                  p-8
                  hover:border-blue-600
                  hover:shadow-xl
                  transition-all
                  duration-300
                  text-center
                "
              >
                <FaUserTie
                  className="
                    text-6xl
                    text-blue-600
                    mb-6
                    mx-auto
                    group-hover:scale-110
                    transition
                  "
                />

                <h3 className="text-2xl font-bold">
                  AI Expert
                </h3>

                <p className="mt-3 text-gray-500">
                  Find projects and connect with companies.
                </p>
              </Link>

              <Link
                href="/company/login"
                className="
                  group
                  border-2 border-gray-200
                  rounded-2xl
                  p-8
                  hover:border-blue-600
                  hover:shadow-xl
                  transition-all
                  duration-300
                  text-center
                "
              >
                <FaBuilding
                  className="
                    text-6xl
                    text-blue-600
                    mb-6
                    mx-auto
                    group-hover:scale-110
                    transition
                  "
                />

                <h3 className="text-2xl font-bold">
                  Company
                </h3>

                <p className="mt-3 text-gray-500">
                  Hire AI experts and build your team.
                </p>
              </Link>
            </div>
          </div>
        </div>
      </div>
    </>
  )
}