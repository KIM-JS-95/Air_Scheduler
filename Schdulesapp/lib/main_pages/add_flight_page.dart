import 'package:flutter/material.dart';

import '../sub_pages/darte_flights_page.dart';
import '../sub_pages/flight_list_page.dart';
import '../widgets/custom_tab_bar/custom_tab_bar.dart';
import '../widgets/custom_tab_bar/custom_tab_bar_controller.dart';
import '../widgets/fade_in_out_widget/fade_in_out_widget.dart';
import '../widgets/fade_in_out_widget/fade_in_out_widget_controller.dart';
import 'add_flight_page_controller.dart';

class AddFlightPage extends StatefulWidget {
  final AddFlightPageController addFlightPageController;

  const AddFlightPage({
    super.key,
    required this.addFlightPageController,
  });

  @override
  State<AddFlightPage> createState() => _AddFlightPageState();
}

class _AddFlightPageState extends State<AddFlightPage> {
  late final List<Widget> _pages;
  late final ValueNotifier<int> _pageNotifier;
  late final CustomTabBarController _customTabBarController;
  late final FadeInOutWidgetController _fadeInOutWidgetController;

  @override
  void initState() {
    super.initState();
    _fadeInOutWidgetController = FadeInOutWidgetController();

    _pages = [DateFlightsPage(), FlightListPage()];  /// 날짜별 & 전체 리스트 페이지

    _pageNotifier = ValueNotifier(0);
    _customTabBarController = CustomTabBarController();

    widget.addFlightPageController.customTabBarController = _customTabBarController;
    widget.addFlightPageController.pageNotifier = _pageNotifier;
    widget.addFlightPageController.fadeInOutWidgetController = _fadeInOutWidgetController;
  }

  @override
  Widget build(BuildContext context) => Padding(
        padding: const EdgeInsets.only(top: 10.0),
        child: Column(
          children: [
            CustomTabBar(
              customTabBarController:
                  widget.addFlightPageController.customTabBarController,
            ),
            const SizedBox(
              height: 32.0,
            ),
            Expanded(
              child: FadeInOutWidget(
                slideDuration: const Duration(milliseconds: 500),
                slideEndingOffset: const Offset(0, 0.04),
                fadeInOutWidgetController: _fadeInOutWidgetController,
                child: ValueListenableBuilder(
                  valueListenable: widget.addFlightPageController.pageNotifier,
                  builder: (_, value, __) => _pages[value],
                ),
              ),
            ),
          ],
        ),
      );
}
