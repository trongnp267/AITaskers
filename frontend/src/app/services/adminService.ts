import { PendingAccount } from "../types/account";
import api from "../lib/axios";

export const getPendingAccounts = async (): Promise<PendingAccount[]> => {
    const response = await api.get<PendingAccount[]>(
        "/admin/accounts/pending"
    );

    return response.data;
};

export const approveAccount = async (id: number): Promise<void> => {
    await api.put(
        `/admin/accounts/${id}/approve`
    );
};

export const rejectAccount = async (id: number): Promise<void> => {
    await api.put(
        `/admin/accounts/${id}/reject`,
        {
            reason: "Rejected by administrator"
        }
    );
};