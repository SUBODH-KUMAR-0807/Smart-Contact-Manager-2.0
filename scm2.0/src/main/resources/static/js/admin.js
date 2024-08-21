console.log("admin user");

//if we write this code then Image is shown on AddContact Page
//query selector se object milega

document.querySelector("#image_file_input").addEventListener("change",function(event){

  //fetch first file. and load the file on screen.
  let file =event.target.files[0];
  let reader = new FileReader();

  reader.onload = function(){
    document.querySelector("#upload_image_preview").setAttribute("src",reader.result);
  };

  reader.readAsDataURL(file);

});




