<?php

namespace App\Exports;

use Carbon\Carbon;
use App\Models\Attendance;
use Maatwebsite\Excel\Concerns\WithTitle;
use Maatwebsite\Excel\Concerns\Exportable;
use Maatwebsite\Excel\Concerns\WithStyles;
use PhpOffice\PhpSpreadsheet\Style\Border;
use Maatwebsite\Excel\Concerns\WithHeadings;
use PhpOffice\PhpSpreadsheet\Cell\Coordinate;
use PhpOffice\PhpSpreadsheet\Style\Alignment;
use Maatwebsite\Excel\Concerns\FromCollection;
use Maatwebsite\Excel\Concerns\ShouldAutoSize;
use PhpOffice\PhpSpreadsheet\Worksheet\Worksheet;
use Maatwebsite\Excel\Concerns\WithMultipleSheets;

class AbsensiExport implements FromCollection, WithHeadings, WithTitle, ShouldAutoSize, WithStyles
{
    private $year;
    private $month;

    public function __construct(int $year, int $month)
    {
        $this->year = $year;
        $this->month = $month;
    }


    public function headings(): array
    {
        $headings = [
            ['No', 'Siswa', 'Sekolah'],
            ['', '', '']
        ];

        $daysInMonth = cal_days_in_month(CAL_GREGORIAN, $this->month, $this->year);

        // Append days as subheaders under the "Tanggal" header
        for ($day = 1; $day <= $daysInMonth; $day++) {
            $headings[0][] = 'Tanggal';
            $headings[1][] = $day;
        }

        // Append the totals headers with a preceding merged cell header
        $totalsHeaders = ['Masuk', 'Alpha', 'Izin', 'Sakit'];
        foreach ($totalsHeaders as $header) {
            $headings[0][] = 'Jumlah';
            $headings[1][] = $header;
        };

        return $headings;
    }

    public function collection()
    {
        $attendances = Attendance::with('employe', 'detailAttendances')
            ->whereYear('created_at', $this->year)
            ->whereMonth('created_at', $this->month)
            ->get()
            ->groupBy('employee_id');

        $data = [];
        $no = 1;

        foreach ($attendances as $studentId => $attendanceRecords) {
            $row = $this->prepareAttendanceRow($attendanceRecords, $no++);
            $data[] = $row;
        }

        return collect($data);
    }

    private function prepareAttendanceRow($attendanceRecords, $rowNumber)
    {
        $firstRecord = $attendanceRecords->first();
        $daysInMonth = cal_days_in_month(CAL_GREGORIAN, $this->month, $this->year);
        $row = [
            $rowNumber,
            $firstRecord->employe->name,
            $firstRecord->employe->sekolah,
        ];

        for ($day = 1; $day <= $daysInMonth; $day++) {
            $status = $this->getAttendanceStatusForDay($attendanceRecords, $day);
            $row[] = $status;
        }

        $this->appendTotalColumns($row, $attendanceRecords);

        return $row;
    }

    private function getAttendanceStatusForDay($attendanceRecords, $day)
    {
        $date = Carbon::createFromDate($this->year, $this->month, $day)->startOfDay();
        $weekendDays = [Carbon::SATURDAY, Carbon::SUNDAY];
        $publicHolidays = [/* an array containing the dates of public holidays in 'Y-m-d' format */];

        if (in_array($date->format('Y-m-d'), $publicHolidays) || in_array($date->dayOfWeek, $weekendDays)) {
            return 'libur';
        }

        $attendanceForDay = $attendanceRecords->first(function ($attendance) use ($date) {
            return $attendance->created_at->startOfDay()->equalTo($date);
        });

        if ($date->isFuture()) {
            return '';
        }

        return $attendanceForDay ? $attendanceForDay->status : 'alpha';
    }

    private function appendTotalColumns(&$row, $attendanceRecords)
    {
        $row[] = $attendanceRecords->where('status', 'masuk')->count();
        $row[] = collect($row)->filter(function ($status) {
            return $status === 'alpha';
        })->count();
        $row[] = $attendanceRecords->where('status', 'izin')->count();
        $row[] = $attendanceRecords->where('status', 'sakit')->count();
    }

    public function styles(Worksheet $sheet)
    {
        $highestRow = $sheet->getHighestRow();
        $highestColumn = $sheet->getHighestColumn();

        $sheet->getStyle("A1:{$highestColumn}{$highestRow}")->getAlignment()
            ->setHorizontal(Alignment::HORIZONTAL_CENTER)
            ->setVertical(Alignment::VERTICAL_CENTER);

        $sheet->getStyle("A1:{$highestColumn}{$highestRow}")->applyFromArray([
            'borders' => [
                'allBorders' => [
                    'borderStyle' => Border::BORDER_THIN,
                    'color' => ['argb' => '000000'],
                ],
            ],
        ]);

        $this->applyConditionalStyles($sheet, $highestRow, $highestColumn);
        $this->mergeHeaderCells($sheet);
    }

    private function applyConditionalStyles(Worksheet $sheet, int $highestRow, string $highestColumn): void
    {
        for ($row = 2; $row <= $highestRow; $row++) {
            for ($col = 'B'; $col !== $highestColumn; $col++) {
                $cellValue = $sheet->getCell($col . $row)->getValue();
                $fillColor = $this->getFillColorByStatus($cellValue);
                if ($fillColor) {
                    $sheet->getStyle($col . $row)->getFill()
                        ->setFillType(\PhpOffice\PhpSpreadsheet\Style\Fill::FILL_SOLID)
                        ->getStartColor()->setARGB($fillColor);
                }
            }
        }
    }

    private function getFillColorByStatus(?string $status): ?string
    {
        return match ($status) {
            'masuk' => 'd2f0e3', // bg-success equivalent color
            'sakit', 'izin' => 'fcedd4', // bg-warning equivalent color
            'alpha' => 'fedbda', // bg-danger equivalent color
            default => null
        };
    }

    private function mergeHeaderCells(Worksheet $sheet): void
    {
        $daysInMonth = cal_days_in_month(CAL_GREGORIAN, $this->month, $this->year);

        $mergeRanges = [
            'A1:A2',
            'B1:B2',
            'C1:C2',
            "D1:" . Coordinate::stringFromColumnIndex(3 + $daysInMonth) . '1',
        ];

        // Merge "Jumlah" cells from center to right
        $jumlahStartColumnIndex = 4 + $daysInMonth;
        $jumlahHeadersCount = 4; // Assuming there are 4 headers for 'Masuk', 'Alpha', 'Izin', 'Sakit'
        $jumlahEndColumnIndex = $jumlahStartColumnIndex + $jumlahHeadersCount - 1;
        $sheet->mergeCells(Coordinate::stringFromColumnIndex($jumlahStartColumnIndex) . '1:' . Coordinate::stringFromColumnIndex($jumlahEndColumnIndex) . '1');
        $sheet->getStyle(Coordinate::stringFromColumnIndex($jumlahStartColumnIndex) . '1')->getAlignment()->setHorizontal(Alignment::HORIZONTAL_CENTER);

        foreach ($mergeRanges as $range) {
            $sheet->mergeCells($range);
            $sheet->getStyle($range)->getAlignment()->setHorizontal(Alignment::HORIZONTAL_CENTER);
        }
    }

    public function title(): string
    {
        return date('F Y', mktime(0, 0, 0, $this->month, 1, $this->year));
    }
}
