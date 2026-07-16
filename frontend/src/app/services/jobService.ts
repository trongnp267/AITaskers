import api from "../lib/axios";
import { Job, JobRequest } from "../types/domain";

export const getJobs = async (): Promise<Job[]> => {
    const response = await api.get<Job[]>("/jobs");
    return response.data;
};

export const getJob = async (id: number): Promise<Job> => {
    const response = await api.get<Job>(`/jobs/${id}`);
    return response.data;
};

export const createJob = async (request: JobRequest): Promise<Job> => {
    const response = await api.post<Job>("/jobs", request);
    return response.data;
};
