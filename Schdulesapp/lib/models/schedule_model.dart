class ScheduleModel {
  final int id;
  final String? date;
  final String? pairing;

  final String? dc;
  final String? ci;
  final String? co;

  final String? activity;

  final String? cntFrom;
  final String? stdL;
  final String? stdB;

  final String? cntTo;
  final String? staL;
  final String? staB;

  final String? achotel;

  final String? blk;

  ScheduleModel(
      {
        required this.id,
        required this.date,
        required this.pairing,
        required this.dc,
        required this.ci,
        required this.co,
        required this.activity,
        required this.cntFrom,
        required this.staL,
        required this.staB,
        required this.stdB,
        required this.stdL,
        required this.achotel,
        required this.blk,
        required this.cntTo});

  factory ScheduleModel.fromJson(Map<String, dynamic> json) {
    return ScheduleModel(
      id: json['id'],
        date: json['date'],
        pairing: json['pairing'],
        dc: json['dc'],
        ci: json['ci'],
        co: json['co'],
        activity: json['activity'],
        cntFrom: json['cntFrom'],
        staL: json['staL'],
        staB: json['staB'],
        stdB: json['stdB'],
        stdL: json['stdL'],
        achotel: json['achotel'],
        blk: json['blk'],
        cntTo: json['cntTo']);
  }
}