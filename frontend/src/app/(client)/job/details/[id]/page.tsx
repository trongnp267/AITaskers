import { CompanyDescription } from "@/app/components/description/CompanyDescription";
import { BoxGroup1 } from "@/app/components/form/BoxGroup1";
import { Tag1 } from "@/app/components/tag/Tag1";
import { Tag2 } from "@/app/components/tag/Tag2";
import Link from "next/link";
import { FaBriefcase, FaUserTie } from "react-icons/fa"
import { FaArrowRight, FaLocationDot } from "react-icons/fa6"

export default function Page() {
  return (
    <>
      <div className="pt-[30px] pb-[60px]">
        <div className="contain">
          <div className="grid lg:grid-cols-[770fr_395fr] gap-[20px]">
            <div>
              {/* Thông tin công việc */}
              <div className="bg-white rounded-[8px] border border-[#DEDEDE] p-[20px]">
                <h1 className="font-[700] sm:text-[28px] text-[24px] text-[#121212] mb-[10px]">
                  Front End Developer ( Javascript, ReactJS)
                </h1>
                <div className="font-[400] text-[16px] text-[#414042] mb-[10px]">
                  LG CNS Việt Nam
                </div>
                <div className="font-[700] text-[20px] text-[#0088FF] mb-[20px]">
                  1.000$ - 1.500$
                </div>
                <Link href="#" className="h-[48px] px-[10px] bg-[#0088FF] rounded-[4px] font-[700] text-[16px] text-[#FFFFFF] flex items-center justify-center mb-[20px]">
                  Ứng tuyển
                </Link>
                <div className="grid grid-cols-3 sm:gap-[16px] gap-[8px] mb-[20px]">
                  <img src="/assets/images/job-detail-image-1.png" alt="" className="rounded-[4px] object-cover" />
                  <img src="/assets/images/job-detail-image-2.png" alt="" className="rounded-[4px] object-cover" />
                  <img src="/assets/images/job-detail-image-3.png" alt="" className="rounded-[4px] object-cover" />
                </div>
                <div className="flex flex-col gap-[10px] mb-[10px]">
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
                    label="Tầng 15, tòa Keangnam Landmark 72, Mễ Trì, Nam Tu Liem, Ha Noi"
                  />
                </div>
                <div className="flex gap-[8px]">
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
              {/* Hết Thông tin công việc */}

              {/* Mô tả chi tiết công việc */}
              <div className="bg-white rounded-[8px] border border-[#DEDEDE] p-[20px] mt-[20px]">
                Lorem ipsum dolor sit amet consectetur adipisicing elit. Mollitia doloremque voluptas, doloribus maxime, rerum quod reprehenderit atque recusandae itaque natus eos inventore ad sapiente voluptatum repellendus minus laborum ex iusto. Lorem ipsum dolor, sit amet consectetur adipisicing elit. A natus doloribus nemo ullam. Doloribus sed molestiae consequuntur voluptate enim nesciunt quisquam aliquid accusamus quam, sint voluptas, doloremque hic inventore eligendi. Lorem ipsum dolor sit amet consectetur, adipisicing elit. Tempora sit explicabo voluptates aspernatur sint tempore voluptatibus inventore nihil accusamus veritatis dolores pariatur magnam iusto, delectus error. Doloribus voluptates tempore voluptatum!
              </div>
              {/* Hết Mô tả chi tiết công việc */}

              {/* Form ứng tuyển */}
              <div className="bg-white rounded-[8px] border border-[#DEDEDE] p-[20px] mt-[20px]">
                <h2 className="font-[700] text-[20px] text-black mb-[20px]">
                  Ứng tuyển ngay
                </h2>
                <form 
                  action=""
                  className="flex flex-col gap-[15px]"
                >
                  <BoxGroup1
                    label="Họ tên *"
                    name="name"
                    type="text"
                  />
                  <BoxGroup1
                    label="Email *"
                    name="email"
                    type="email"
                  />
                  <BoxGroup1
                    label="Số điện thoại *"
                    name="phone"
                    type="text"
                  />
                  <BoxGroup1
                    label="File CV dạng PDF *"
                    name="fileCV"
                    type="file"
                  />
                  <button className="h-[48px] rounded-[4px] bg-[#0088FF] px-[10px] font-[700] text-[16px] text-white flex items-center justify-center">
                    Gửi CV ứng tuyển
                  </button>
                </form>
              </div>
              {/* Hết Form ứng tuyển */}
            </div>
            
            <div>
              {/* Thông tin công ty */}
              <div className="p-[20px] bg-white border border-[#DEDEDE] rounded-[8px]">
                <div className="flex gap-[12px] mb-[20px]">
                  <img src="/assets/images/company-in4-logo.png" alt="" className="w-[100px] aspect-square rounded-[4px] object-contain" />
                  <div className="flex-1">
                    <div className="font-[700] text-[18px] text-[#121212] mb-[10px]">
                      LG CNS Việt Nam
                    </div>
                    <Link href="#" className="font-[400] text-[16px] text-[#0088FF] flex items-center gap-[8px]">
                      Xem công ty <FaArrowRight />
                    </Link>
                  </div>
                </div>
                <div className="flex flex-col gap-[10px]">
                  <CompanyDescription
                    label="Mô hình công ty"
                    content="Sản phẩm"
                  />
                  <CompanyDescription
                    label="Quy mô công ty"
                    content="151 - 300 nhân viên"
                  />
                  <CompanyDescription
                    label="Thời gian làm việc"
                    content="Thứ 2 - Thứ 6"
                  />
                  <CompanyDescription
                    label="Làm việc ngoài giờ"
                    content="Không có OT"
                  />
                </div>
              </div>
              {/* Hết Thông tin công ty */}
            </div>
          </div>
        </div>
      </div>
    </>
  )
}