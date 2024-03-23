class User {
  String userid;
  String password;
  String auth='';

  User({
    required this.userid,
    required this.password,
  });

  Map<String, dynamic> toJson() {
    return {
      'userid': userid,
      'password': password,
    };
  }
}
