import Link from "next/link"
import { FaBriefcase, FaUserTie } from "react-icons/fa"
import { FaLocationDot } from "react-icons/fa6"
import { Tag1 } from "../tag/Tag1"
import { Tag2 } from "../tag/Tag2"

export const JobCart = () => {
  return (
    <>
      <div className="card-item">
        <Link href="#">
          <img src="/assets/images/bg-image-company-cart.svg" alt="" className="inner-bg" />
          <div className="inner-main py-[20px] px-[17px]">
            <div className="inner-image w-[116px] aspect-square mb-[20px]">
              <img src="/assets/images/lg-company.png" alt="" />
            </div>
            <div className="font-[700] text-[18px] text-[#121212] line-clamp-1 mb-[6px] text-center">
              Frontend Engineer (ReactJS)
            </div>
            <div className="font-[400] text-[14px] text-[#121212] line-clamp-1 mb-[12px] text-center">
              LG CNS Việt Nam
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
                icon={<FaLocationDot className="text-[16px] text-black" />}
                label="Ha Noi"
              />
            </div>
            <div className="flex gap-[8px] mt-[13px] justify-center flex-wrap">
              <Tag1
                label="ReactJS"
              />
              <Tag1
                label="NextJS"
              />
              <Tag1
                label="Javascript"
              />
            </div>
          </div>
        </Link>
      </div>
    </>
  )
}