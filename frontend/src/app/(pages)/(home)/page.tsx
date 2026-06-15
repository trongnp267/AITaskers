import { ProfileCart } from "@/app/components/cart/ProfileCart";
import { Section1 } from "@/app/components/section/Section1";

export default function Page() {
  return (
    <>
      {/* Section 1 */}
      <Section1 />
      {/* End Section 1 */}

      {/* Section 2 */}
      <div className="py-[60px]">
        <div className="contain">
          <h2 className="font-[700] sm:text-[28px] text-[24px] text-[#121212] text-center mb-[30px]">
            Các chuyên gia AI hàng đầu
          </h2>
          <div className="grid lg:grid-cols-3 sm:grid-cols-2 grid-cols-1 lg:gap-x-[20px] gap-x-[10px] gap-y-[20px]">
            <ProfileCart />
            <ProfileCart />
            <ProfileCart />
            <ProfileCart />
            <ProfileCart />
            <ProfileCart />
            <ProfileCart />
            <ProfileCart />
            <ProfileCart />
          </div>
        </div>
      </div>
      {/* End Section 2 */}
    </>
  );
}
