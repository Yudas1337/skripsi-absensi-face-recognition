import os
import uuid

import cv2

nama_folder = "pegawai"
nama_orang = "femas"

IMAGES_PATH = os.path.join(nama_folder, nama_orang)

# Periksa apakah direktori sudah ada
if not os.path.exists(IMAGES_PATH):
    # Jika belum ada, buat direktori
    os.mkdir(IMAGES_PATH)

# rumus dijalankan 3x
# 1 : 10 (hadap depan)
# 2 : 10 (hadap kiri)
# 3 : 10 (hadap kanan)
number_images = 10

cap = cv2.VideoCapture(0)

frame_width = int(cap.get(cv2.CAP_PROP_FRAME_WIDTH))
frame_height = int(cap.get(cv2.CAP_PROP_FRAME_HEIGHT))
out = cv2.VideoWriter('output.avi', cv2.VideoWriter_fourcc(*'XVID'), 60, (frame_width, frame_height))

for imgnum in range(number_images):
    print('Collecting Image {}'.format(imgnum))
    ret, frame = cap.read()
    unique_id = uuid.uuid4()
    imgname = os.path.join(IMAGES_PATH, f'{imgnum}-{unique_id}.jpg')
    cv2.imwrite(imgname, frame)
    out.write(frame)  # Menyimpan frame ke video
    cv2.imshow('frame', frame)
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

cap.release()
out.release()
cv2.destroyAllWindows()