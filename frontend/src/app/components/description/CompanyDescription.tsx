export const CompanyDescription = (props: {
  label: string,
  content: string,
  className?: string,
  gap?: string
}) => {
  const {label, content, className = "", gap = ""} = props;
  return (
    <>
      <div className={"flex justify-between font-[400] text-[16px] " + className + " " + gap} >
        <div className="text-[#A6A6A6]">{label}</div>
        <div className="text-[#121212]">{content}</div>
      </div>
    </>
  )
}