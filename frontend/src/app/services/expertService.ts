import api from "../lib/axios";

export interface ExpertSummary {
    expertId: number;
    userId: number;
    username: string;
    description: string | null;
    experienceYears: number;
    rating: number;
    completedJobs: number;
}

export const getExperts = async (): Promise<ExpertSummary[]> => {
    const response = await api.get<ExpertSummary[]>("/experts");
    return response.data;
};

export const getExpert = async (id: number): Promise<ExpertSummary> => {
    const response = await api.get<ExpertSummary>(`/experts/${id}`);
    return response.data;
};

export const updateExpert = async (
    id: number,
    body: { description?: string; experienceYears?: number }
): Promise<ExpertSummary> => {
    const response = await api.put<ExpertSummary>(`/experts/${id}`, body);
    return response.data;
};
