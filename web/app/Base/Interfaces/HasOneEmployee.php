<?php

namespace App\Base\Interfaces;

use Illuminate\Database\Eloquent\Relations\HasOne;

interface HasOneEmployee {

    /**
     * employee
     *
     * @return HasOne
     */
    public function employee(): HasOne;
}
