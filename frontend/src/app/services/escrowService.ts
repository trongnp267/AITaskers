import api from "../lib/axios";
import { ApiResult, Escrow } from "../types/domain";

export const getEscrowByJob = async (jobId: number): Promise<Escrow | null> => {
    try {
        const response = await api.get<Escrow>(`/escrow/job/${jobId}`);
        return response.data;
    } catch {
        return null;
    }
};

export const releaseEscrow = async (id: number): Promise<ApiResult<Escrow>> => {
    const response = await api.post<ApiResult<Escrow>>(`/escrow/${id}/release`);
    return response.data;
};

export const refundEscrow = async (id: number): Promise<ApiResult<Escrow>> => {
    const response = await api.post<ApiResult<Escrow>>(`/escrow/${id}/refund`);
    return response.data;
};
