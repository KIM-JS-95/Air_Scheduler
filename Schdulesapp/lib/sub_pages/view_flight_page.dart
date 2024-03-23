import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:schdulesapp/ajax/schedule_repository.dart';

import '../models/UserProvider.dart';
import '../models/flight_data.dart';
import '../utils/r.dart';
import '../widgets/fading_item_list/fading_item_list_controller.dart';
import '../widgets/weather_widget.dart';

class ViewFlightPage extends StatefulWidget {
  final FlightData flightData;

  const ViewFlightPage({
    required this.flightData,
    super.key
  });

  @override
  State<StatefulWidget> createState() => _ViewFlightPage();

}

class _ViewFlightPage extends State<ViewFlightPage> {
  late final FadingItemListController fadingItemListController;
  DateTime currentDate = DateTime.now();

  @override
  Widget build(BuildContext context) {
    UserProvider userProvider = Provider.of<UserProvider>(context);
    return FutureBuilder<List<FlightData>>(
      future: ScheduleRepository.getScheduleByDate(currentDate, userProvider.user.auth),
      builder: (context, snapshot) {
        if (snapshot.connectionState == ConnectionState.waiting) {
          // 데이터 로딩 중인 경우에 보여줄 UI
          return Center(child: CircularProgressIndicator());
        } else if (snapshot.hasError) {
          // 에러 발생 시에 보여줄 UI
          return Text('Error: ${snapshot.error}');
        } else {
          return SingleChildScrollView(
            child: Padding(
              padding: const EdgeInsets.symmetric(horizontal: 32.0),
              child: Column(
                children: [
                  const SizedBox(height: 15.0),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    crossAxisAlignment: CrossAxisAlignment.end,
                    children: [
                      _buildFirstColumn(),
                      _buildSecondColumn("flight_takeoff"),
                      _buildThirdColumn(),
                    ],
                  ),
                  const SizedBox(height: 10.0),
                  Divider(color: R.secondaryColor),
                  const SizedBox(height: 10.0),
                  WeatherWidget(lat: widget.flightData.lat, lon: widget.flightData.lon),
                ],
              ),
            ),
          );
        }
      },
    );
  }

  Widget _buildFirstColumn() {
    final firstColumn = [
      Text(widget.flightData.departureShort,
        style: TextStyle(
          color: R.secondaryColor,
          fontSize: 32,
        ),
      ),
      const SizedBox(height: 8.0,),
      Text(widget.flightData.departure,
        style: const TextStyle(
          color: Colors.white,
          fontWeight: FontWeight.bold,
        ),
      )
    ];
    final secondColumn = [
      const Text(
        "FLIGHT DATE",
        style: TextStyle(
          color: Colors.white,
        ),
      ),
      const SizedBox(
        height: 8.0,
      ),
      Text(
        widget.flightData.date,
        style: const TextStyle(
          color: Colors.white,
          fontWeight: FontWeight.bold,
        ),
      )
    ];
    final thirdColumn = [
      const Text("BOARDING TIME",
        style: TextStyle(
          color: Colors.white,
        ),
      ),
      const SizedBox(height: 8.0),
      Text('STD(L): ${widget.flightData.stdl}',
        style: const TextStyle(
          color: Colors.white,
          fontWeight: FontWeight.bold,
        ),
      ),
      SizedBox(height: 5),
      Text('STD(B): ${widget.flightData.stdb}',
        style: const TextStyle(
          color: Colors.white,
          fontWeight: FontWeight.bold,
        ),
      )
    ];
    return Column(
      mainAxisSize: MainAxisSize.min,
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        ...firstColumn,
        const SizedBox(
          height: 40.0,
        ),
        ...secondColumn,
        const SizedBox(height: 32.0),
        ...thirdColumn,
      ],
    );
  }

  Widget _buildSecondColumn(String statue) {
    final firstColumn = [

      /// 출발도착 아이콘
      Icon(
        (statue == "flight_takeoff") ? Icons.flight_takeoff : Icons.flight_land,
        color: R.secondaryColor,
        size: 32.0,
      ),

      const SizedBox(height: 8.0),

      Text(widget.flightData.flightNumber,

        /// Pairing
        style: const TextStyle(
          color: Colors.white, fontWeight: FontWeight.bold,),
      ),
    ];
    final secondColumn = [
      const Text("C/I(L)", style: TextStyle(color: Colors.white,),
      ),
      const SizedBox(height: 8.0),
      Text(widget.flightData.ci,
        style: const TextStyle(
          color: Colors.white,
          fontWeight: FontWeight.bold,
        ),
      )
    ];

    final thirdColumn = [
      const Text("C/O(L)",
        style: TextStyle(color: Colors.white,
        ),
      ),
      const SizedBox(height: 8.0),

      Text(widget.flightData.co,
        style: const TextStyle(color: Colors.white, fontWeight: FontWeight.bold,
        ),
      )
    ];
    return Column(
      mainAxisSize: MainAxisSize.min,
      crossAxisAlignment: CrossAxisAlignment.center,
      children: [
        ...firstColumn,
        const SizedBox(height: 40.0,),
        ...secondColumn,
        const SizedBox(height: 32.0),
        ...thirdColumn,
      ],
    );
  }

  Widget _buildThirdColumn() {
    final firstColumn = [
      Text(
        widget.flightData.destinationShort,
        style: TextStyle(
          color: R.secondaryColor,
          fontSize: 32,
        ),
      ),
      const SizedBox(
        height: 8.0,
      ),
      Text(
        widget.flightData.destination,
        style: const TextStyle(
          color: Colors.white,
          fontWeight: FontWeight.bold,
        ),
      ),
    ];
    final secondColumn = [
      const Text(
        "FLIGHT NO",
        style: TextStyle(
          color: Colors.white,
        ),
      ),
      const SizedBox(
        height: 8.0,
      ),
      Text(
        widget.flightData.flightNumber,
        style: const TextStyle(
          color: Colors.white,
          fontWeight: FontWeight.bold,
        ),
      )
    ];
    final thirdColumn = [
      const Text("BOARDING TIME",
        style: TextStyle(
          color: Colors.white,
        ),
      ),
      const SizedBox(height: 8.0),
      Text('STA(L): ${widget.flightData.stal}',
        style: const TextStyle(
          color: Colors.white,
          fontWeight: FontWeight.bold,
        ),
      ),
      SizedBox(height: 5),
      Text('STA(B): ${widget.flightData.stab}',
        style: const TextStyle(
          color: Colors.white,
          fontWeight: FontWeight.bold,
        ),
      )
    ];
    return Column(
      mainAxisSize: MainAxisSize.min,
      crossAxisAlignment: CrossAxisAlignment.end,
      children: [
        ...firstColumn,
        const SizedBox(
          height: 40.0,
        ),
        ...secondColumn,
        const SizedBox(height: 32.0),
        ...thirdColumn,
      ],
    );
  }

}