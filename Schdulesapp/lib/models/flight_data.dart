
class FlightData {
  final String departureShort;
  final String departure;
  final String date;
  final String destinationShort;
  final String destination;
  final String flightNumber;
  final String stdl;
  final String stdb;
  final int id;
  final String activity;
  final String ci;
  final String co;
  final String stal;
  final String stab;

  final String lat;
  final String lon;

  FlightData(
      {required this.departureShort,
      required this.departure,
      required this.date,
      required this.destinationShort,
      required this.destination,
      required this.flightNumber,
      required this.stdl,
      required this.stdb,
      required this.stal,
      required this.stab,
      required this.id,
      required this.activity,
      required this.co,
      required this.ci,
      required this.lat,
      required this.lon});

  FlightData copyWith({
    String? departureShort,
    String? departure,
    String? date,
    String? destinationShort,
    String? destination,
    String? flightNumber,
    String? duration,
    String? stdl,
    String? stdb,
    String? stal,
    String? stab,
    int? id,
    String? activity,
    String? ci,
    String? co,
    String? lat,
    String? lon,
  }) =>
      FlightData(
          departureShort: departureShort ?? this.departureShort,
          departure: departure ?? this.departure,
          date: date ?? this.date,
          destinationShort: destinationShort ?? this.destinationShort,
          destination: destination ?? this.destination,
          flightNumber: flightNumber ?? this.flightNumber,
          stdl: stdl ?? this.stdl,
          stdb: stdb ?? this.stdb,
          stal: stal ?? this.stal,
          stab: stab ?? this.stab,
          id: id ?? this.id,
          activity: activity ?? this.activity,
          ci: ci ?? this.ci,
          co: co ?? this.co,
          lat: lat ?? this.lat,
          lon: lon ?? this.lon
      );
}
