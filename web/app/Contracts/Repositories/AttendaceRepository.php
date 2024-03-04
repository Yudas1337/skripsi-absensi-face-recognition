<?php
namespace App\Contracts\Repositories;

use App\Contracts\Interfaces\AttendanceInterface;
use App\Contracts\Repositories\BaseRepository;
use App\Models\attendance;

class AttendaceRepository extends BaseRepository implements AttendanceInterface
{
    public function __construct(attendance $attendance)
    {
        $this->model = $attendance;
    }

    public function studentAttendanceToday(mixed $id): mixed
    {
        return $this->model->query()
            ->where('student_id', $id)
            ->whereDate('created_at', now())
            ->first();
    }

    public function get(): mixed
    {

    }

    public function store(array $data): mixed
    {
        return $this->model->query()->create($data);
    }
}

