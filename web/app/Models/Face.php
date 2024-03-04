<?php

namespace App\Models;

use App\Base\Interfaces\HasEmployee;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class Face extends Model implements HasEmployee
{
    use HasFactory;

    protected $table = 'faces';
    protected $primaryKey = 'id';
    public $incrementing = false;
    public $keyType = 'char';

    protected $fillable = ['employee_id', 'photo'];
    protected $guarded = [];

    /**
     * employee
     *
     * @return BelongsTo
     */
    public function employee(): BelongsTo
    {
        return $this->belongsTo(Employee::class);
    }

}
