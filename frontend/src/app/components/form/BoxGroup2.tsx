export const BoxGroup2 = (props: {
  label?: string,
  name: string,
  id?: string,
  options: {
    value: string,
    label: string
  }[],
  value: string,
  onChange: (e: React.ChangeEvent<HTMLSelectElement>) => void
}) => {
  const {label, name, id = name, options, value, onChange} = props;
  return (
    <>
      <div>
        {label && <label htmlFor={id} className="font-[500] text-[14px] text-black">{label}</label>}
        <select name={name} id={id} className="bg-white border border-[#DEDEDE] rounded-[4px] py-[14px] px-[20px] w-full font-[500] text-[14px] text-black mt-[5px]" value={value} onChange={onChange}>
          {options.map((option, index) => (
            <option key={index} value={option.value}>{option.label}</option>
          ))}
        </select>
      </div>
    </>
  )
}