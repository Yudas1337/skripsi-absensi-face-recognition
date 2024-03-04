@extends('layouts.app')
@section('content')
    <!-- start page title -->
    <div class="row">
        <div class="col-12">
            <div class="page-title-box d-sm-flex align-items-center justify-content-between">
                <h4 class="mb-sm-0 font-size-18">Dashboard</h4>
            </div>
        </div>
    </div>
    <!-- end page title -->

    <div class="row">
        <div class="col-xl-5">
            <div class="card">
                <div class="card-body">
                    <h4 class="card-title mb-4">Atur Jam Masuk Kantor</h4>
                    <ul class="nav nav-pills bg-light rounded" role="tablist">
                        <li class="nav-item">
                            <a class="nav-link active" data-bs-toggle="tab" href="#senin" role="tab">Senin</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" data-bs-toggle="tab" href="#selasa" role="tab">Selasa</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" data-bs-toggle="tab" href="#rabu" role="tab">Rabu</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" data-bs-toggle="tab" href="#kamis" role="tab">Kamis</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" data-bs-toggle="tab" href="#jumat" role="tab">Jumat</a>
                        </li>
                    </ul>
                    <div class="tab-content mt-4" id="jadwal">

                    </div>
                </div>
            </div>
        </div>
        <div class="col-xl-7">
            <div class="row">
                <div class="col">
                    <div class="card mini-stats-wid">
                        <div class="card-body">
                            <div class="d-flex">
                                <div class="flex-grow-1">
                                    <p class="text-muted fw-medium">Total Pegawai</p>
                                    <h4 class="mb-0">{{ $employees }}</h4>
                                </div>

                                <div class="flex-shrink-0 align-self-center">
                                    <div class="mini-stat-icon avatar-sm rounded-circle bg-primary">
                                        <span class="avatar-title">
                                            <i class="bx bx-happy-beaming font-size-24"></i>
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- end row -->
            {{-- <div class="row">
                <div class="col-12">
                    <div class="card">
                        <div class="card-body">
                            <h4 class="card-title mb-4">Pegawai Terlambat</h4>
                            <div data-simplebar style="max-height: 376px;">
                                <div class="vstack gap-4">
                                    <div class="table-responsive">
                                        <table class="table">
                                            <thead>
                                                <tr>
                                                    <th>
                                                        No
                                                    </th>
                                                    <th>
                                                        Nama
                                                    </th>
                                                    <th>
                                                        Sekolah
                                                    </th>
                                                    <th>
                                                        Tanggal
                                                    </th>
                                                    <th>
                                                        Jam Absensi
                                                    </th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <tr>
                                                    <td>
                                                        2
                                                    </td>
                                                    <td>
                                                        <div class="d-flex">
                                                            <div class="flex-shrink-0 me-2">
                                                                <div class="avatar-xs">
                                                                    <span
                                                                        class="avatar-title rounded-circle bg-success text-white font-size-16">
                                                                        A
                                                                    </span>
                                                                </div>
                                                            </div>
                                                            <div class="ms-2 flex-grow-1">
                                                                <h6 class="mb-1 font-size-15 mt-2">
                                                                    <p class="text-body">Abdul Kader</p>
                                                                </h6>
                                                            </div>
                                                        </div>
                                                    </td>
                                                    <td>
                                                        <h6 class="mb-1 font-size-15 mt-2">
                                                            <p class="text-body">Smkn 1 Kraksaan</p>
                                                        </h6>
                                                    </td>
                                                    <td>
                                                        <h6 class="mb-1 font-size-15 mt-2">
                                                            <p class="text-body">12 januari 2023</p>
                                                        </h6>
                                                    </td>
                                                    <td>
                                                        <h6 class="mb-1 font-size-15 mt-2">
                                                            <p class="text-body text-center">12:00</p>
                                                        </h6>
                                                    </td>
                                                </tr>

                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div> --}}
        </div>
    </div>
@endsection
@section('script')
    <script>
        get(1)
        function get(page) {
    const token = localStorage.getItem('token');
    $.ajax({
        url: "https://pkl.hummatech.com/api/entry-time",
        type: 'GET',
        dataType: "JSON",
        data: {
            pagination: $('#pagination-page').val(),
            school_year: $('#search-name').val(),
        },
        headers: {
            'Accept': 'application/json',
            'Authorization': 'Bearer ' + token,
        },
        beforeSend: function() {
            $('#jadwal').html('');
            $('#pagination').html('');
        },
        success: function(response) {
            // console.log(response)
            $('#jadwal').html('');
            $('#loading').html('');
            if (response.result.length > 0) {
                var days = ['senin', 'selasa', 'rabu', 'kamis', 'jumat'];
                $.each(response.result, function(index, item) {
                    var active = (index === 0) ? 'active' : ''; // Menandai tab pane pertama sebagai aktif
                    var tabId = days[index].toLowerCase(); // Mengkonversi nama hari menjadi lowercase
                    var row =
                        `<div class="tab-pane ${active}" id="${tabId}" role="tabpanel">
                            <div class="table-responsive" data-simplebar style="max-height: 330px;">
                                <form action="#" method="post">
                                    <input type="hidden" name="day" value="monday">
                                    @csrf
                                    @method('POST')
                                    <table class="table align-middle table-nowrap">
                                        <tbody>
                                            <tr>
                                                <td style="width: 50px;">
                                                    <div class="font-size-22 text-primary">
                                                        <i class="bx bx-time-five"></i>
                                                    </div>
                                                </td>

                                                <td>
                                                    <div>
                                                        <h5 class="font-size-14 mb-1">Masuk</h5>
                                                    </div>
                                                </td>
                                                <td>
                                                    <div>
                                                        <h5 class="font-size-14 mb-1">:</h5>
                                                    </div>
                                                </td>

                                                <td>
                                                    <div class="">
                                                        <input type="time" name="checkin_starts" class="form-control"
                                                            placeholder="Sampai" value="${item.checkin_starts}" disabled>
                                                    </div>
                                                </td>
                                                <td>
                                                    <div class="">
                                                        -
                                                    </div>
                                                </td>
                                                <td>
                                                    <div class="">
                                                        <input type="time" name="checkin_ends" class="form-control"
                                                            placeholder="Sampai" value="${item.checkin_ends}" disabled>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="width: 50px;">
                                                    <div class="font-size-22 text-primary">
                                                        <i class="bx bx-time-five"></i>
                                                    </div>
                                                </td>

                                                <td>
                                                    <div>
                                                        <h5 class="font-size-14 mb-1">Istirahat</h5>
                                                    </div>
                                                </td>
                                                <td>
                                                    <div>
                                                        <h5 class="font-size-14 mb-1">:</h5>
                                                    </div>
                                                </td>

                                                <td>
                                                    <div class="">
                                                        <input type="time" name="break_starts" class="form-control"
                                                            placeholder="Sampai" value="${item.break_starts}" disabled>
                                                    </div>
                                                </td>
                                                <td>
                                                    <div class="">
                                                        -
                                                    </div>
                                                </td>
                                                <td>
                                                    <div class="">
                                                        <input type="time" name="break_ends" class="form-control"
                                                            placeholder="Sampai" value="${item.break_ends}" disabled>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="width: 50px;">
                                                    <div class="font-size-22 text-primary">
                                                        <i class="bx bx-time-five"></i>
                                                    </div>
                                                </td>

                                                <td>
                                                    <div>
                                                        <h5 class="font-size-14 mb-1">Balik Istirahat</h5>
                                                    </div>
                                                </td>
                                                <td>
                                                    <div>
                                                        <h5 class="font-size-14 mb-1">:</h5>
                                                    </div>
                                                </td>

                                                <td>
                                                    <div class="">
                                                        <input type="time" name="return_starts" class="form-control"
                                                            placeholder="Sampai" value="${item.return_starts}" disabled>
                                                    </div>
                                                </td>
                                                <td>
                                                    <div class="">
                                                        -
                                                    </div>
                                                </td>
                                                <td>
                                                    <div class="">
                                                        <input type="time" name="return_ends" class="form-control"
                                                            placeholder="Sampai" value="${item.return_ends}" disabled>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="width: 50px;">
                                                    <div class="font-size-22 text-primary">
                                                        <i class="bx bx-time-five"></i>
                                                    </div>
                                                </td>
                                                <td>
                                                    <div>
                                                        <h5 class="font-size-14 mb-1">Pulang</h5>
                                                    </div>
                                                </td>
                                                <td>
                                                    <div>
                                                        <h5 class="font-size-14 mb-1">:</h5>
                                                    </div>
                                                </td>

                                                <td>
                                                    <div class="">
                                                        <input type="time" name="checkout_starts" class="form-control"
                                                            placeholder="Sampai" value="${item.checkout_starts}" disabled>
                                                    </div>
                                                </td>
                                                <td>
                                                    <div class="">
                                                        -
                                                    </div>
                                                </td>
                                                <td>
                                                    <div class="">
                                                        <input type="time" name="checkout_ends" class="form-control"
                                                            placeholder="Sampai" value="${item.checkout_ends}" disabled>
                                                    </div>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </form>
                            </div>
                        </div>`;
                    $('#jadwal').append(row);
                });

                $('.btn-create').click(function() {
                    $('#form-create').trigger('reset');
                    $('#modal-edit').modal('show');
                });
                $('.btn-edit').click(function() {
                    $('#form-update').data('id', $(this).data('id'));
                    $('.name').val($(this).data('name'));
                    $('#modal-edit').modal('show');
                });
                $('.btn-delete').click(function() {
                    $('#form-delete').data('id', $(this).data('id'));
                    $('#modal-delete').modal('show');
                });
            } else {
                $('#loading').html(showNoData('Tahun ajaran Tidak Ada!'));
            }
        }
    });
}
    </script>
@endsection
