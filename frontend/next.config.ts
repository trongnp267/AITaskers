import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  reactStrictMode: false,
  // Proxy API: trinh duyet goi /api/... tren cung cong 3000, Next chuyen
  // tiep den backend 8080. Nho vay web chi can MOT cong duy nhat -> vao
  // duoc tu localhost, IP LAN, hay duong ham (tunnel) ma khong can cau hinh.
  async rewrites() {
    const backend = process.env.BACKEND_URL || "http://localhost:8080";
    return [
      {
        source: "/api/:path*",
        destination: `${backend}/api/:path*`,
      },
    ];
  },
};

export default nextConfig;
