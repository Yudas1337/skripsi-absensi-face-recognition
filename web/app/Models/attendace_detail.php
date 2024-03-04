<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class attendace_detail extends Model
{
    use HasFactory;
    protected $table = 'attendace_details';
    protected $primaryKey = 'id';
    public $incrementing = false;
    public $keyType = 'char';

    protected $fillable = ['attendance_id', 'status', 'created_at', 'updated_at'];
    protected $guarded = [];

    /**
     * attendance
     *
     * @return BelongsTo
     */
    public function attendance(): BelongsTo
    {
        return $this->belongsTo(attendance::class);
    }
}
