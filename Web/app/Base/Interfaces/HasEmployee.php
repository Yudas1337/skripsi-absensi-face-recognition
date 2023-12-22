<?php

namespace App\Base\Interfaces;

use Illuminate\Database\Eloquent\Relations\HasOne;

interface HasEmployee
{
    /**
     * One-to-One relationship with Employee Model
     *
     * @return HasOne
     */

    public function employee(): HasOne;
}
