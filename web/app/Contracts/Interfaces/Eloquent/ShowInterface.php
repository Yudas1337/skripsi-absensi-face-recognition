<?php

namespace App\Contracts\Interfaces\Eloquent;

interface ShowInterface
{
    /**
     * show
     *
     * @param  mixed $id
     * @return mixed
     */
    public function show(mixed $id): mixed;
}
