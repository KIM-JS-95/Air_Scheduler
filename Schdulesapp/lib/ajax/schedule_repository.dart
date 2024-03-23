import 'dart:convert';
import '../models/flight_data.dart';
import '../models/schedule_model.dart';
import '../utils/r.dart';
import 'package:http/http.dart' as http;

class ScheduleRepository {
  /// 일정 리스트
  static Future<List<FlightData>> getAllSchedules(String Authorization) async {
    Map<String, dynamic>? codes;

    final response = await http.get(
      Uri.parse('${R.back_addr}/showschedule'),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
        'Authorization': Authorization
      },
    );

    print(response.statusCode);
    if (response.statusCode == 200) {
      codes = await getNationCode(Authorization);

      List<dynamic> jsonList = json.decode(response.body);
      List<ScheduleModel> s_list = jsonList.map((json) => ScheduleModel.fromJson(json)).toList();
      return generateFlightsData(jsonList.length, s_list, codes!);
    }

    return generateFlightsData(0, [], codes!);
  }

  /// 날짜 선택
  static Future<List<FlightData>> getScheduleByDate(DateTime selectedDay, String Authorization) async {
    Map<String, dynamic>? codes;

    final response = await http.post(Uri.parse('${R.back_addr}/getschedule'),
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
          'Authorization': Authorization
        },
        //body: jsonEncode({'dateTime': selectedDay.toIso8601String()}),
        body: jsonEncode({'dateTime': '15Apr23'}));
    if (response.statusCode == 200) {
      codes = await getNationCode(Authorization);

      List<dynamic> jsonList = json.decode(response.body);
      List<ScheduleModel> s_list =
          jsonList.map((json) => ScheduleModel.fromJson(json)).toList();
      return generateFlightsData(jsonList.length, s_list, codes!);
    }
    return generateFlightsData(0, [], codes!);
  }

  /// 메인 페이지 당일 일정
  static Future<List<FlightData>> gettodayschedule(String Authorization) async {
    Map<String, dynamic>? codes;

    final response = await http.post(
      Uri.parse('${R.back_addr}/gettodayschedule'),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
        'Authorization': Authorization
      },
    );
    print(response.statusCode);
    if (response.statusCode == 200) {
      codes = await getNationCode(Authorization); // 비동기 함수 호출

      List<dynamic> jsonList = json.decode(response.body);
      List<ScheduleModel> s_list = jsonList.map((json) => ScheduleModel.fromJson(json)).toList();

      print(generateFlightsData(jsonList.length, s_list, codes!).toString());
      return generateFlightsData(jsonList.length, s_list, codes!);
    }
    return generateFlightsData(0, [], codes!);
  }

  static Future<Map<String, dynamic>> getNationCode(String Authorization) async {
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
  static List<FlightData> generateFlightsData(int cnt, List<ScheduleModel> s_list, Map<String, dynamic> codes) {
    return List.generate(
        cnt,
            (index) {
          String departureShort = s_list[index].cntFrom.toString();
          String departure = codes.containsKey(s_list[index].cntFrom.toString()) ? codes[s_list[index].cntFrom.toString()]['code'].toString() : 'Unknown'; /// 출발지 코드
          String date = s_list[index].date.toString();
          String destinationShort = s_list[index].cntTo.toString();
          String destination = codes.containsKey(s_list[index].cntTo.toString()) ? codes[s_list[index].cntTo.toString()]['code'].toString() : 'Unknown'; /// 도착지 코드
          String flightNumber = s_list[index].pairing.toString();
          String stal = s_list[index].staL.toString();
          String stab = s_list[index].staB.toString();
          String stdl = s_list[index].stdL.toString();
          String stdb = s_list[index].stdB.toString();
          String activity = s_list[index].activity.toString();
          int id = s_list[index].id;
          String ci = s_list[index].ci.toString();
          String co = s_list[index].co.toString();
          String lat = codes.containsKey(destinationShort) ? codes[destinationShort]["lat"].toString() : 'unknown'; /// 도착지 위도
          String lon = codes.containsKey(destinationShort) ? codes[destinationShort]["lon"].toString() : 'unknown'; /// 도착지 경도

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
        }
    );
  }
}
