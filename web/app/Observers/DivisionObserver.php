<?php

namespace App\Observers;

use App\Models\division;
use Faker\Provider\Uuid;

class DivisionObserver
{
    /**
     * Handle the Classification "created" event.
     */
    public function creating(division $Division): void
    {
        $Division->id = Uuid::uuid();
    }
}
