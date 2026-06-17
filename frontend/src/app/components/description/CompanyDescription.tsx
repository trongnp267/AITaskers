export const CompanyDescription = (props: {
  label: string,
  content: string
}) => {
  const {label, content} = props;
  return (
    <>
      <div className="flex justify-between font-[400] text-[16px]">
        <div className="text-[#A6A6A6]">{label}</div>
        <div className="text-[#121212]">{content}</div>
      </div>
    </>
  )
}