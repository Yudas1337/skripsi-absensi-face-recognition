<?php

namespace App\Contracts\Interfaces\Eloquent;

use Illuminate\Http\Request;

interface SearchInterface
{
    /**
     * Handle restore data instantly from models.
     *
     * @param Request $request
     *
     * @return mixed
     */

    public function search(Request $request): mixed;
}
