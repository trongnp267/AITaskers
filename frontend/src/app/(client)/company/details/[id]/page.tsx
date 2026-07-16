import { JobCard } from "@/app/components/card/JobCard";
import { CompanyDescription } from "@/app/components/description/CompanyDescription";
import { FaLocationDot } from "react-icons/fa6";

export default function Page() {
  return (
    <>
      <div className="pt-[30px] pb-[60px]">
        <div className="contain">
          {/* Thônng tin công ty */}
          <div className="p-[20px] border border-[#DEDEDE] bg-white rounded-[8px]">
            <div className="flex gap-[16px] mb-[20px] md:flex-nowrap flex-wrap">
              <img src="/assets/images/company-in4-logo.png" alt="" className="w-[100px] aspect-square rounded-[4px] object-contain" />
              <div className="md:flex-1">
                <h1 className="font-[700] text-[28px] text-[#121212] mb-[10px]">
                  LG CNS Việt Nam
                </h1>
                <div className="font-[400] text-[14px] text-[#121212] flex items-center gap-[8px]">
                  <FaLocationDot className="text-[16px]" /> Tầng 15, tòa Keangnam Landmark 72, Mễ Trì, Nam Tu Liem, Ha Noi
                </div>
              </div>
            </div>
            <div className="flex flex-col gap-[10px]">
              <CompanyDescription
                label="Mô hình công ty:"
                content="Sản phẩm"
                className="justify-start"
                gap="gap-[5px]"
              />
              <CompanyDescription
                label="Quy mô công ty:"
                content="151 - 300 nhân viên"
                className="justify-start"
                gap="gap-[5px]"
              />
              <CompanyDescription
                label="Thời gian làm việc:"
                content="Thứ 2 - Thứ 6"
                className="justify-start"
                gap="gap-[5px]"
              />
              <CompanyDescription
                label="Làm việc ngoài giờ:"
                content="Không có OT"
                className="justify-start"
                gap="gap-[5px]"
              />
            </div>
          </div>
          {/* Hết Thônng tin công ty */}

          {/* Mô tả chi tiết */}
          <div className="p-[20px] border border-[#DEDEDE] bg-white rounded-[8px] mt-[20px]">
            Lorem ipsum dolor sit amet consectetur, adipisicing elit. Voluptatum, magnam. Assumenda accusamus possimus minima maiores at a quis sunt pariatur esse, cumque facilis recusandae, dolor odit distinctio illum! Sit, culpa? Lorem ipsum dolor sit amet consectetur adipisicing elit. Odit nisi, ad necessitatibus porro non id odio similique nam, eos excepturi debitis quam facilis doloremque nihil fuga maiores mollitia dolorem sequi. Lorem, ipsum dolor sit amet consectetur adipisicing elit. Et, mollitia soluta aut, autem, saepe veniam sunt quis beatae quibusdam repellendus iusto minus at blanditiis nihil distinctio ea quaerat eaque tempora.
          </div>
          {/* Hết Mô tả chi tiết */}
          {/* Việc làm */}
          <div className="mt-[30px]">
            <h2 className="font-[700] text-[28px] text-[#121212] mb-[20px]">
              Công ty có 6 việc làm
            </h2>
            <div className="grid lg:grid-cols-3 sm:grid-cols-2 gap-[20px]">
              <JobCard />
              <JobCard />
              <JobCard />
              <JobCard />
              <JobCard />
            </div>
          </div>
          {/* Hết Việc làm */}
        </div>
      </div>
    </>
  )
}