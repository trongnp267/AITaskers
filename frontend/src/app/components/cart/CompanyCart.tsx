import Link from "next/link"
import { FaUserTie } from "react-icons/fa"

export const CompanyCart = () => {
  return (
    <>
      <div
        className="company-cart"
      >
        <Link
          href="#"
        >
          <img src="assets/images/bg-image-company-cart.svg" alt="" className="inner-bg" />
          <div className="inner-main">
            <div
              className="inner-image w-[160px] h-[160px] mt-[32px] mb-[24px]"
            >
              <img src="assets/images/lg-company.png" alt=""/>
            </div>
            <div className="h-[50px] font-[700] text-[18px] text-[#121212] text-center line-clamp-2 mx-[16px]">
              LG Electronics Development Vietnam (LGEDV)
            </div>
            <div
              className="flex justify-between items-center bg-[#F7F7F7] px-[16px] py-[12px] mt-[24px]"
            >
              <div
                className="font-[400] text-[14px] text-[#414042]"
              >
                Ho Chi Minh
              </div>
              <div
                className="flex gap-[6px] items-center"
              >
                <FaUserTie className="text-[14px] text-[#000096]" />
                <div className="font-[400] text-[14px] text-[#121212]">5 Việc làm</div>
              </div>
            </div>
          </div>
        </Link>
      </div>
    </>
  )
}