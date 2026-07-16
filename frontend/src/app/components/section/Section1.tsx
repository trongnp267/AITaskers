import { FaSearch } from "react-icons/fa";
import { ButtonTag } from "@/app/components/button/ButtonTag";

export const Section1 = () => {
  return (
    <>
      <div className="bg-[#000065] py-[60px]">
        <div className="contain">
          <div>
            <h1 
              className="font-[700] text-[28px] text-[white] mb-[30px] text-center"
            >
              Tìm Chuyên Gia AI Phù Hợp Cho Mọi Dự Án Của Bạn...
            </h1>
            <form 
              action=""
              className="w-[100%] flex gap-x-[15px] mb-[30px] md:flex-nowrap flex-wrap gap-y-[12px]"
            >
              <select
                name="adress" 
                id=""
                className="bg-[white] rounded-[4px] px-[20px] text-[#121212] font-[500] text-[16px] h-[56px] md:w-[240px] w-[100%]"
              >
                <option value="ha-noi">Hà Nội</option>
                <option value="ho-chi-minh">Hồ Chí Minh</option>
                <option value="da-nang">Đà Nẵng</option>
              </select>
              <input 
                type="text" 
                name="search" 
                placeholder="Nhập từ khóa..." 
                className="h-[56px] flex-1 bg-[white] rounded-[4px] h-[56px] px-[20px] font-[500] text-[16px] text-[#121212] placeholder:text-[#A8A8A8]"
              />
              <button
                className="bg-[#0088FF] rounded-[4px] md:w-[240px] w-[100%] h-[56px] px-[20px] flex items-center gap-[10px] justify-center text-[white] font-[500] text-[16px]"
              >
                <FaSearch
                  className="text-[20px]"
                />
                Tìm kiếm
              </button>
            </form>
            <div
              className="flex gap-x-[12px] gap-y-[15px] items-center flex-wrap"
            >
              <p
                className="font-[500] text-[16px] text-[#DEDEDE]"
              >
                Mọi người đang tìm kiếm:
              </p>
              <div
                className="flex gap-[10px] flex-wrap"
              >
                <ButtonTag
                  link="#"
                  label="ChatGPT"
                />
                <ButtonTag
                  link="#"
                  label="Python"
                />
                <ButtonTag
                  link="#"
                  label="E-commerce"
                />
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  )
}