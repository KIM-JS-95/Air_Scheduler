# KT_지니 & pilot time table

1. Goal
   pilot 비행 스케줄 이미지에서 스케줄을 분석하여 저장하고 `KT_GENIE`를 통해 호출하여 일정을 확인할 수 있도록 개발

## Problem

- Pilot 개인 ID와 Schedule 유출에 관한 문제

## TODO

### Spring Boot

- 사용자 관리 및 입력 (SpringBoot Secutiry) ✅
- 스케출 이미지 정보로부터 데이터 추출 (AWS Textrect) ✅
    - 테스트 전용 코드 로직 작성 (Dummy Entity) ✅
- 이미지 정보 객체화 후 JPA 저장 ✅
- 스케쥴 수정 및 전체 삭제 로직
- ResponseEntity 잡아주기 -> Return 코드 만들어 보기

![sample.jpg](AirAPI%2Fsrc%2Fmain%2Fresources%2Fstatic%2Fimg%2FNovember.jpg)
* 2023년 11월 부터 양식이 바뀜

## API Reference
1. User (파일럿)
    1. 스케줄 관리(등록, 수정, 삭제)
        2. **스케줄 관리는 별도의 컨트롤러에서 관리**

    2. 회원가입


2. Admin
    1. 스케줄 DB관리
    2. 회원관리

### UserController
> Sign Up & In & Out

| URL       | Method | Method Parameter | Type     | Description (* is Required)                            | Response    |
|:----------|:-------|:-----------------|:---------|:-------------------------------------------------------|:------------|
| `/join`   | `POST` | `User`           | `User`   | * userid / password <br/><br/>`userid` is pilot number | `Boolean`   |
| `/login`  | `POST` | `User`           | `string` | * userid / name                                        | `JWT Token` |
| `/logout` | `POST` | `User`           | `string` | 헤더에 토큰이 포함되어 있을 것                                      | `Boolean`   |

### SchedulesController

```
    This Function converts your schedule image to save at Entity with AWS_Textrack.
    Those Entities are used to show at Display which connected by module name 'KT_GEINE'.
```

| URL            | Method   | Parameter  | Type                | Description |
|:---------------|:---------|:-----------|:--------------------|:------------|
| `/upload`      | `POST`   | `file`     | `MockMultipartFile` | *           |
| `/modify/{id}` | `POST`   | `Schedule` | `Schedule`          | `단일 스케줄 수정` |
| `/delete`      | `Delete` | `Id`       | *                   | `스케줄 전체 삭제` |

### PilotController

| URL            | Method | Parameter | Type | Description                              |
|:---------------|:-------|:----------|:-----|:-----------------------------------------|
| `/getschedule` | `GET`  | ``        | ''   | `today + 2days` '해당 국가의 날씨 데이터도 같이 가져오기' |


### Flutter

- 이미지 편집 기능 및 ajax 전송✅
- 로그인 기능


## PAGE Layout(Flutter)
| layout                       | Method | Parameter | Type | Description                         |
|:-----------------------------|:-------|:----------|:-----|:------------------------------------|
| `/join`                      |        | ``        | ''   | `sign up!`                          |
| `/login`                     |        | ``        | ''   | `Home layout is a login page!`      |
| `/schedules_Upload and Edit` |        | ``        | ''   | `Schedules Edit and POST To server` |
| `/schedules_view_all`        |        | ``        | ''   | `Total Schedules show up!`          |
| `/schedules_view`            |        | ``        | ''   | `3-days  Schedules show up!`        |
| `/schedules_modify`          |        | ``        | ''   | `modify your  Schedules`            |

* 별도의 메인 홈 페이지 없이 바로 로그인 페이지로 이동합니다.
** 솔직히 로그인 계정을 사용하기 보다 지문이 조금더 편리하지 않을까요?


## Framework

	Back_end: SpringBoot, Spring JPA, Spring Security(whit. JWT)
    Front_end: FLutter (android)

## API

	AWS Textrack, KT_Genie_API

## Infra structure

	Docker, AWS, KT_Genie