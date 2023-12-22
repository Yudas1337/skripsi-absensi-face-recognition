<?php

namespace App\Helpers;

class UserHelper
{
    /**
     * Handle get user role
     *
     * @return string
     */

    public static function getUserRole(): string
    {
        return auth()->user()->roles->pluck('name')[0];
    }
}
