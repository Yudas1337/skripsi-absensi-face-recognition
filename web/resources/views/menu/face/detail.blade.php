@extends('layouts.app')
@section('content')
<div class="d-flex justify-content-between mb-3">
    <h4 class="card-title mb-4">Tambah Image Wajah</h4>
    <a href="/face" class="btn btn-primary">Kembali</a>
</div>
<div class="row">
    <div class="col-12">
        <div class="card">
            <div class="card-body">
                <form class="repeater" action="/face/create" method="POST" enctype="multipart/form-data">
                    @csrf
                    <div data-repeater-list="group-a">
                        <div data-repeater-item class="row">
                            <div  class="mb-3 col-lg-12">
                                <input type="hidden" name="employee_id" value="{{ $id }}">
                                <label for="name">Image</label>
                                <input type="file" id="name" name="photo[]" class="form-control" placeholder="Enter Your Name" multiple/>
                            </div>
                        </div>

                    </div>

                    <div class="d-flex justify-content-end">
                        <button type="submit" class="btn btn-primary">Tambah</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<h4 class="card-title mb-4">Data Image Wajah</h4>

<div class="row">
    @forelse ($faces as $face)
    <div class="col-12 col-xl-3 col-lg-12 ">
        <div class="card">
            <div class="card-body">
                <img src="{{ asset('storage/' . $face->photo ) }}" alt="{{ $face->photo }}" class="w-100">
            </div>
        </div>
    </div>
    @empty
    <div class="d-flex justify-content-center">
        <div class="">
            <img src="{{ asset('nodata.png') }}" width="300px" height="300px" alt="" srcset="">
            <p class="fs-5 text-center mt-4 text-dark" style="font-weight: 500">
                Data Tidak Tersedia
            </p>
        </div>
    </div>
    @endforelse
</div>
@endsection
@section('script')

<script src="{{ asset('assets/libs/jquery.repeater/jquery.repeater.min.js') }}"></script>

<script src="{{ asset('assets/js/pages/form-repeater.int.js') }}"></script>
@endsection
