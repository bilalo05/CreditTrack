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
          class="form-control w-50 "
          id="inlineFormInputGroup"
          placeholder="Search ..."
        />
      </div>
    </div>
      </div>

    <div v-if="errors != 0" class="text-danger">
   {{ errors.username[0]}} <br>
   {{errors.firstname[0]}} <br>
    {{errors.lastname[0]}} <br>
    {{errors.mac[0]}} 
    </div>

    <!--===============================================-->
    <div class="accordion" id="accordionExample">
  <div class="card" v-for="(client, index) in clients"
                    :key="index" >
                    <div v-if="client['status']==0">
    <div class="card-header" id="headingOne">
      <h2 class="mb-0">
        <button class="btn btn-link btn-block text-left " >

<div class="row">
              <div class="col-2 containr"> <img src="/assets/users.jpg"></div>
               <div class="col-4  " type="button" data-toggle="collapse" :data-target="'#j'+client['id']" aria-expanded="true" :aria-controls="'j'+client['id']"><h4 class="mr-3 mt-4 font-weight-bolder fullname">{{client['firstname']}} {{client['lastname']}} </h4><h6 class=" font-weight-bolder">{{stores[client['store_id']-clients[0]['store_id']]['store_name']}}</h6> <p>{{client['created_at']}}</p></div>
               <div class="col-6 mt-5 d-inline-block d-flex justify-content-end">
                 <form class="mr-2" :action="update">  <button type="submit" class="btn btn-outline-success  ">Unblock user <i class="fas fa-unlock-alt"></i></button> </form>
                
                    <!--Delete an user confirmation-->
                 <button class="btn  btn-outline-danger remov" data-toggle="modal" :data-target="'#md'+client['id']" >Delete User <i class="fas fa-ban"></i></button>
               
               <!-- Modal -->
      <form :action="remove">
<div class="modal fade" :id="'md'+client['id']" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLabel">Delete User</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        Are you sure you wanna delete this user from your system ? 
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
        <!-- <input type="hidden" name="_method" value="PUT"> -->
          <!-- <input type="hidden" name="id" :value="client['id']"> -->
        <button type="submit" class="btn btn-primary" >Confirm</button>

      </div>
    </div>
  </div>
</div>
      </form>  
                <!--Delete an user confirmation end-->
                  
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

<script >
export default {

   props:['clients','debtors','action','errors','stores','update','remove'],
      mounted() {
        console.log(this.clients[0]['created_at']);
       console.log(this.action);
    }
};


//search filter slider

// $(document).ready(function(){
//   $(".inputS").on("keyup", function() {
//     var value = $(this).val().toLowerCase();
//     $(".filtr").filter(function() {
//       $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
//     });
//   });
// });

//

//


// window.onbeforeunload = function() { 
//     window.setTimeout(function () { 
//         window.location = '/';
//     }, 0); 
//     window.onbeforeunload = null; // necessary to prevent infinite loop, that kills your browser 
// }

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
.button{
    display: inline-block;
}

.remov{
  height: 37px;
}
.remov:hover{
  text-decoration: none;
}
button:hover{
 text-decoration: none;
}

.fullname:hover{
  text-decoration: underline;
}
</style>