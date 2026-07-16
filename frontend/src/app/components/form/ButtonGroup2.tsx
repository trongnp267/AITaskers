export const ButtonGroup2 = (props: {
  label: string,
  className?: string
}) => {
  const {label, className = ""} = props;
  return (
    <>
      <div className={className}>
        <button className="h-[48px] w-full bg-[#0088FF] rounded-[4px] px-[10px] font-[700] text-[16px] text-white">
          {label}
        </button>
      </div>
    </>
  )
}