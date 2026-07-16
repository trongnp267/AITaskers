import Link from "next/link"

export const ButtonTag = (props : {
  link: string,
  label: string
}) => {
  const {link, label} = props;
  return (
    <>
      <Link
          href={link} 
          className="rounded-[20px] border border-solid border-[#414042] py-[8px] px-[22px] bg-[#121212] hover:bg-[#414042] text-[#DEDEDE] font-[500] text-[16px] hover:text-white"
        >
          {label}
        </Link>
    </>
  )
}