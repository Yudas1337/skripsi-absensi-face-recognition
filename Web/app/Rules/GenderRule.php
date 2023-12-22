<?php

namespace App\Rules;

use App\Enums\GenderEnum;
use Closure;
use Illuminate\Contracts\Validation\ValidationRule;

class GenderRule implements ValidationRule
{
    /**
     * Run the validation rule.
     *
     * @param string $attribute
     * @param mixed $value
     * @param Closure $fail
     */
    public function validate(string $attribute, mixed $value, Closure $fail): void
    {
        if (!in_array($value, array_column(GenderEnum::cases(), 'value'))) {
            $fail('The :attribute is not match');
        }
    }
}
