<?php

namespace Database\Seeders;

use App\Enums\GenderEnum;
use App\Models\Employee;
use App\Models\User;
use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\Hash;

class UserSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        $user= User::create([
            'name' => 'Admin',
            'email' => 'admin@example.com',
            'password' => Hash::make('password'),
        ]);

        Employee::create([
            'nik' => '12313',
            'name' => 'name',
            'email' => 'p@gmail.com',
            'password' => Hash::make('password'),
            'no' => '02121921',
            'position' => '1',
            'gender' => GenderEnum::MALE->value,
            'wages' => '12',
            'address' => 'afawe',
            'user_id' => $user->id,
            'date_of_birth' => now(),
        ]);
    }
}
