@extends('dashboard.layouts.main')

@section('content')
    <!-- start page title -->
    <div class="row">
        <div class="col-12">
            <div class="page-title-box d-sm-flex align-items-center justify-content-between">
                <h4 class="mb-sm-0 font-size-18">Selamat Datang, {{ auth()->user()->name }}</h4>
            </div>
        </div>
    </div>
    <!-- end page title -->

@endsection
