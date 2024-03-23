# schdulesapp

A new Flutter project.

## Getting Started

This project is a starting point for a Flutter application.

A few resources to get you started if this is your first Flutter project:

- [Lab: Write your first Flutter app](https://docs.flutter.dev/get-started/codelab)
- [Cookbook: Useful Flutter samples](https://docs.flutter.dev/cookbook)

For help getting started with Flutter development, view the
[online documentation](https://docs.flutter.dev/), which offers tutorials,
samples, guidance on mobile development, and a full API reference.

- 비행코드 입력 (ex. GMP / 김포공항)

# TODO
- 비행코드 입력 (ex. GMP / 김포공항)

# API 
|mathod| request type | response type | content |
|:--:|:---:|:---:|:---:|
| getScheduleByDate() | DateTime selectedDay | List<ScheduleModel> | 특정일 데이터 가져오기 & 메인 페이지 금일 일정 가져오기 |
| getAllSchedules() | . | List<ScheduleModel> | 모든 일정 가져오기 |
| uploadajax() | MultipartFile | ? | 이미지 전송 |
## getAllSchedules
- url: /show-schedule
### Request
``` json
    header: {
        "Content-Type": "application/json; charset=UTF-8",
        "Authorization": "<JWT Token>"
    },
    body: {
        "dateTime" : "2023-11-02"
    }
```
### Response

``` json
   {
        "id": 1,
        "date": "01Nov23",
        "pairing": null,
        "dc": null,
        "ci": null,
        "co": null,
        "activity": "VAC",
        "cntFrom": "GMP",
        "stdL": "0000",
        "stdB": "0000",
        "cntTo": "GMP",
        "staL": "2359",
        "staB": "2359",
        "achotel": null,
        "blk": null
  }
  ...
  
```


## getScheduleByDate
- url: /getschedule
### Request
``` json
    header: {
        "Content-Type": "application/json; charset=UTF-8",
        "Authorization": "<JWT Token>"
    },
    body: {
        "dateTime" : "2023-11-02"
    }
```

### Response
``` json
  {
    "id": 2,
    "date": "02Nov23",
    "pairing": "F1508A",
    "dc": null,
    "ci": "0840",
    "co": null,
    "activity": "1508",
    "cntFrom": "ICN",
    "stdL": "1050",
    "stdB": "1050",
    "cntTo": "OIT",
    "staL": "1245",
    "staB": "1245",
    "achotel": "null",
    "blk": null
  }
```

