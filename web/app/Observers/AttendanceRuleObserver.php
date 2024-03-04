<?php

namespace App\Observers;

use App\Models\AttendanceRule;
use Faker\Provider\Uuid;

class AttendanceRuleObserver
{
    /**
     * Handle the AttendanceRule "created" event.
     */
    public function creating(AttendanceRule $attendanceRule): void
    {
        $attendanceRule->id = Uuid::uuid();
    }
}
