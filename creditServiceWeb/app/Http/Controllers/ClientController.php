<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Clients;
use Carbon\Carbon;

class ClientController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
       // return Clients::all();
     $clients = Clients::all();
     $clients->toJson();
     return view('home')->with('clients',$clients);

    }

    /**
     * Show the form for creating a new resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function create()
    {
        //
    }

    /**
     * Store a newly created resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return \Illuminate\Http\Response
     */
    public function store(Request $request)
    {
        //
    }

    /**
     * Display the specified resource.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function show($id)
    {
        // $client = Clients::find($id);
        // return view('home')->with('client',$client);
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function edit($id)
    {
        $client = Clients::find($id);
        return view('home')->with('client',$client);
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, $id)
    {
        
     //block  client

     $client = Clients::find($id) ;

    
     if($client ->status==1){
       $client ->status = 0;
       $client->save();
       return redirect('/') ->with('success','A client has been sent to block list');
    }
        if($client ->status==0){
        $client ->status = 1;
        $client ->created_at = Carbon::now()->timestamp;
        $client->save();
        return redirect('/') ->with('success','A client has been Unblocked');

    }
     }
    


    /**
     * Remove the specified resource from storage.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function destroy($id)
    {
        $client  = Clients::find($id);
        $client ->delete();
        return redirect('/') ->with('success','A client has been removed');
    }
}
