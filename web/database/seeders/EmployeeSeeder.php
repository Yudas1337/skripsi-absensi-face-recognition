<?php

namespace Database\Seeders;

use App\Enums\GenderEnum;
use App\Models\Employee;
use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\Hash;

class EmployeeSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
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
            ''
        ]);
    }
}
