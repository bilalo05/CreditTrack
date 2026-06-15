<template>
  <div>
    
    <div class="card">
      <h5 class="card-header">{{userl['lastname']}}  Informations</h5>
      <div class="card-body">
    <div class="accordion" id="accordionExample">
<form :action="editinfo">
  <div class="card">
    <div class="card-header" id="headingOne">
      <h2 class="mb-0">
        <button class="btn btn-link btn-block text-left " type="button" data-toggle="collapse" data-target="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
        <i class="far fa-user"></i>  Personal Informations 
        </button>
      </h2>
    </div>

    <div id="collapseOne" class="collapse" aria-labelledby="headingOne" data-parent="#accordionExample">
      <div class="card-body">
<!--peronal : username firstname lastname-->



 <div class="input-group mb-3 mt-3">

  <div class="col">
        <label for="inputName">FullName</label>
      <input type="text" class="form-control" placeholder="Full-name" id="inputName" name="fullname" :value="userl['lastname']">
    </div>
  
</div>

 <div class="input-group mb-3 mt-3">

  <div class="col">
        <label for="inputUsername4">UserName</label>
      <input type="text" class="form-control" placeholder="Username" id="inputUsername4" name="username" :value="userl['name']">
    </div>
  
</div>
<div class="text-center mt-3"><button type="submit" class="btn btn-outline-info"><i class="far fa-edit pr-1"></i>Save changes</button></div>
<!--personal end-->
      </div>
    </div>
  </div>
  <div class="card">
    <div class="card-header" id="headingTwo">
      <h2 class="mb-0">
        <button class="btn btn-link btn-block text-left collapsed" type="button" data-toggle="collapse" data-target="#collapseTwo" aria-expanded="false" aria-controls="collapseTwo">
        <i class="far fa-envelope"></i>  Contact Informations
        </button>
      </h2>
    </div>
    <div id="collapseTwo" class="collapse" aria-labelledby="headingTwo" data-parent="#accordionExample">
      <div class="card-body">
<!--contact-->

    <div class="form-group col-md-6 ">
      <label for="inputEmail4">Email</label>
      <input type="email" class="form-control" id="inputEmail4" name="email" :value="userl['email']">
    </div>
  <div class=" mt-3 ml-3">  <button type="submit" class="btn btn-outline-info "><i class="far fa-edit pr-1"></i>Save changes</button></div>
<!--contact end-->
      </div>
    </div>
  </div>
  <div class="card">
    <div class="card-header" id="headingThree">
      <h2 class="mb-0">
        <button class="btn btn-link btn-block text-left collapsed" type="button" data-toggle="collapse" data-target="#collapseThree" aria-expanded="false" aria-controls="collapseThree">
<i class="fas fa-shield-alt"></i> Security Informations
        </button>
      </h2>
    </div>
    <div id="collapseThree" class="collapse" aria-labelledby="headingThree" data-parent="#accordionExample">
      <div class="card-body">
<!--security-->

<div class="form-group col-md-6 ">
      <label for="inputPassword4">Password</label>
      <input type="password" class="form-control" id="inputPassword4" name="password" value="shit">
    </div>
 <div class=" mt-3 ml-3">   <button type="submit" class="btn btn-outline-info"><i class="far fa-edit pr-1"></i>Save changes</button></div>

<!--security end-->
      </div>
    </div>
  </div>

</form>
  
</div>
   
    
  </div>


      </div>
    </div>
</template>

<script>
export default {
     props:['errors','userl','editinfo'],
       mounted() {
        
        console.log('0500'+this.userl['name']+' action =='+this.editinfo);
    }

}
</script>