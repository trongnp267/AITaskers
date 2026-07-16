import api from "../lib/axios";

export interface WalletResponse {
    status: string;
    balance: number;
}

export const getBalance = async (userId: number): Promise<number> => {
    const response = await api.get<WalletResponse>(`/wallet/${userId}`);
    return response.data.balance;
};

export const deposit = async (
    userId: number,
    amount: number
): Promise<WalletResponse> => {
    const response = await api.post<WalletResponse>(
        `/wallet/deposit?userId=${userId}&amount=${amount}`
    );
    return response.data;
};
