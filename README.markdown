# KT_지니 & pilot time table

## Tools

### Framework
	SpringBoot, Spring JPA, Spring Security(whit. JWT)

### API
	AWS Textrack, KT_Genie_API

### Infra structure
	Docker, AWS, KT_Genie

## TODO
pilot 비행 스케줄 이미지에서 스케줄을 분석하여 저장하고 `KT_GENIE`를 통해 호출하여 일정을 확인할 수 있도록 개발

## Test Environment
1. Mock Schedule 데이터 사용 -> aws Textrak API는 최종 단계에서 사용할 예정
2. 모든 테스드 환경은 Local 에서 진행 (KT_Genie 또한 Local 환경 작동 확인 후 테스트)


## Problem
- Pilot 개인 ID와 Schedule 유출에 관한 문제


## OverCome

## TODO
- 사용자 관리 및 입력 (SpringBoot Secutiry)
- 스케출 이미지 정보로부터 데이터 추출 (AWS Textrect)
  - 테스트 전용 코드 로직 작성 (Dummy Entity)
- 이미지 정보 객체화 후 JPA 저장

## API Reference

### UserController
#### Sign up

```http
  POST /join
```

| Parameter | Type   | Description                                                 | Data                                                                                            |
| :-------- |:-------|:------------------------------------------------------------|-------------------------------------------------------------------------------------------------|
| `User`      | `User` | **Required**. userid / password <br/><br/>`userid` is pilot number | {<br/>"userid":"1234",<br/>"name":"홍길동"<br/>"email":"abc@gmail.com",<br/>"password":"1234"<br/>} |

#### Sign in

```http
  POST /login
```

| Parameter | Type     |         Description         | Data                                               |
| :-------- | :------- |:---------------------------:|----------------------------------------------------|
| `User`      | `string` | **Required**. userid / name | {<br/>"userid":"1234",<br/>"password":"1234"<br/>} |


#### Log out

```http
  POST /logout
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
|       |  | |
---

### PilotController
#### Upload Pilot Schedules

```http
  POST /upload
```

This Function converts your schedule image to save at Entity with AWS_Textrack.
Those Entities are used to show at Display which connected by module name 'KT_GEINE'.

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `file` | `MockMultipartFile` | * |


## TODO List
- api request and response value Documenting