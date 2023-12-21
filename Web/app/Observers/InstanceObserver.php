<?php

namespace App\Observers;

use App\Models\Instance;
use Faker\Provider\Uuid;

class InstanceObserver
{
    /**
     * Handle the Instance "creating" event.
     */
    public function creating(Instance $instance): void
    {
        $instance->id = Uuid::uuid();
    }

}
