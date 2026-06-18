import Link from "next/link"
import { FaUserTie } from "react-icons/fa"

export const CompanyCard = () => {
  return (
    <>
      <div
        className="card-item"
      >
        <Link
          href="#"
        >
          <img src="/assets/images/bg-image-company-cart.svg" alt="" className="inner-bg" />
          <div className="inner-main">
            <div
              className="inner-image sm:w-[160px] w-[125px] sm:h-[160px] h-[125px] sm:mt-[32px] mt-[20px] sm:mb-[24px] mb-[16px]"
            >
              <img src="/assets/images/lg-company.png" alt=""/>
            </div>
            <div className="sm:h-[50px] h-[40px] font-[700] sm:text-[18px] text-[14px] text-[#121212] text-center line-clamp-2 sm:mx-[16px] mx-[8px]">
              LG Electronics Development Vietnam (LGEDV)
            </div>
            <div
              className="flex sm:justify-between justify-center items-center bg-[#F7F7F7] px-[16px] py-[12px] sm:mt-[24px] mt-[16px] sm:flex-nowrap flex-wrap sm:gap-[0px] gap-[12px]"
            >
              <div
                className="font-[400] sm:text-[14px] text-[12px] text-[#414042] sm:w-[auto] w-[100%] text-center"
              >
                Ho Chi Minh
              </div>
              <div
                className="flex gap-[6px] items-center"
              >
                <FaUserTie className="text-[14px] text-[#000096]" />
                <div className="font-[400] sm:text-[14px] text-[12px] text-[#121212]">5 Việc làm</div>
              </div>
            </div>
          </div>
        </Link>
      </div>
    </>
  )
}