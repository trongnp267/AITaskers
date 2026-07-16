import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  reactStrictMode: false,
  // Xuat ra trang tinh (out/) de nhet vao Spring Boot phuc vu chung mot cong
  // voi backend. Khong dung rewrites nua: axios goi "/api" tuong doi -> cung
  // origin voi noi phuc vu web (backend) nen khong can proxy, khong dinh CORS.
  output: "export",
  trailingSlash: true,
  images: { unoptimized: true },
};

export default nextConfig;
