@php
    use App\Enums\GenderEnum;use App\Helpers\DateHelper;
@endphp
@extends('dashboard.layouts.main')
@section('content')
    <div class="row">
        <div class="col-12">
            <div class="page-title-box d-sm-flex align-items-center justify-content-between">
                <h4 class="mb-sm-0 font-size-18">Profile</h4>
            </div>
            @if (session('success'))
                <x-alert-success></x-alert-success>
            @elseif(session('error'))
                <x-alert-failed></x-alert-failed>
            @endif
        </div>
    </div>
    <div class="row">
        <div class="col-xl-4 col-sm-6">
            <div class="card text-center">
                <div class="card-body">
                    <div class="mb-4">
                        @if(!auth()->user()->photo)
                            <img class="rounded-circle avatar-lg" src="{{ asset('avatar.png') }}" alt="">
                        @else
                            <img class="rounded-circle avatar-lg" src="{{ asset('storage/'. auth()->user()->photo) }}"
                                 alt="">
                        @endif
                    </div>
                    <h5 class="font-size-15 mb-1"><a href="javascript: void(0);"
                                                     class="text-dark">{{ auth()->user()->name }}</a>
                    </h5>
                    <p class="text-muted">{{ $user->employee->position }}</p>

                    <div>
                        <a class="badge bg-primary font-size-11 m-1">Bergabung Sejak
                            : {{ DateHelper::parseDate($user->employee->start_work, 'd F Y')   }}</a>
                    </div>
                </div>

            </div>
        </div>

        <div class="col-xl-8">

            <div class="card">
                <div class="card-body">
                    <h4 class="card-title mb-4">Biodata Diri</h4>
                    <div class="table-responsive">
                        <form method="POST" action="{{ route('user.profile.update', $user) }}"
                              enctype="multipart/form-data">
                            @method('PATCH')
                            @csrf
                            <table class="table table-nowrap mb-0">
                                <tbody>
                                <tr>
                                    <th scope="row">Nama Lengkap <span class="text-danger">*</span></th>
                                    <td>
                                        <input autocomplete="off" type="text" name="name"
                                               class="form-control @error('name') is-invalid @enderror"
                                               value="{{ $user->name }}">
                                        @error('name')
                                        <span class="invalid-feedback" role="alert">
                                        <strong>{{ $message }}</strong>
                                        </span>
                                        @enderror
                                    </td>
                                </tr>
                                <tr>
                                    <th scope="row">NIP <span class="text-danger">*</span></th>
                                    <td>
                                        <input autocomplete="off" type="text" name="nip"
                                               class="form-control @error('nip') is-invalid @enderror"
                                               value="{{ $user->employee->nip }}">
                                        @error('nip')
                                        <span class="invalid-feedback" role="alert">
                                        <strong>{{ $message }}</strong>
                                        </span>
                                        @enderror
                                    </td>
                                </tr>
                                <tr>
                                    <th scope="row">E-mail <span class="text-danger">*</span></th>
                                    <td>
                                        <input autocomplete="off" type="text" name="email"
                                               class="form-control @error('email') is-invalid @enderror"
                                               value="{{ $user->email }}">
                                        @error('email')
                                        <span class="invalid-feedback" role="alert">
                                        <strong>{{ $message }}</strong>
                                        </span>
                                        @enderror
                                    </td>
                                </tr>
                                <tr>
                                    <th scope="row">Nomor Telepon <span class="text-danger">*</span></th>
                                    <td>
                                        <input autocomplete="off" type="text" name="phone_number"
                                               class="form-control @error('phone_number') is-invalid @enderror"
                                               value="{{ $user->phone_number }}">
                                        @error('phone_number')
                                        <span class="invalid-feedback" role="alert">
                                        <strong>{{ $message }}</strong>
                                        </span>
                                        @enderror
                                    </td>
                                </tr>
                                <tr>
                                    <th scope="row">Tempat Tanggal Lahir <span class="text-danger">*</span></th>
                                    <td>
                                        <input autocomplete="off" type="text" name="birth_place"
                                               class="form-control @error('birth_place') is-invalid @enderror"
                                               value="{{ $user->birth_place }}">
                                        @error('birth_place')
                                        <span class="invalid-feedback" role="alert">
                                        <strong>{{ $message }}</strong>
                                        </span>
                                        @enderror

                                        <input autocomplete="off" type="date" name="birth_date"
                                               class="form-control mt-3 @error('birth_date') is-invalid @enderror"
                                               value="{{ $user->birth_date }}">
                                        @error('birth_date')
                                        <span class="invalid-feedback" role="alert">
                                        <strong>{{ $message }}</strong>
                                        </span>
                                        @enderror
                                    </td>
                                </tr>
                                <tr>
                                    <th scope="row">Jenis Kelamin <span class="text-danger">*</span></th>
                                    <td>
                                        <div class="row">
                                            <div class="col-lg-12">
                                                <div>
                                                    <div class="form-check form-radio-danger mb-3">
                                                        <input
                                                            value="{{ GenderEnum::MALE->value }}"
                                                            class="form-check-input @error('gender') is-invalid @enderror"
                                                            type="radio"
                                                            name="gender"
                                                            id="male" {{ $user->gender == 'male' ? 'checked' : '' }}>
                                                        <label class="form-check-label" for="male">
                                                            Pria
                                                        </label>
                                                        @error('gender')
                                                        <span class="invalid-feedback" role="alert">
                                                        <strong>{{ $message }}</strong>
                                                        </span>
                                                        @enderror
                                                    </div>
                                                    <div class="form-check form-radio-info mb-3">
                                                        <input
                                                            value="{{ GenderEnum::FEMALE->value }}"
                                                            class="form-check-input @error('gender') is-invalid @enderror"
                                                            type="radio"
                                                            name="gender"
                                                            id="female" {{ $user->gender == 'female' ? 'checked' : '' }}>
                                                        <label class="form-check-label" for="female">
                                                            Wanita
                                                        </label>
                                                        @error('gender')
                                                        <span class="invalid-feedback" role="alert">
                                                        <strong>{{ $message }}</strong>
                                                        </span>
                                                        @enderror
                                                    </div>

                                                </div>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th scope="row">Alamat <span class="text-danger">*</span></th>
                                    <td><textarea autocomplete="off" type="text" name="address"
                                                  class="form-control @error('address') is-invalid @enderror">{{ $user->address }}</textarea>
                                        @error('address')
                                        <span class="invalid-feedback" role="alert">
                                        <strong>{{ $message }}</strong>
                                        </span>
                                        @enderror
                                    </td>
                                </tr>
                                <tr>
                                    <th scope="row">Agama <span class="text-danger">*</span></th>
                                    <td><select name="religion"
                                                class="form-control @error('religion') is-invalid @enderror">
                                            <option value="">--Pilih--</option>
                                            @foreach($religions as $religion)
                                                <option
                                                    value="{{ $religion }}" {{ $user->religion == $religion ? 'selected' : '' }}>{{ $religion }}</option>
                                            @endforeach
                                        </select>
                                        @error('religion')
                                        <span class="invalid-feedback" role="alert">
                                        <strong>{{ $message }}</strong>
                                        </span>
                                        @enderror
                                    </td>
                                </tr>
                                <tr>
                                    <th scope="row">Foto</th>
                                    <td>
                                        <input type="file" name="photo"
                                               class="form-control @error('photo') is-invalid @enderror">
                                        @error('photo')
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
