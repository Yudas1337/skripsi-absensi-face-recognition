<?php

namespace App\Observers;

use App\Models\attendance;
use Faker\Provider\Uuid;

class AttendanceObserver
{
    /**
     * creating
     *
     * @param  mixed $attendance
     * @return void
     */
    public function creating(attendance $attendance): void
    {
        $attendance->id = Uuid::uuid();
    }
}
