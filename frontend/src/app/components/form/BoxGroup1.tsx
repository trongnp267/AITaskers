export const BoxGroup1 = (props: {
  label?: string,
  name: string,
  id?: string,
  type?: string
}) => {
  const {label, name, id = name, type} = props;
  return (
    <>
      <div>
        {label && <label htmlFor={id} className="font-[500] text-[14px] text-black">{label}</label>}
        <input type={type} name={name} id={id} className="bg-white border border-[#DEDEDE] rounded-[4px] py-[14px] px-[20px] w-full font-[500] text-[14px] text-black mt-[5px]" />
      </div>
    </>
  )
}