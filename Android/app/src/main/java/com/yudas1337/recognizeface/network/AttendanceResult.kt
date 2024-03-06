package com.yudas1337.recognizeface.network

class AttendanceResult {

    internal var id: String? = null
    internal var user_id: String? = null
    internal var status: String? = null
    internal var role: String? = null
    internal var created_at: String? = null
    internal var updated_at: String? = null

    internal var attendance_id: String? = null

    internal var faces: List<FaceList>? = null
}