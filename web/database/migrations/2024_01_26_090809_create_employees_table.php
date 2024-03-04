<?php

use App\Enums\GenderEnum;
use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        Schema::create('employees', function (Blueprint $table) {
            $table->uuid('id')->primary();
            $table->string('nik');
            $table->string('name');
            $table->string('password');
            $table->string('email');
            $table->string('no');
            $table->string('position');
            $table->string('photo')->nullable();
            $table->enum('gender',[GenderEnum::MALE->value,GenderEnum::FEMALE->value]);
            $table->string('wages');
            $table->string('rfid')->nullable();
            $table->string('address');
            $table->foreignUuid('user_id')->constrained()->cascadeOnDelete()->cascadeOnUpdate();
            $table->date('date_of_birth');
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('employees');
    }
};
