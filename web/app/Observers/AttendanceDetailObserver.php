<?php

namespace App\Observers;

use App\Models\attendace_detail;
use Faker\Provider\Uuid;

class AttendanceDetailObserver
{
    /**
     * creating
     *
     * @param  mixed $attendance_detail
     * @return void
     */
    public function creating(attendace_detail $attendance_detail): void
    {
        $attendance_detail->id = Uuid::uuid();
    }
}
