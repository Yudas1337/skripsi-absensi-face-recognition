<?php

namespace App\Base\Interfaces;

use Illuminate\Database\Eloquent\Relations\BelongsTo;

interface HasEmployee {
    /**
     * employee
     *
     * @return BelongsTo
     */
    public function employee(): BelongsTo;
}
