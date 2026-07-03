import { BoxGroup1 } from "@/app/components/form/BoxGroup1";
import { BoxGroup2 } from "@/app/components/form/BoxGroup2";
import { BoxGroup3 } from "@/app/components/form/BoxGroup3";
import { ButtonGroup1 } from "@/app/components/form/ButtonGroup1";

export default function Page() {
  return (
    <>
      <div className="py-[60px]">
        <div className="contain">
          <div className="bg-white rounded-[8px] border border-[#DEDEDE] p-[20px]">
            <h1 className="font-[700] text-[20px] text-black mb-[20px]">
              Thông tin công ty
            </h1>
            <form action="" className="grid sm:grid-cols-2 gap-x-[20px] gap-y-[15px]">
              <BoxGroup1
                name="name"
                label="Tên công ty *"
                id="name"
                type="text"
                className="sm:col-span-2"
              />
              <BoxGroup1
                name="logo"
                label="Logo"
                id="logo"
                type="file"
                className="sm:col-span-2"
              />
              <BoxGroup2
                label="Thành phố"
                name="city"
                id="city"
                options={[
                  {value: "ha-noi", label: "Hà Nội"},
                  {value: "da-nang", label: "Đà Nẵng"},
                  {value: "ho-chi-minh", label: "Hồ Chí Minh"},
                ]}
              />
              <BoxGroup1
                name="address"
                label="Địa chỉ"
                id="address"
                type="text"
              />
              <BoxGroup1
                name=""
                label="Mô hình công ty"
                id=""
                type="text"
              />
              <BoxGroup1
                name=""
                label="Quy mô công ty"
                id=""
                type="text"
              />
              <BoxGroup1
                name=""
                label="Thời gian làm việc"
                id=""
                type="text"
              />
              <BoxGroup1
                name=""
                label="Làm việc ngoài giờ"
                id=""
                type="text"
              />
              <BoxGroup1
                name="email"
                label="Email *"
                id="email"
                type="email"
              />
              <BoxGroup1
                name="phone"
                label="Số điện thoại"
                id="phone"
                type="text"
              />
              <BoxGroup3
                label="Mô tả chi tiết"
                name="description"
                id="description"
                className="sm:col-span-2"
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