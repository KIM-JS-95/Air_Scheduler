import 'package:flutter/material.dart';
import '../models/TextFieldData.dart';
import 'r.dart';

class HardCodedData {
  static late final List<TextFieldData> _loginPageFieldsData;
  static late final List<TextFieldData> _modifyPageFieldsData;

  static List<TextFieldData> get loginPageFieldsData => _loginPageFieldsData;
  static List<TextFieldData> get modifyPageFieldsData => _modifyPageFieldsData;

  static generateHardCodedData() {
    _generateLoginPageFieldsData();
    _generatemodifyPageFieldsData();
  }


  static Future<void> _generateLoginPageFieldsData() async {
    _loginPageFieldsData = [
      TextFieldData(
        Icon(
          Icons.email,
          color: R.secondaryColor,
        ),
        "아이디",
        TextEditingController(),
        "001200",
      ),
      TextFieldData(
        Icon(
          Icons.password,
          color: R.secondaryColor,
        ),
        "비밀번호",
        TextEditingController(),
        "1234",
      ),
    ];
  }


  static Future<void> _generatemodifyPageFieldsData() async {
    _modifyPageFieldsData = [
      TextFieldData(
        Icon(
          Icons.email,
          color: R.secondaryColor,
        ),
        "아이디",
        TextEditingController(),
        "1",
      ),
      TextFieldData(
        Icon(
          Icons.email,
          color: R.secondaryColor,
        ),
        "이메일",
        TextEditingController(),
        "",
      ),
      TextFieldData(
        Icon(
          Icons.password,
          color: R.secondaryColor,
        ),
        "비밀번호",
        TextEditingController(),
        "1234",
      ),
    ];
  }

}
