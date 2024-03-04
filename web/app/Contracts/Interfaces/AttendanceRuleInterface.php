<?php

namespace App\Contracts\Interfaces;

use App\Contracts\Interfaces\Eloquent\GetInterface;
use App\Contracts\Interfaces\Eloquent\StoreInterface;

interface AttendanceRuleInterface extends GetInterface, StoreInterface
{
    /**
     * ruleToday
     *
     * @return mixed
     */
    public function ruleToday(): mixed;
}
