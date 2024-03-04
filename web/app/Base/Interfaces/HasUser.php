<?php

namespace App\Base\Interfaces;

use Illuminate\Database\Eloquent\Relations\BelongsTo;

interface HasUser {
    /**
     * user
     *
     * @return BelongsTo
     */
    public function user(): BelongsTo;
}
