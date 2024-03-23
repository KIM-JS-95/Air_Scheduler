import 'package:flutter/material.dart';

import '../../main_pages/add_flight_page_controller.dart';
import '../../utils/r.dart';
import 'custom_tab_bar_controller.dart';

class CustomTabBar extends StatefulWidget {
  final Function(int)? onTabTap;
  final CustomTabBarController customTabBarController;

  const CustomTabBar({
    super.key,
    this.onTabTap,
    required this.customTabBarController,
  });

  @override
  State<CustomTabBar> createState() => _CustomTabBarState();
}

class _CustomTabBarState extends State<CustomTabBar>
    with SingleTickerProviderStateMixin {
  late final TabController _tabController;

  @override
  void initState() {
    super.initState();
    _tabController = TabController(length: 2, vsync: this);
    widget.customTabBarController.tabController = _tabController;
  }

  @override
  Widget build(BuildContext context) => Stack(
    alignment: Alignment.bottomCenter,
    children: [
      Positioned(
        bottom: 1.0,
        child: Container(
          height: 0.5,
          width: 500,
          color: R.tertiaryColor,
        ),
      ),
      TabBar(
        controller: _tabController,
        isScrollable: false,
        indicatorColor: R.secondaryColor,
        indicatorPadding: const EdgeInsets.symmetric(horizontal: 24.0),
        indicatorWeight: 3.0,
        labelColor: R.secondaryColor,
        unselectedLabelColor: R.tertiaryColor,
        splashFactory: NoSplash.splashFactory,
        onTap: (_) {
          // 클릭 기능을 제거하기 위해 아무 동작도 하지 않음
        },
        tabs: const [
          Tab(
            text: "날짜별",
          ),
          Tab(
            text: "전체",
          ),
        ],
      ),
    ],
  );
}


