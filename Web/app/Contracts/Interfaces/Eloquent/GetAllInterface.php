<?php

namespace App\Contracts\Interfaces\Eloquent;

interface GetAllInterface
{
    /**
     * Handle the Get all data event from models.
     *
     * @return mixed
     */

    public function getAll(): mixed;
}
