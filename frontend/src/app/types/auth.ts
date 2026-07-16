export interface LoginRequest {
    username: string;
    password: string;
}

export interface LoginResponse {
    status?: string;
    message?: string;
    token: string;
    user: {
        id: number;
        username: string;
        role: string;
        profileId: number | null;
    };
}
