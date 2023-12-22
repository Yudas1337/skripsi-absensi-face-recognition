<?php

namespace App\Enums;

enum RoleEnum: string
{
    case ADMIN = 'admin';
    case SCHOOL = 'school';
    case EMPLOYEE = 'employee';
    case STUDENT = 'student';
}
