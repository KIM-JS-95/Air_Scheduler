import 'package:flutter/cupertino.dart';

class TextFieldData {
  final Icon icon;
  final String label;
  final TextEditingController controller;
  final String text;

  TextFieldData(
    this.icon,
    this.label,
    this.controller,
    this.text,
  );
}