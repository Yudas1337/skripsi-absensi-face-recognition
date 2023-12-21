<?php

namespace App\Enums;

enum AttendanceEnum: string
{
    case MASUK = 'masuk';
    case IZIN = 'izin';
    case SAKIT = 'sakit';
    case ALPHA = 'alpha';
}
