<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;
use App\Clients;
use App\Debtor;
use App\Store;
use App\User;

class HomeController extends Controller
{
    /**
     * Create a new controller instance.
     *
     * @return void
     */
    public function __construct()
    {
        $this->middleware('auth');
    }

    /**
     * Show the application dashboard.
     *
     * @return \Illuminate\Contracts\Support\Renderable
     */
    public function index()
    {
     //Stores
     $store = Store::all();
     $store->toJson();

        //Clients
     $clients = Clients::all();
        // $clients = Clients::orderBy('firstname','desc')->paginate(1);
     $clients->toJson();
     //Debtors
     $debtors = Debtor::all();
     $debtors->toJson();
     return view('home')->with('clients',$clients)->with('debtors', $debtors)->with('stores', $store);
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
        $this->validate($request,[
               'username'=> 'required',
               'firstname'=> 'required',
               'lastname'=> 'required',
                'mac' => 'required'
         ]);
        //create new client

        $client = new Clients ;
        $client ->username = $request->input('username');
        $client ->firstname = $request->input('firstname');
        $client ->lastname = $request->input('lastname');
        $client ->mac = $request->input('mac');
        $client ->store_id = $request->input('storeName');
        $client->save();

        return redirect('/') ->with('success','New user has been added successufly');
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
         //update infos admin

         $user = User::find($id) ;
         $user ->name = $request->input('username');
         $user ->lastname = $request->input('fullname');
         $user ->email = $request->input('email');
         if($request->input('password') != "shit" ){
             $user ->password =Hash::make( $request->input('password'));
         }
         $user->save();
 
         return redirect('/') ->with('success','informations changed successfuly ');
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function destroy($id)
    {
      

    }
}
