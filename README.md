# 🎓 Certif

> 자격증 정보를 캘린더로 확인하고, 자격증 관련 스터디 모집/참여 및 정보 공유 커뮤니티 웹서비스  
> (이 저장소는 **제가 기여한 백엔드 부분**을 중심으로 정리했습니다.)


## 🌟 About
Certif는 자격증 취득을 준비하는 사람들을 위한 통합 플랫폼입니다.  
- 관심 있는 자격증 즐겨찾기 등록 -> 자격증 시험 일정을 **캘린더**로 한눈에 확인  
- 자격증 관련 스터디 모집 & 참여 게시판  
- 자격증 관련 정보 공유 게시판



## 🛠️ Tech Stack
### ⚙️ Backend
- Java 
- Spring Boot 
- Spring Security + JWT  
- MySQL 8.0  
- JPA (Hibernate)

### 🎨 Frontend
- React  
- styled-components  
- JavaScript / HTML / CSS  

### ☁️ Infra & Tools
- AWS EC2, RDS  
- Git / GitHub  
- IntelliJ / VS Code / Eclipse  


## 👩‍💻 My Role (Backend)
-  **스터디 게시판 (study_post)**  
  - 카테고리별 게시글 조회 (기본: 첫 번째 카테고리 자동 출력)  
  - 게시글 작성 / 수정 / 삭제 
  - 작성자 권한 제어 (본인만 수정·삭제 가능)  

- **댓글 (study_comment)**  
  - 댓글 등록 / 조회 / 수정 / 삭제  
  - JWT 토큰 기반 사용자 정보 자동 반영  

-  **DB 설계 & JPA 매핑**  
  - 게시글 ↔ 댓글 연관관계(Entity) 매핑  

- **인증 & 인가**  
  - JWT 기반 로그인/회원가입 로직 적용  


## 📁 Documents
- **노션 페이지** : https://www.notion.so/3-22e6ce44b403803487edd499c0c91a1e
