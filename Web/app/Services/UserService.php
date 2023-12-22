<?php

namespace App\Services;

use App\Enums\UploadDiskEnum;
use App\Http\Requests\ProfileRequest;
use App\Traits\UploadTrait;

class UserService
{
    use UploadTrait;

    /**
     * Handle update user data from UserRepository
     *
     * @param ProfileRequest $request
     * @param object $user
     * @return array
     */

    public function handleUpdate(ProfileRequest $request, object $user): array
    {
        $data = $request->validated();

        if ($request->hasFile('photo')) {
            if ($user->photo && $this->exist($user->photo)) $this->remove($user->photo);
            $data['photo'] = $this->upload(UploadDiskEnum::USER_PHOTOS->value, $request->file('photo'));
        }

        return $data;
    }
}
