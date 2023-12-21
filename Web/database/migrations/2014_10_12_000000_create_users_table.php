<?php

use App\Enums\GenderEnum;
use App\Enums\ModelEnum;
use App\Enums\ReligionEnum;
use App\Enums\StatusEnum;
use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration {
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        Schema::create('users', function (Blueprint $table) {
            $table->uuid('id')->primary();
            $table->string('rfid')->nullable()->unique();
            $table->string('name');
            $table->string('username')->unique();
            $table->string('email')->unique();
            $table->string('birth_place');
            $table->date('birth_date');
            $table->enum('gender', array_column(GenderEnum::cases(), 'value'))->nullable();
            $table->text('address')->nullable();
            $table->timestamp('email_verified_at')->nullable();
            $table->string('password');
            $table->text('photo')->nullable();
            $table->enum('religion', array_column(ReligionEnum::cases(), 'value'))->nullable();
            $table->enum('status', array_column(StatusEnum::cases(), 'value'))->default(StatusEnum::AKTIF->value);
            $table->enum('model_used', array_column(ModelEnum::cases(), 'value'))->default(ModelEnum::PRE_TRAINED->value);
            $table->char('passphrase', 6)->nullable();
            $table->rememberToken();
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('users');
    }
};
