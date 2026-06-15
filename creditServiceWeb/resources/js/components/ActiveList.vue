<template>

  <div
  
    class="tab-pane fade show active"
    id="nav-users"
    role="tabpanel"
    aria-labelledby="nav-users-tab"
  >
  <div class="row mt-4">
    <div class=" srch col-9">
      <label class="sr-only " for="inlineFormInputGroup">Search ...</label>
      <div class="input-group mb-2 sss">
        <div class="input-group-prepend">
          <div class="input-group-text">
            <i class="fas fa-search"></i>
          </div>
        </div>
        <input
          type="text"
          class="form-control w-50 inputS"
          id="inlineFormInputGroup"
          placeholder="Search ..."
        />
      </div>
    </div>
     <div class="col-3"> <button data-toggle="collapse" href="#collapseExample" role="button" aria-expanded="false" aria-controls="collapseExample" type="button" class="btn btn-outline-info" >Add a new User <i class="fas fa-user-plus"></i></button></div>
   
    <div class="collapse" id="collapseExample">

<!--Add new form-->
<form :action=action >

  <div class="form-row">
    <div class="form-group col-md-4">
      <label for="inputUsername">Username</label>
      <input type="text" class="form-control" id="inputUsername" name="username">
    </div>
    <div class="form-group col-md-4">
      <label for="inputFirstname">Firstname</label>
      <input type="text" class="form-control" id="inputFirstname" name="firstname">
    </div>
    <div class="form-group col-md-4">
      <label for="inputLastname">Lastname</label>
      <input type="text" class="form-control" id="inputLastname" name="lastname">
    </div>

    <select class="custom-select" name="storeName" >
  <option  selected>Open this select menu</option>


  <option  v-for="(store, index) in stores"
                    :key="index" :value="store['id']" >{{store['store_name'] }}</option>

   </select>
    
  </div>
  <div class="form-group">
    <label for="inputAddress">Mac Address</label>
    <input type="text" class="form-control" id="inputAddress" name="mac" placeholder="00:00:00:00:00:00:">
  </div>
 
  <div class="text-center"><button type="submit" class="btn btn-primary">Add A New User <i class="fas fa-plus"></i></button></div>
</form>

<!---->

    </div>
   </div>
    <div v-if="errors != 0" class="text-danger">
   {{ errors.username[0]}} <br>
   {{errors.firstname[0]}} <br>
    {{errors.lastname[0]}} <br>
    {{errors.mac[0]}} 
    </div>

    <!--===============================================-->

    <div class="accordion" id="accordionExample" v-if="clients.length !=0">
  <div class="card" v-for="(client, index) in clients"
                    :key="index" >
                    <div v-if="client['status']==1" class=" filtr">
    <div class="card-header" id="headingOne">
      <h2 class="mb-0">
        <button class="btn btn-link btn-block text-left " >

<div class="row">
              <div class="col-2 containr"> <img src="/assets/users.jpg"></div>
               <div class="col-7 srch" type="button" data-toggle="collapse" :data-target="'#j'+client['id']" aria-expanded="true" :aria-controls="'j'+client['id']"><h4 class="mr-3 mt-4 font-weight-bolder saw3d" id="demo">{{client['firstname']}} {{client['lastname']}} </h4><h6 class=" font-weight-bolder">{{stores[client['store_id']-clients[0]['store_id']]['store_name']}}</h6> <p>{{client['created_at']}}</p><p :id="'b'+index" style="display:inline"></p><p :id="'h'+index" style="display:inline"></p></div>
               <div class="col-3 "> 
                 <!--Block confirmation-->
                 <button class="btn btn-outline-danger mt-4 " data-toggle="modal" :data-target="'#m'+client['id']" >Block <i class="fas fa-user-lock"></i></button>
               
               <!-- Modal -->
      <form :action="update">
<div class="modal fade" :id="'m'+client['id']" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLabel">Block confirmation</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        Are you sure you wanna block this user ? 
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
        <!-- <input type="hidden" name="_method" value="PUT"> -->
          <!-- <input type="hidden" name="id" :value="client['id']"> -->
        <button type="submit" class="btn btn-primary" >Block</button>

      </div>
    </div>
  </div>
</div>
      </form>  
                <!--Block confirmation end-->
               </div>
          </div>

        </button>
      </h2>
    </div>

    <div :id="'j'+client['id']" class="collapse" aria-labelledby="headingOne" data-parent="#accordionExample">
      <div class="card-body">
<!--peronal : username firstname lastname-->

<table class="table table-hover">
  <thead>
    <tr>
      <th scope="col">#</th>
      <th scope="col">FullName</th>
      <th scope="col">Current Debit</th>
    </tr>
  </thead>
  <tbody v-for="(debtor,index) in debtors" :key="index">
    <tr  v-if="client['store_id']==debtor['store_id']">
      <th scope="row">{{index+1}}</th>
      <td>{{debtor['firstname']}} {{debtor['lastname']}}</td>
      <td>{{debtor['current_debit']}}</td>
    </tr>
   
  </tbody>
</table>

<!--personal end-->
      </div>
    </div>
  </div>
</div>

</div>
   

    <!--=================================================-->

  </div>
</template>

<script>
export default {

   props:['clients','debtors','action','errors','stores','update'],
      mounted() {
        console.log(this.clients[0]['status']);
       console.log(this.action);
       console.log("sizzzzzzzzzze ====== "+this.clients['store_name'])
       var i;var str;var s = 0
       for ( i = 0 ; i<this.clients.length;i++){
  console.log('fff');
  document.getElementById('b'+i).style.display="inline";
  document.getElementById('h'+i).style.display="inline";
  str = this.clients[i]['created_at'];

var res = parseInt(str.slice(3,5)) ;

var dat=parseInt(new Date().getMonth()+1);
var result = res +6 - dat;
if (result >12){
  result = result -12;
}
var mydate = res+6;
if(mydate >12){
  mydate = res-6;
}
//mydate = dat;
if(mydate.toString().length==1){
  var mynew = str.slice(0,3)+"0"+mydate+""+str.slice(5,str.length-8);
}else{
  var mynew = str.slice(0,3)+""+mydate+""+str.slice(5,str.length-8);
}

if(document.getElementById('b'+i) != null){
 document.getElementById('b'+i).innerHTML="End Date : "+mynew+"   ";
  document.getElementById('b'+i).style.color="red";
  document.getElementById('h'+i).innerHTML=" ("+result+"  months left  )";
  document.getElementById('h'+i).style.color="blue";
}

  //document.getElementById('shit').innerHTML=0;
if(dat == mydate && document.getElementById('b'+i) != null){
   document.getElementById('h'+i).style.color="white";
  document.getElementById('h'+i).style.background="red";
  document.getElementById('shit').innerHTML=s+1;
}
  //console.log(x);
  console.log(res);
  console.log(mydate.toString().length);
}
    }
};

//add user slider

$(document).ready(function(){
  $("#flip").click(function(){
    $("#panel").slideToggle("slow");
  });
});

//search filter slider

$(document).ready(function(){
  $(".inputS").on("keyup", function() {
    var value = $(this).val().toLowerCase();
    $(".filtr").filter(function() {
      $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
    });
  });
});



</script>

<style scoped>
.inputS {
  max-width: 150px;
  transition: max-width 2s;
}
.inputS:focus {
  max-width: 50%;
}
img{
    width: 80px;
    height: 75px;
    border-radius: 43px;
}
.containr{
        background: #30336b;
    padding: 16px;
}
p{
    font-size: 12px;
}

#collapseExample{
  padding: 15px; 
  background-color: #b5c5c552;
}



</style>