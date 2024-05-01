import 'dart:convert';
import 'dart:ffi';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:intl/intl.dart';
import 'package:schdulesapp/main_pages/login_page.dart';

import '../models/flight_data.dart';
import '../models/schedule_model.dart';
import '../utils/r.dart';
import 'package:http/http.dart' as http;

class ScheduleRepository {
  /// 메인 페이지 당일 일정
  static Future<List<FlightData>> gettodayschedule(
      String Authorization, BuildContext context) async {
    Map<String, dynamic>? codes;

    final response = await http.post(
      Uri.parse('${R.back_addr}/gettodayschedule'),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
        'Authorization': Authorization
      },
    );

    if (response.statusCode == 200) {
      codes = await getNationCode(Authorization); // 비동기 함수 호출
      List<dynamic> jsonList = json.decode(response.body);
      List<ScheduleModel> s_list =
          jsonList.map((json) => ScheduleModel.fromJson(json)).toList();
      return generateFlightsData(jsonList.length, s_list, codes!);
    } else if (response.statusCode == 403) {
      _showErrorDialog(context, '조회권한이 없습니다!');
    }
    return generateFlightsData(0, [], codes!);
  }

  /// 날짜 선택
  static Future<List<FlightData>> getScheduleByDate(
      DateTime selectedDay, String Authorization, BuildContext context) async {
    Map<String, dynamic>? codes;
    String formattedDate = DateFormat('ddMMMyy').format(selectedDay);
    final response = await http.post(Uri.parse('${R.back_addr}/getschedule'),
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
          'Authorization': Authorization
        },
        body: jsonEncode({'dateTime': formattedDate}));

    if (response.statusCode == 200) {
      codes = await getNationCode(Authorization);
      List<dynamic> jsonList = json.decode(response.body);
      List<ScheduleModel> s_list =
          jsonList.map((json) => ScheduleModel.fromJson(json)).toList();
      return generateFlightsData(jsonList.length, s_list, codes!);
    } else if (response.statusCode == 403) {
      _showErrorDialog(context, '조회권한이 없습니다!');
    }
    return generateFlightsData(0, [], codes!);
  }

  /// 일정 리스트
  static Future<List<FlightData>> getAllSchedules(
      String Authorization, BuildContext context) async {
    Map<String, dynamic>? codes;

    final response = await http.post(
      Uri.parse('${R.back_addr}/showschedules'),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
        'Authorization': Authorization
      },
    );
    print(response.statusCode);
    if (response.statusCode == 200) {
      codes = await getNationCode(Authorization);
      List<dynamic> jsonList = json.decode(response.body);
      List<ScheduleModel> s_list =
          jsonList.map((json) => ScheduleModel.fromJson(json)).toList();
      return generateFlightsData(jsonList.length, s_list, codes!);
    } else if (response.statusCode == 403) {
      _showErrorDialog(context, '조회권한이 없습니다!');
    }
    return generateFlightsData(0, [], codes!);
  }

  /// 일정 자세히 보기
  static Future<List<FlightData>> getViewSchedule(
      int id, String Authorization, BuildContext context) async {
    final response = await http.post(
      Uri.parse('${R.back_addr}/viewschedule'),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
        'Authorization': Authorization
      },
      body: jsonEncode({'id': id}),
    );

    if (response.statusCode == 200) {
      Map<String, dynamic>? codes = await getNationCode(Authorization);
      dynamic jsonData = json.decode(response.body);

      ScheduleModel schedule = ScheduleModel.fromJson(jsonData);
      List<ScheduleModel> s_list = [schedule];
      return generateFlightsData(1, s_list, codes);

    } else if (response.statusCode == 403) {
      _showErrorDialog(context, '조회권한이 없습니다!');
    }
    return generateFlightsData(0, [], {}); // 예상되는 데이터 형식으로 반환
  }

  static Future<Map<String, dynamic>> getNationCode(
      String Authorization) async {
    final response = await http.get(
      Uri.parse('${R.back_addr}/getnationcode'),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
        'Authorization': Authorization
      },
    );

    if (response.statusCode == 200) {
      Map<String, dynamic> jsonData = jsonDecode(response.body);

      return jsonData;
    } else {
      throw Exception('Failed to load nation codes');
    }
  }

  /// 일정 객체 오버라이드 ScheduleModel.dart -> FlightData.dart
  static List<FlightData> generateFlightsData(
      int cnt, List<ScheduleModel> s_list, Map<String, dynamic> codes) {
    return List.generate(cnt, (index) {
      String departureShort = s_list[index].cntFrom.toString();
      String departure = codes.containsKey(s_list[index].cntFrom.toString())
          ? codes[s_list[index].cntFrom.toString()]['code'].toString()
          : 'Unknown';

      /// 출발지 코드
      String date = s_list[index].date.toString();
      String destinationShort = s_list[index].cntTo.toString();
      String destination = codes.containsKey(s_list[index].cntTo.toString())
          ? codes[s_list[index].cntTo.toString()]['code'].toString()
          : 'Unknown';

      /// 도착지 코드
      String flightNumber = s_list[index].pairing.toString();
      String stal = s_list[index].staL.toString();
      String stab = s_list[index].staB.toString();
      String stdl = s_list[index].stdL.toString();
      String stdb = s_list[index].stdB.toString();
      String activity = s_list[index].activity.toString();
      int id = s_list[index].id;
      String ci = s_list[index].ci.toString();
      String co = s_list[index].co.toString();
      String lat = codes.containsKey(destinationShort)
          ? codes[destinationShort]["lat"].toString()
          : 'unknown';

      /// 도착지 위도
      String lon = codes.containsKey(destinationShort)
          ? codes[destinationShort]["lon"].toString()
          : 'unknown';

      /// 도착지 경도

      return FlightData(
        departureShort: departureShort,
        departure: departure,
        date: date,
        destinationShort: destinationShort,
        destination: destination,
        flightNumber: flightNumber,
        stal: stal,
        stab: stab,
        stdl: stdl,
        stdb: stdb,
        activity: activity,
        id: id,
        ci: ci,
        co: co,
        lat: lat,
        lon: lon,
      );
    });
  }
}

void _showErrorDialog(BuildContext context, String message) {
  showDialog(
    context: context,
    builder: (BuildContext context) {
      return AlertDialog(
        title: const Text("Error!"),
        content: Text(message),
        actions: <Widget>[
          TextButton(
            child: const Text("OK"),
            onPressed: () {
              Navigator.of(context).pop(); // Close the dialog

              // Navigate to HomePage with a custom transition
              Navigator.of(context).pushAndRemoveUntil(
                PageRouteBuilder(
                  pageBuilder: (context, animation, secondaryAnimation) =>
                      LoginPage(),
                  transitionDuration: const Duration(milliseconds: 500),
                  transitionsBuilder:
                      (context, animation, secondaryAnimation, child) {
                    var begin = Offset(1.0, 0.0); // Transition from right
                    var end = Offset.zero;
                    var curve = Curves.ease;
                    var tween = Tween(begin: begin, end: end)
                        .chain(CurveTween(curve: curve));
                    var offsetAnimation = animation.drive(tween);
                    return SlideTransition(
                      position: offsetAnimation,
                      child: child,
                    );
                  },
                ),
                (Route<dynamic> route) => false,
              );
            },
          ),
        ],
      );
    },
  );
}
