@php
    use App\Enums\GenderEnum;use App\Helpers\DateHelper;
@endphp
@extends('dashboard.layouts.main')
@section('content')
    <div class="row">
        <div class="col-12">
            <div class="page-title-box d-sm-flex align-items-center justify-content-between">
                <h4 class="mb-sm-0 font-size-18">Ubah Password</h4>
            </div>
            @if (session('success'))
                <x-alert-success></x-alert-success>
            @elseif(session('error'))
                <x-alert-failed></x-alert-failed>
            @endif
        </div>
    </div>
    <div class="row">

        <div class="col-xl-12">

            <div class="card">
                <div class="card-body">
                    <div class="table-responsive">
                        <form method="POST" action="{{ route('user.change-password.update', $user) }}">
                            @method('PATCH')
                            @csrf
                            <table class="table table-nowrap mb-0">
                                <tbody>
                                <tr>
                                    <th scope="row">Password Lama<span class="text-danger">*</span></th>
                                    <td>
                                        <input type="password" name="old_password"
                                               class="form-control @error('old_password') is-invalid @enderror">
                                        @error('old_password')
                                        <span class="invalid-feedback" role="alert">
                                        <strong>{{ $message }}</strong>
                                        </span>
                                        @enderror
                                    </td>
                                </tr>
                                <tr>
                                    <th scope="row">Password Baru<span class="text-danger">*</span></th>
                                    <td>
                                        <input type="password" name="password"
                                               class="form-control @error('password') is-invalid @enderror">
                                        @error('password')
                                        <span class="invalid-feedback" role="alert">
                                        <strong>{{ $message }}</strong>
                                        </span>
                                        @enderror
                                    </td>
                                </tr>
                                <tr>
                                    <th scope="row">Konfirmasi Password<span class="text-danger">*</span></th>
                                    <td>
                                        <input type="password" name="password_confirmation"
                                               class="form-control @error('password_confirmation') is-invalid @enderror">
                                        @error('password_confirmation')
                                        <span class="invalid-feedback" role="alert">
                                        <strong>{{ $message }}</strong>
                                        </span>
                                        @enderror
                                    </td>
                                </tr>
                                <tr>
                                    <th scope="row"></th>
                                    <td>
                                        <button type="submit" class="btn btn-primary">Update Profile</button>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </form>

                    </div>
                </div>
            </div>

        </div>
    </div>
@endsection
