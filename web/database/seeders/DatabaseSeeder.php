<?php

namespace Database\Seeders;

// use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;

class DatabaseSeeder extends Seeder
{
    /**
     * Seed the application's database.
     */
    public function run(): void
    {
        // \App\Models\User::factory(10)->create();

        \App\Models\User::factory()->create([
            'name' => 'Admin',
            'email' => 'admin@presensi.com',
            'password' => 'password'
        ]);

        \App\Models\Position::factory()->create([
            'name' => 'Admin'
        ]);
        \App\Models\Position::factory()->create([
            'name' => 'Staff'
        ]);
        \App\Models\Position::factory()->create([
            'name' => 'User'
        ]);

    }
}
