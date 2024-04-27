class User {
  String userid;
  String password;
  String auth='';
  String email='';

  User({
    required this.userid,
    required this.password,
    required this.email
  });

  Map<String, dynamic> toJson() {
    return {
      'userid': userid,
      'password': password,
      'email': email
    };
  }
}
