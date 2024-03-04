@extends('layouts.app')
@section('content')
    <div class="row">
        <div class="col-12">
            <div class="page-title-box d-sm-flex align-items-center justify-content-between">
                <h4 class="mb-sm-0 font-size-18">Pegawai</h4>
            </div>
        </div>
    </div>
    {{-- modal  --}}
    <div class="row">
        <div class="col">
            <div class="card">
                <div class="card-body">
                    <div class="d-flex justify-content-between mb-3">
                        <h4 class="card-title mb-4">Pegawai</h4>
                        <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#myModal"
                            type="button">Tambah</button>
                    </div>
                    <div data-simplebar style="max-height: 376px;">
                        <div class="vstack gap-4">
                            <div class="table-responsive">
                                <table id="example" class="table table-striped" style="width:100%">
                                    <thead>
                                        <tr>
                                            <th style="background-color: #1B3061" class="text-white">No</th>
                                            <th style="background-color: #1B3061" class="text-white">Nama</th>
                                            <th style="background-color: #1B3061" class="text-white">Email</th>
                                            <th style="background-color: #1B3061" class="text-white">NIK</th>
                                            <th style="background-color: #1B3061" class="text-white">Jabatan</th>
                                            <th style="background-color: #1B3061" class="text-white">Rfid</th>
                                            <th style="background-color: #1B3061" class="text-white">Jenis Kelamin</th>
                                            <th style="background-color: #1B3061" class="text-white">Aksi</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        @forelse ($employees as $index=>$employes)
                                            <tr>
                                                <td>{{ $index + 1 }}</td>
                                                <td>{{ $employes->name }}</td>
                                                <td>{{ $employes->email }}</td>
                                                <td>{{ $employes->nik }}</td>
                                                <td>{{ $employes->position }}</td>
                                                <td>{{ $employes->rfid }}</td>
                                                <td>
                                                    @if ($employes->gender == 'male')
                                                        Laki-Laki
                                                    @else
                                                        Perempuan
                                                    @endif
                                                </td>
                                                <td>
                                                    <div class="d-flex gap-2">
                                                        {{-- <button class="btn btn-info">Detail</button> --}}
                                                        <button class="btn btn-warning btn-edit" data-id="{{ $employes->id }}"
                                                            data-name="{{ $employes->name }}"
                                                            data-email="{{ $employes->email }}"
                                                            data-nik="{{ $employes->nik }}"
                                                            data-jabatan="{{ $employes->position }}"
                                                            data-jk="{{ $employes->gender }}"
                                                            data-gaji="{{ $employes->wages }}"
                                                            data-rfid="{{ $employes->rfid }}"
                                                            data-tgl="{{ $employes->date_of_birth }}"
                                                            data-no="{{ $employes->no }}"
                                                            data-alamat="{{ $employes->address }}">Edit</button>
                                                        <button class="btn btn-danger btn-delete" data-id="{{ $employes->id }}">Hapus</button>
                                                    </div>
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
    <div id="myModal" class="modal fade" tabindex="-1" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-xl">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="myModalLabel">Tambah pegawai</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form action="{{ route('employe.store') }}" method="post">
                    @csrf
                    @method('POST')
                    <div class="modal-body">
                        <div class="row">
                            <div class="col-12 col-xl-4">
                                <label for="">Nama</label>
                                <input type="text" name="name" class="form-control">
                                @error('name')
                                    <p class="text-danger">
                                        {{ $message }}
                                    </p>
                                @enderror
                            </div>
                            <div class="col-12 col-xl-4">
                                <label for="">Email</label>
                                <input type="gmail" name="email" class="form-control">
                                @error('email')
                                    <p class="text-danger">
                                        {{ $message }}
                                    </p>
                                @enderror
                            </div>
                            <div class="col-12 col-xl-4">
                                <label for="">NIK</label>
                                <input type="text" name="nik" class="form-control">
                                @error('nik')
                                    <p class="text-danger">
                                        {{ $message }}
                                    </p>
                                @enderror
                            </div>
                            <div class="col-12 col-xl-4 mt-2">
                                <label for="">Jabatan</label>
                                <input type="text" name="position" class="form-control">
                                @error('position')
                                    <p class="text-danger">
                                        {{ $message }}
                                    </p>
                                @enderror
                            </div>
                            <div class="col-12 col-xl-4 mt-2">
                                <label for="">Jenis Kelamin</label><br>
                                <input type="radio" name="gender" value="male"> Laki-Laki
                                <input type="radio" name="gender" value="female"> Perempuan
                                @error('gender')
                                    <p class="text-danger">
                                        {{ $message }}
                                    </p>
                                @enderror
                            </div>
                            <div class="col-12 col-xl-4 mt-2">
                                <label for="">Gaji</label>
                                <input type="number" name="wages" class="form-control">
                                @error('wages')
                                    <p class="text-danger">
                                        {{ $message }}
                                    </p>
                                @enderror
                            </div>
                            <div class="col-12 col-xl-4 mt-2">
                                <label for="">RFID</label>
                                <input type="text" name="rfid" class="form-control">
                                @error('rfid')
                                    <p class="text-danger">
                                        {{ $message }}
                                    </p>
                                @enderror
                            </div>
                            <div class="col-12 col-xl-4 mt-2">
                                <label for="">Tanggal Lahir</label>
                                <input type="date" name="date_of_birth" class="form-control">
                                @error('date_of_birth')
                                    <p class="text-danger">
                                        {{ $message }}
                                    </p>
                                @enderror
                            </div>
                            <div class="col-12 col-xl-4 mt-2">
                                <label for="">No Telephone</label>
                                <input type="number" name="no" class="form-control">
                                @error('no')
                                    <p class="text-danger">
                                        {{ $message }}
                                    </p>
                                @enderror
                            </div>
                            <div class="col-12 mt-2">
                                <label for="">Alamat</label>
                                <textarea name="address" class="form-control" id=""></textarea>
                                @error('address')
                                    <p class="text-danger">
                                        {{ $message }}
                                    </p>
                                @enderror
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary waves-effect"
                            data-bs-dismiss="modal">Batal</button>
                        <button type="submit" class="btn btn-primary waves-effect waves-light">Simpan</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <div id="modal-edit" class="modal fade" tabindex="-1" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-xl">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="myModalLabel">Ubah pegawai</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form method="post" id="form-update">
                    @csrf
                    @method('PUT')
                    <div class="modal-body">
                        <div class="row">
                            <div class="col-12 col-xl-4">
                                <label for="">Nama</label>
                                <input type="text" name="name" id="name" class="form-control">
                                @error('name')
                                    <p class="text-danger">
                                        {{ $message }}
                                    </p>
                                @enderror
                            </div>
                            <div class="col-12 col-xl-4">
                                <label for="">Email</label>
                                <input type="gmail" name="email" id="email" class="form-control">
                                @error('email')
                                    <p class="text-danger">
                                        {{ $message }}
                                    </p>
                                @enderror
                            </div>
                            <div class="col-12 col-xl-4">
                                <label for="">NIK</label>
                                <input type="text" name="nik" id="nik" class="form-control">
                                @error('nik')
                                    <p class="text-danger">
                                        {{ $message }}
                                    </p>
                                @enderror
                            </div>
                            <div class="col-12 col-xl-4 mt-2">
                                <label for="">Jabatan</label>
                                <input type="text" name="position" id="position" class="form-control">
                                @error('position')
                                    <p class="text-danger">
                                        {{ $message }}
                                    </p>
                                @enderror
                            </div>
                            <div class="col-12 col-xl-4 mt-2">
                                <label for="">Jenis Kelamin</label><br>
                                <input type="radio" name="gender" id="gender" value="male" > Laki-Laki
                                <input type="radio" name="gender" id="gender" value="female"> Perempuan
                                @error('gender')
                                    <p class="text-danger">
                                        {{ $message }}
                                    </p>
                                @enderror
                            </div>
                            <div class="col-12 col-xl-4 mt-2">
                                <label for="">Gaji</label>
                                <input type="number" name="wages" id="wages" class="form-control">
                                @error('wages')
                                    <p class="text-danger">
                                        {{ $message }}
                                    </p>
                                @enderror
                            </div>
                            <div class="col-12 col-xl-4 mt-2">
                                <label for="">RFID</label>
                                <input type="text" name="rfid" id="rfid" class="form-control">
                                @error('rfid')
                                    <p class="text-danger">
                                        {{ $message }}
                                    </p>
                                @enderror
                            </div>
                            <div class="col-12 col-xl-4 mt-2">
                                <label for="">Tanggal Lahir</label>
                                <input type="date" name="date_of_birth" id="date_of_birth" class="form-control">
                                @error('date_of_birth')
                                    <p class="text-danger">
                                        {{ $message }}
                                    </p>
                                @enderror
                            </div>
                            <div class="col-12 col-xl-4 mt-2">
                                <label for="">No Telephone</label>
                                <input type="number" name="no" id="no" class="form-control">
                                @error('no')
                                    <p class="text-danger">
                                        {{ $message }}
                                    </p>
                                @enderror
                            </div>
                            <div class="col-12 mt-2">
                                <label for="">Alamat</label>
                                <textarea name="address" class="form-control" id="address"></textarea>
                                @error('address')
                                    <p class="text-danger">
                                        {{ $message }}
                                    </p>
                                @enderror
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary waves-effect"
                            data-bs-dismiss="modal">Batal</button>
                        <button type="submit" class="btn btn-primary waves-effect waves-light">Simpan</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    {{-- end modal  --}}

    @include('components.delete-modal-component')
@endsection
@section('script')
    <script>
        $('.btn-edit').click(function() {
            var id = $(this).data('id');
            var name = $(this).data('name');
            var email = $(this).data('email');
            var nik = $(this).data('nik');
            var jabatan = $(this).data('jabatan');
            var jk = $(this).data('jk');
            var gaji = $(this).data('gaji');
            var rfid = $(this).data('rfid');
            var tgl = $(this).data('tgl');
            var no = $(this).data('no');
            var alamat = $(this).data('alamat');
            $('#form-update').attr('action', '/employee/' + id);
            $('.modal-body #id').val(id);
            $('.modal-body #name').val(name);
            $('.modal-body #email').val(email);
            $('.modal-body #nik').val(nik);
            $('.modal-body #position').val(jabatan);
            $('input[name="gender"][value="' + jk + '"]').prop('checked', true);
            $('.modal-body #wages').val(gaji);
            $('.modal-body #rfid').val(rfid);
            $('.modal-body #date_of_birth').val(tgl);
            $('.modal-body #no').val(no);
            $('.modal-body #address').val(alamat);
            $('#modal-edit').modal('show');

        });
        $('.btn-delete').click(function() {
            var id = $(this).data('id');
            $('#form-delete').attr('action', '/delete-employee/' + id);
            $('#modal-delete').modal('show');
        });
    </script>
@endsection
