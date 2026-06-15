<?php

use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| contains the "web" middleware group. Now create something great!
|
*/

/*
Route::get('/', function () {
    return view('welcome');
});
*/

Auth::routes();

 Route::get('/', 'HomeController@index')->name('home');
 Route::get('/s', 'HomeController@store')->name('store');
 Route::get('/update/{id}','ClientController@update')->name('update');
 Route::get('/delete/{id}','ClientController@destroy')->name('delete');
 Route::get('/editinfo/{id}','HomeController@update')->name('editinfo');
 Route::get('/msg', 'MessageController@store')->name('Mstore');
//   Route::get('/', 'ClientController@index')->name('home');
//Route::resource('/','ClientController');
// Route::get('/{id}', 'HomeController@edit')->name('edit');


//Route::get('/', 'HomeController@index')->name('home');