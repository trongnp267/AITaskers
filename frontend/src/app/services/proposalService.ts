import api from "../lib/axios";
import { ApiResult, Proposal, ProposalRequest } from "../types/domain";

export const getProposalsByJob = async (jobId: number): Promise<Proposal[]> => {
    const response = await api.get<Proposal[]>(`/proposals/job/${jobId}`);
    return response.data;
};

export const getProposalsByExpert = async (
    expertId: number
): Promise<Proposal[]> => {
    const response = await api.get<Proposal[]>(`/proposals/expert/${expertId}`);
    return response.data;
};

export const getProposal = async (id: number): Promise<Proposal> => {
    const response = await api.get<Proposal>(`/proposals/${id}`);
    return response.data;
};

export const createProposal = async (
    request: ProposalRequest
): Promise<ApiResult<Proposal>> => {
    const response = await api.post<ApiResult<Proposal>>("/proposals", request);
    return response.data;
};

export const acceptProposal = async (
    id: number
): Promise<ApiResult<Proposal>> => {
    const response = await api.post<ApiResult<Proposal>>(`/proposals/${id}/accept`);
    return response.data;
};
