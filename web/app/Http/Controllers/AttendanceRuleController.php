<?php

namespace App\Http\Controllers;

use App\Contracts\Interfaces\AttendanceRuleInterface;
use App\Http\Requests\AttendanceRuleRequest;
use App\Http\Resources\AttendanceRuleResource;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;

class AttendanceRuleController extends Controller
{
    private AttendanceRuleInterface $attendanceRule;

    public function __construct(AttendanceRuleInterface $attendanceRule)
    {
        $this->attendanceRule = $attendanceRule;
    }

    /**
     * index
     *
     * @return void
     */
    public function index(): JsonResponse
    {
        $attendanceRules = $this->attendanceRule->get();
        return response()->json([
            'result' => AttendanceRuleResource::collection($attendanceRules),
        ], 200);
    }

    /**
     * store
     *
     * @param  mixed $request
     * @return JsonResponse
     */
    public function store(AttendanceRuleRequest $request): JsonResponse
    {
        $this->attendanceRule->store($request->validated());
        return response()->json([
            'message' => 'Berhasil menambahkan data',
        ], 200);
    }
}
