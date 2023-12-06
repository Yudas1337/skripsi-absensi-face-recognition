import face_recognition
from flask import Flask, jsonify, request

app = Flask(__name__)

# Route untuk meng-handle request POST yang mengirimkan gambar
@app.route('/predict', methods=['POST'])
def predict():
    if 'image' not in request.files:
        return jsonify({'error': 'No file part'})

    file = request.files['image']

    if file.filename == '':
        return jsonify({'error': 'No selected file'})

    if file:        
        picture_of_me = face_recognition.load_image_file("images/yudas/yudas.jpg")
        my_face_encoding = face_recognition.face_encodings(picture_of_me)[0]

        unknown_picture = face_recognition.load_image_file(file)
        
        unknown_face_encoding = face_recognition.face_encodings(unknown_picture)[0]
        
        results = face_recognition.compare_faces([my_face_encoding], unknown_face_encoding)

        if results[0] == True:
            code = 200
            message = 'Wajah cocok'
        else:
            code = 404
            message = 'Wajah tidak cocok atau tidak ditemukan'
            
        return jsonify({
            'message': message,
            'code': code
            })
    
if __name__ == '__main__':
    app.run(host='11.11.2.11', port=5000)