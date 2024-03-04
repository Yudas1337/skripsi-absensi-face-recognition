<?php

namespace App\Http\Controllers;

use App\Contracts\Interfaces\EmployeeInterface;
use App\Contracts\Interfaces\UserInterface;
use App\Http\Controllers\Controller;
use App\Http\Requests\EmployeeRequest;
use App\Http\Requests\EmployeeUpdateRequest;
use App\Http\Resources\EmployeeResource;
use App\Models\Employee;
use App\Services\EmployeeService;
use Illuminate\Contracts\View\View;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\RedirectResponse;
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

    public function index(): View
    {
        $employees = $this->employee->get();
        return view('menu.pegawai', compact('employees'));
    }

    public function store(EmployeeRequest $request): RedirectResponse
    {
        $data = $this->service->store($request);
        $data['password'] = Hash::make("password");
        $data['user_id'] = $this->user->store($data)->id;
        $this->employee->store($data);
        return redirect()->back()->with('success', 'Berhasil menambah employee');
    }

    /**
     * show
     *
     * @param  mixed $employee
     * @return JsonResponse
     */
    public function show(Employee $employee): View
    {
        return view('', compact('employee'));
    }

    /**
     * update
     *
     * @param  mixed $employee
     * @param  mixed $request
     * @return JsonResponse
     */
    public function update(Employee $employee, EmployeeUpdateRequest $request): RedirectResponse
    {
        $data = $this->service->update($request, $employee);
        $this->employee->update($employee->id, $data);
        return redirect()->back()->with('success', 'Berhasil merubah employee');
    }

    /**
     * destroy
     *
     * @param  mixed $employee
     * @return JsonResponse
     */
    public function destroy(Employee $employee): RedirectResponse
    {
        if ($employee->photo != null) $this->service->remove($employee->photo);
        $this->user->delete($employee->user->id);
        return redirect()->back()->with('success', 'Berhasil menghapus employee');
    }
}
