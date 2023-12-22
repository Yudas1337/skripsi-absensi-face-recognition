<?php

namespace App\Contracts\Interfaces\Eloquent;

interface UserTypeInterface
{
    /**
     * Handle Get User Type from models.
     *
     * @param string $user
     *
     * @return object
     */

    public function userType(string $user): object;
}
