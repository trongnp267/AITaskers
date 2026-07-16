"use client"

import { FaSearch } from "react-icons/fa";
import { ButtonTag } from "@/app/components/button/ButtonTag";
import { useRouter } from "next/navigation";
import { useState } from "react";

export const Section1 = () => {
  const router = useRouter();
  const [keyword, setKeyword] = useState("");
  const [type, setType] = useState("job");

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    const q = encodeURIComponent(keyword.trim());
    if (type === "expert") {
      router.push(`/search?q=${q}`);
    } else {
      router.push(`/search/job?q=${q}`);
    }
  };

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
              onSubmit={handleSearch}
              className="w-[100%] flex gap-x-[15px] mb-[30px] md:flex-nowrap flex-wrap gap-y-[12px]"
            >
              <select
                name="type"
                value={type}
                onChange={(e) => setType(e.target.value)}
                className="bg-[white] rounded-[4px] px-[20px] text-[#121212] font-[500] text-[16px] h-[56px] md:w-[240px] w-[100%]"
              >
                <option value="job">Tìm công việc</option>
                <option value="expert">Tìm chuyên gia</option>
              </select>
              <input
                type="text"
                name="search"
                value={keyword}
                onChange={(e) => setKeyword(e.target.value)}
                placeholder="Nhập từ khóa..."
                className="h-[56px] flex-1 bg-[white] rounded-[4px] px-[20px] font-[500] text-[16px] text-[#121212] placeholder:text-[#A8A8A8]"
              />
              <button
                type="submit"
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
                  link="/search/job?q=ChatGPT"
                  label="ChatGPT"
                />
                <ButtonTag
                  link="/search/job?q=Python"
                  label="Python"
                />
                <ButtonTag
                  link="/search/job?q=E-commerce"
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
