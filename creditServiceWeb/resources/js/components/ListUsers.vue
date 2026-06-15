<template>
  <div id="app">
    <div class="text-center p-2 border-bottom">
    <router-link to="/" class=" rounded-pill p-2 bkgreen text-white" id="activeU">Active Users</router-link> |
    <router-link to="/b" class="text-danger" id="blockU">Blocked Users</router-link>
    </div>
     <router-view :clients=clients :debtors=debtors :action=action :errors=errors :stores=stores :update=update :remove=remove></router-view>
  </div>
</template>

<script>
export default {
    props:['clients','debtors','action','errors','stores','update','remove'],
      mounted() {
        console.log(this.clients[0]['created_at']);
       console.log(this.action);
    }
}



//search filter slider

$(document).ready(function(){
  $("#activeU").click(function(){
    $("#activeU").addClass("rounded-pill p-2 bkgreen text-white");
    $("#blockU").addClass(" bg-transparent text-danger");
  });

   $("#blockU").click(function(){
    $("#blockU").removeClass("text-danger").addClass("rounded-pill p-2 bkred text-white");
    $("#activeU").removeClass("bkgreen text-white").addClass(" bg-transparent text-success");
  });
  
});


//

</script>

<style scoped>


#activeU:hover{
  text-decoration: none;
}
#blockU:hover{
  text-decoration: none;
}

.bkred{
  background-color: #eb4d4b;
}
.bkgreen{
  background-color: #20bf6b;
}
</style>