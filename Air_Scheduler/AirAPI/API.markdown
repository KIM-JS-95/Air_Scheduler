# API Reference

## A. UserController

### 회원가입

| URL     | Method | Method Parameter | Type   | Description |
|:--------|:-------|:-----------------|:-------|-------------|
| `/join` | `POST` | `User`           | `User` | 회원가입        |

#### Request

```json

{
  "header": {
    "Content-Type": "application/json"
  },
  "body": {
    "userid": "<pilot Number>",
    "password": "<pilot PW>"
  }
}

```

#### Response

```json

{
  "header": {
    "Content-Type": "application/json"
  },
  "body": {
    "String": "true"
  }
}

```

### 로그인

| URL      | Method | Method Parameter | Type     | Description |
|:---------|:-------|:-----------------|:---------|-------------|
| `/login` | `POST` | `User`           | `string` | 로그인         |

#### Request

```json

{
  "header": {
    "Content-Type": "application/json"
  },
  "body": {
    "userid": "<pilot Number>",
    "password": "<pilot PW>"
  }
}

```

#### Response

```json

{
  "header": {
    "Content-Type": "application/json",
    "Authorization": "<JWT Token you get when login>"
  },
  "body": {
    "String": "<Page Redirect to main page.>"
  }
}

```

### 로그아웃

| URL       | Method | Method Parameter | Type     | Description |
|:----------|:-------|:-----------------|:---------|-------------|
| `/logout` | `POST` | `User`           | `string` | 로그아웃        |

#### Request

```json

{
  "header": {
    "Authorization": "<JWT Token you get when login>",
    "Content-Type": "application/json"
  },
  "body": {}
}

```

#### Response

```json

{
  "header": {
    "Authorization": "<JWT Token you get when login>",
    "Content-Type": "application/json"
  },
  "body": {
    "String": "true"
  }
}

```

---

## B. SchedulesController

#### 업로드

| URL       | Method | Parameter | Type                | Description |
|:----------|:-------|:----------|:--------------------|:------------|
| `/upload` | `POST` | `file`    | `MockMultipartFile` | 스케쥴 이미지 업로드 |

#### Request

```json

{
  "header": {
    "Authorization": "<JWT Token you get when login>",
    "Content-Type": "multipart/form-data"
  },
  "body": {
    "file": "<Image File Cropped> .jpg"
  }
}

```

### 수정

| URL       | Method | Parameter  | Type       | Description |
|:----------|:-------|:-----------|:-----------|:------------|
| `/modify` | `POST` | `Schedule` | `Schedule` | 단일 스케줄 수정   |

#### Request

```json
{
  "header": {
    "Authorization": "<JWT Token you get when login>",
    "Content-Type": "application/json"
  },
  "body": {
    "Schedule": "<Schedule Entity>"
  }
}
```

#### 삭제

| URL       | Method   | Parameter | Type | Description |
|:----------|:---------|:----------|:-----|:------------|
| `/delete` | `Delete` | `Id`      | *    | 스케줄 삭제      |

```json
{
  "header": {
    "Authorization": "<JWT Token you get when login>",
    "Content-Type": "application/json"
  },
  "body": {
    "id": "1"
  }
}
```

--- 

## C. PilotController

### 당일 일정

| URL                  | Method | Parameter | Return Type      | Description   |
|:---------------------|:-------|:----------|:-----------------|:--------------|
| `/getTodaySchedules` | `GET`  | ``        | 'List<Schedule>' | 당일 모든 일정 가져오기 |

#### Request

```json
{
  "header": {
    "Authorization": "<JWT Token you get when login>",
    "Content-Type": "application/json"
  },
  "body": {}
}
```

#### Response

```json
{
  "header": {
    "Authorization": "<JWT Token you get when login>",
    "Content-Type": "application/json"
  },
  "body": {
    "Schedule": [
      {
        "id": "2",
        "date": "02Nov23",
        "pairing": "F1508A",
        "dc": "",
        "ci": "0840",
        "co": "",
        "activity": "1508",
        "cntFrom": "ICN",
        "stdL": "1050",
        "stdB": "1050",
        "cntTo": "OIT",
        "staL": "1245",
        "staB": "1245",
        "achotel": "738",
        "userid": "User_ID"
      },
      {
        "id": "3",
        "date": "02Nov23",
        "pairing": "F1508A",
        "dc": "",
        "ci": "",
        "co": "1715",
        "activity": "1507",
        "cntFrom": "OIT",
        "stdL": "1410",
        "stdB": "1410",
        "cntTo": "ICN",
        "staL": "1615",
        "staB": "1615",
        "achotel": "738",
        "userid": "User_ID"
      }
    ]
  }
}

```

#### 선택날짜

| URL                 | Method | Parameter | Return Type      | Description        |
|:--------------------|:-------|:----------|:-----------------|:-------------------|
| `/getDateSchedules` | `GET`  | `Date`    | 'List<Schedule>' | 지정한 기간 동안의 일정 가져오기 | 

```json
{
  "header": {
    "Authorization": "<JWT Token you get when login>",
    "Content-Type": "application/json"
  },
  "body": {
    "date": "2023-11-02"
  }
}
```

#### Response

```json
{
  "header": {
    "Authorization": "<JWT Token you get when login>",
    "Content-Type": "application/json"
  },
  "body": {
    "Schedule": [
      {
        "id": "2",
        "date": "02Nov23",
        "pairing": "F1508A",
        "dc": "",
        "ci": "0840",
        "co": "",
        "activity": "1508",
        "cntFrom": "ICN",
        "stdL": "1050",
        "stdB": "1050",
        "cntTo": "OIT",
        "staL": "1245",
        "staB": "1245",
        "achotel": "738",
        "userid": "User_ID"
      },
      {
        "id": "3",
        "date": "02Nov23",
        "pairing": "F1508A",
        "dc": "",
        "ci": "",
        "co": "1715",
        "activity": "1507",
        "cntFrom": "OIT",
        "stdL": "1410",
        "stdB": "1410",
        "cntTo": "ICN",
        "staL": "1615",
        "staB": "1615",
        "achotel": "738",
        "userid": "User_ID"
      }
    ]
  }
}

```

#### 모든 일정

| URL                 | Method | Parameter | Return Type      | Description    |
|:--------------------|:-------|:----------|:-----------------|:---------------|
| `/showAllSchedules` | `GET`  | ``        | 'List<Schedule>' | '해당일 모든 일정 획득' |

```json
{
  "header": {
    "Authorization": "<JWT Token you get when login>",
    "Content-Type": "application/json"
  },
  "body": {
    "Schedule": [
      {
        "id": "1",
        "date": "01Nov23",
        "pairing": "",
        "dc": "",
        "ci": "0840",
        "co": "",
        "activity": "VAC",
        "cntFrom": "GMP",
        "stdL": "0000",
        "stdB": "0000",
        "cntTo": "GMP",
        "staL": "2359",
        "staB": "2359",
        "achotel": "",
        "userid": "User_ID"
      },
      {
        "id": "2",
        "date": "02Nov23",
        "pairing": "F1508A",
        "dc": "",
        "ci": "0840",
        "co": "",
        "activity": "1508",
        "cntFrom": "ICN",
        "stdL": "1050",
        "stdB": "1050",
        "cntTo": "OIT",
        "staL": "1245",
        "staB": "1245",
        "achotel": "738",
        "userid": "User_ID"
      },
      {
        "id": "3",
        "date": "02Nov23",
        "pairing": "F1508A",
        "dc": "",
        "ci": "",
        "co": "1715",
        "activity": "1507",
        "cntFrom": "OIT",
        "stdL": "1410",
        "stdB": "1410",
        "cntTo": "ICN",
        "staL": "1615",
        "staB": "1615",
        "achotel": "738",
        "userid": "User_ID"
      }
    ]
  }
}

```



