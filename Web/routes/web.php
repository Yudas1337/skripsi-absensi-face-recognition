<?php

use App\Http\Controllers\Auth\LoginController;
use App\Http\Controllers\ChangePasswordController;
use App\Http\Controllers\DashboardController;
use App\Http\Controllers\EmployeeController;
use App\Http\Controllers\InstanceController;
use App\Http\Controllers\ProfileController;
use App\Http\Controllers\StudentController;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider and all of them will
| be assigned to the "web" middleware group. Make something great!
|
*/

Route::get('/', [LoginController::class, 'showLoginForm'])->name('index');

Auth::routes([
    'register' => false,
    'reset' => false
]);

Route::middleware('auth')->prefix('dashboard')->group(function () {

    Route::name('dashboard.')->group(function () {
        Route::get('/', [DashboardController::class, 'index'])->name('index');
    });

    Route::middleware('role:admin')->group(function () {

        Route::resources([
            'employees' => EmployeeController::class,
            'students' => StudentController::class,
            'instances' => InstanceController::class
        ]);
    });

    Route::name('user.')->group(function () {
        Route::resources([
            'profile' => ProfileController::class,
            'change-password' => ChangePasswordController::class
        ], ['only' => ['index', 'update']]);
    });

});

Route::get('/home', [App\Http\Controllers\HomeController::class, 'index'])->name('home');
