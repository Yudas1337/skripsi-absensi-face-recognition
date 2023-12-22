<?php

namespace App\Contracts\Interfaces\Eloquent;

interface UnverifiedInterface
{
    /**
     * Handle unverified data event to models.
     *
     * @return mixed
     */

    public function unverified(): mixed;
}
