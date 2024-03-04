<?php

namespace App\Contracts\Interfaces\Eloquent;

use Illuminate\Http\Request;

interface SearchInterface {
    /**
     * search
     *
     * @param  mixed $request
     * @return mixed
     */
    public function search(Request $request): mixed;
}
