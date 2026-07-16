export interface Skill {
    skillId: number;
    skillName: string;
    description?: string | null;
}

export interface JobSkill {
    jobSkillId: number;
    skill: Skill;
    skillName: string;
    skillId: number;
}

export interface Job {
    jobId: number;
    title: string;
    description: string;
    positionRequirement: string;
    minExperienceYears: number;
    jobSkills: JobSkill[];
    budgetMin: number;
    budgetMax: number;
    jobStatus: string;
    createdAt: string;
    updatedAt: string;
    deadline: string;
    clientId: number;
    clientCompanyName: string;
}

export interface JobRequest {
    clientId: number;
    title: string;
    description: string;
    positionRequirement: string;
    minExperienceYears: number;
    budgetMin: number;
    budgetMax: number;
    deadline: string;
    jobSkills: string[];
}

export interface ExpertProfile {
    expertId: number;
    user?: { id: number; username: string; role: string };
    description: string;
    experienceYears: number;
    rating: number;
    completedJobs: number;
}

export interface Proposal {
    proposalId: number;
    expert: ExpertProfile;
    job: Job;
    coverLetter: string;
    proposalPrice: number;
    estimatedDays: number;
    proposalStatus: string;
    submittedAt: string;
}

export interface ProposalRequest {
    expertId: number;
    jobId: number;
    coverLetter: string;
    proposalPrice: number;
    estimatedDays: number;
}

export interface Milestone {
    id: number;
    projectId: number;
    title: string;
    description: string | null;
    amount: number;
    status: string;
    dueDate: string | null;
}

export interface MilestoneRequest {
    projectId: number;
    title: string;
    description?: string;
    amount: number;
    dueDate?: string;
}

export interface Escrow {
    escrowId: number;
    amount: number;
    escrowStatus: string;
    createdAt: string;
    updatedAt: string;
}

export interface NotificationItem {
    notificationId: number;
    recipientUserId: number;
    message: string;
    type: string;
    read: boolean;
    createdAt: string;
}

export interface Review {
    reviewId: number;
    jobId: number;
    reviewerUserId: number;
    expertId: number;
    rating: number;
    comment: string;
    createdAt: string;
}

export interface ReviewRequest {
    jobId: number;
    reviewerUserId: number;
    expertId: number;
    rating: number;
    comment: string;
}

export interface ApiResult<T> {
    status: string;
    message: string;
    data: T;
}
