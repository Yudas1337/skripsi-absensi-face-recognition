<?php

namespace App\Http\Controllers;

use App\Contracts\Interfaces\ProfileInterface;
use App\Enums\ReligionEnum;
use App\Enums\RoleEnum;
use App\Helpers\UserHelper;
use App\Http\Requests\ProfileRequest;
use App\Models\User;
use App\Services\UserService;
use Illuminate\Http\RedirectResponse;
use Illuminate\View\View;

class ProfileController extends Controller
{
    private UserService $service;
    private ProfileInterface $profile;

    public function __construct(UserService $service, ProfileInterface $profile)
    {
        $this->service = $service;
        $this->profile = $profile;
    }

    /**
     * Display a listing of the resource.
     *
     * @return View
     */
    public function index(): View
    {
        if (UserHelper::getUserRole() == RoleEnum::ADMIN->value)
            return view('dashboard.pages.profile.index', [
                'user' => $this->profile->get(),
                'religions' => array_column(ReligionEnum::cases(), 'value')
            ]);

        return view('dashboard.pages.profile.index');
    }

    /**
     * Update the specified resource in storage.
     *
     * @param ProfileRequest $request
     * @param User $profile
     * @return RedirectResponse
     */
    public function update(ProfileRequest $request, User $profile): RedirectResponse
    {
        $this->profile->update($profile->id, $this->service->handleUpdate($request, $profile));

        return back()->with('success', trans('alert.profile_updated'));
    }
}
