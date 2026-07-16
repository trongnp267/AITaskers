import api from "../lib/axios";
import { LoginRequest, LoginResponse } from "../types/auth";

export const login = async (
    request: LoginRequest
): Promise<LoginResponse> => {

    const response = await api.post<LoginResponse>(
        "/login",
        request
    );

    return response.data;
};