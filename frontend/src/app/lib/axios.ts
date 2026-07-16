import axios from "axios";

const api = axios.create({
    baseURL:
        process.env.NEXT_PUBLIC_API_URL ||
        process.env.NEXT_PUBLIC_ADMIN ||
        "http://localhost:8080/api",
    headers: {
        "Content-Type": "application/json",
    },
});

api.interceptors.request.use((config) => {
    if (typeof window !== "undefined") {
        const token = localStorage.getItem("token");
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
    }
    return config;
});

api.interceptors.response.use(
    (response) => response,
    (error) => {
        if (typeof window !== "undefined") {
            const status = error?.response?.status;
            const hasToken = !!localStorage.getItem("token");
            const url: string = error?.config?.url || "";
            if ((status === 401 || status === 403) && hasToken && !url.includes("/admin/")) {
                localStorage.removeItem("token");
                localStorage.removeItem("user");
                window.location.href = "/login?expired=1";
            }
        }
        return Promise.reject(error);
    }
);

export default api;
