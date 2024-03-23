import 'package:flutter/material.dart';
import 'package:schdulesapp/sub_pages/view_flight.dart';

import '../models/flight_data.dart';
import '../utils/r.dart';

class FlightsListItemWidget extends StatelessWidget {
  final FlightData flightData;


  const FlightsListItemWidget({
    Key? key,
    required this.flightData,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) => GestureDetector(
    onTap: () {
      // Navigate to the target page on tap
      Navigator.push(
        context,
        MaterialPageRoute(builder: (context) => ViewFlight(flightData:flightData)),
      );
    },
    child: Padding(
      padding: const EdgeInsets.only(
        left: 32.0,
        top: 32.0,
        right: 32.0,
        bottom: 0.0,
      ),
      child: Column(
        children: [
          Row(
            children: [
              Expanded(
                child: _buildLeftColumn(),
              ),
              Expanded(
                child: _buildMiddlePart(),
              ),
              Expanded(
                child: _buildRightColumn(),
              ),
            ],
          ),
          SizedBox(height: 32.0),
        ],
      ),
    ),
  );

  Widget _buildLeftColumn() => Column(
    crossAxisAlignment: CrossAxisAlignment.start,
    children: [
      Text(
        flightData.departureShort,
        style: TextStyle(color: R.secondaryColor, fontSize: 32.0),
      ),
      const SizedBox(
        height: 6.0,
      ),
      Text(
        flightData.departure,
        style: const TextStyle(
          color: Colors.white,
          fontWeight: FontWeight.bold,
          fontSize: 12.0,
        ),
      ),
      const SizedBox(
        height: 24.0,
      ),
      Text(
        "DATE",
        style: TextStyle(color: R.tertiaryColor),
      ),
      const SizedBox(
        height: 6.0,
      ),
      Text(
        "${flightData.date} ${flightData.stdl}",
        style: const TextStyle(
          color: Colors.white,
          fontWeight: FontWeight.bold,
          fontSize: 12.0,
        ),
      ),
    ],
  );

  Widget _buildRightColumn() => Column(
    crossAxisAlignment: CrossAxisAlignment.end,
    children: [
      Text(
        flightData.destinationShort,
        style: TextStyle(color: R.secondaryColor, fontSize: 32.0),
      ),
      const SizedBox(
        height: 6.0,
      ),
      Text(
        flightData.destination,
        style: const TextStyle(
          color: Colors.white,
          fontWeight: FontWeight.bold,
          fontSize: 12.0,
        ),
      ),
      const SizedBox(
        height: 24.0,
      ),
      Text(
        "FLIGHT NO",
        style: TextStyle(color: R.tertiaryColor),
      ),
      const SizedBox(
        height: 6.0,
      ),
      Text(
        "${flightData.date} ${flightData.stdb}",
        style: const TextStyle(
          color: Colors.white,
          fontWeight: FontWeight.bold,
          fontSize: 12.0,
        ),
      ),
    ],
  );

  Widget _buildMiddlePart() => Column(
    children: [
      Icon(
        Icons.flight_takeoff,
        color: R.secondaryColor,
      ),
      Text(
        "FLIGHT NO",
        style: TextStyle(color: R.tertiaryColor),
      ),
      const SizedBox(
        height: 6.0,
      ),
      Text(
        flightData.flightNumber == "null"
            ? flightData.activity.toString()
            : flightData.flightNumber.toString(),
        style: const TextStyle(
          color: Colors.white,
          fontWeight: FontWeight.w600,
        ),
      )
    ],
  );
}
