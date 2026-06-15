<template>
    <div>
      <!---->
<ul class="nav nav-tabs" id="myTab" role="tablist">
   <li class="nav-item" role="presentation">
    <a class="nav-link active" id="pay-tab" data-toggle="tab" href="#pay" role="tab" aria-controls="pay" aria-selected="true"><i class="fas fa-solid fa-cash-register pr-1"></i>Caisse</a>
  </li>
  <li class="nav-item" role="presentation">
    <a class="nav-link" id="home-tab" data-toggle="tab" href="#home" role="tab" aria-controls="home" aria-selected="false"><i class="fas fa-users pr-1"></i>Utilisateurs</a>
  </li>
  <li class="nav-item" role="presentation">
    <a class="nav-link" id="profile-tab" data-toggle="tab" href="#profile" role="tab" aria-controls="profile" aria-selected="false"><i class="fas fa-user-circle pr-1"></i>Profile</a>
  </li>
  <li class="nav-item" role="presentation">
    <a class="nav-link" id="contact-tab" data-toggle="tab" href="#contact" role="tab" aria-controls="contact" aria-selected="false"><i class="far fa-envelope pr-1"></i>Contact</a>
  </li>
</ul>
<div class="tab-content" id="myTabContent">
    <div class="tab-pane fade show active" id="pay" role="tabpanel" aria-labelledby="pay-tab">
                              <pay-form></pay-form>
    </div>
  <!-- USERS LIST-->
  <div class="tab-pane fade" id="home" role="tabpanel" aria-labelledby="home-tab">
                               <list-users :clients=clients :debtors=debtors :action=action :errors=errors :stores=stores :update=update :remove=remove ></list-users>
  </div>
  <!-- USERS LIST end-->
  <!--EDIT PROFILE-->
  <div class="tab-pane fade" id="profile" role="tabpanel" aria-labelledby="profile-tab">
    <user-profile :errors=errors :userl=userl :editinfo=editinfo></user-profile>
  </div>
  <!--EDIT PROFILE end-->
   <!--CONTACT SECTION-->
  <div class="tab-pane fade" id="contact" role="tabpanel" aria-labelledby="contact-tab">
     <contact-form :msgaction=msgaction :errors=errors :clients=clients></contact-form>
  </div>
   <!--CONTACT SECTION end-->
</div>



      <!---->
        

                      <!-- <div v-for="(client, index) in clients"
                    :key="index">
                    {{ client['firstname']}}
                    </div> -->
                       
    </div>
</template>

<script>
export default {
    
    props:['clients','debtors','action','errors','sessionerr','stores','update','remove','userl','editinfo','msgaction'],
      mounted() {
        console.log('111'+this.clients[0]['firstname']);
        console.log('222'+this.debtors);
        console.log('333'+this.action);
        console.log('444'+this.errors);
        console.log('555'+this.sessionerr);
        console.log('666'+this.userl);
    }
}
</script>