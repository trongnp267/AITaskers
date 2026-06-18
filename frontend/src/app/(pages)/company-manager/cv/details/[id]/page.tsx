import Link from "next/link";

export default function Page() {
  return (
    <>
      <div className="py-[60px]">
        <div className="contain">
          {/* Thông tin CV */}
          <div className="p-[20px] border border-[#DEDEDE] bg-white rounded-[8px]">
            <div className="flex flex-wrap gap-[20px] justify-between items-center mb-[20px]">
              <div className="font-[400] text-[20px] text-black">
                Thông tin CV
              </div>
              <Link href="/company-manager/cv/list" className="font-[400] text-[14px] text-[#0088FF] underline">
                Quay lại danh sách
              </Link>
            </div>
            <div className="flex flex-col gap-[10px]">
              <div className="font-[400] text-[16px] text-black">
                Họ tên: <b>Lê Văn A</b>
              </div>
              <div className="font-[400] text-[16px] text-black">
                Email: <b>levana@gmail.com</b>
              </div>
              <div className="font-[400] text-[16px] text-black">
                Số điện thoại: <b>0123456789</b>
              </div>
              <div className="font-[400] text-[16px] text-black">
                File CV:
              </div>
              <div className="h-[736px] bg-[#D9D9D9]">
                Preview File CV dạng PDF tại đây
              </div>
            </div>
          </div>
          {/* Hết Thông tin CV */}

          {/* Thông tin công việc */}
          <div className="p-[20px] border border-[#DEDEDE] bg-white rounded-[8px] mt-[20px]">
            <div className="font-[700] text-[20px] text-black mb-[20px]">
              Thông tin công việc
            </div>
            <div className="flex flex-col gap-[10px]">
              <div className="font-[400] text-[16px] text-black">
                Tên công việc: <b>Frontend Engineer (ReactJS)</b>
              </div>
              <div className="font-[400] text-[16px] text-black">
                Mức lương: <b>1.000$ - 1.500$</b>
              </div>
              <div className="font-[400] text-[16px] text-black">
                Cấp bậc: <b>Fresher</b>
              </div>
              <div className="font-[400] text-[16px] text-black">
                Hình thức làm việc: <b>Tại văn phòng</b>
              </div>
              <div className="font-[400] text-[16px] text-black">
                Công nghệ: <b>HTML5, CSS3, Javascript, ReactJS</b>
              </div>
            </div>
            <Link href={"/job/details/123"} className="font-[400] text-[14px] text-[#0088FF] underline mt-[10px]">Xem chi tiết công việc</Link>
          </div>
          {/* Hết Thông tin công việc */}
        </div>
      </div>
    </>
  )
}