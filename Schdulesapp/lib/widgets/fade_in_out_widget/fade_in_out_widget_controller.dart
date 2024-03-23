import 'package:flutter/material.dart';

class FadeInOutWidgetController {
  late ValueNotifier<bool> notifier;

  FadeInOutWidgetController() {
    notifier = ValueNotifier<bool>(false); // 초기화
  }

  void show() => notifier.value = true;
  void hide() => notifier.value = false;
}