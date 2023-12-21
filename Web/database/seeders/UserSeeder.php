<?php

namespace Database\Seeders;

use App\Enums\GenderEnum;
use App\Models\Employee;
use App\Models\Instance;
use App\Models\User;
use Illuminate\Database\Seeder;
use Spatie\Permission\Models\Role;

class UserSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        $users = ['admin', 'school', 'employee', 'student'];

        $instance = Instance::query()->create([
            'name' => 'PT Hummatech Digital Indonesia',
            'email' => 'hummatech.id@gmail.com',
            'contact' => '06285176777785',
            'address' => 'Perum Permata Regency 1 Blok 10/28, Perun Gpa, Ngijo, Kec. Karang Ploso, Kabupaten Malang, Jawa Timur 65152',
            'website' => 'https://hummatech.com'
        ]);

        foreach ($users as $user) {
            $role = Role::create([
                'name' => $user
            ]);
            $profile = User::query()
                ->create([
                    'name' => $user,
                    'username' => $user,
                    'birth_place' => 'Malang',
                    'birth_date' => '2002-01-13',
                    'gender' => GenderEnum::MALE->value,
                    'address' => 'Malang',
                    'email' => $user . "@gmail.com",
                    'password' => bcrypt('password'),
                    'email_verified_at' => now()
                ]);

            Employee::query()->create([
                'nip' => '123456789123456789',
                'user_id' => $profile->id,
                'instance_id' => $instance->id
            ]);

            $profile->assignRole($role);

            break;
        }


    }
}
