<?php

namespace App\Contracts\Repositories;

use App\Contracts\Interfaces\ChangePasswordInterface;
use App\Models\User;

class ChangePasswordRepository extends BaseRepository implements ChangePasswordInterface
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
        return $this->model->query()
            ->findOrFail($id)
            ->update($data);
    }
}
