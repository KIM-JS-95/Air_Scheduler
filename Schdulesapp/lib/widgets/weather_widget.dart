import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;

import '../utils/r.dart';

class WeatherWidget extends StatefulWidget {
  final String lat;
  final String lon;

  const WeatherWidget({Key? key, required this.lat, required this.lon})
      : super(key: key);

  @override
  _WeatherWidgetState createState() => _WeatherWidgetState();
}

class _WeatherWidgetState extends State<WeatherWidget> {
  late bool _isLoading = true;
  late Map<String, dynamic> _weatherInfo;
  late Map<String, dynamic> _main;
  late Map<String, dynamic> _wind;

  @override
  void initState() {
    super.initState();
    _fetchWeather();
  }

  @override
  Widget build(BuildContext context) {
    if (widget.lat == "unknown" || widget.lon == "unknown") {
      return Container(
        padding: EdgeInsets.all(16.0),
        child: Card(
          elevation: 4.0,
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(16.0),
          ),
          child: Padding(
            padding: EdgeInsets.all(16.0),
            child: Column(
              children: [
                Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Text(
                      "Weather: Unknown",
                      style: TextStyle(fontSize: 18.0),
                    ),
                    SizedBox(width: 10.0),
                    Icon(
                        Icons.device_unknown,
                        color: R.secondaryColor,
                        size: 32.0,
                      ),
                  ],
                ),
                SizedBox(height: 20.0),
                Text(
                  "Plz, check Weather Widget",
                  style: TextStyle(fontSize: 16.0),
                ),

              ],
            ),
          ),
        ),
      );
    } else {
      return _isLoading
          ? _buildLoadingScreen()
          : Container(
              padding: EdgeInsets.all(16.0),
              child: Card(
                elevation: 4.0,
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(16.0),
                ),
                child: Padding(
                  padding: EdgeInsets.all(16.0),
                  child: Column(
                    children: [
                      Row(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          Text(
                            "${_weatherInfo['description']}",
                            style: TextStyle(fontSize: 18.0),
                          ),
                        ],
                      ),
                      SizedBox(height: 10.0),
                      if (_weatherInfo['icon'].isNotEmpty)
                        Image.network(
                          "https://openweathermap.org/img/w/${_weatherInfo['icon']}.png",
                          width: 50.0,
                          height: 50.0,
                        ),
                      SizedBox(height: 20.0),
                      Text(
                        "Temperature:${_main['temp']}°C",
                        style: TextStyle(fontSize: 16.0),
                      ),
                      SizedBox(height: 10.0),
                      Text(
                        "Humidity: ${_main['humidity']}%",
                        style: TextStyle(fontSize: 16.0),
                      ),
                      SizedBox(height: 10.0),
                      Text(
                        "Wind Speed: ${_wind['speed']} m/s",
                        style: TextStyle(fontSize: 16.0),
                      ),
                    ],
                  ),
                ),
              ),
            );
    }
  }

  Future<void> _fetchWeather() async {
    if (widget.lat == "unknown" || widget.lon == "unknown") {
      setState(() {
        _isLoading = false;
      });
    } else {
      final apiKey = R.apiKey;
      final url = Uri.parse(
          'https://api.openweathermap.org/data/2.5/weather?lat=${widget.lat}&lon=${widget.lon}&appid=$apiKey&units=metric');
      try {
        final response = await http.get(url);

        if (response.statusCode == 200) {
          final Map<String, dynamic> data = json.decode(response.body);
          final List<dynamic> weather = data['weather'];
          setState(() {
            _main = data['main'];
            _wind = data['wind'];
            _weatherInfo = weather[0];
          });
        } else {
          print('Failed to load weather: ${response.statusCode}');
        }
      } catch (error) {
        print('Error: $error');
      } finally {
        setState(() {
          _isLoading = false; // Set loading to false after API call\
        });
      }
    }
  }

  Widget _buildLoadingScreen() {
    return Center(
      child: CircularProgressIndicator(),
    );
  }
}
