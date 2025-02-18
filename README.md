"방.학"은 "방에서 학습"을 줄여 붙여진 이름이며, 찾아가는 방문학습 과외 같은 시스템으로 구성되어 있는 프로젝트입니다.
복잡한 기존의 UI들을 간단하게 바꾸고, 학부모와 학습하는 학생의 편의 및 더 나아가 선생님의 편리함도 충족 시키기 위해 이 프로젝트를 기획하게 되었습니다.

프로젝트 조원 소개
- 조장, DB 담당, 백엔드 서포트 - 조다은 (greenwen)
- 백엔드 담당, DB 서포트 - 김은채
- 기획 담당, CSS/프론트엔드 서포트 - 이시연
- 프론트엔드 담당 - 조민주

목차
프로젝트 개요
사용된 기술
개발 환경
설치
사용 방법
기여하기
라이센스
설정 파일

프로젝트 개요

이 애플리케이션은 학부모, 과외 선생님, 관리자를 위한 기능을 제공합니다:
- 학부모: 자녀를 관리하고 학습 신청을 쉽게 할 수 있는 UI를 제공합니다.
- 과외 선생님: 간단한 UI로 자신의 일정을 확인하고 학생 관리가 가능합니다.
- 관리자: 과외 선생님들의 일정을 최적의 경로로 저장하여 효율적인 관리와 계획을 도와줍니다.

이 웹 애플리케이션은 Spring Boot 기반으로 개발되었으며, 다양한 기능들이 통합되어 있습니다:
- Spring Data JPA를 이용한 데이터베이스 연동
- Spring Boot Mail을 통한 이메일 기능
- Spring Security를 사용한 애플리케이션 보안
- Thymeleaf 템플릿 엔진을 사용한 동적 뷰
- WebSocket(SockJS와 STOMP)을 통한 실시간 메시징
- Jsprit을 사용한 차량 경로 최적화
- 카카오 지도 API 및 카카오 페이 API 통합

사용된 기술
- Backend: Spring Boot 3.4.1, Spring Data JPA, Spring Security, Thymeleaf, WebSocket (SockJS, STOMP), Jsprit
- Database: Oracle Database (JPA 연동)
- Dev Tools: Lombok, JUnit, Gradle
- APIs: 카카오 지도 API, 카카오 페이 API
- Frontend: HTML, CSS, JavaScript, jQuery, Bootstrap
- Etc: Apache Tomcat, ERD 설계, Photoshop, Figma, Illustrator

개발 환경

- IDE: IntelliJ IDEA
- Version Control: Git, GitHub
- Build Tool: Gradle
- Deployment: Apache Tomcat
- Programming Languages: Java, Python
- Design Tools: Photoshop, Figma, Illustrator
- Database Management: Oracle SQL Developer

설치

이 프로젝트를 로컬 환경에 설정하고 실행하려면 다음 단계를 따르세요:

1. 레포지토리 클론:
git clone https://github.com/your-username/fproject2.git

2. 프로젝트 디렉토리로 이동:
cd fproject2

3. Java 17 설치 확인:
java -version

4. 프로젝트 종속성 설치:
./gradlew build

5. Spring Boot 애플리케이션 실행:
./gradlew bootRun

브라우저를 열고 http://localhost:8080에서 애플리케이션에 접속하세요.

사용 방법:

학부모
- 자녀의 학습 신청 및 자녀 관리 기능을 통해 학습 진행 상황을 확인하고, 학습 계획을 간편하게 신청할 수 있습니다.

과외 선생님
- 간단한 UI를 통해 자신의 일정을 확인하고, 학생들을 관리하는 데 필요한 기능들을 손쉽게 이용할 수 있습니다.

관리자
- 모든 선생님들의 일정을 최적화된 경로로 관리하여 이동 시간을 최소화하고, 효율적인 스케줄링을 돕습니다.

설정 파일

이 프로젝트에서는 여러 가지 중요한 설정이 application.properties 파일에 포함되어 있습니다.

데이터베이스 설정:
spring.datasource.driver-class-name=oracle.jdbc.driver.OracleDriver
spring.datasource.url=jdbc:oracle:thin:@localhost:1521/xe
spring.datasource.username=ICIA
spring.datasource.password=1111

JPA 설정:
spring.jpa.database-platform=org.hibernate.dialect.OracleDialect
spring.jpa.open-in-view=false
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.format_sql=true

이메일 설정 (JavaMail):
spring.mail.host=smtp.gmail.com
spring.mail.port=000
spring.mail.username=email
spring.mail.password=password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.ssl.trust=smtp.gmail.com

파일 업로드 설정:
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.enabled=true

세션 설정:
server.servlet.session.cookie.http-only=true
server.servlet.session.cookie.secure=false
server.servlet.session.timeout=30m
server.servlet.session.tracking-modes=cookie

기여하기
이 프로젝트에 기여하려면 다음 단계를 따르세요:

- 레포지토리를 포크하세요

- 새로운 브랜치를 생성하세요:
git checkout -b feature-branch

- 변경 사항을 작업하세요.

- 변경 사항을 커밋하세요:
git commit -m '새로운 기능 추가 또는 버그 수정'

- 자신의 브랜치로 푸시하세요:
git push origin feature-branch

- 원본 레포지토리에 풀 리퀘스트를 생성하세요.
- 
