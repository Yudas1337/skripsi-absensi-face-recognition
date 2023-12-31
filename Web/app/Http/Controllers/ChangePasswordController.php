<?php

namespace App\Http\Controllers;

use App\Contracts\Interfaces\ChangePasswordInterface;
use App\Http\Requests\ChangePasswordRequest;
use App\Models\User;
use Illuminate\Http\RedirectResponse;
use Illuminate\View\View;

class ChangePasswordController extends Controller
{
    private ChangePasswordInterface $changePassword;

    public function __construct(ChangePasswordInterface $changePassword)
    {
        $this->changePassword = $changePassword;
    }

    /**
     * Display a listing of the resource.
     */
    public function index(): View
    {
        $user = auth()->user();

        return view('dashboard.pages.profile.change-password', compact('user'));
    }

    /**
     * Update the specified resource in storage.
     *
     * @param ChangePasswordRequest $request
     * @param User $change_password
     * @return RedirectResponse
     */
    public function update(ChangePasswordRequest $request, User $change_password): RedirectResponse
    {
        $validated = $request->validated();

        $this->changePassword->update($change_password->id, [
            'password' => bcrypt($validated['password'])
        ]);

        return back()->with('success', trans('alert.password_updated'));
    }
}
