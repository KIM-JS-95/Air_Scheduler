import 'package:flutter/material.dart';
import 'package:schdulesapp/main_pages/home_page.dart';
import 'package:schdulesapp/sub_pages/user_modify.dart';
import 'package:schdulesapp/sub_pages/view_flight_page.dart';

import '../component/Schedule_import.dart';
import '../models/MainPageEnum.dart';
import '../models/flight_data.dart';
import '../utils/r.dart';
import '../widgets/fade_in_out_widget/fade_in_out_widget.dart';
import '../widgets/fade_in_out_widget/fade_in_out_widget_controller.dart';
import '../widgets/fading_item_list/fading_item_list_controller.dart';

class ViewFlight extends StatefulWidget {
  final FlightData flightData;

  const ViewFlight({
    Key? key,
    required this.flightData,
  }) : super(key: key);

  @override
  State<ViewFlight> createState() => _ViewFlightState();
}

class _ViewFlightState extends State<ViewFlight> with TickerProviderStateMixin {
  late final AnimationController _sheetAnimationController;
  late final FadingItemListController _fadingItemListController;
  late final FadeInOutWidgetController _sheetContentFadeInOutController;
  late final FadeInOutWidgetController _headerFadeInOutController;
  late final ValueNotifier<MainPageEnum> _currentMainPage;
  late final Map<MainPageEnum, Widget> pages;

  @override
  void initState() {
    super.initState();
    _initControllers();
  }

  void _initControllers() {
    _fadingItemListController = FadingItemListController();
    _sheetContentFadeInOutController = FadeInOutWidgetController();
    _headerFadeInOutController = FadeInOutWidgetController();
    _sheetAnimationController = AnimationController(
      vsync: this,
      duration: const Duration(
        milliseconds: 600,
      ),
    );

    _sheetAnimationController.forward().whenComplete(
          () {
        _headerFadeInOutController.show();
        //_fadingItemListController.showItems();
      },
    );
    _currentMainPage = ValueNotifier(MainPageEnum.myFlights);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SafeArea(
        bottom: false,
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 30.0),
              child: _buildHeader,
            ),
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 32.0),
              child: FadeInOutWidget(
                initialVisibilityValue: false,
                slideDuration: const Duration(milliseconds: 500),
                fadeInOutWidgetController: _headerFadeInOutController,
                child: ValueListenableBuilder<MainPageEnum>(
                  valueListenable: _currentMainPage,
                  builder: (_, value, __) => _myFlightsTextWidget,
                ),
              ),
            ),
            const SizedBox(
              height: 20.0,
            ),
            AnimatedBuilder(
              animation: _sheetAnimationController,
              builder: (context, child) => Container(
                height: (1 - _sheetAnimationController.value) * 600,
              ),
            ),
            Flexible(
              child: _buildBottomSheet,
            ),
          ],
        ),
      ),
    );}

  Text get _myFlightsTextWidget => Text(
    "My flight",
    style: TextStyle(
      color: R.primaryColor,
      fontSize: 30.0,
      fontWeight: FontWeight.w600,
    ),
  );

  Widget get _buildHeader {
    final AnimationController controller = AnimationController(
      vsync: this,
      duration: const Duration(milliseconds: 500),
    );

    return AnimatedBuilder(
      animation: controller,
      builder: (context, child) {
        return Container(
          // 배경색을 변경할 컨테이너 추가
          color: R.secondaryColor, // 원하는 배경색으로 변경
          child: Opacity(
            opacity: 1.0,
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                PopupMenuButton<int>(
                  icon: Icon(Icons.menu, color: R.primaryColor), // 메뉴 아이콘
                  onSelected: (item) {
                    switch (item) {
                      case 0:
                        Navigator.push(
                          context,
                          MaterialPageRoute(builder: (context) => Schedule_import()),
                        );
                        break;
                      case 1:
                      //다른 화면으로 이동하거나 다른 기능을 수행
                        Navigator.push(
                          context,
                          MaterialPageRoute(builder: (context) => UserModify()),
                        );
                        break;
                    }
                  },
                  itemBuilder: (context) => [
                    PopupMenuItem<int>(
                      value: 0,
                      child: Text('일정 등록'),
                    ),
                    PopupMenuItem<int>(
                      value: 0,
                      child: Text('일정 수정'),
                    ),
                    PopupMenuItem<int>(
                      value: 1,
                      child: Text('회원정보 수정'),
                    ),
                    // 여기에 더 많은 메뉴 아이템을 추가할 수 있습니다.
                  ],
                ),
                GestureDetector(
                  onTap: () {
                    Future.delayed(
                      const Duration(milliseconds: 500),
                          () => Navigator.push(
                        context,
                        PageRouteBuilder(
                          transitionDuration: const Duration(milliseconds: 500),
                          pageBuilder: (context, animation, secondaryAnimation) =>
                              HomePage(routeTransitionValue: animation),
                        ),
                      ),
                    );
                  },
                  child: Container(
                    height: 40.0,
                    width: 40.0,
                    decoration: BoxDecoration(
                      borderRadius: BorderRadius.circular(10.0),
                      color: R.primaryColor,
                    ),
                    child: Icon(
                      Icons.home,
                      color: R.secondaryColor,
                      size: 40.0,
                    ),
                  ),
                ),
              ],
            ),
          ),
        );
      },
    );
  }


  Widget get _buildBottomSheet => Container(
    height: double.infinity,
    width: double.infinity,
    decoration: BoxDecoration(
      color: R.primaryColor,
      borderRadius: const BorderRadius.vertical(
          top: Radius.circular(20), bottom: Radius.circular(20)),
    ),
    child: FadeInOutWidget(
      slideDuration: const Duration(milliseconds: 500),
      slideEndingOffset: const Offset(0, 0.1),
      fadeInOutWidgetController: _sheetContentFadeInOutController,
      child: ValueListenableBuilder<MainPageEnum>(
        valueListenable: _currentMainPage,
        builder: (_, value, __) => ViewFlightPage(flightData:widget.flightData),
      ),
    ),
  );

}
