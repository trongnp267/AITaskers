"use client";

import { PendingAccount } from "@/app/types/account";


interface Props {

    accounts: PendingAccount[];

    onApprove: (id: number) => void;

    onReject: (id: number) => void;

}

export default function PendingAccountTable({

    accounts,

    onApprove,

    onReject,

}: Props) {

    return (

        <div className="overflow-hidden rounded-xl bg-white shadow">

            <table className="w-full">

                <thead className="bg-gray-100">

                    <tr>

                        <th className="p-4">Username</th>

                        <th>Role</th>

                        <th>Status</th>

                        <th>Date</th>

                        <th>Action</th>

                    </tr>

                </thead>

                <tbody>

                    {

                        accounts.map((account) => (

                            <tr
                                key={account.id}
                                className="border-t"
                            >

                                <td className="p-4 text-center">

                                    {account.username}

                                </td>

                                <td className="text-center">

                                    {account.role}

                                </td>

                                <td className="text-center">

                                    <span className="rounded bg-yellow-100 px-3 py-1 text-sm text-yellow-700">

                                        {account.status}

                                    </span>

                                </td>

                                <td className="text-center">

                                    {account.createdAt}

                                </td>

                                <td>

                                    <div className="flex gap-2 justify-center">

                                        <button
                                            onClick={() => onApprove(account.id)}
                                            className="rounded bg-green-500 px-4 py-2 text-white"
                                        >
                                            Approve
                                        </button>

                                        <button
                                            onClick={() => onReject(account.id)}
                                            className="rounded bg-red-500 px-4 py-2 text-white"
                                        >
                                            Reject
                                        </button>

                                    </div>

                                </td>

                            </tr>

                        ))

                    }

                </tbody>

            </table>

        </div>

    );

}