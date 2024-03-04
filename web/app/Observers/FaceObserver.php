<?php

namespace App\Observers;

use App\Models\Face;
use Faker\Provider\Uuid;

class FaceObserver
{
    /**
     * Handle the Face "creating" event.
     */
    public function creating(Face $face): void
    {
        $face->id = Uuid::uuid();
    }

    /**
     * Handle the Face "created" event.
     */
    public function created(Face $face): void
    {
        //
    }

    /**
     * Handle the Face "updated" event.
     */
    public function updated(Face $face): void
    {
        //
    }

    /**
     * Handle the Face "deleted" event.
     */
    public function deleted(Face $face): void
    {
        //
    }

    /**
     * Handle the Face "restored" event.
     */
    public function restored(Face $face): void
    {
        //
    }

    /**
     * Handle the Face "force deleted" event.
     */
    public function forceDeleted(Face $face): void
    {
        //
    }
}
