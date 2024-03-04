<?php

namespace App\Traits;

use Illuminate\Database\Eloquent\Builder;

trait ScopeSearchTrait
{
    /**
     * Scope a query to search with where
     *
     * @param mixed $query
     * @param mixed $column
     * @param mixed $value
     *
     * @return Builder
     */

    public function scopewhereLike(mixed $query, mixed $column, mixed $value): Builder
    {
        return $query->where($column, 'like', '%' . $value . '%');
    }
}
