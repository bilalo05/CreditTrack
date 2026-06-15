@extends('layouts.app')

@section('content')

<div class="container" >
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="card">
                <div class="card-header">{{ __('Dashboard') }}</div>

                <div class="card-body">
                    @if (session('status'))
                        <div class="alert alert-success" role="alert">
                            {{ session('status') }}
                        </div>
                    @endif

               <!--     {{ __('You are logged in!') }} -->
                    
                    <!-- =========== TABS =========== -->
                    @php
                     $clientList= $clients ?? '';
                    @endphp

<tabs-nav :clients="{{$clientList}}"></tabs-nav>
                  
                    <!-- =========== END TABS ======== -->
{{-- 
                    @if(count($clients)>=1)
                         @foreach($clients as $client)
                    <h3>{{$client->firstname}}</h3>
                         @endforeach
                    @else
                          <p>No clients </p>
                    @endif --}}

                    {{-- {{gettype($clients)}} --}}

                </div>
            </div>
        </div>
    </div>
</div>



  
@endsection
