<template>
    <div class="container text-center">
  <div class="row">
    <div class="col " id="price" style="background-image : url('/assets/green-scree.png') ; background-repeat: round; margin-block:25px">
      25 DA
    </div>
    <div class="col " >
     <div class="form-group ">
      <label for="inputPrix" >Prix</label>
      <input type="text" class="form-control" id="inputPrix" name="prix" style="height:100px">
    </div>
    </div>
   
  </div>
   <div class="row">
    <div class="col ">
       <div class="text-center"><button type="submit" class="btn btn-primary">Confirmer <i class="fas fa-plus"></i></button></div>
    </div>
    <div class="col ">
         <div class="form-group ">
      <label for="inputQuantite">Quantité</label>
      <input type="text" class="form-control" id="inputAuantite" name="quantite" style="height:70px ">
    </div>
    </div>
   
  </div>
  <div class="row">
    <table class="table table-striped table-bordered border-primary">
 <thead>
    <tr>
      <th scope="col">Produit</th>
      <th scope="col">Prix Unitaire</th>
      <th scope="col">Quantité</th>
      <th scope="col">Prix</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th scope="row">sdf</th>
      <td>25</td>
      <td>2</td>
      <td>50</td>
    </tr>
    <tr>
      <th scope="row">2</th>
      <td>100</td>
      <td>3</td>
      <td>300</td>
    </tr>
    <tr>
      <th scope="row">#</th>
      <td colspan="2">Total</td>
      <td> 350 DA </td>
    </tr>
  </tbody>
</table>
  </div>
</div>
</template>

<script>
export default {
    props:['clients','debtors','action','errors','sessionerr','stores','update','remove','userl','editinfo','msgaction'],
      mounted() {
        //console.log('111'+this.clients[0]['firstname']);
        console.log('222'+this.debtors);
        console.log('333'+this.action);
        console.log('444'+this.errors);
        console.log('555'+this.sessionerr);
        console.log('666'+this.userl);
    }
}
</script>
<style scoped>
.col{ padding: 25px;}
#price{ font-size: 30pt;}
</style>