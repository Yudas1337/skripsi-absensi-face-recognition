<?php

use App\Enums\DayEnum;
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
        Schema::create('attendance_rules', function (Blueprint $table) {
            $table->uuid('id')->primary();
            $table->enum('day', [DayEnum::MONDAY->value, DayEnum::TUESDAY->value, DayEnum::WEDNESDAY->value, DayEnum::THRUSDAY->value, DayEnum::FRIDAY->value]);
            $table->time('get_in_start');
            $table->time('get_in_end');
            $table->time('go_home_start');
            $table->time('go_home_end');
            $table->time('break_start');
            $table->time('break_end');
            $table->time('coming_from_break_start');
            $table->time('coming_from_break_end');
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('attendance_rules');
    }
};
