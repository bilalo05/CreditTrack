<template>
  <div>
    <form :action="msgaction">
        <!-- <div class="alert alert-info mt-2" role="alert">
You have new messages from user2 ! <span class="badge badge-danger float-right">4</span>
        </div>
        <div class="alert alert-info mt-0" role="alert">
You have new messages from user4 ! <span class="badge badge-danger float-right">2</span>
        </div> -->
          <select class="custom-select mt-4" name="username"  >
  <option  selected>Choose a client</option>

  <option v-for="(client, index) in clients"
                    :key="index"
                    :value="client['id']">{{client['firstname']}} {{client['lastname']}}</option>
 
   </select>
      <div class="form-group mt-4 font-weight-bolder">
        <label for="exampleFormControlTextarea1 ">Write your message here</label>
        <textarea class="form-control" id="exampleFormControlTextarea1" rows="3" name="content"></textarea>
      </div>
  <div class="text-center">    <button type="submit" class="btn btn-outline-info"><i class="far fa-paper-plane pr-1"></i>Send</button></div>
    </form>
    <div v-if="errors != 0" class="text-danger">
   {{ errors.content[0]}} <br>
 
    </div>
  </div>
</template>

<script>
export default {
    
    props:['clients','errors','msgaction'],
 
}
</script>