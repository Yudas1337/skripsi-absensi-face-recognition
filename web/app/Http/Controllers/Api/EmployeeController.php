<?php

namespace App\Http\Controllers\Api;

use App\Contracts\Interfaces\EmployeeInterface;
use App\Contracts\Interfaces\UserInterface;
use App\Http\Controllers\Controller;
use App\Http\Requests\EmployeeRequest;
use App\Http\Requests\EmployeeUpdateRequest;
use App\Http\Resources\EmployeeResource;
use App\Models\Employee;
use App\Services\EmployeeService;
use Illuminate\Http\JsonResponse;
use Illuminate\Support\Facades\Hash;

class EmployeeController extends Controller
{
    private EmployeeInterface $employee;
    private EmployeeService $service;
    private UserInterface $user;
    public function __construct(EmployeeInterface $employee, UserInterface $userInterface, EmployeeService $employeeService)
    {
        $this->service = $employeeService;
        $this->user = $userInterface;
        $this->employee = $employee;
    }

    public function index(): JsonResponse
    {
        $employees = $this->employee->get();

        $response = [
            'total' => count($employees),
            'result' => EmployeeResource::collection($employees)
        ];

        return response()->json($response, 200);
    }

    public function store(EmployeeRequest $request): JsonResponse
    {
        $data = $this->service->store($request);
        $data['password'] = Hash::make("password");
        $data['user_id'] = $this->user->store($data)->id;
        $this->employee->store($data);
        return response()->json(['message' => 'Berhasil menambah pengguna'], 200);
    }

    /**
     * show
     *
     * @param  mixed $employee
     * @return JsonResponse
     */
    public function show(Employee $employee): JsonResponse
    {
        return response()->json(['result' => EmployeeResource::make($employee)], 200);
    }

    /**
     * update
     *
     * @param  mixed $employee
     * @param  mixed $request
     * @return JsonResponse
     */
    public function update(Employee $employee, EmployeeUpdateRequest $request): JsonResponse
    {
        $data = $this->service->update($request, $employee);
        $this->employee->update($employee->id, $data);
        return response()->json(['message' => 'Berhasil mengubah pengguna'], 200);
    }

    /**
     * destroy
     *
     * @param  mixed $employee
     * @return JsonResponse
     */
    public function destroy(Employee $employee): JsonResponse
    {
        if ($employee->photo != null) $this->service->remove($employee->photo);
        $this->user->delete($employee->user->id);
        return response()->json(['message' => 'Berhasil menghapus pengguna'], 200);
    }
}
