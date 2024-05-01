// 이미지 전송 비동기 전송
import 'package:http/http.dart' as http;
import 'package:image_picker/image_picker.dart';
import '../utils/r.dart';
import '../models/User.dart';


class Uploadajax {
  static Future<int?> uploadajax(User user) async {
    XFile? _pickedFile;

      _pickedFile = await ImagePicker().pickImage(source: ImageSource.gallery);
      if (_pickedFile == null) {
        print('No image picked.');
        return null;
      }

      final uri = Uri.parse(R.back_addr+"/upload");
      final request = http.MultipartRequest('POST', uri);
      request.files.add(await http.MultipartFile.fromPath('file', _pickedFile.path));
      request.headers.addAll(<String, String>{
        'Content-Type': 'multipart/form-data',
        'Authorization': user.auth,
      });

      final response = await request.send();
      return response.statusCode;
  }
}