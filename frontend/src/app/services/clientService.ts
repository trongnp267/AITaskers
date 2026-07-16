import api from "../lib/axios";

export interface ClientSummary {
    clientId: number;
    userId: number;
    username: string;
    companyName: string | null;
    description: string | null;
    createdAt: string | null;
}

export const getClients = async (): Promise<ClientSummary[]> => {
    const response = await api.get<ClientSummary[]>("/clients");
    return response.data;
};

export const getClient = async (id: number): Promise<ClientSummary> => {
    const response = await api.get<ClientSummary>(`/clients/${id}`);
    return response.data;
};

export const updateClient = async (
    id: number,
    body: { companyName?: string; description?: string }
): Promise<ClientSummary> => {
    const response = await api.put<ClientSummary>(`/clients/${id}`, body);
    return response.data;
};
