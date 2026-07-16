import Link from "next/link"
import { FaBriefcase, FaCheckCircle, FaUserTie } from "react-icons/fa"
import { Tag2 } from "../tag/Tag2"

export const CVCard1 = () => {
  return (
    <>
      <div className="card-item">
        <img src="/assets/images/bg-image-company-cart.svg" alt="" className="inner-bg" />
        <div className="inner-main py-[20px] px-[17px]">
          <div className="font-[700] text-[18px] text-[#121212] line-clamp-1 mb-[12px] text-center">
            Frontend Engineer (ReactJS)
          </div>
          <div className="font-[400] text-[14px] text-black mb-[6px] text-center">
              Công ty: <b>Công ty ABC</b>
            </div>
          <div className="font-[600] text-[16px] text-[#0088FF] mb-[6px] text-center">
            1.000$ - 1.500$
          </div>
          <div className="flex flex-col gap-[6px] items-center">
            <Tag2
              icon={<FaUserTie className="text-[16px] text-black" />}
              label="Fresher"
            />
            <Tag2
              icon={<FaBriefcase className="text-[16px] text-black" />}
              label="Tại văn phòng"
            />
            <Tag2
              icon={<FaCheckCircle className="text-[16px] text-black" />}
              label="Chưa duyệt"
            />
          </div>
          <div className="flex justify-center gap-[8px] flex-wrap mt-[12px]">
            <Link 
              href={"#"}
              className="px-[20px] py-[8px] rounded-[8px] font-[400] text-[14px] text-[white] bg-[#0088FF] inline-block"
            >
              Xem
            </Link>
            <Link 
              href={"#"}
              className="px-[20px] py-[8px] rounded-[8px] font-[400] text-[14px] text-[#FFFFFF] bg-[#FF0000] inline-block"
            >
              Xóa
            </Link>
          </div>
        </div>
      </div>
    </>
  )
}