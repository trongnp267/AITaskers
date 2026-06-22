USE master;
GO
IF DB_ID(N'AITaskers') IS NOT NULL BEGIN ALTER DATABASE AITaskers SET SINGLE_USER WITH ROLLBACK IMMEDIATE; DROP DATABASE AITaskers; END
GO
CREATE DATABASE AITaskers;
GO
USE AITaskers;
GO

CREATE TABLE accounts (
 account_id UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY, email VARCHAR(255) NOT NULL,
 password_hash VARCHAR(255) NULL, full_name NVARCHAR(100) NOT NULL, auth_provider VARCHAR(20) NOT NULL,
 google_id VARCHAR(255) NULL, account_status VARCHAR(20) NOT NULL, user_role VARCHAR(20) NOT NULL,
 subscribed BIT NOT NULL DEFAULT 1, created_at DATETIME2 NOT NULL DEFAULT SYSDATETIME(), updated_at DATETIME2 NOT NULL DEFAULT SYSDATETIME()
);
CREATE UNIQUE INDEX uq_accounts_email ON accounts(email);
CREATE UNIQUE INDEX uq_accounts_google_id_not_null ON accounts(google_id) WHERE google_id IS NOT NULL;
CREATE TABLE client_profiles (
 client_profile_id UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY, account_id UNIQUEIDENTIFIER NOT NULL UNIQUE REFERENCES accounts(account_id),
 company_name NVARCHAR(255) NOT NULL, industry NVARCHAR(100) NOT NULL, company_size NVARCHAR(100), ai_needs NVARCHAR(MAX), main_problem NVARCHAR(MAX),
 profile_status VARCHAR(20) NOT NULL, created_at DATETIME2 NOT NULL DEFAULT SYSDATETIME(), updated_at DATETIME2 NOT NULL DEFAULT SYSDATETIME()
);
CREATE TABLE expert_profiles (
 expert_profile_id UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY, account_id UNIQUEIDENTIFIER NOT NULL UNIQUE REFERENCES accounts(account_id),
 bio NVARCHAR(MAX), ai_skills NVARCHAR(MAX) NOT NULL, skill_level VARCHAR(20) NOT NULL, portfolio_link NVARCHAR(2048), experience_years INT NOT NULL,
 hourly_rate DECIMAL(10,2), average_rating DECIMAL(3,2) NOT NULL DEFAULT 0, total_reviews INT NOT NULL DEFAULT 0,
 reputation_score DECIMAL(5,2) NOT NULL DEFAULT 0, availability_status VARCHAR(20) NOT NULL, profile_status VARCHAR(20) NOT NULL,
 created_at DATETIME2 NOT NULL DEFAULT SYSDATETIME(), updated_at DATETIME2 NOT NULL DEFAULT SYSDATETIME()
);
CREATE TABLE job_postings (
 job_id UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY, client_profile_id UNIQUEIDENTIFIER NOT NULL REFERENCES client_profiles(client_profile_id),
 job_title NVARCHAR(255) NOT NULL, description NVARCHAR(MAX) NOT NULL, required_skills NVARCHAR(MAX) NOT NULL,
 budget_min DECIMAL(12,2), budget_max DECIMAL(12,2), project_type NVARCHAR(100), job_status VARCHAR(20) NOT NULL,
 created_at DATETIME2 NOT NULL DEFAULT SYSDATETIME(), updated_at DATETIME2 NOT NULL DEFAULT SYSDATETIME()
);
CREATE TABLE projects (
 project_id UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY, job_id UNIQUEIDENTIFIER NOT NULL UNIQUE REFERENCES job_postings(job_id),
 client_profile_id UNIQUEIDENTIFIER NOT NULL REFERENCES client_profiles(client_profile_id), expert_profile_id UNIQUEIDENTIFIER NOT NULL REFERENCES expert_profiles(expert_profile_id),
 project_title NVARCHAR(255) NOT NULL, agreed_budget DECIMAL(12,2) NOT NULL, project_status VARCHAR(20) NOT NULL,
 start_date DATE, end_date DATE, created_at DATETIME2 NOT NULL DEFAULT SYSDATETIME()
);
CREATE TABLE project_milestones (
 milestone_id UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY, project_id UNIQUEIDENTIFIER NOT NULL REFERENCES projects(project_id),
 milestone_name NVARCHAR(255) NOT NULL, description NVARCHAR(MAX), amount DECIMAL(12,2) NOT NULL, due_date DATE,
 milestone_status VARCHAR(20) NOT NULL, created_at DATETIME2 NOT NULL DEFAULT SYSDATETIME()
);
CREATE TABLE payments (
 payment_id UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY, project_id UNIQUEIDENTIFIER NOT NULL REFERENCES projects(project_id),
 milestone_id UNIQUEIDENTIFIER NULL REFERENCES project_milestones(milestone_id), client_profile_id UNIQUEIDENTIFIER NOT NULL REFERENCES client_profiles(client_profile_id),
 expert_profile_id UNIQUEIDENTIFIER NOT NULL REFERENCES expert_profiles(expert_profile_id), amount DECIMAL(12,2) NOT NULL,
 platform_fee DECIMAL(12,2), payment_method VARCHAR(50), payment_status VARCHAR(20) NOT NULL,
 released_at DATETIME2, created_at DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
 CONSTRAINT ck_payment_status CHECK(payment_status IN ('HELD','RELEASED','REFUNDED','FAILED'))
);
CREATE TABLE reviews (
 review_id UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY, project_id UNIQUEIDENTIFIER NOT NULL REFERENCES projects(project_id),
 payment_id UNIQUEIDENTIFIER NOT NULL UNIQUE REFERENCES payments(payment_id), client_profile_id UNIQUEIDENTIFIER NOT NULL REFERENCES client_profiles(client_profile_id),
 expert_profile_id UNIQUEIDENTIFIER NOT NULL REFERENCES expert_profiles(expert_profile_id), rating_star INT NOT NULL,
 review_comment NVARCHAR(MAX), delivery_quality INT, communication_quality INT, deadline_satisfaction INT, would_hire_again BIT,
 review_status VARCHAR(20) NOT NULL DEFAULT 'PUBLISHED', created_at DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
 CONSTRAINT ck_review_rating CHECK(rating_star BETWEEN 1 AND 5)
);

-- Notification module retained; it now targets Account UUIDs and supports UUID references.
CREATE TABLE project_notifications (
 id BIGINT IDENTITY(1,1) PRIMARY KEY, user_id UNIQUEIDENTIFIER NOT NULL REFERENCES accounts(account_id), title NVARCHAR(255) NOT NULL,
 message NVARCHAR(4000) NOT NULL, notification_type VARCHAR(50) NOT NULL, reference_type VARCHAR(255), reference_id UNIQUEIDENTIFIER,
 sent BIT NOT NULL DEFAULT 0, is_read BIT NOT NULL DEFAULT 0, created_at DATETIME2 NOT NULL DEFAULT SYSDATETIME(), read_at DATETIME2
);

GO
