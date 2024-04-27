import 'dart:convert';
import '../models/User.dart';
import '../utils/r.dart';
import 'package:http/http.dart' as http;

class UserRepository {
  static Future<Map<String, dynamic>> join(User user) async {
    final response = await http.post(
      Uri.parse(R.back_addr + '/join'),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
      },
      body: jsonEncode(user.toJson()),
    );

    if (response.statusCode == 201) {
      // 회원가입 성공
      return {'success': true, 'message': 'Join Success', 'token': response};
    } else {
      // 회원가입 실패
      return {
        'success': false,
        'message': 'Join Failed',
      };
    }
  }

  static Future<Map<String, dynamic>> login(User user) async {
    final response = await http.post(
      Uri.parse(R.back_addr + '/login'),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
      },
      body: jsonEncode(user.toJson()), // user 객체를 JSON으로 인코딩하여 전송
    );

    Map<String, String> responseHeaders = response.headers;

    if (response.statusCode == 200) {
      // 로그인 성공
      // 서버에서 반환된 데이터를 확인하고 필요에 따라 처리
      return {
        'success': true,
        'message': 'Login Success!',
        'token': responseHeaders['authorization'],
      };
    } else {
      // 로그인 실패
      return {
        'success': false,
        'message': 'Your not my User',
      };
    }
  }

  static Future<Map<String, dynamic>> logout(String Authorization) async {
    final response = await http.post(
      Uri.parse(R.back_addr + '/logout'),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
        'Authorization': Authorization
      },
    );

    if (response.statusCode == 200) {
      // 로그아웃 성공
      return {
        'success': true,
        'message': 'Logout Success',
      };
    } else {
      // 로그아웃 실패
      return {
        'success': false,
        'message': 'Logout Failed',
      };
    }
  }

  static Future<Map<String, dynamic>> getUserInfoByToken(
      String Authorization) async {
    final response = await http.post(
      Uri.parse('${R.back_addr}/getuserinfobyToken'),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
        'Authorization': Authorization,
      },
    );

    if (response.statusCode == 200) {
      // If the server returns a 200 OK response, parse the JSON
      Map<String, dynamic> jsonResponse = json.decode(response.body);

      return jsonResponse;
    } else {
      // If the server returns an error response, throw an exception
      throw Exception('Failed to load user information');
    }
  }

  static Future<Map<String, dynamic>> modify(User user,String Authorization) async {
    final response = await http.post(
      Uri.parse('${R.back_addr}/user_modify'),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
        'Authorization': Authorization
      },
      body: jsonEncode(user.toJson()),
    );
    if(response.statusCode == 200){
      Map<String, String> responseHeaders = response.headers;
      return {
        'success': true,
        'message': 'Login Success!',
        'token': responseHeaders['authorization'],
      };
    }else{
      return {
        'success': false,
        'message': 'update Failed',
      };
    }

    }
}
