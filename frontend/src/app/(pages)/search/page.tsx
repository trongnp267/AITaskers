import { ProfileCart } from "@/app/components/cart/ProfileCart";
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
            76 AI Expert  <span className="text-[#0088FF]">AI Chatbot</span>
          </h2>
          <div 
            className="mb-[30px] px-[20px] py-[10px] rounded-[8px] shadow-[0px_4px_20px_0px_#0000000F] flex gap-[12px] flex-wrap"
          >
            <select 
              name="experience" 
              id=""
              className="rounded-[20px] bg-white border border-[#DEDEDE] h-[36px] px-[18px] font-[400] text-[16px] text-[#414042]"
            >
              <option defaultValue="">Kinh Nghiệm</option>
              <option defaultValue="">1-3 Kinh Nghiệm</option>
              <option defaultValue="">3-5 Kinh Nghiệm</option>
              <option defaultValue="">5-8 Kinh Nghiệm</option>
              <option defaultValue="">8+ Kinh Nghiệm</option>
            </select>
            <select 
              name="price" 
              id=""
              className="rounded-[20px] bg-white border border-[#DEDEDE] h-[36px] px-[18px] font-[400] text-[16px] text-[#414042]"
            >
              <option defaultValue="">Giá</option>
              <option defaultValue="">1000$-3000$</option>
              <option defaultValue="">3000$-5000$</option>
              <option defaultValue="">5000$+</option>
            </select>
          </div>
          <div
            className="grid lg:grid-cols-3 sm:grid-cols-2 grid-cols-1 gap-[20px]"
          >
            <ProfileCart />
            <ProfileCart />
            <ProfileCart />
            <ProfileCart />
            <ProfileCart />
            <ProfileCart />
            <ProfileCart />
          </div>
          <Select1 />
        </div>
      </div>
      {/* Hết Kết quả tìm kiếm */}
    </>
  )
}