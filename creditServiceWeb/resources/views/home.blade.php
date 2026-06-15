@extends('layouts.app')

@section('content')

    <div class="container">
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
                        $clientList= $clients ?? '' ->toJson();
                        $debtorList= $debtors ?? '';
                        $errorList = $errors ?? '';
                        $storeList = $stores ?? '';
                        $errorList -> toJson();
                        
                        @endphp

                        <!--Session error -->
                        @if (session('success'))

                            <div class="alert alert-success">

                                {{ session('success') }}

                            </div>

                        @endif

                        @if (session('error'))

                            <div class="alert alert-danger">

                                {{ session('error') }}

                            </div>
                          @endif
                          <!--session error end-->
                          
                                                      @if (count($clientList) >= 1)
                                                          @foreach ($clientList as $client)
                                                       @php
                                                           $id =  $client->id;

                                                       @endphp     
                                                          
                                                          @endforeach
                                                          @else
                                                          <p>No clients </p>
                                                      @endif

                              
                                                           @php
                                                               
                                                               $userl=Auth::user();
                                                               $userl -> toJson();
                                                               $userid=Auth::user()->id;
                                                           @endphp     
                                                         
                                                       
                        <tabs-nav :clients="{{ $clientList }}" :debtors="{{ $debtorList }}" :stores="{{$storeList}}"
                                action="{{ route('store') }} " :errors="{{ $errors }}" update="{{ route('update',$id) }} " remove="{{ route('delete',$id) }} "
                        :userl="{{$userl}}" editinfo="{{route('editinfo',$userid)}}" Msgaction="{{ route('Mstore') }} "></tabs-nav>
                      
                        {{-- @include('include.messages') --}}

                            <!-- =========== END TABS ======== -->

                            {{-- {{ gettype($clients) }} --}}

                    </div>
                </div>
            </div>
        </div>
    </div>




@endsection
