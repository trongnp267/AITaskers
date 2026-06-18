import Link from "next/link"
import { FaBriefcase, FaCheckCircle, FaEye, FaUserTie } from "react-icons/fa"
import { FaPhone } from "react-icons/fa6"
import { Tag2 } from "../tag/Tag2"
import { IoMail } from "react-icons/io5"

export const CVCard = () => {
  return (
    <>
      <div className="card-item">
        <img src="/assets/images/bg-image-company-cart.svg" alt="" className="inner-bg" />
        <div className="inner-main py-[20px] px-[17px]">
          <div className="font-[700] text-[18px] text-[#121212] line-clamp-1 mb-[12px] text-center">
            Frontend Engineer (ReactJS)
          </div>
          <div className="flex flex-col gap-[6x] items-center mb-[12px]">
            <div className="font-[400] text-[14px] text-black">
              Ứng viên: <b>Lê Văn A</b>
            </div>
            <Tag2
              icon={<IoMail className="text-[16px] text-black" />}
              label="levana@gmail.com"
            />
            <Tag2
              icon={<FaPhone className="text-[16px] text-black" />}
              label="0123456789"
            />
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
              icon={<FaEye className="text-[16px]" />}
              label="Chưa xem"
              className="text-[#FF0000]"
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
              className="px-[20px] py-[8px] rounded-[8px] font-[400] text-[14px] text-[black] bg-[#9FDB7C] inline-block"
            >
              Duyệt
            </Link>
            <Link 
              href={"#"}
              className="px-[20px] py-[8px] rounded-[8px] font-[400] text-[14px] text-[white] bg-[#FF5100] inline-block"
            >
              Từ chối
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