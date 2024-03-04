@extends('layouts.app')
@section('content')
    <div class="row">
        <div class="col-12">
            <div class="page-title-box d-sm-flex align-items-center justify-content-between">
                <h4 class="mb-sm-0 font-size-18">Divisi</h4>
                <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#myModal" type="button">Tambah</button>
            </div>
        </div>
    </div>
    {{-- modal  --}}
    <div id="myModal" class="modal fade" tabindex="-1" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="myModalLabel">Tambah Divisi</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form action="/division" method="post">
                    @csrf
                    @method('POST')
                    <div class="modal-body">
                        <label for="">Nama</label>
                        <input type="text" name="name" class="form-control">
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary waves-effect" data-bs-dismiss="modal">Close</button>
                        <button type="submit" class="btn btn-primary waves-effect waves-light">Simpan</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    {{-- end modal  --}}
    {{-- modal edit --}}
    <div id="modal-edit" class="modal fade" tabindex="-1" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="myModalLabel">Tambah Divisi</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form id="form-update" method="post">
                    @csrf
                    @method('PUT')
                    <div class="modal-body">
                        <label for="">Nama</label>
                        <input type="text" name="name" class="form-control name">
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary waves-effect" data-bs-dismiss="modal">Close</button>
                        <button type="submit" class="btn btn-primary waves-effect waves-light">Simpan</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    {{-- end modal  --}}
    <div class="row">
        @forelse ($divisions as $division)
            <div class="col-xl-4 col-xxl-3 col-lg-6 col-md-6 col-sm-6">
                <div class="card border">
                    <div class="card-body">
                        <div class="d-flex">
                            <div class="flex-shrink-0 me-2">
                                <div class="avatar-xs">
                                    <span class="avatar-title rounded-circle bg-success text-white font-size-16">
                                        {{ $division->name[0] }}
                                    </span>
                                </div>
                            </div>
                            <div class="row overflow-hidden align-items-center">
                                <h5 class="text-truncate font-size-15 m-0"><a href="javascript: void(0);"
                                        class="text-dark">{{ $division->name }}</a></h5>
                            </div>
                        </div>
                    </div>
                    <div class="px-4 py-3 border-top d-flex ">
                        <ul class="list-inline mb-0">
                            <li class="list-inline-item me-0">
                                <i class= "bx bx-calendar me-0"></i>
                                {{ \Carbon\Carbon::parse($division->created_at)->translatedFormat('d F Y') }}
                            </li>
                        </ul>
                        <div class="d-flex ms-auto gap-2">
                            <span>
                                <button class="btn btn-sm btn-info btn-edit" id="{{ $division->id }}"
                                    data-id="{{ $division->id }}" data-name="{{ $division->name }}">
                                    Edit
                                </button>

                            </span>
                            <span>
                                <button data-bs-toggle="modal" class=" btn btn-sm btn-danger btn-delete"
                                    data-id="{{ $division->id }}" id="{{ $division->id }}" style="">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18"
                                        viewBox="0 0 48 48">
                                        <defs>
                                            <mask id="ipSDelete0">
                                                <g fill="none" stroke-linejoin="round" stroke-width="4">
                                                    <path fill="#fff" stroke="#fff" d="M9 10v34h30V10z" />
                                                    <path stroke="#000" stroke-linecap="round" d="M20 20v13m8-13v13" />
                                                    <path stroke="#fff" stroke-linecap="round" d="M4 10h40" />
                                                    <path fill="#fff" stroke="#fff" d="m16 10l3.289-6h9.488L32 10z" />
                                                </g>
                                            </mask>
                                        </defs>
                                        <path fill="currentColor" d="M0 0h48v48H0z" mask="url(#ipSDelete0)" />
                                    </svg>
                                </button>
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        @empty
            <div class="d-flex justify-content-center" style="min-height:16rem">
                <div class="my-auto">
                    <img src="{{ asset('no-data.png') }}" width="300" height="300" />
                    <h4 class="text-center mt-4">Divisi Masih Kosong
                    </h4>
                </div>
            </div>
        @endforelse
    </div>
    <x-delete-modal-component />
@endsection
@section('script')
    @if (session('success'))
        <script>
            Swal.fire({
                icon: 'success',
                title: 'Success',
                text: '{{ session('success') }}',
            });
        </script>
    @endif
    <script>
        $('.btn-edit').click(function() {
            var id = $(this).data('id'); // Mengambil nilai id dari tombol yang diklik
            var name = $(this).data('name'); // Mengambil nilai name dari tombol yang diklik
            $('#form-update').attr('action', '/division/' + id); // Mengubah nilai atribut action form
            $('.name').val(name);
            $('#modal-edit').modal('show');
        });
        $('.btn-delete').click(function() {
            var id = $(this).data('id'); // Mengambil nilai id dari tombol yang diklik
            $('#form-delete').attr('action', '/delete-division/' + id); // Mengubah nilai atribut action form
            $('#modal-delete').modal('show');
        });
    </script>
@endsection
