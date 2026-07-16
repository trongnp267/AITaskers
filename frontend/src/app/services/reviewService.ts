import api from "../lib/axios";
import { ApiResult, Review, ReviewRequest } from "../types/domain";

export const createReview = async (
    request: ReviewRequest
): Promise<ApiResult<Review>> => {
    const response = await api.post<ApiResult<Review>>("/reviews", request);
    return response.data;
};

export const getReviewsForExpert = async (
    expertId: number
): Promise<Review[]> => {
    const response = await api.get<Review[]>(`/reviews/expert/${expertId}`);
    return response.data;
};
