import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:schdulesapp/ajax/schedule_repository.dart';
import 'package:schdulesapp/sub_pages/today_flight.dart';

import '../models/UserProvider.dart';
import '../models/flight_data.dart';
import '../utils/r.dart';
import '../widgets/days_calendar_widget.dart';
import '../widgets/fading_item_list/fading_item_list.dart';
import '../widgets/fading_item_list/fading_item_list_controller.dart';
import '../widgets/flights_list_item_widget.dart';

/*
* title: AvailableFlightsPage.class
* contents: 달력 선택시 해당 날짜의 일정 노출
*
* */

class DateFlightsPage extends StatefulWidget {
  const DateFlightsPage({
    super.key,
  });

  @override
  State<DateFlightsPage> createState() => _DateFlightsPageState();
}

class _DateFlightsPageState extends State<DateFlightsPage> {
  late final FadingItemListController _fadingItemListController;
  late ValueNotifier<int> _selectedFlight;
  late UserProvider userProvider;

  DateTime selectedDate = DateTime.utc(
    // ➋ 선택된 날짜를 관리할 변수
    DateTime.now().year,
    DateTime.now().month,
    DateTime.now().day,
  );

  @override
  void initState() {
    super.initState();
    _fadingItemListController = FadingItemListController();
    _selectedFlight = ValueNotifier<int>(0);
    userProvider = Provider.of<UserProvider>(context, listen: false);
  }

  @override
  void dispose() {
    _selectedFlight.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) => Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          DaysCalendarWidget(onDayPressed: (DateTime selectedDay) {
            setState(() {
              this.selectedDate = selectedDay;
            });
            _fadingItemListController.showItems();
          }),
          const SizedBox(
            height: 10.0,
          ),
          Expanded(
            child: FutureBuilder<UserProvider>(
              future: Future.microtask(() => Provider.of<UserProvider>(context, listen: false)),
              builder: (context, userSnapshot) {
                if (userSnapshot.connectionState == ConnectionState.waiting) {
                  return Center(child: CircularProgressIndicator());
                } else if (userSnapshot.hasError) {
                  return Center(child: Text('Error: ${userSnapshot.error}'));
                } else {
                  return FutureBuilder<List<FlightData>>(
                    future: ScheduleRepository.getScheduleByDate(
                        selectedDate, userSnapshot.data!.user.auth),
                    builder: (context, snapshot) {
                      if (snapshot.connectionState == ConnectionState.waiting) {
                        return Center(child: CircularProgressIndicator());
                      } else if (snapshot.hasError) {
                        return Center(child: Text('Error: ${snapshot.error}'));
                      } else {
                        return ListView.builder(
                          itemCount: snapshot.data!.length,
                          itemBuilder: (context, index) => Padding(
                            padding: const EdgeInsets.all(10.0),
                            child: ElevatedButton(
                              style: ElevatedButton.styleFrom(
                                backgroundColor:
                                    R.tertiaryColor.withOpacity(0.5),
                                shape: RoundedRectangleBorder(
                                  borderRadius: BorderRadius.circular(20.0),
                                  side: BorderSide(color: R.tertiaryColor),
                                ),
                              ),
                              onPressed: () {
                                TodayFlight(flightData: snapshot.data![index]);
                              },
                              child: FlightsListItemWidget(
                                flightData: snapshot.data![index],
                              ),
                            ),
                          ),
                        );
                      }
                    },
                  );
                }
              },
            ),
          ),
        ],
      );
}
