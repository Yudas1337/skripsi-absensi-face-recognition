<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;
use Illuminate\Database\Eloquent\Relations\HasMany;

class Attendance extends Model
{
    use HasFactory;
    protected $table = 'attendances';
    protected $primaryKey = 'id';
    public $incrementing = false;
    public $keyType = 'char';
    protected $guarded = [];
    protected $fillable = ['employee_id', 'status', 'created_at', 'updated_at'];

    /**
     * student
     *
     * @return BelongsTo
     */
    public function employe(): BelongsTo
    {
        return $this->belongsTo(Employee::class, 'employee_id');
    }

    /**
     * detailAttendance
     *
     * @return HasMany
     */
    public function detailAttendances(): HasMany
    {
        return $this->hasMany(attendace_detail::class);
    }
}
