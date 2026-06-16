import { ReactNode } from "react"

export const Tag2 = (props: {
  icon: ReactNode,
  label: string
}) => {
  const {icon, label} = props;
  return (
    <>
      <div className="flex gap-[8px] items-center font-[400] text-[14px] text-[#121212]">
        {icon} {label}
      </div>
    </>
  )
}