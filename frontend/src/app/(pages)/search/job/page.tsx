import { JobCart } from "@/app/components/cart/JobCard";
import { Section1 } from "@/app/components/section/Section1";
import { Select1 } from "@/app/components/select/Select1";

export default function Page() {
  return (
    <>
      {/* Section 1 */}
      <Section1 />
      {/* End Section 1 */}

      {/* Kết quả tìm kiếm */}
      <div className="py-[60px]">
        <div className="contain">
          <h2 className="mb-[30px] font-[700] text-[28px] text-[#121212]">
            76 việc làm <span className="text-[#0088FF]">reactjs</span>
          </h2>
          <div 
            className="mb-[30px] px-[20px] py-[10px] rounded-[8px] shadow-[0px_4px_20px_0px_#0000000F] flex gap-[12px] flex-wrap"
          >
            <select 
              name="" 
              id=""
              className="rounded-[20px] bg-white border border-[#DEDEDE] h-[36px] px-[18px] font-[400] text-[16px] text-[#414042]"
            >
              <option defaultValue="">Cấp bậc</option>
              <option defaultValue="">Intern</option>
              <option defaultValue="">Fresher</option>
              <option defaultValue="">Junior</option>
              <option defaultValue="">Middle</option>
              <option defaultValue="">Senior</option>
              <option defaultValue="">Manager</option>
            </select>
            <select 
              name="" 
              id=""
              className="rounded-[20px] bg-white border border-[#DEDEDE] h-[36px] px-[18px] font-[400] text-[16px] text-[#414042]"
            >
              <option defaultValue="">Hình thức làm việc</option>
              <option defaultValue="">Tại văn phòng</option>
              <option defaultValue="">Làm từ xa</option>
              <option defaultValue="">Linh hoạt</option>
            </select>
          </div>
          <div
            className="grid lg:grid-cols-3 sm:grid-cols-2 grid-cols-1 gap-x-[20px] gap-y-[20px]"
          >
            <JobCart />
            <JobCart />
            <JobCart />
            <JobCart />
            <JobCart />
            <JobCart />
            <JobCart />
          </div>
          <Select1 totalPage={4} />
        </div>
      </div>
      {/* Hết Kết quả tìm kiếm */}
    </>
  )
}