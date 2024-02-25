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

[Novenber.jpg](AirAPI%2Fsrc%2Fmain%2Fresources%2Fstatic%2Fimg%2FNovember.jpg)

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

> 유저 관리 [회원가입 / 로그인 / 로그아웃]

| URL       | Method | Method Parameter | Type     | Description | Request                               | Response    |
|:----------|:-------|:-----------------|:---------|-------------|:--------------------------------------|:------------|
| `/join`   | `POST` | `User`           | `User`   | 회원가입        | [user ID / user PW](#UserConstroller) | `Boolean`   |
| `/login`  | `POST` | `User`           | `string` | 로그인         | [user ID / Name](#UserConstroller)    | `JWT Token` |
| `/logout` | `POST` | `User`           | `string` | 로그아웃        | `JWT Token`                             | `Boolean`   |

### SchedulesController

> 일정 관리 [업로드 / 수정 / 삭제]

| URL       | Method   | Parameter  | Type                | Description     | Request     | Response |
|:----------|:---------|:-----------|:--------------------|:----------------|:------------|:---------|
| `/upload` | `POST`   | `file`     | `MockMultipartFile` | 스케쥴 이미지 분적 후 저장 | Image file  | *        |
| `/modify` | `POST`   | `Schedule` | `Schedule`          | 단일 스케줄 수정       | Schedule Id | *        |
| `/delete` | `Delete` | `Id`       | *                   | 스케줄 전체 삭제       | Schedule Id | *        |

### PilotController

> 일정 요청[오늘일정 / 날짜 선택 일정 / 모든 일정]

| URL                  | Method | Parameter | Return Type      | Description        | Request(Sample)       | Response |
|:---------------------|:-------|:----------|:-----------------|:-------------------|:----------------------|:---------|
| `/getTodaySchedules` | `GET`  | ``        | 'List<Schedule>' | 당일 모든 일정 가져오기      | *                     | *        |
| `/getDateSchedules`  | `GET`  | `Date`    | 'List<Schedule>' | 지정한 기간 동안의 일정 가져오기 | [getDateSchedules](#PilotController) | *        |
| `/showAllSchedules`  | `GET`  | ``        | ''               | 모든 일정 가져오기         | *                     |          |

## Framework
```
Back_end: SpringBoot, Spring JPA, Spring Security(whit. JWT)

Front_end: FLutter (android)

Infra structure: Docker, AWS

API: AWS Textrack
```


## Sample Json
#### UserController
```json
{
  "userid": "<pilot Number>",
  "password": "<pilot PW>"
}
```

```json
{
  "userid": "<pilot Number>",
  "name": "<pilot Name>"
}
```


#### PilotController
```json
{
  "sdate": "01Nov23",
  "edate": "05Nov23"
}
```