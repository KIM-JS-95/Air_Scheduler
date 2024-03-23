import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:schdulesapp/utils/hard_coded_data.dart';

import 'main_pages/login_page.dart';
import 'models/UserProvider.dart';

void main() async {
  HardCodedData.generateHardCodedData();
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider(
      create: (context) => UserProvider(),
      child: MaterialApp(
        title: 'Flight Booking UI Concept',
        home: LoginPage(),
      ),
    );
  }
}