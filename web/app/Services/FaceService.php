<?php

namespace App\Services;

use App\Http\Requests\FaceRequest;
use App\Traits\UploadTrait;

class FaceService {

    use UploadTrait;

    public function store(FaceRequest $request): array
    {
        $data = $request->validated();
        $newPhoto = [];

        foreach ($request->file('photo') as $photo) {
            array_push($newPhoto, $photo->store('faces', 'public'));
        }
        $data['photo'] = $newPhoto;
        return $data;
    }
}
