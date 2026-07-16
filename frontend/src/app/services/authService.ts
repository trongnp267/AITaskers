import api from "../lib/axios";
import { LoginRequest, LoginResponse } from "../types/auth";

export const login = async (
    request: LoginRequest
): Promise<LoginResponse> => {
    const response = await api.post<LoginResponse>("/login", request);
    return response.data;
};

export interface RegisterRequest {
    username: string;
    password: string;
    confirmPassword: string;
    role: string;
    companyName?: string;
    description?: string;
}

export const register = async (
    request: RegisterRequest
): Promise<{ status: string; message: string }> => {
    const response = await api.post("/register", request);
    return response.data;
};
