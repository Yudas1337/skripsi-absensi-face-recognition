<?php

namespace App\Http\Controllers;

use App\Contracts\Interfaces\AttendanceDetailInterface;
use App\Contracts\Interfaces\AttendanceInterface;
use App\Contracts\Interfaces\AttendanceRuleInterface;
use App\Contracts\Interfaces\EmployeeInterface;
use App\Exports\AbsensiExport;
use App\Http\Requests\StoreattendanceRequest;
use App\Http\Requests\UpdateattendanceRequest;
use App\Models\Attendance;
use App\Models\Employee;
use Illuminate\Http\Request;
use Maatwebsite\Excel\Facades\Excel;

class AttendanceController extends Controller
{
    private AttendanceInterface $attendance;
    private AttendanceDetailInterface $attendanceDetail;
    private EmployeeInterface $employee;
    private AttendanceRuleInterface $attendanceRule;
    // private AttendanceDetailInterface $attendanceDetail;
    public function __construct(AttendanceInterface $attendance, AttendanceDetailInterface $attendanceDetailInterface, EmployeeInterface $employeeInterface, AttendanceRuleInterface $attendanceRuleInterface)
    {
        $this->attendanceRule = $attendanceRuleInterface;
        $this->employee = $employeeInterface;
        $this->attendanceDetail = $attendanceDetailInterface;
        // $this->attendanceDetail = $attendanceDetailInterface;
        $this->attendance = $attendance;
    }

    /**
     * getAttendance
     *
     * @param  mixed $request
     * @return void
     */
    public function getAttendance(Request $request) {
        $date = now();
        if ($request->has('date')) {
            $date = $request->date;
        }
        $request->merge(['date' => $date]);
        $employees = $this->employee->search($request);

        $employes = Employee::all();
        // dd($employes);

        $attendanceYears = Attendance::query()
            ->selectRaw('YEAR(created_at) as year')
            ->groupBy('year')
            ->orderBy('year', 'desc')
            ->get();
            $attendanceMonth = Attendance::query()
            ->selectRaw('MONTH(created_at) as month')
            ->groupBy('month')
            ->orderBy('month', 'desc')
            ->get();
        $attendanceRule = $this->attendanceRule->ruleToday();
        return view('menu.absensi', compact('employees', 'attendanceRule','attendanceYears','attendanceMonth', 'employes'));
    }

    public function export_excel(Request $request)
    {
        $request->validate([
            'month' => 'min:1,max:12',
        ], [
            'month.max' => 'Batas bulan hanya 12',
        ]);
        $year = $request->input('year');
        $month = $request->input('month');
        return Excel::download(new AbsensiExport($year, $month), "absensi-{$year}-{$month}.xlsx");
    }
    /**
     * Display a listing of the resource.
     */
    public function index()
    {
        $url = "https://pkl.hummatech.com/api/entry-time";
        $response = file_get_contents($url);
        dd($response);
    }
    /**
     * Show the form for creating a new resource.
     */
    public function create()
    {
        //
    }

    /**
     * Store a newly created resource in storage.
     */
    public function store(Request $request)
    {
        $dataJson = $request->data;
        foreach ($dataJson as $data) {
            $dataAttendance['employee_id'] = $data['user_id'];
            $dataAttendance['status'] = $data['status'];
            $attendance = $this->attendance->store($dataAttendance);

            foreach ($data['detail_attendances'] as $detailAttendance) {
                $dataAttendanceDetail['attendance_id'] = $attendance->id;
                $dataAttendanceDetail['status'] = $detailAttendance['status'];
                $dataAttendanceDetail['created_at'] = $detailAttendance['created_at'];
                $dataAttendanceDetail['updated_at'] = $detailAttendance['updated_at'];
                $this->attendanceDetail->store($dataAttendanceDetail);
            }
            // $this->attendanceDetail->store($dataDetail);
        }
        return response()->json(['message' => 'Berhasil menambah absensi'], 200);
    }

    /**
     * Display the specified resource.
     */
    public function show(attendance $attendance)
    {
        //
    }

    /**
     * Show the form for editing the specified resource.
     */
    public function edit(attendance $attendance)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(UpdateattendanceRequest $request, attendance $attendance)
    {
        //
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy(attendance $attendance)
    {
        //
    }
}
