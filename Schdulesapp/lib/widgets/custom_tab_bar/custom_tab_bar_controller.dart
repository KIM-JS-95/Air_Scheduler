import 'package:flutter/material.dart';

class CustomTabBarController {
  late TabController tabController;

  int get currentIndex => tabController.index;
  bool get isLastTab => currentIndex == tabController.length - 1;

  void nextTab() {
    final currentIndex = tabController.index;
    if (currentIndex < tabController.length - 1) {
      tabController.animateTo(currentIndex + 1);
    } else {
      tabController.animateTo(0);
    }
  }

  void previousTab() {
    final currentIndex = tabController.index;
    if (currentIndex > 0) {
      tabController.animateTo(currentIndex - 1);
    }
  }

  void closeTab() {
    final currentIndex = tabController.index;
    if (currentIndex > 0) {
      tabController.animateTo(currentIndex - 1);
    }
  }
}
