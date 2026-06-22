USE master;
GO

IF DB_ID(N'AITaskers') IS NOT NULL
BEGIN
    ALTER DATABASE AITaskers SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE AITaskers;
END
GO

CREATE DATABASE AITaskers;
GO

USE AITaskers;
GO

CREATE TABLE dbo.project_users (
    id BIGINT IDENTITY(1,1) NOT NULL,
    name NVARCHAR(255) NOT NULL,
    email NVARCHAR(255) NOT NULL,
    subscribed BIT NOT NULL CONSTRAINT df_project_users_subscribed DEFAULT (1),
    CONSTRAINT pk_project_users PRIMARY KEY (id)
);
GO

CREATE TABLE dbo.project_notifications (
    id BIGINT IDENTITY(1,1) NOT NULL,
    user_id BIGINT NOT NULL,
    title NVARCHAR(255) NOT NULL,
    message NVARCHAR(4000) NOT NULL,
    notification_type NVARCHAR(50) NOT NULL CONSTRAINT df_project_notifications_type DEFAULT (N'SYSTEM_ANNOUNCEMENT'),
    reference_type NVARCHAR(255) NULL,
    reference_id BIGINT NULL,
    sent BIT NOT NULL CONSTRAINT df_project_notifications_sent DEFAULT (0),
    is_read BIT NOT NULL CONSTRAINT df_project_notifications_is_read DEFAULT (0),
    created_at DATETIME2 NULL CONSTRAINT df_project_notifications_created_at DEFAULT (SYSDATETIME()),
    read_at DATETIME2 NULL,
    CONSTRAINT pk_project_notifications PRIMARY KEY (id),
    CONSTRAINT fk_project_notifications_user FOREIGN KEY (user_id) REFERENCES dbo.project_users(id),
    CONSTRAINT ck_project_notifications_type CHECK (notification_type IN (
        N'PROPOSAL_RECEIVED',
        N'PROPOSAL_UPDATED',
        N'PROPOSAL_ACCEPTED',
        N'PROPOSAL_REJECTED',
        N'EXPERT_RECOMMENDATION_READY',
        N'NEW_JOB_RECOMMENDATION',
        N'CONTRACT_CREATED',
        N'JOB_STARTED',
        N'JOB_PROGRESS_UPDATED',
        N'JOB_DELIVERED',
        N'MILESTONE_SUBMITTED',
        N'DEADLINE_REMINDER',
        N'REVISION_REQUESTED',
        N'JOB_COMPLETED',
        N'JOB_CANCELLED',
        N'PAYMENT_REQUIRED',
        N'PAYMENT_REMINDER',
        N'PAYMENT_SUCCESS',
        N'PAYMENT_RELEASED',
        N'PAYOUT_AVAILABLE',
        N'RATING_REQUEST',
        N'RATING_RECEIVED',
        N'AUTOMATION_DEPLOYED',
        N'AUTOMATION_FAILED',
        N'NEW_DISPUTE_ALERT',
        N'SUSPICIOUS_TRANSACTION_ALERT',
        N'USER_REPORT_NOTIFICATION',
        N'PAYMENT_ISSUE_NOTIFICATION',
        N'SYSTEM_ACTIVITY_NOTIFICATION',
        N'SYSTEM_ANNOUNCEMENT'
    ))
);
GO

CREATE TABLE dbo.project_payments (
    id VARCHAR(100) NOT NULL,
    status NVARCHAR(20) NOT NULL CONSTRAINT df_project_payments_status DEFAULT (N'UNPAID'),
    updated_at DATETIME2 NOT NULL CONSTRAINT df_project_payments_updated_at DEFAULT (SYSDATETIME()),
    CONSTRAINT pk_project_payments PRIMARY KEY (id),
    CONSTRAINT ck_project_payments_status CHECK (status IN (N'UNPAID', N'PENDING', N'PAID'))
);
GO

CREATE TABLE dbo.project_reviews (
    id BIGINT IDENTITY(1,1) NOT NULL,
    payment_id VARCHAR(100) NOT NULL,
    job_id NVARCHAR(100) NULL,
    client_id BIGINT NOT NULL,
    expert_id BIGINT NOT NULL,
    score INT NOT NULL,
    comment NVARCHAR(2000) NULL,
    created_at DATETIME2 NOT NULL CONSTRAINT df_project_reviews_created_at DEFAULT (SYSDATETIME()),
    CONSTRAINT pk_project_reviews PRIMARY KEY (id),
    CONSTRAINT fk_project_reviews_payment FOREIGN KEY (payment_id) REFERENCES dbo.project_payments(id),
    CONSTRAINT fk_project_reviews_client FOREIGN KEY (client_id) REFERENCES dbo.project_users(id),
    CONSTRAINT fk_project_reviews_expert FOREIGN KEY (expert_id) REFERENCES dbo.project_users(id),
    CONSTRAINT uq_project_reviews_payment_client_expert UNIQUE (payment_id, client_id, expert_id),
    CONSTRAINT ck_project_reviews_score CHECK (score BETWEEN 1 AND 5),
    CONSTRAINT ck_project_reviews_different_users CHECK (client_id <> expert_id)
);
GO

INSERT INTO dbo.project_users (
    name,
    email,
    subscribed
)
VALUES
    (N'Client 1', N'client1@gmail.com', 1),
    (N'Expert 1', N'expert1@gmail.com', 1),
    (N'Admin 1', N'admin1@gmail.com', 1),
    (N'Inactive User', N'inactive@gmail.com', 0);
GO

INSERT INTO dbo.project_notifications (
    user_id,
    title,
    message,
    notification_type,
    reference_type,
    reference_id,
    sent,
    is_read,
    created_at,
    read_at
)
VALUES
    (1, N'Proposal moi', N'Ban vua nhan duoc proposal moi cho job AI Chatbot.', N'PROPOSAL_RECEIVED', N'JOB', 100, 1, 0, SYSDATETIME(), NULL),
    (1, N'Proposal da cap nhat', N'Expert vua cap nhat proposal cho job AI Chatbot.', N'PROPOSAL_UPDATED', N'JOB', 100, 1, 0, SYSDATETIME(), NULL),
    (1, N'Gemini da goi y expert', N'Gemini da goi y danh sach expert phu hop cho job cua ban.', N'EXPERT_RECOMMENDATION_READY', N'JOB', 100, 1, 0, SYSDATETIME(), NULL),
    (1, N'Job da bat dau', N'Expert da bat dau thuc hien job AI Chatbot.', N'JOB_STARTED', N'JOB', 100, 1, 0, SYSDATETIME(), NULL),
    (1, N'Cap nhat tien do', N'Expert da cap nhat tien do job AI Chatbot len 60%.', N'JOB_PROGRESS_UPDATED', N'JOB', 100, 1, 0, SYSDATETIME(), NULL),
    (1, N'Expert da giao san pham', N'Expert da giao ket qua automation de ban review.', N'JOB_DELIVERED', N'JOB', 100, 1, 0, SYSDATETIME(), NULL),
    (1, N'Expert da nop milestone', N'Expert da nop milestone dau tien de ban review.', N'MILESTONE_SUBMITTED', N'JOB', 100, 1, 0, SYSDATETIME(), NULL),
    (1, N'Job da hoan thanh', N'Job AI Chatbot da duoc danh dau hoan thanh.', N'JOB_COMPLETED', N'JOB', 100, 1, 1, SYSDATETIME(), SYSDATETIME()),
    (1, N'Job da bi huy', N'Job AI Chatbot da bi huy theo yeu cau cua client.', N'JOB_CANCELLED', N'JOB', 100, 1, 0, SYSDATETIME(), NULL),
    (1, N'Can thanh toan escrow', N'Vui long nap escrow de expert co the bat dau cong viec.', N'PAYMENT_REQUIRED', N'PAYMENT', 200, 1, 0, SYSDATETIME(), NULL),
    (1, N'Nhac thanh toan', N'Vui long thanh toan escrow de bat dau job.', N'PAYMENT_REMINDER', N'PAYMENT', 200, 1, 0, SYSDATETIME(), NULL),
    (1, N'Thanh toan thanh cong', N'Giao dich escrow cua ban da thanh cong.', N'PAYMENT_SUCCESS', N'PAYMENT', 200, 1, 1, SYSDATETIME(), SYSDATETIME()),
    (1, N'Can danh gia expert', N'Hay danh gia expert sau khi job hoan tat.', N'RATING_REQUEST', N'RATING', 300, 1, 0, SYSDATETIME(), NULL),
    (1, N'Automation da deploy', N'Workflow automation cua ban da duoc deploy thanh cong.', N'AUTOMATION_DEPLOYED', N'JOB', 100, 1, 0, SYSDATETIME(), NULL),
    (1, N'Automation bi loi', N'Workflow automation gap loi khi ket noi voi cong cu ben thu ba.', N'AUTOMATION_FAILED', N'JOB', 100, 1, 0, SYSDATETIME(), NULL),
    (2, N'Job moi phu hop', N'Ban co mot job automation moi phu hop voi ky nang.', N'NEW_JOB_RECOMMENDATION', N'JOB', 101, 1, 0, SYSDATETIME(), NULL),
    (2, N'Proposal duoc chap nhan', N'Proposal cua ban da duoc client chap nhan.', N'PROPOSAL_ACCEPTED', N'JOB', 101, 1, 0, SYSDATETIME(), NULL),
    (2, N'Proposal khong duoc chon', N'Proposal cua ban khong duoc client lua chon cho job nay.', N'PROPOSAL_REJECTED', N'JOB', 101, 1, 0, SYSDATETIME(), NULL),
    (2, N'Hop dong da tao', N'Contract va milestone cho job moi da duoc tao.', N'CONTRACT_CREATED', N'JOB', 101, 1, 0, SYSDATETIME(), NULL),
    (2, N'Sap den deadline', N'Milestone cua job sap den han nop.', N'DEADLINE_REMINDER', N'JOB', 101, 1, 0, SYSDATETIME(), NULL),
    (2, N'Client yeu cau chinh sua', N'Client da gui yeu cau revision cho milestone vua nop.', N'REVISION_REQUESTED', N'JOB', 101, 1, 0, SYSDATETIME(), NULL),
    (2, N'Tien da duoc release', N'Client da release payment cho job cua ban.', N'PAYMENT_RELEASED', N'PAYMENT', 201, 1, 1, SYSDATETIME(), SYSDATETIME()),
    (2, N'Tien co the rut', N'Khoan thanh toan cua ban da san sang de rut.', N'PAYOUT_AVAILABLE', N'PAYMENT', 201, 1, 0, SYSDATETIME(), NULL),
    (2, N'Danh gia moi', N'Ban vua nhan duoc rating moi tu client.', N'RATING_RECEIVED', N'RATING', 301, 1, 0, SYSDATETIME(), NULL),
    (3, N'Khieu nai moi', N'Co dispute moi duoc tao cho contract C001.', N'NEW_DISPUTE_ALERT', N'DISPUTE', 400, 1, 0, SYSDATETIME(), NULL),
    (3, N'Giao dich bat thuong', N'He thong phat hien giao dich bat thuong trong escrow.', N'SUSPICIOUS_TRANSACTION_ALERT', N'PAYMENT', 202, 1, 0, SYSDATETIME(), NULL),
    (3, N'User bi report', N'Mot user vua bi report ve hanh vi khong phu hop.', N'USER_REPORT_NOTIFICATION', N'USER', 2, 1, 0, SYSDATETIME(), NULL),
    (3, N'Loi thanh toan', N'Co loi xay ra khi xu ly escrow payment.', N'PAYMENT_ISSUE_NOTIFICATION', N'PAYMENT', 203, 1, 0, SYSDATETIME(), NULL),
    (3, N'Hoat dong he thong', N'He thong vua ghi nhan mot su kien quan trong.', N'SYSTEM_ACTIVITY_NOTIFICATION', N'SYSTEM', 1, 1, 0, SYSDATETIME(), NULL),
    (3, N'Thong bao he thong', N'AITasker se bao tri he thong vao toi nay.', N'SYSTEM_ANNOUNCEMENT', N'SYSTEM', 1, 1, 0, SYSDATETIME(), NULL);
GO

INSERT INTO dbo.project_payments (id, status)
VALUES
    (N'pay_review_001', N'PAID'),
    (N'pay_pending_001', N'PENDING'),
    (N'pay_unpaid_001', N'UNPAID');
GO

SELECT * FROM dbo.project_users;
SELECT * FROM dbo.project_notifications;
SELECT * FROM dbo.project_payments;
SELECT * FROM dbo.project_reviews;
GO
