<?php

namespace App\Http\Controllers\Api;

use App\Contracts\Interfaces\EmployeeInterface;
use App\Contracts\Interfaces\FaceInterface;
use App\Http\Controllers\Controller;
use App\Http\Requests\FaceRequest;
use App\Http\Resources\EmployeeFaceResource;
use App\Services\FaceService;
use Illuminate\Http\JsonResponse;

class FaceController extends Controller
{

    private FaceService $service;
    private EmployeeInterface $employee;
    private FaceInterface $face;
    public function __construct(FaceInterface $faceInterface, EmployeeInterface $employeeInterface, FaceService $faceService)
    {
        $this->face = $faceInterface;
        $this->service = $faceService;
        $this->employee = $employeeInterface;
    }

    /**
     * index
     *
     * @return JsonResponse
     */
    public function index(): JsonResponse
    {
        $employees = $this->employee->get();
        return response()->json(['result' => EmployeeFaceResource::collection($employees)]);
    }

    public function store(FaceRequest $request): JsonResponse
    {
        $data = $this->service->store($request);
        foreach ($data['photo'] as $photo) {
            $this->face->store([
                'employee_id' => $data['employee_id'],
                'photo' => $photo,
            ]);
        }
        return response()->json(['message' => 'Berhasil menambah', 'status' => 200,]);
    }
}
