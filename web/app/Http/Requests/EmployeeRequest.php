<?php

namespace App\Http\Requests;

use Illuminate\Foundation\Http\FormRequest;

class EmployeeRequest extends FormRequest
{

    /**
     * Get the validation rules that apply to the request.
     *
     * @return array<string, \Illuminate\Contracts\Validation\ValidationRule|array<mixed>|string>
     */
    public function rules(): array
    {
        return [
            'name' => 'required',
            'email' => 'required|email',
            'nik' => 'required',
            'position' => 'required',
            'photo' => 'nullable|image',
            'no' => 'required',
            'gender' => 'required',
            'wages' => 'required',
            'rfid' => 'nullable',
            'address' => 'required',
            'date_of_birth' => 'required|date',
        ];
    }

    public function messages()
    {
        return [
            'name.required' => 'Nama Tidak boleh kosong',
            'email.required' => 'Email Tidak boleh kosong',
            'email.email' => 'Email Tidak Valid',
            'nik.required' => 'NIK Tidak boleh kosong',
            'position.required' => 'Posisi Tidak boleh kosong',
            'no.required' => 'Nomor Tidak boleh kosong',
            'gender.required' => 'Jenis Kelamin Tidak boleh kosong',
            'wages.required' => 'Gaji Tidak boleh kosong',
            'address.required' => 'Alamat Tidak boleh kosong',
            'date_of_birth.required' => 'Tgl Lahir Tidak boleh kosong',
            'date_of_birth.date' => 'Tgl Lahir Tidak Valid',
        ];
    }
}
