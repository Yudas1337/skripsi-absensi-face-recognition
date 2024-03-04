<?php

namespace App\Base\Interfaces;

use Illuminate\Database\Eloquent\Relations\HasMany;

interface HasAttendances
{
    /**
     * attendances
     *
     * @return HasMany
     */
    public function attendances(): HasMany;
}
