<?php

namespace App\Models;

use App\Base\Interfaces\HasAttendances;
use App\Base\Interfaces\HasFaces;
use App\Base\Interfaces\HasUser;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;
use Illuminate\Database\Eloquent\Relations\HasMany;

class Employee extends Model implements HasUser, HasAttendances, HasFaces
{
    use HasFactory;

    protected $table = 'employees';
    protected $primaryKey = 'id';
    public $incrementing = false;
    public $keyType = 'char';

    protected $guarded = [];

    /**
     * user
     *
     * @return BelongsTo
     */
    public function user(): BelongsTo
    {
        return $this->belongsTo(User::class);
    }

    /**
     * attendances
     *
     * @return HasMany
     */
    public function attendances(): HasMany
    {
        return $this->hasMany(Attendance::class);
    }

    /**
     * faces
     *
     * @return HasMany
     */
    public function faces(): HasMany
    {
        return $this->hasMany(Face::class);
    }
}
