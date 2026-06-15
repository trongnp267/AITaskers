import { CompanyCart } from "@/app/components/cart/CompanyCart";
import { Select1 } from "@/app/components/select/Select1";

export default function Page() {
  return (
    <>
      <div className="py-[60px]">
        <div className="contain">
          <h1 className="text-[#121212] font-[700] text-[28px] mb-[30px] text-center">
            Danh sách công ty
          </h1>
          <div
            className="grid grid-cols-3 gap-x-[20px] gap-y-[20px]"
          >
            <CompanyCart />
            <CompanyCart />
            <CompanyCart />
            <CompanyCart />
            <CompanyCart />
            <CompanyCart />
            <CompanyCart />
          </div>
          <Select1 />
        </div>
      </div>
    </>
  )
}