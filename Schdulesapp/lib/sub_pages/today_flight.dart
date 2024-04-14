/*
* title:
* contents:
*
* */
import 'package:flutter/material.dart';
import 'package:schdulesapp/widgets/weather_widget.dart';
import '../models/flight_data.dart';
import '../utils/r.dart';

class TodayFlight extends StatelessWidget {
  final Function(bool)? isSelectionCompleted;
  final FlightData flightData;

  const TodayFlight({
    super.key,
    required this.flightData,
    this.isSelectionCompleted,
  });

  @override
  Widget build(BuildContext context) {

    return flightData != null
        ? Padding(
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
                const SizedBox(height: 5.0),
                Divider(
                  color: R.secondaryColor,
                ),
                const SizedBox(height: 5.0),
                WeatherWidget(lat: flightData.lat, lon: flightData.lon),
              ],
            ),
          )
        : Padding(
            padding: EdgeInsets.symmetric(horizontal: 32.0),
            child: Text(
              'No flight data available',
              style: TextStyle(
                color: R.secondaryColor,
                fontSize: 16.0,
                fontWeight: FontWeight.bold,
              ),
            ),
          );
  }

  Widget _buildFirstColumn() {
    final firstColumn = [
      Text(
        flightData.departureShort,
        style: TextStyle(
          color: R.secondaryColor,
          fontSize: 32,
        ),
      ),
      const SizedBox(
        height: 8.0,
      ),
      Text(
        flightData.departure,
        style:  TextStyle(
          color: R.secondaryColor,
          fontWeight: FontWeight.bold,
        ),
      )
    ];
    final secondColumn = [
       Text(
        "FLIGHT DATE",
        style: TextStyle(
          color: R.secondaryColor,
        ),
      ),
      const SizedBox(
        height: 8.0,
      ),
      Text(
        flightData.date,
        style:  TextStyle(
          color: R.secondaryColor,
          fontWeight: FontWeight.bold,
        ),
      )
    ];
    final thirdColumn = [
       Text(
        "BOARDING TIME",
        style: TextStyle(
          color: R.secondaryColor,
        ),
      ),
      const SizedBox(height: 8.0),
      Text(
        'STD(L): ${flightData.stdl}',
        style:  TextStyle(
          color: R.secondaryColor,
          fontWeight: FontWeight.bold,
        ),
      ),
      SizedBox(height: 5),
      Text(
        'STD(B): ${flightData.stdb}',
        style:  TextStyle(
          color: R.secondaryColor,
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

      Text(
        flightData.flightNumber,
        style: TextStyle(
          color: R.secondaryColor,
          fontWeight: FontWeight.bold,
        ),
      ),
    ];
    final secondColumn = [
       Text(
        "C/I(L)",
        style: TextStyle(
          color: R.secondaryColor,
        ),
      ),
      const SizedBox(height: 8.0),
      Text(
        flightData.ci,
        style: TextStyle(
          color: R.secondaryColor,
          fontWeight: FontWeight.bold,
        ),
      )
    ];

    final thirdColumn = [
       Text(
        "C/O(L)",
        style: TextStyle(
          color: R.secondaryColor,
        ),
      ),
      const SizedBox(height: 8.0),
      Text(
        flightData.co,
        style: TextStyle(
          color: R.secondaryColor,
          fontWeight: FontWeight.bold,
        ),
      )
    ];
    return Column(
      mainAxisSize: MainAxisSize.min,
      crossAxisAlignment: CrossAxisAlignment.center,
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

  Widget _buildThirdColumn() {
    final firstColumn = [
      Text(
        flightData.destinationShort,
        style: TextStyle(
          color: R.secondaryColor,
          fontSize: 32,
        ),
      ),
      const SizedBox(
        height: 8.0,
      ),
      Text(
        flightData.destination,
        style: TextStyle(
          color: R.secondaryColor,
          fontWeight: FontWeight.bold,
        ),
      ),
    ];
    final secondColumn = [
       Text(
        "FLIGHT NO",
        style: TextStyle(
          color: R.secondaryColor,
        ),
      ),
      const SizedBox(
        height: 8.0,
      ),
      Text(
        flightData.flightNumber,
        style:  TextStyle(
          color: R.secondaryColor,
          fontWeight: FontWeight.bold,
        ),
      )
    ];
    final thirdColumn = [
       Text(
        "BOARDING TIME",
        style: TextStyle(
          color: R.secondaryColor,
        ),
      ),
      const SizedBox(height: 8.0),
      Text(
        'STA(L): ${flightData.stal}',
        style:  TextStyle(
          color: R.secondaryColor,
          fontWeight: FontWeight.bold,
        ),
      ),
      SizedBox(height: 5),
      Text(
        'STA(B): ${flightData.stab}',
        style:  TextStyle(
          color: R.secondaryColor,
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
