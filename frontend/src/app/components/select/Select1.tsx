export const Select1 = () => {
  return (
    <>
      <div
        className="mt-[30px]"
      >
        <select 
          name="page" 
          id=""
          className="rounded-[8px] border border-[#DEDEDE] h-[44px] px-[18px] font-[400] text-[16px] text-[#414042]"
        >
          <option defaultValue="1">Trang 1</option>
          <option defaultValue="2">Trang 2</option>
          <option defaultValue="3">Trang 3</option>
        </select>
      </div>
    </>
  )
}