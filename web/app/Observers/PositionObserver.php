<?php

namespace App\Observers;

use App\Models\Position;
use Faker\Provider\Uuid;

class PositionObserver
{
    /**
     * Handle the Position "created" event.
     */
    public function creating(Position $position): void
    {
        $position->id = Uuid::uuid();
    }

    /**
     * Handle the Position "updated" event.
     */
    public function updated(Position $position): void
    {
        //
    }

    /**
     * Handle the Position "deleted" event.
     */
    public function deleted(Position $position): void
    {
        //
    }

    /**
     * Handle the Position "restored" event.
     */
    public function restored(Position $position): void
    {
        //
    }

    /**
     * Handle the Position "force deleted" event.
     */
    public function forceDeleted(Position $position): void
    {
        //
    }
}
