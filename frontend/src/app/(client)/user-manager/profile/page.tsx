import { BoxGroup1 } from "@/app/components/form/BoxGroup1";
import { BoxGroup2 } from "@/app/components/form/BoxGroup2";
import { BoxGroup3 } from "@/app/components/form/BoxGroup3";
import { ButtonGroup1 } from "@/app/components/form/ButtonGroup1";

export default function Page() {
  return (
    <>
      <div className="py-[60px] sm:min-h-[calc(100vh-121px-72px)] min-h-[calc(100vh-121px-60px)]">
        <div className="contain">
          <div className="bg-white rounded-[8px] border border-[#DEDEDE] p-[20px]">
            <h1 className="font-[700] text-[20px] text-black mb-[20px]">
              Thông tin cá nhân
            </h1>
            <form action="" className="grid sm:grid-cols-2 gap-x-[20px] gap-y-[15px]">
              <BoxGroup1
                name="name"
                label="Họ tên *"
                id="name"
                type="text"
                className="sm:col-span-2"
              />
              <BoxGroup1
                name="avatar"
                label="Avatar"
                id="avatar"
                type="file"
                className="sm:col-span-2"
              />
              <BoxGroup1
                name="email"
                label="Email *"
                id="emai"
                type="text"
              />
              <BoxGroup1
                name="phone"
                label="Số điện thoại"
                id="phone"
                type="text"
              />
              <ButtonGroup1 
                className="sm:col-span-2"
                label="Cập nhật"
              />
            </form>
          </div>
        </div>
      </div>
    </>
  )
}