<?php

namespace App\Services;

use App\Enums\UploadDiskEnum;
use App\Http\Requests\EmployeeRequest;
use App\Http\Requests\EmployeeUpdateRequest;
use App\Models\Employee;
use App\Traits\UploadTrait;

class EmployeeService {

    use UploadTrait;

    /**
     * store
     *
     * @param  mixed $request
     * @return array
     */
    public function store(EmployeeRequest $request): array
    {
        $data = $request->validated();
        if ($request->has('photo')) {
            $data['photo'] = $request->file('photo')->store(UploadDiskEnum::PROFILE->value, 'public');
        }
        return $data;
    }

    /**
     * update
     *
     * @param  mixed $request
     * @param  mixed $employee
     * @return array
     */
    public function update(EmployeeUpdateRequest $request, Employee $employee): array
    {
        $data = $request->validated();
        if ($request->has('photo')) {
            $this->remove($employee->photo);
            $data['photo'] = $request->file('photo')->store(UploadDiskEnum::PROFILE->value, 'public');
        }
        return $data;
    }
}
