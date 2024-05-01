import 'package:flutter/material.dart';
import 'package:schdulesapp/ajax/schedule_repository.dart';

import '../models/User.dart';
import '../models/flight_data.dart';
import '../sub_pages/today_flight.dart';

class HomeFlightPage extends StatefulWidget {
  final User user;

  const HomeFlightPage({
    super.key,
    required this.user,
  });

  @override
  State<StatefulWidget> createState() => _HomeFlightPage();
}

class _HomeFlightPage extends State<HomeFlightPage> {
  DateTime selectedDate = DateTime.utc(
    // ➋ 선택된 날짜를 관리할 변수
    DateTime.now().year,
    DateTime.now().month,
    DateTime.now().day,
  );

  /// 좌우 슬라이드로 당일 일전 표시하기
  @override
  Widget build(BuildContext context) {
    return FutureBuilder<List<FlightData>>(
      future: ScheduleRepository.gettodayschedule(widget.user.auth, context),
      builder: (context, snapshot) {
        if (snapshot.connectionState == ConnectionState.waiting) {
          // Show UI when data is loading
          return Center(
            child: CircularProgressIndicator(),
          );
        } else {
          if (snapshot.hasData && snapshot.data!.isNotEmpty) {
            return ListView.builder(
              itemCount: snapshot.data!.length,
              itemBuilder: (context, index) =>
                  TodayFlight(flightData: snapshot.data![index]),
            );
          } else if (!snapshot.hasData) {
            return Center(child: Text('DB에 등록된 일정이 없어요! \n 일정을 등록해 주세요!'));
          } else {
            return Center(
              child: Text(snapshot.hasError.toString()),
            );
          }
        }
      },
    );
  }
}
