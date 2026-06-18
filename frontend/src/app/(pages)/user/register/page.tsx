import { BoxGroup1 } from "@/app/components/form/BoxGroup1";
import { ButtonGroup2 } from "@/app/components/form/ButtonGroup2";

export default function Page() {
  return (
    <>
      <div className="py-[60px] sm:min-h-[calc(100vh-121px-72px)] min-h-[calc(100vh-121px-60px)]">
        <div className="contain">
          <div className="bg-white rounded-[8px] border border-[#DEDEDE] px-[20px] py-[50px] max-w-[602px] w-full mx-[auto]">
            <h1 className="font-[700] text-[20px] text-black mb-[20px] text-center">
              Đăng ký (Ứng viên)
            </h1>
            <form action="" className="grid gap-x-[20px] gap-y-[15px]">
              <BoxGroup1
                name="fullName"
                label="Họ tên *"
                id="fullName"
                type="text"
              />
              <BoxGroup1
                name="email"
                label="Email *"
                id="emai"
                type="text"
              />
              <BoxGroup1
                name="password"
                label="Mật khẩu *"
                id="password"
                type="password"
              />
              <ButtonGroup2
                label="Đăng ký"
              />
            </form>
          </div>
        </div>
      </div>
    </>
  )
}