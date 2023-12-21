<?php

namespace App\Observers;

use App\Models\Employee;
use Faker\Provider\Uuid;

class EmployeeObserver
{
    /**
     * Handle the Employee "creating" event.
     */
    public function creating(Employee $employee): void
    {
        $employee->id = Uuid::uuid();
    }

}
