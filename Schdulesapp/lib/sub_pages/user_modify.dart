import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../ajax/user_repository.dart';
import '../main_pages/home_page.dart';
import '../models/User.dart';
import '../models/UserProvider.dart';
import '../utils/custom_flutter_logo.dart';
import '../utils/r.dart';
import '../widgets/custom_text_field.dart';
import '../widgets/fade_in_out_widget/fade_in_out_widget.dart';
import '../widgets/fade_in_out_widget/fade_in_out_widget_controller.dart';

class UserModify extends StatelessWidget {
  final FadeInOutWidgetController _fadeInOutWidgetController =
      FadeInOutWidgetController();
  final TextEditingController emailController = TextEditingController();
  final TextEditingController passwordController = TextEditingController();
  final TextEditingController useridController = TextEditingController();

  UserModify({Key? key});

  @override
  Widget build(BuildContext context) {
    return FutureBuilder<User>(
      future: _fillFields(context),
      builder: (context, snapshot) {
        if (snapshot.connectionState == ConnectionState.waiting) {
          return CircularProgressIndicator(); // Show loading indicator while fetching user data
        }
        if (snapshot.hasError) {
          return Text(
              'Error: ${snapshot.error}'); // Show error message if fetching fails
        }
        final user = snapshot.data!;

        return _buildUserModifyScreen(context, user);
      },
    );
  }

  Widget _buildUserModifyScreen(BuildContext context, User user) {
    const hSpace = SizedBox(height: 24.0);

    return Stack(
      children: [
        FadeInOutWidget(
          fadeInOutWidgetController: _fadeInOutWidgetController,
          slideEndingOffset: const Offset(0, 0.01),
          child: Scaffold(
            backgroundColor: R.primaryColor,
            body: Center(
              child: Padding(
                padding: const EdgeInsets.all(32.0),
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    const CustomFlutterLogo(
                      size: 100,
                    ),
                    hSpace,
                    _buildIdField(user.userid), // 사용자 ID
                    hSpace,
                    _buildEmailField(user.email), // 이메일
                    hSpace,
                    _buildPasswordField(user.password), // 비밀번호
                    hSpace,
                    _buildModifyButton(context),
                    hSpace,
                    IconButton(
                      icon: Icon(Icons.arrow_back),
                      onPressed: () {
                        Navigator.of(context).pop();
                      },
                    )
                  ],
                ),
              ),
            ),
          ),
        ),
      ],
    );
  }

  Widget _buildModifyButton(BuildContext context) => FilledButton(
      style: FilledButton.styleFrom(
        backgroundColor: R.secondaryColor,
        minimumSize: const Size(
          double.infinity,
          50,
        ),
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(20.0),
        ),
      ),
      onPressed: () async {
        String userid = useridController.text;
        String email = emailController.text;
        String password = passwordController.text;
        User user = User(userid: userid, password: password, email: email);

        UserProvider userProvider =
            Provider.of<UserProvider>(context, listen: false);
        Map<String, dynamic> userData =
            await UserRepository.modify(user, userProvider.user.auth);
        if (userData['success']) {
          user.auth = userData['token'];
          final userProvider =
              Provider.of<UserProvider>(context, listen: false);
          userProvider.setUser(user);
          _fadeInOutWidgetController.hide();
          Future.delayed(
            const Duration(milliseconds: 500),
            () => Navigator.push(
              context,
              PageRouteBuilder(
                transitionDuration: const Duration(milliseconds: 500),
                pageBuilder: (context, animation, secondaryAnimation) =>
                    HomePage(routeTransitionValue: animation),
              ),
            ),
          );
        }
      },
      child: Text(
        "수정하기",
        style: TextStyle(color: R.primaryColor, fontWeight: FontWeight.bold),
      ));

  Widget _buildEmailField(String email) {
    emailController.text = email; // Set the initial value
    return CustomTextField(
      controller: emailController,
      labelText: "이메일",
      prefixIcon: Icon(
        Icons.email,
        color: R.secondaryColor,
      ),
      mainColor: R.secondaryColor,
      secondaryColor: R.tertiaryColor,
    );
  }

  Widget _buildPasswordField(String password) {
    passwordController.text = password;
    return CustomTextField(
      controller: passwordController,
      labelText: "비밀번호",
      prefixIcon: Icon(
        Icons.lock,
        color: R.secondaryColor,
      ),
      mainColor: R.secondaryColor,
      secondaryColor: R.tertiaryColor,
    );
  }

  Widget _buildIdField(String userid) {
    useridController.text = userid;
    return CustomTextField(
      controller: useridController,
      labelText: "사용자 ID",
      prefixIcon: Icon(
        Icons.person,
        color: R.secondaryColor,
      ),
      mainColor: R.secondaryColor,
      secondaryColor: R.tertiaryColor,
    );
  }

  Future<User> _fillFields(BuildContext context) async {
    try {
      UserProvider userProvider =
          Provider.of<UserProvider>(context, listen: false);
      Map<String, dynamic> userData =
          await UserRepository.getUserInfoByToken(userProvider.user.auth);
      User user = User(
        userid: userData['userid'],
        email: userData['email'],
        password: userData['password'],
      );

      return user;
    } catch (e) {
      print("Error fetching user information: $e");
      // Handle errors or return a default user object
      return User(userid: '', password: '', email: '');
    }
  }
}
