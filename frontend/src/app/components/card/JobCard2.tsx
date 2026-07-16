import Link from "next/link"
import { FaBriefcase, FaClock, FaUserTie } from "react-icons/fa"
import { FaLocationDot } from "react-icons/fa6"
import { Tag1 } from "../tag/Tag1"
import { Tag2 } from "../tag/Tag2"

export const JobCard2 = (props: {
  id: number;
  title: string;
  budgetMin: number;
  budgetMax: number;
  experience: number;
  positionRequirement: string;
  deadline: string;
  technologies: {
    jobSkillId: number;
    skillName: string;
    skillId: number;
    skill?: {}
  }[];
}) => {
  const {id, title, budgetMin, budgetMax, experience, technologies, positionRequirement, deadline} = props;
  return (
    <>
      <div className="card-item">
        <img src="/assets/images/bg-image-company-cart.svg" alt="" className="inner-bg" />
        <div className="inner-main py-[20px] px-[17px]">
          <div className="font-[700] text-[18px] text-[#121212] line-clamp-1 mb-[12px] text-center">
            {title}
          </div>
          <div className="font-[600] text-[16px] text-[#0088FF] mb-[6px] text-center">
            {budgetMin}$ - {budgetMax}$
          </div>
          <div className="flex flex-col gap-[8px] items-center">
            <Tag2
              icon={<FaUserTie className="text-[16px] text-black" />}
              label={positionRequirement}
            />
            <Tag2
              icon={<FaBriefcase className="text-[16px] text-black" />}
              label={experience.toString() + " năm kinh nghiệm"}
            />
            <Tag2
              icon={<FaClock className="text-[16px] text-black" />}
              label={deadline}
            />
          </div>
          <div className="flex gap-[8px] mt-[13px] justify-center flex-wrap">
            {
              technologies.map((technologie, index) => (
                <Tag1
                  key={index}
                  label={technologie.skillName}
                />
              ))
            }
          </div>
          <div className="flex justify-center gap-[12px] flex-wrap mt-[20px]">
            <Link
              href={`/jobs/${id}`}
              className="px-[20px] py-[8px] rounded-[8px] font-[600] text-[14px] text-[#FFFFFF] bg-[#0088FF] inline-block"
            >
              Xem chi tiết &amp; báo giá
            </Link>
          </div>
        </div>
      </div>
    </>
  )
}