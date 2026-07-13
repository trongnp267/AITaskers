-- =================================================================
-- KICH BAN SQL TEST TONG THE CHO HE THONG NEN TANG AITASKER
-- THU NGHIEM LUONG CONG VIEC VA GIAO DICH KY QUY (ESCROW)
--
-- GHI CHU QUAN TRONG: day la script THU CONG de test/demo logic nghiep vu
-- bang T-SQL thuan tuy. Ung dung Spring Boot thuc te KHONG dung script nay
-- de tao bang - no dung Hibernate (spring.jpa.hibernate.ddl-auto=update,
-- xem application.properties) de tu sinh schema tu cac @Entity trong
-- src/main/java/.../entity. Ten bang/cot trong 2 noi nay khac nhau mot
-- chut (vi du entity Wallet -> bang "wallets" chu thuong, con script nay
-- dung "Wallet") - do la chu y, khong phai loi, vi day chi la ban demo
-- doc lap de ban hinh dung luong du lieu.
-- =================================================================

USE master;
GO
IF EXISTS (SELECT * FROM sys.databases WHERE name = 'AITaskerDB')
BEGIN
    ALTER DATABASE AITaskerDB SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE AITaskerDB;
END
GO

CREATE DATABASE AITaskerDB;
GO
USE AITaskerDB;
GO

-- =================================================================
-- 1. TAO CAC BANG DU LIEU (SCHEMA)
-- =================================================================

-- Bang 1: Quan ly nguoi dung (Khach hang va Chuyen gia AI) kem so du vi
CREATE TABLE Users (
    UserID INT IDENTITY(1,1) PRIMARY KEY,
    Username NVARCHAR(50) NOT NULL,
    Email VARCHAR(100) UNIQUE NOT NULL,
    Role VARCHAR(20) CHECK (Role IN ('Client', 'AI_Expert')),
    WalletBalance DECIMAL(18, 2) DEFAULT 0.00,
    CreatedAt DATETIME DEFAULT GETDATE()
);

-- Bang 2: Quan ly yeu cau cong viec (Job) do Client dang
CREATE TABLE Jobs (
    JobID INT IDENTITY(1,1) PRIMARY KEY,
    ClientID INT FOREIGN KEY REFERENCES Users(UserID),
    Title NVARCHAR(100) NOT NULL,
    Description NVARCHAR(MAX),
    Budget DECIMAL(18, 2) NOT NULL,
    Status VARCHAR(20) DEFAULT 'Open'
        CHECK (Status IN ('Open', 'Assigned', 'In_Progress', 'Completed', 'Cancelled')),
    CreatedAt DATETIME DEFAULT GETDATE()
);

-- Bang 3: Quan ly danh sach ung tuyen (Applications) cua cac AI Expert
CREATE TABLE Applications (
    ApplicationID INT IDENTITY(1,1) PRIMARY KEY,
    JobID INT FOREIGN KEY REFERENCES Jobs(JobID) ON DELETE CASCADE,
    ExpertID INT FOREIGN KEY REFERENCES Users(UserID),
    BidAmount DECIMAL(18, 2) NOT NULL,
    Proposal NVARCHAR(MAX),
    Status VARCHAR(20) DEFAULT 'Pending'
        CHECK (Status IN ('Pending', 'Accepted', 'Rejected')),
    AppliedAt DATETIME DEFAULT GETDATE()
);

-- Bang 4: He thong Ky quy Trung gian (Escrow) de khoa tien giao dich
CREATE TABLE EscrowTransactions (
    EscrowID INT IDENTITY(1,1) PRIMARY KEY,
    JobID INT UNIQUE FOREIGN KEY REFERENCES Jobs(JobID),
    ClientID INT FOREIGN KEY REFERENCES Users(UserID),
    ExpertID INT FOREIGN KEY REFERENCES Users(UserID),
    LockedAmount DECIMAL(18, 2) NOT NULL,
    Status VARCHAR(20) DEFAULT 'Locked'
        CHECK (Status IN ('Locked', 'Released', 'Refunded')),
    UpdatedAt DATETIME DEFAULT GETDATE()
);

-- Bang 5: Vi (Wallet) rieng, tach khoi WalletBalance tren Users.
-- (TRUOC DAY bang nay bi dinh nghia lai 3 lan khac nhau va xung dot -
-- gop lai thanh MOT phien ban duy nhat, nhat quan.)
CREATE TABLE Wallet (
    wallet_id INT PRIMARY KEY IDENTITY(1,1),
    user_id INT FOREIGN KEY REFERENCES Users(UserID),
    balance DECIMAL(18, 2) DEFAULT 0.00,
    proposal_id INT NULL
);
GO

-- =================================================================
-- 2. CHEN DU LIEU MAU (MO PHONG KICH BAN CHAY THUC TE)
-- =================================================================

-- Buoc 2.1: Khoi tao tai khoan nguoi dung va nap tien vao vi
INSERT INTO Users (Username, Email, Role, WalletBalance) VALUES
(N'Cong ty Cong nghe ABC', 'client.abc@example.com', 'Client', 5000.00),
(N'Nguyen Van A (AI Engineer)', 'expert.a@example.com', 'AI_Expert', 150.00),
(N'Tran Thi B (Data Scientist)', 'expert.b@example.com', 'AI_Expert', 0.00);

-- Buoc 2.2: Khach hang dang mot cong viec (Job) yeu cau xay dung Model
INSERT INTO Jobs (ClientID, Title, Description, Budget, Status) VALUES
(1, N'Xay dung mo hinh Chatbot AI', N'Phat trien mo hinh nhan dien y dinh nguoi dung bang Python', 1200.00, 'Open');

-- Buoc 2.3: Cac chuyen gia xem thay cong viec va nop don ung tuyen (Apply)
INSERT INTO Applications (JobID, ExpertID, BidAmount, Proposal) VALUES
(1, 2, 1100.00, N'Toi co 3 nam kinh nghiem lam NLP, cam ket hoan thanh trong 5 ngay.'),
(1, 3, 1200.00, N'Giai phap toi uu, su dung mo hinh toi tan nhat hien nay.');

-- Buoc 2.4: Nap thu vi mau cho User 1
INSERT INTO Wallet (user_id, balance) VALUES (1, 1000.00);
GO

-- =================================================================
-- 3. MO PHONG LOGIC NGHIEP VU BANG TRANSACTION (QUY TRINH CHON UNG VIEN & KY QUY)
-- =================================================================
-- Kich ban: Client chon Nguyen Van A (ExpertID = 2) voi gia thau 1100$.
-- He thong phai cap nhat trang thai Job, tru tien vi Client, chuyen tien vao vi khoa Escrow.

BEGIN TRANSACTION;
BEGIN TRY
    DECLARE @TargetJob INT = 1;
    DECLARE @SelectedExpert INT = 2;
    DECLARE @Client INT = 1;
    DECLARE @FinalPrice DECIMAL(18,2) = 1100.00;

    -- 1. Cap nhat trang thai Job thanh 'Assigned'
    UPDATE Jobs SET Status = 'Assigned' WHERE JobID = @TargetJob;

    -- 2. Chap nhan ung vien duoc chon, tu choi ung vien con lai
    UPDATE Applications SET Status = 'Accepted' WHERE JobID = @TargetJob AND ExpertID = @SelectedExpert;
    UPDATE Applications SET Status = 'Rejected' WHERE JobID = @TargetJob AND ExpertID <> @SelectedExpert;

    -- 3. Tru tien kha dung trong vi cua Client
    UPDATE Users SET WalletBalance = WalletBalance - @FinalPrice WHERE UserID = @Client;

    -- 4. Khoi tao ban ghi dong bang tien trong he thong Escrow
    INSERT INTO EscrowTransactions (JobID, ClientID, ExpertID, LockedAmount, Status)
    VALUES (@TargetJob, @Client, @SelectedExpert, @FinalPrice, 'Locked');

    COMMIT TRANSACTION;
    PRINT N'>>> LUONG CHON UNG VIEN VA KY QUY (ESCROW) THANH CONG!';
END TRY
BEGIN CATCH
    ROLLBACK TRANSACTION;
    PRINT N'>>> LOI HE THONG: QUY TRINH KY QUY BI HUY BO!';
END CATCH;
GO

-- =================================================================
-- 4. TRUY VAN KIEM TRA DE KHOP VOI CODE TONG (API TESTING SELECTION)
-- =================================================================

PRINT N'--- DANH SACH CONG VIEC VA SO LUONG DON UNG TUYEN ---';
SELECT
    J.JobID,
    J.Title AS [Ten Cong Viec],
    U.Username AS [Khach Hang Dang],
    J.Budget AS [Ngan Sach Goc ($)],
    J.Status AS [Trang Thai Job],
    COUNT(A.ApplicationID) AS [So Luong Ung Vien App]
FROM Jobs J
JOIN Users U ON J.ClientID = U.UserID
LEFT JOIN Applications A ON J.JobID = A.JobID
GROUP BY J.JobID, J.Title, U.Username, J.Budget, J.Status;

PRINT N'--- QUAN LY DONG TIEN TRUNG GIAN (ESCROW WALLET) ---';
SELECT
    E.EscrowID,
    J.Title AS [Cong Viec],
    C.Username AS [Khach Hang (Nguoi Tra)],
    EX.Username AS [Chuyen Gia (Nguoi Nhan)],
    E.LockedAmount AS [So Tien Dang Bi Khoa ($)],
    E.Status AS [Trang Thai Ky Quy]
FROM EscrowTransactions E
JOIN Jobs J ON E.JobID = J.JobID
JOIN Users C ON E.ClientID = C.UserID
JOIN Users EX ON E.ExpertID = EX.UserID;

PRINT N'--- SO DU VI KHA DUNG HIEN TAI CUA KHACH HANG ---';
SELECT Username, Role, WalletBalance AS [So Du Vi Hien Tai ($)] FROM Users WHERE Role = 'Client';
GO
