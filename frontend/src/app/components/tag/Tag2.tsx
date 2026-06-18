import { ReactNode } from "react"

export const Tag2 = (props: {
  icon: ReactNode,
  label: string
  className?: string
}) => {
  const {icon, label, className = ""} = props;
  return (
    <>
      <div className={"flex gap-[8px] items-center font-[400] text-[14px] text-[#121212] " + className}>
        {icon} {label}
      </div>
    </>
  )
}