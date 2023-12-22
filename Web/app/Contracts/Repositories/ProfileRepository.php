<?php

namespace App\Contracts\Repositories;

use App\Contracts\Interfaces\ProfileInterface;
use App\Models\User;

class ProfileRepository extends BaseRepository implements ProfileInterface
{
    public function __construct(User $user)
    {
        $this->model = $user;
    }

    /**
     * Handle show method and update data instantly from models.
     *
     * @param mixed $id
     * @param array $data
     *
     * @return mixed
     */
    public function update(mixed $id, array $data): mixed
    {
        $this->get()->employee->update($data);

        return $this->get()->update($data);
    }

    /**
     * Handle the Get all data event from models.
     *
     * @return mixed
     */
    public function get(): mixed
    {
        return $this->model->query()
            ->findOrFail(auth()->id());
    }
}
