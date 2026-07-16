import { redirect } from "next/navigation";

export default async function Page({ params }: { params: Promise<{ id: string }> }) {
  await params;
  redirect("/company-manager/cv/list");
}
