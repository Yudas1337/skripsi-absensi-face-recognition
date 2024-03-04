<?php

namespace App\Http\Resources;

use Illuminate\Http\Request;
use Illuminate\Http\Resources\Json\JsonResource;

class AttendanceRuleResource extends JsonResource
{
    /**
     * Transform the resource into an array.
     *
     * @return array<string, mixed>
     */
    public function toArray(Request $request): array
    {
        return [
            'id' => $this->id,
            'get_in_start' => $this->get_in_start,
            'get_in_end' => $this->get_in_end,
            'go_home_start' => $this->go_home_start,
            'go_home_end' => $this->go_home_end,
            'break_start' => $this->break_start,
            'break_end' => $this->break_end,
            'coming_from_break_start' => $this->coming_from_break_start,
            'coming_from_break_end' => $this->coming_from_break_end
        ];
    }
}
