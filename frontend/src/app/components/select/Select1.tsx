export const Select1 = (props: {
  totalPage?: number
}) => {
  const {totalPage = 1} = props;
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
          {
            Array(totalPage).fill("").map((_, index) => (
              <option key={index} defaultValue={index + 1}>Trang {index + 1}</option>
            ))
          }
        </select>
      </div>
    </>
  )
}