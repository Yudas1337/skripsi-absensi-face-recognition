import cv2
import face_recognition

# Baca gambar
image_path = 'path/to/your/image.jpg'
image = cv2.imread(image_path)

# Ubah gambar menjadi RGB (face_recognition memerlukan format ini)
rgb_image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)

# Deteksi wajah dalam gambar
face_locations = face_recognition.face_locations(rgb_image)

# Loop melalui setiap wajah yang terdeteksi
for face_location in face_locations:
    top, right, bottom, left = face_location
    
    # Gambar kotak pembatas di sekitar wajah
    cv2.rectangle(image, (left, top), (right, bottom), (0, 255, 0), 2)

# Tampilkan gambar hasil dengan wajah yang terdeteksi
cv2.imshow('Face Detection', image)
cv2.waitKey(0)
cv2.destroyAllWindows()