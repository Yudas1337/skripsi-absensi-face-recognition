<?php

namespace App\Http\Requests;

use App\Rules\DayRule;
use Illuminate\Foundation\Http\FormRequest;

class AttendanceRuleRequest extends FormRequest
{
    /**
     * Get the validation rules that apply to the request.
     *
     * @return array<string, \Illuminate\Contracts\Validation\ValidationRule|array<mixed>|string>
     */
    public function rules(): array
    {
        return [
            'day' => ['required', new DayRule],
            'get_in_start' => 'required|date_format:H:i:s|before:get_in_end',
            'get_in_end' => 'required|date_format:H:i:s|after:get_in_start',
            'go_home_start' => 'required|date_format:H:i:s|before:go_home_end',
            'go_home_end' => 'required|date_format:H:i:s|after:go_home_start',
            'break_start' => 'required|date_format:H:i:s|before:break_end',
            'break_end' => 'required|date_format:H:i:s|after:break_start',
            'coming_from_break_start' => 'required|date_format:H:i:s|before:coming_from_break_end',
            'coming_from_break_end' => 'required|date_format:H:i:s|after:coming_from_break_start',
        ];
    }
}
