# Muud
![t](https://github.com/SWYP-3rd-muud/muud-backend/assets/143480682/6ae65111-33ed-4072-87c8-2b68666f8117)
<br>
뮤직 플레이리스트와 함께 기록하는 감정 다이어리 뮤드 (MUUD)
<br>
음악과 함께 감정을 기록해 보세요.
<br>

## 🖥️ 프로젝트 소개

안녕하세요. 음악 감정 기록 서비스 ‘뮤드(MUUD)’입니다! <br> 
뮤드는 감정 테스트를 통해 감정별 플레이리스트를 추천받고, 플레이리스트와 함께 감정 일기를 기록할 수 있는 서비스입니다. <br>
사용자가 매개체로 자신의 감정을 이해하고, 꾸준히 상호작용하게 만드는 가치를 제공하고자 합니다.
<br>
### 🚀️ 배포 주소
- https://muud.swygbro.com

## 🕰️ 개발 기간
* 2024.02.10 - 2024.03.10

### 🧑‍🤝‍🧑 백엔드 팀원 구성
- 김태현 - 일기, 감정, 이미지, 배포
- 김가은 - 로그인, 회원가입, 플레이리스트, 컬렉션

### ⚙️ 개발 환경
- **Language** : Java 17
- **IDE** : IntelliJ
- **Framework** : Springboot 3.2.2
- **Database** : MySQL
- **ORM** : JPA
- **Server** : AWS(EC2, S3)
- **CI/CD** : Github Actions

### 🔄 브랜치 전략
![flow](https://github.com/SWYP-3rd-muud/muud-backend/assets/143480682/97232b23-9bef-43bc-a25b-2fea467bbcb7)

### 🏗️ 서비스 아키텍쳐
![a](https://github.com/SWYP-3rd-muud/muud-backend/assets/143480682/f436a3e9-bfbd-48d9-b4b8-3d2ac59830b3)

### ☁ CICD pipeline
![CICD](https://github.com/SWYP-3rd-muud/muud-backend/assets/143480682/4d63ab25-0c69-41c5-a656-9691d759a9e1)

## 📌 도메인별 주요 기능
#### 로그인 
- 이메일 로그인
- 소셜 로그인
- JWT
#### 회원가입 
- 이메일 회원가입
일기 
#### 일기 - <a href="https://github.com/SWYP-3rd-muud/muud-backend/wiki/주요-기능-소개(Diary)" >상세보기(WIKI 이동)</a>
- 일기 CRUD
- 책갈피
- 월간 리포트
- 이미지 첨부
#### 감정
- 감정 조회
- 감정별 개수 조회
- 질문지 조회
#### 플레이리스트
- YouTube API 연동