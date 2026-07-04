import Link from "next/link"
import { FaAngleDown, FaAngleRight, FaBars } from "react-icons/fa6"

export const Header = () => {
  return (
    <>
      <header className="py-[15px] bg-[#000071]">
        <div className="contain">
          <div className="flex lg:justify-between items-center">
            <Link href="/" className="text-[#FFFFFF] sm:text-[28px] text-[20px] font-[800] lg:flex-none flex-1">AITasker</Link>
            <nav className="menu">
              <ul>
                <li>
                  <Link href="#">Tìm chuyên gia</Link>
                  <FaAngleDown />
                  <ul>
                    <li>
                      <Link href="#">Theo Kỹ Năng</Link>
                      <FaAngleRight />
                      <ul>
                        <li>
                          <Link href="#">AI Engineer</Link>
                        </li>
                        <li>
                          <Link href="#">Frontend Developer</Link>
                        </li>
                        <li>
                          <Link href="#">Backend Developer</Link>
                        </li>
                        <li>
                          <Link href="#">Data Scientist</Link>
                        </li>
                        <li>
                          <Link href="#">DevOps Engineer</Link>
                        </li>
                      </ul>
                    </li>
                    <li>
                      <Link href="#">Theo Danh Mục</Link>
                      <FaAngleRight />
                    </li>
                    <li>
                      <Link href="#">Theo Mức Giá</Link>
                      <FaAngleRight />
                    </li>
                  </ul>
                </li>
                <li>
                  <Link href="#">Dự Án</Link>
                  <FaAngleDown />
                  <ul>
                    <li>
                      <Link href="#">Dự Án AI</Link>
                      <FaAngleRight />
                    </li>
                    <li>
                      <Link href="#">Dự Án Website</Link>
                      <FaAngleRight />
                    </li>
                    <li>
                      <Link href="#">Dự Án Mobile App</Link>
                      <FaAngleRight />
                    </li>
                    <li>
                      <Link href="#">Dự Án Thiết Kế</Link>
                      <FaAngleRight />
                    </li>
                    <li>
                      <Link href="#">Dự Án Marketing</Link>
                      <FaAngleRight />
                    </li>
                  </ul>
                </li>
                <li>
                  <Link href="#">Dịch Vụ</Link>
                  <FaAngleDown />
                  <ul>
                    <li>
                      <Link href="#">AI Services</Link>
                      <FaAngleRight />
                    </li>
                    <li>
                      <Link href="#">Design</Link>
                      <FaAngleRight />
                    </li>
                    <li>
                      <Link href="#">Marketing</Link>
                      <FaAngleRight />
                    </li>
                    <li>
                      <Link href="#">Development</Link>
                      <FaAngleRight />
                    </li>
                  </ul>
                </li>
                <li>
                  <Link href="#">Doanh Nghiệp</Link>
                  <FaAngleDown />
                  <ul>
                    <li>
                      <Link href="#">Doanh Nghiệp Nổi Bật</Link>
                      <FaAngleRight />
                    </li>
                    <li>
                      <Link href="#">Đối Tác Chiến Lược</Link>
                      <FaAngleRight />
                    </li>
                    <li>
                      <Link href="#">Startup Tuyển Dụng</Link>
                      <FaAngleRight />
                    </li>
                    <li>
                      <Link href="#">Công Ty Công Nghệ</Link>
                      <FaAngleRight />
                    </li>
                  </ul>
                </li>
              </ul>
            </nav>
            <div className="sm:text-[16px] text-[12px] font-[600] text-[#FFFFFF] flex gap-[5px]">
              <Link href="#">Đăng nhập</Link>
              <span>/</span> 
              <Link href="#">Đăng ký</Link>
            </div>
            <button className="text-[23px] text-[#FFFFFF] lg:hidden ml-[12px]">
              <FaBars />
            </button>
          </div>
        </div>
      </header>
    </>
  )
}