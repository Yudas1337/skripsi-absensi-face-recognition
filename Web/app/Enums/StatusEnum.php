<?php

namespace App\Enums;

enum StatusEnum: string
{
    case AKTIF = 'aktif';
    case LULUS = 'lulus';
    case CUTI = 'cuti';
    case MENGUNDURKAN_DIRI = 'mengundurkan diri';
    case DIKELUARKAN = 'dikeluarkan';
}
