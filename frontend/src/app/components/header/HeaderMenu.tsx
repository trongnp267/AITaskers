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
      link: "/search",
      children: [
        {
          name: "AI Engineer",
          link: "/search?q=AI",
          children: []
        },
        {
          name: "Data Scientist",
          link: "/search?q=Data",
          children: []
        },
        {
          name: "NLP / Chatbot",
          link: "/search?q=NLP",
          children: []
        },
      ]
    },
    {
      name: "Tìm việc",
      link: "/search/job",
      children: [
        {
          name: "Dự Án AI",
          link: "/search/job?q=AI",
          children: []
        },
        {
          name: "Dự Án Website",
          link: "/search/job?q=Website",
          children: []
        },
        {
          name: "Dự Án Mobile App",
          link: "/search/job?q=Mobile",
          children: []
        },
      ]
    },
    {
      name: "Doanh nghiệp",
      link: "/company/list",
      children: []
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