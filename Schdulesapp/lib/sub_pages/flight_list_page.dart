/*
 title: FlightListPage.class
 contents: 한달간의 모든 일정을 리스트로 노출
*/

import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:schdulesapp/ajax/schedule_repository.dart';
import 'package:schdulesapp/sub_pages/today_flight.dart';

import '../models/UserProvider.dart';
import '../models/flight_data.dart';
import '../utils/r.dart';
import '../widgets/flights_list_item_widget.dart';

class FlightListPage extends StatefulWidget {
  const FlightListPage({
    Key? key,
  }) : super(key: key);

  @override
  State<FlightListPage> createState() => _FlightListPageState();
}

class _FlightListPageState extends State<FlightListPage> {
  late Future<List<FlightData>> flightDatas;

  @override
  void initState() {
    super.initState();
    flightDatas = _loadFlightData(); /// Assign the future to flightDatas
  }

  Future<List<FlightData>> _loadFlightData() async {
    UserProvider userProvider = Provider.of<UserProvider>(context, listen: false);
    try {
      return ScheduleRepository.getAllSchedules(userProvider.user.auth); // Return the future directly
    } catch (error) {
      // Handle error

      print('Error loading flight data: $error');
      return []; // Return an empty list in case of error
    }
  }

  @override
  Widget build(BuildContext context) => Column(
    mainAxisAlignment: MainAxisAlignment.center,
    children: [
      Expanded(
        child: FutureBuilder<List<FlightData>>(
          future: flightDatas, // Use the flightDatas future
          builder: (context, snapshot) {
            if (snapshot.connectionState == ConnectionState.waiting) {
              return const Center(
                child: CircularProgressIndicator(),
              );
            } else if (snapshot.hasError) {
                return Center(
                  child: Text(
                    'Error(E001): ${snapshot.error}',
                    style: TextStyle(
                      fontSize: 18,
                      color: Colors.black54,
                    ),
                  ),
                );
            } else if (!snapshot.hasData || snapshot.data!.isEmpty) {
                return const Center(
                  child: Text(
                    'No flight data available',
                    style: TextStyle(
                      fontSize: 18,
                      color: Colors.black54,
                    ),
                  ),
                );
            } else {
              return ListView.builder(
                itemCount: snapshot.data!.length,
                itemBuilder: (context, index) => Padding(
                  padding: const EdgeInsets.all(10.0),
                  child: ElevatedButton(
                    style: ElevatedButton.styleFrom(
                      backgroundColor: R.tertiaryColor.withOpacity(0.5),
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(20.0),
                        side: BorderSide(color: R.tertiaryColor),
                      ),
                    ),
                    onPressed: () {
                      TodayFlight(flightData: snapshot.data![index]);
                    },
                    child: FlightsListItemWidget(
                        flightData: snapshot.data![index]),
                  ),
                ),
              );
            }
          },
        ),
      ),
    ],
  );
}

