<?php

namespace App\Contracts\Interfaces\Eloquent;

interface VerifiedInterface
{
    /**
     * Handle verified data event to models.
     *
     * @return mixed
     */

    public function verified(): mixed;
}
