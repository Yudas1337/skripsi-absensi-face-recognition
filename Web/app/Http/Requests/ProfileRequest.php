<?php

namespace App\Http\Requests;

use App\Rules\GenderRule;
use App\Rules\ReligionRule;
use Illuminate\Contracts\Validation\ValidationRule;
use Illuminate\Validation\Rule;

class ProfileRequest extends BaseRequest
{
    /**
     * Get the validation rules that apply to the request.
     *
     * @return array<string, ValidationRule|array<mixed>|string>
     */
    public function rules(): array
    {
        return [
            'name' => 'required|min:3|max:255|regex:/^[a-z-A-Z_\s\.]*$/',
            'nip' => ['required', 'size:18', Rule::unique('employees', 'nip')->ignore($this->profile->id, 'user_id')],
            'email' => ['required', 'email', Rule::unique('users')->ignore($this->profile)],
            'phone_number' => ['required', 'min:3', 'max:15', 'regex:/^[0-9]*$/', Rule::unique('users')->ignore($this->profile)],
            'birth_place' => 'required|max:255',
            'birth_date' => 'required|date',
            'gender' => ['required', new GenderRule],
            'address' => 'required',
            'religion' => ['required', new ReligionRule],
            'photo' => 'nullable|max:5000|mimes:jpg,png,jpeg|image',
        ];
    }

    /**
     * Custom Validation Messages
     *
     * @return array<string, ValidationRule|array<mixed>|string>
     */
    public function messages(): array
    {
        return [
            'name.required' => 'Kolom nama wajib diisi.',
            'name.min' => 'Panjang nama minimal 3 karakter.',
            'name.max' => 'Panjang nama maksimal 255 karakter.',
            'name.regex' => 'Format nama tidak valid.',
            'nip.required' => 'Kolom NIP wajib diisi.',
            'nip.size' => 'Panjang NIP harus 18 karakter.',
            'nip.unique' => 'NIP sudah digunakan oleh pengguna lain.',
            'email.required' => 'Kolom email wajib diisi.',
            'email.email' => 'Format email tidak valid.',
            'email.unique' => 'Email sudah digunakan oleh pengguna lain.',
            'phone_number.required' => 'Kolom nomor telepon wajib diisi.',
            'phone_number.min' => 'Panjang nomor telepon minimal 3 karakter.',
            'phone_number.max' => 'Panjang nomor telepon maksimal 15 karakter.',
            'phone_number.regex' => 'Format nomor telepon tidak valid.',
            'phone_number.unique' => 'Nomor telepon sudah digunakan oleh pengguna lain.',
            'birth_place.required' => 'Kolom tempat lahir wajib diisi.',
            'birth_place.max' => 'Panjang tempat lahir maksimal 255 karakter.',
            'birth_date.required' => 'Kolom tanggal lahir wajib diisi.',
            'birth_date.date' => 'Format tanggal lahir tidak valid.',
            'gender.required' => 'Kolom jenis kelamin wajib diisi.',
            'address.required' => 'Kolom alamat wajib diisi.',
            'religion.required' => 'Kolom agama wajib diisi.',
            'photo.max' => 'Ukuran file foto tidak boleh lebih dari 5000 kilobita.',
            'photo.mimes' => 'Format file foto harus jpg, png, atau jpeg.',
            'photo.image' => 'File yang diunggah bukan berupa gambar.',
        ];
    }

}
