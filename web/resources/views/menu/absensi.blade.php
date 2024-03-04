@extends('layouts.app')
@section('content')
    <div class="row">
        <div class="d-flex justify-content-between mb-3">
            <div class="page-title-box d-sm-flex align-items-center justify-content-between">
                <h4 class="mb-sm-0 font-size-18">Absensi Pegawai</h4>
            </div>

        </div>
    </div>
    <div class="card">
        <div class="card-body">

            <h4 class="card-title">Informasi</h4>
            <div class="row mt-3">
                <div class="col">
                    <div class="text-center alert alert-danger fs-6" role="alert">
                        Belum Hadir
                    </div>
                </div>
                <div class="col">
                    <div class="alert text-center alert-warning fs-6" role="alert">
                        Izin, Sakit, Telat,
                    </div>
                </div>
                <div class="col">
                    <div class="alert text-center alert-success fs-6" role="alert">
                        Sudah Absen
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="mb-3 w-25">

    </div>

    <!-- Export Excel Modal -->
    <div class="modal fade" id="exportExcelModal" tabindex="-1" aria-labelledby="exportExcelModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exportExcelModalLabel">Rekap Excel</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form action="{{ route('list.attendance.admin.export.excel') }}" method="get">
                    <div class="modal-body">
                        <div class="mb-3">
                            <label for="yearInput" class="form-label">Tahun</label>
                            <select class="form-select" id="yearInput" name="year">
                                @foreach ($attendanceYears as $year)
                                    <option
                                        value="{{ $year->year }}"{{ request()->input('year', now()->year) == $year->year ? ' selected' : '' }}>
                                        {{ $year->year }}
                                    </option>
                                @endforeach
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="monthInput" class="form-label">Bulan</label>
                            <select class="form-select" id="monthInput" name="month">
                                @foreach ($attendanceMonth as $month)
                                    <option
                                        value="{{ $month->month }}"{{ request()->input('month', now()->month) == $month->month ? ' selected' : '' }}>
                                        {{ $month->month }}
                                    </option>
                                @endforeach
                            </select>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="submit" class="btn btn-primary">Rekap</button>
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col">
            <div class="card">
                <div class="card-body">
                    <div class="d-flex justify-content-between">
                        <h4 class="card-title mb-4">Absensi</h4>
                        <div class="">
                            <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#exportExcelModal">Rekap
                                Excel</button>
                        </div>
                    </div>
                    <div data-simplebar style="max-height: 376px;">
                        <div class="vstack gap-4">
                            <div class="table-responsive">
                                <table id="example" class="table table-striped" style="width:100%">
                                    <thead>
                                        <tr>
                                            <th style="background-color: #1B3061" class="text-white">No</th>
                                            <th style="background-color: #1B3061" class="text-white">Pegawai</th>
                                            <th style="background-color: #1B3061" class="text-white">Keterangan</th>
                                            <th style="background-color: #1B3061" class="text-white">Masuk</th>
                                            <th style="background-color: #1B3061" class="text-white">Istirahat</th>
                                            <th style="background-color: #1B3061" class="text-white">Kembali</th>
                                            <th style="background-color: #1B3061" class="text-white">Pulang</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        @forelse ($employes as $employe)
                                        <tr>
                                            <td>{{ $loop->iteration }}</td>
                                            <td>{{ $employe->name }}</td>
                                            <td>
                                                @if (isset($employe->attendances[0]))
                                                    @if ($employe->attendances[0]->status == 'masuk')
                                                        <div class="btn btn-soft-success waves-effect waves-light">
                                                            {{ $employe->attendances[0]->status }}
                                                        </div>
                                                    @endif
                                                    @if ($employe->attendances[0]->status == 'izin')
                                                        <div class="btn btn-soft-warning waves-effect waves-light">
                                                            {{ $employe->attendances[0]->status }}
                                                        </div>
                                                    @endif
                                                    @if ($employe->attendances[0]->status == 'sakit')
                                                        <div class="btn btn-soft-warning waves-effect waves-light">
                                                            {{ $employe->attendances[0]->status }}
                                                        </div>
                                                    @endif
                                                    @if ($employe->attendances[0]->status == 'alpha')
                                                        <div
                                                            class="btn btn-soft-danger non-active waves-effect waves-light">
                                                            {{ $employe->attendances[0]->status }}
                                                        </div>
                                                    @endif
                                                @else
                                                    <div class="btn btn-soft-danger waves-effect waves-light">
                                                        @php
                                                            $waktuSaatIni = \Carbon\Carbon::now();
                                                            $waktuJamDelapan = \Carbon\Carbon::today()->setHour(8);
                                                        @endphp

                                                        @if ($waktuSaatIni->greaterThan($waktuJamDelapan))
                                                            Alpha
                                                        @else
                                                            Belum Hadir
                                                        @endif

                                                    </div>
                                                @endif
                                            </td>
                                            <td>
                                                @if (isset($employe->attendances[0]))
                                                    @foreach ($employe->attendances[0]->detailAttendances as $detailAttendance)
                                                        @if ($detailAttendance->status == 'present')
                                                            @if (date('H:i:s', strtotime($detailAttendance->created_at)) <=
                                                                    \Carbon\Carbon::createFromFormat('H:i:s', $attendanceRule?->checkin_ends ?? '08:00:00')->addMinutes(1)->format('H:i:s'))
                                                                <div
                                                                    class="btn btn-soft-success waves-effect waves-light">
                                                                    {{ date('H:i', strtotime($detailAttendance->created_at)) }}
                                                                </div>
                                                            @else
                                                                <div
                                                                    class="btn btn-soft-warning waves-effect waves-light">
                                                                    {{ date('H:i', strtotime($detailAttendance->created_at)) }}
                                                                </div>
                                                            @endif
                                                        @endif
                                                    @endforeach
                                                @endif
                                            </td>
                                            <td>
                                                @if (isset($employe->attendances[0]))
                                                    @foreach ($employe->attendances[0]->detailAttendances as $detailAttendance)
                                                        @if ($detailAttendance->status == 'break')
                                                            <div class="btn btn-soft-success waves-effect waves-light">
                                                                {{ date('H:i', strtotime($detailAttendance->created_at)) }}
                                                            </div>
                                                        @endif
                                                    @endforeach
                                                @endif
                                            </td>
                                            <td>
                                                @if (isset($employe->attendances[0]))
                                                    @foreach ($employe->attendances[0]->detailAttendances as $detailAttendance)
                                                        @if ($detailAttendance->status == 'return_break')
                                                            @if (date('H:i:s', strtotime($detailAttendance->created_at)) <=
                                                                    \Carbon\Carbon::createFromFormat('H:i:s', $attendanceRule?->return_ends ?? '13:00:00')->addMinutes(1)->format('H:i:s'))
                                                                <div
                                                                    class="btn btn-soft-success waves-effect waves-light">
                                                                    {{ date('H:i', strtotime($detailAttendance->created_at)) }}
                                                                </div>
                                                            @else
                                                                <div
                                                                    class="btn btn-soft-warning waves-effect waves-light">
                                                                    {{ date('H:i', strtotime($detailAttendance->created_at)) }}
                                                                </div>
                                                            @endif
                                                        @endif
                                                    @endforeach
                                                @endif
                                            </td>
                                            <td>
                                                @if (isset($employe->attendances[0]))
                                                    @foreach ($employe->attendances[0]->detailAttendances as $detailAttendance)
                                                        @if ($detailAttendance->status == 'return')
                                                            <div class="btn btn-soft-success waves-effect waves-light">
                                                                {{ date('H:i', strtotime($detailAttendance->created_at)) }}
                                                            </div>
                                                        @endif
                                                    @endforeach
                                                @endif
                                            </td>
                                        </tr>
                                        @empty
                                        <tr>
                                            <td colspan="9">
                                                <div class="d-flex justify-content-center">
                                                    <img src="{{ asset('nodata.png') }}"
                                                        width="300px
                                                "
                                                        alt="">
                                                </div>
                                                <p class="text-center fs-5 mt-4" style="font-weight:700">
                                                    Data Masih Kosong
                                                </p>
                                            </td>
                                        </tr>
                                        @endforelse
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
@endsection
