"use client"

import Link from "next/link"
import { FaBars } from "react-icons/fa6"
import { HeaderMenu } from "./HeaderMenu"
import { useState } from "react"
import { HeaderAccount } from "./HeaderAccount"

export const Header = () => {
  const [showMenu, setShowMenu] = useState(false);

  return (
    <>
      {showMenu && (
        <div 
          onClick={() => setShowMenu(false)}
          className="bg-[black] fixed top-0 left-0 w-[100%] h-[100%] z-[9] cursor-pointer opacity-[.4]"
        >
        </div>
      )}
      <header className="py-[15px] bg-[#000071]">
        <div className="contain">
          <div className="flex lg:justify-between items-center">
            <Link href="/" className="text-[#FFFFFF] sm:text-[28px] text-[20px] font-[800] lg:flex-none flex-1">AITasker</Link>
            <HeaderMenu
              showMenu={showMenu}
            />
            <HeaderAccount />
            <button 
              className="text-[23px] text-[#FFFFFF] lg:hidden ml-[12px] cursor-pointer"
              onClick={() => setShowMenu(true)}
            >
              <FaBars />
            </button>
          </div>
        </div>
      </header>
    </>
  )
}