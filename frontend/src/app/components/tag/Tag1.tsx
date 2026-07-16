export const Tag1 = (props: {
  label: string
}) => {
  const {label} = props;
  return (
    <div
      className="rounded-[20px] border border-[#DEDEDE] px-[16px] py-[6px] font-[400] text-[12px] text-[#414042]"
    >
      {label}
    </div>
  )
}