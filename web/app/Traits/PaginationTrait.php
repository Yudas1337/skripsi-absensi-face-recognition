<?php

namespace App\Traits;

use Illuminate\Database\Eloquent\Collection;
use Illuminate\Pagination\LengthAwarePaginator;
use Illuminate\Pagination\Paginator;

trait PaginationTrait
{
    /**
     * paginate collection .
     *
     * @param mixed $items
     * @param int $perPage
     * @param mixed $page
     * @param array $options
     *
     * @return LengthAwarePaginator
     */

    public static function paginateCollection(mixed $items, int $perPage = 15, mixed $page = null, array $options = []): LengthAwarePaginator
    {
        $page = $page ?: (Paginator::resolveCurrentPage() ?: 1);
        $items = $items instanceof Collection ? $items : Collection::make($items);

        return new LengthAwarePaginator(array_values($items->forPage($page, $perPage)->toArray()), $items->count(), $perPage, $page, $options);
    }

    /**
     * custom paginate
     * 
     * @param int $currentPage
     * @param int $lastPage
     * 
     * @return mixed
     */
    public function customPaginate(int $currentPage,int $lastPage): mixed
    {
        return [
            'last_page' => $lastPage,
            'current_page' => $currentPage
        ];
    }

    

}
