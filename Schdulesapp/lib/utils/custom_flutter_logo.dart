import 'package:flutter/material.dart';

class CustomFlutterLogo extends StatelessWidget {
  final double size;
  const CustomFlutterLogo({
    super.key,
    this.size = 40,
  });

  @override
  Widget build(BuildContext context)=> Image.asset(
    './assets/images/jeju_air_logo.png',
    width: size,
    height: size,
      );
}
