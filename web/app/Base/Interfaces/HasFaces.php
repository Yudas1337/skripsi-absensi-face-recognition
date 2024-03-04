<?php

namespace App\Base\Interfaces;

use Illuminate\Database\Eloquent\Relations\HasMany;

interface HasFaces {
    /**
     * faces
     *
     * @return HasMany
     */
    public function faces(): HasMany;
}
