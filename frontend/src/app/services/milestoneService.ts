import api from "../lib/axios";
import { ApiResult, Milestone, MilestoneRequest } from "../types/domain";

export const getMilestonesByProject = async (
    projectId: number
): Promise<Milestone[]> => {
    const response = await api.get<Milestone[]>(`/milestones/project/${projectId}`);
    return response.data;
};

export const createMilestone = async (
    request: MilestoneRequest
): Promise<ApiResult<Milestone>> => {
    const response = await api.post<ApiResult<Milestone>>("/milestones", request);
    return response.data;
};

export const updateMilestone = async (
    id: number,
    request: Partial<MilestoneRequest>
): Promise<ApiResult<Milestone>> => {
    const response = await api.put<ApiResult<Milestone>>(`/milestones/${id}`, request);
    return response.data;
};

export const submitMilestone = async (id: number): Promise<Milestone> => {
    const response = await api.post<Milestone>(`/milestones/${id}/submit`);
    return response.data;
};

export const approveMilestone = async (id: number): Promise<Milestone> => {
    const response = await api.post<Milestone>(`/milestones/${id}/approve`);
    return response.data;
};

export const rejectMilestone = async (id: number): Promise<Milestone> => {
    const response = await api.post<Milestone>(`/milestones/${id}/reject`);
    return response.data;
};
