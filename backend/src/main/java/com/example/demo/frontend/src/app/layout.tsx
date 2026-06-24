import type { Metadata } from "next";
import "./globals.css";
import { Header } from "./components/header/Header";

export const metadata: Metadata = {
  title: "AITasker",
  description: "AITasker website...",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html
      lang="vi"
    >
      <body>
        <Header />
        {children}
      </body>
    </html>
  );
}
