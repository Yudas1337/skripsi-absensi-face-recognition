package com.yudas1337.recognizeface.helpers

import com.yudas1337.recognizeface.constants.FaceFolder
import java.io.File

class FaceHelper {
    companion object{

        private fun deleteFaceDirectory(facesDir: File){
            if(facesDir.exists() && facesDir.isDirectory) facesDir.deleteRecursively()
        }

        private fun deleteProfileDirectory(profilesDir: File){
           if(profilesDir.exists() && profilesDir.isDirectory) profilesDir.deleteRecursively()
        }

        fun initProfileDirectory(folderRole: String): Unit {
            val profilesDir = FaceFolder.profileDir
            val faces = File(profilesDir, folderRole)

            deleteProfileDirectory(faces)

            if(!profilesDir.exists() && !profilesDir.isDirectory){
                profilesDir.mkdir()
            }

            val roleDir = arrayOf(FaceFolder.EMPLOYEE_DIR_FACES_NAME, FaceFolder.STUDENTS_DIR_FACES_NAME)

            for(role in roleDir){
                val tmp = File(profilesDir, role)
                if(!tmp.exists() && !tmp.isDirectory) tmp.mkdir()
            }
        }

        fun initAttendanceFaceDirectory(folderRole: String) {
            val facesDir = FaceFolder.facesDir
            val faces = File(facesDir, folderRole)

           deleteFaceDirectory(faces)

            if(!facesDir.exists() && !facesDir.isDirectory){
                facesDir.mkdir()
            }

            val roleDir = arrayOf(FaceFolder.EMPLOYEE_DIR_FACES_NAME, FaceFolder.STUDENTS_DIR_FACES_NAME)

            for(role in roleDir){
                val tmp = File(facesDir, role)
                if(!tmp.exists() && !tmp.isDirectory) tmp.mkdir()
            }
        }

    }

}