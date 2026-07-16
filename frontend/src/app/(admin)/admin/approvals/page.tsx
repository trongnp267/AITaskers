"use client";

import PendingAccountTable from "@/app/components/table/PendingAccountTable";
import { approveAccount, getPendingAccounts, rejectAccount } from "@/app/services/adminService";
import { PendingAccount } from "@/app/types/account";
import { useEffect, useState } from "react";
import toast from "react-hot-toast";


export default function PendingAccountsPage() {

    const [accounts, setAccounts] = useState<PendingAccount[]>([]);

    const [loading, setLoading] = useState<boolean>(true);

    const fetchPendingAccounts = async () => {

        try {

            const data = await getPendingAccounts();

            setAccounts(data);

        }

        catch (error) {

            console.error(error);

        }

        finally {

            setLoading(false);

        }

    };

    useEffect(() => {

        fetchPendingAccounts();

    }, []);

    const handleApprove = async (id: number) => {
      try {
          await approveAccount(id);

          toast.success("Account approved successfully!");

          await fetchPendingAccounts();
      } catch (error) {
          console.error(error);

          toast.error("Failed to approve account.");
      }
  };

  const handleReject = async (id: number) => {
      try {
          await rejectAccount(id);

          toast.success("Account rejected successfully!");

          await fetchPendingAccounts();
      } catch (error) {
          console.error(error);

          toast.error("Failed to reject account.");
      }
  };

    if (loading) {

        return <h2>Loading...</h2>;

    }

    return (

        <div>

            <h1 className="mb-6 text-3xl font-bold">

                Pending Accounts

            </h1>

            <PendingAccountTable

                accounts={accounts}

                onApprove={handleApprove}

                onReject={handleReject}

            />

        </div>

    );

}