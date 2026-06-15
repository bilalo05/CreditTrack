<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Clients extends Model
{
    //table name
    protected $table  = 'clients';
    //primary key
    public $primaryKey = 'id';
    //timestamp
    public $timestamp = true ;
    //protected $dateFormat = 'Y-m-d\TH:i:s';


    protected $casts = [
        'created_at' => 'datetime:d-m-yy h:m:s',
        'updated_at' => 'datetime:d-m-y h:m:s',
    ];
}
