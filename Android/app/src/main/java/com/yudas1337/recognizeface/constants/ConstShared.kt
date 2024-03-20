package com.yudas1337.recognizeface.constants

object ConstShared {

    const val fileName = "Hummatech_Pref"

    // Serialized data will be stored ( in app's private storage ) with this filename.
    const val SERIALIZED_DATA_FILENAME = "image_data"

    // Shared Pref key to check if the data was stored.
    const val SHARED_PREF_IS_DATA_STORED_KEY = "is_data_stored"

    // shared pref for total employees
    const val TOTAL_EMPLOYEES = "totalEmployees"

    // shared pref for total students
    const val TOTAL_STUDENTS = "totalStudents"

    // shared pref for total schedules
    const val TOTAL_SCHEDULES = "totalSchedules"

    // shared pref for limit
    const val TOTAL_LIMIT = "totalLimit"

    // shared pref for extracted faces
    const val TOTAL_EXTRACTED_FACES = "totalExtractedFaces"

    // shared pref for employee list faces
    const val TOTAL_EMPLOYEE_FACES = "totalEmployeeFaces"

    // shared pref for md5 sum data
    const val MD5_STUDENTS = "md5Students"
    const val MD5_EMPLOYEES = "md5Employees"
    const val MD5_SCHEDULES = "md5Schedules"
    const val MD5_LIMIT = "md5Limit"
    const val MD5_STUDENT_FACES = "md5StudentFaces"
    const val MD5_EMPLOYEE_FACES = "md5EmployeeFaces"

    // shared pref for md5 sync data
    const val FETCH_MD5_STUDENTS = "fetchMd5Students"
    const val FETCH_MD5_EMPLOYEES = "fetchMd5Employees"
    const val FETCH_MD5_SCHEDULES = "fetchMd5Schedules"
    const val FETCH_MD5_LIMIT = "fetchMd5Limit"
    const val FETCH_MD5_STUDENT_FACES = "fetchMd5StudentFaces"
    const val FETCH_MD5_EMPLOYEE_FACES = "fetchMd5EmployeeFaces"

}