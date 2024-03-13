import os

import cv2
import face_recognition

# Direktori gambar pegawai
nama_folder = "pegawai"
nama_orang = "femas"

IMAGES_PATH = os.path.join(nama_folder, nama_orang)

# Direktori tempat menyimpan wajah yang terdeteksi
save_folder = os.path.join(IMAGES_PATH, "cropped_face")

# Fungsi untuk mendeteksi dan melakukan cropping pada wajah dalam gambar
def crop_faces(image_path, save_folder):
    # Baca gambar
    image = cv2.imread(image_path)
    
    # Ubah gambar menjadi RGB (face_recognition memerlukan format ini)
    rgb_image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
    
    # Deteksi wajah dalam gambar
    face_locations = face_recognition.face_locations(rgb_image)
    
    # Loop melalui setiap wajah yang terdeteksi
    for i, face_location in enumerate(face_locations):
        top, right, bottom, left = face_location
        
        # Memperluas area yang dipotong
        width = right - left
        height = bottom - top
        expand_top = max(0, top - int(0.3 * height))
        expand_bottom = min(image.shape[0], bottom + int(0.3 * height))
        expand_left = max(0, left - int(0.3 * width))
        expand_right = min(image.shape[1], right + int(0.3 * width))
        
        # Crop wajah dari gambar
        face_image = image[expand_top:expand_bottom, expand_left:expand_right]
        
        # Simpan wajah yang terdeteksi di dalam folder cropped_face
        cropped_face_path = os.path.join(save_folder, f"{os.path.splitext(os.path.basename(image_path))[0]}_face_{i}.jpg")
        cv2.imwrite(cropped_face_path, face_image)

# Periksa apakah folder untuk menyimpan wajah yang terdeteksi sudah ada
if not os.path.exists(save_folder):
    os.makedirs(save_folder)

# Loop melalui setiap file gambar dalam folder pegawai
for filename in os.listdir(IMAGES_PATH):
    if filename.endswith(".jpg") or filename.endswith(".jpeg") or filename.endswith(".png"):
        image_path = os.path.join(IMAGES_PATH, filename)
        print(f"Processing image: {image_path}")
        crop_faces(image_path, save_folder)

print("Face cropping selesai.")
