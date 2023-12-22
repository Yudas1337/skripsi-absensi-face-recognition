<?php

namespace App\Contracts\Interfaces\Eloquent;

interface DatatableInterface
{
    /**
     * Handle all data with datatables from models.
     *
     * @return mixed
     */

    public function datatable(): mixed;
}
