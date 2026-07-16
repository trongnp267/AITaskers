export interface CurrentUser {
    id: number;
    username: string;
    role: string;
    profileId: number | null;
}

export const getCurrentUser = (): CurrentUser | null => {
    if (typeof window === "undefined") return null;
    try {
        const raw = localStorage.getItem("user");
        return raw ? (JSON.parse(raw) as CurrentUser) : null;
    } catch {
        return null;
    }
};

export const getToken = (): string | null => {
    if (typeof window === "undefined") return null;
    return localStorage.getItem("token");
};

export const saveSession = (token: string, user: CurrentUser) => {
    if (typeof window === "undefined") return;
    localStorage.setItem("token", token);
    localStorage.setItem("user", JSON.stringify(user));
};

export const logout = () => {
    if (typeof window === "undefined") return;
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    window.location.href = "/login";
};
