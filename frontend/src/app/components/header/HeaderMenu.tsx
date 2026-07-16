import Link from "next/link"
import { FaAngleDown, FaAngleRight} from "react-icons/fa6"

export const HeaderMenu = (props: any) => {
  const {showMenu} = props;
  console.log(showMenu);
  const menuList = [
    {
      name: "Công việc",
      link: "/jobs",
      children: []
    },
    {
      name: "Ví",
      link: "/wallet",
      children: []
    },
    {
      name: "Thông báo",
      link: "/notifications",
      children: []
    },
    {
      name: "Tìm chuyên gia",
      link: "#",
      children: [
        {
          name: "Theo Kỹ Năng",
          link: "#",
          children: [
            {
              name: "AI Engineer",
              link: "#",
              children: []
            },
            {
              name: "Frontend Developer",
              link: "#",
              children: []
            },
            {
              name: "Backend Developer",
              link: "#",
              children: []
            },
            {
              name: "Data Scientist",
              link: "#",
              children: []
            },
            {
              name: "DevOps Engineer",
              link: "#",
              children: []
            },
          ]
        },
        {
          name: "Theo Danh Mục",
          link: "#",
          children: []
        },
        {
          name: "Theo Mức Giá",
          link: "#",
          children: []
        },
      ]
    },
    {
      name: "Dự Án",
      link: "#",
      children: [
        {
          name: "Dự Án AI",
          link: "#",
          children: []
        },
        {
          name: "Dự Án Website",
          link: "#",
          children: []
        },
        {
          name: "Dự Án Mobile App",
          link: "#",
          children: []
        },
        {
          name: "Dự Án Thiết Kế",
          link: "#",
          children: []
        },
        {
          name: "Dự Án Marketing",
          link: "#",
          children: []
        },
      ]
    },
    {
      name: "Dịch Vụ",
      link: "#",
      children: [
        {
          name: "AI Services",
          link: "#",
          children: []
        },
        {
          name: "Design",
          link: "#",
          children: []
        },
        {
          name: "Marketing",
          link: "#",
          children: []
        },
        {
          name: "Development",
          link: "#",
          children: []
        },
      ]
    },
    {
      name: "Doanh nghiệp",
      link: "#",
      children: [
        {
          name: "Doanh Nghiệp Nổi Bật",
          link: "#",
          children: []
        },
        {
          name: "Đối Tác Chiến Lược",
          link: "#",
          children: []
        },
        {
          name: "Startup Tuyển Dụng",
          link: "#",
          children: []
        },
        {
          name: "Công Ty Công Nghệ",
          link: "#",
          children: []
        },
      ]
    },
  ]

  return (
    <>
      <nav className={"menu " + (showMenu ? "show" : "")}>
        <ul>
          {
            menuList.map((item: any, index: number) => (
              <li key={index}>
                <Link href={item.link}>{item.name}</Link>
                {item.children.length > 0 && (
                  <>
                    <FaAngleDown />
                    <ul>
                      {item.children.map((itemChild1: any, indexChild1: number) => (
                        <li key={indexChild1}>
                          <Link href={itemChild1.link}>{itemChild1.name}</Link>
                          {itemChild1.children.length > 0 && (
                            <>
                              <FaAngleRight />
                              <ul>
                                {itemChild1.children.map((itemChild2: any, indexChild2: number) => (
                                  <li key={indexChild2}>
                                    <Link href={itemChild2.link}>{itemChild2.name}</Link>
                                  </li>
                                ))}
                              </ul>
                            </>
                          )}
                        </li>
                      ))}
                    </ul>
                  </>
                )}
              </li>
            ))
          }
        </ul>
      </nav>
    </>
  )
}