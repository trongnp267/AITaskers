import api from "../lib/axios";
import { ExpertProfile } from "../types/domain";

export interface AnalyzeResult {
    aiEnabled?: boolean;
    summary?: string;
    suggestedSkills?: string[];
    estimatedComplexity?: string;
    recommendedBudgetMin?: number;
    recommendedBudgetMax?: number;
    [key: string]: unknown;
}

export interface ExpertMatch {
    id: number;
    matchScore: number;
    reasoning: string;
}

export const analyzeJob = async (description: string): Promise<AnalyzeResult> => {
    const response = await api.post<AnalyzeResult>("/ai/analyze", { description });
    return response.data;
};

export const matchExpertsForJob = async (
    jobId: number
): Promise<ExpertMatch[]> => {
    const response = await api.get<ExpertMatch[]>(`/ai/match/job/${jobId}`);
    return response.data;
};

export type { ExpertProfile };
