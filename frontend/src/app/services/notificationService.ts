import api from "../lib/axios";
import { NotificationItem } from "../types/domain";

export const getNotifications = async (
    userId: number
): Promise<NotificationItem[]> => {
    const response = await api.get<NotificationItem[]>(`/notifications/user/${userId}`);
    return response.data;
};

export const getUnread = async (
    userId: number
): Promise<NotificationItem[]> => {
    const response = await api.get<NotificationItem[]>(
        `/notifications/user/${userId}/unread`
    );
    return response.data;
};

export const markAsRead = async (id: number): Promise<NotificationItem> => {
    const response = await api.put<NotificationItem>(`/notifications/${id}/read`);
    return response.data;
};
