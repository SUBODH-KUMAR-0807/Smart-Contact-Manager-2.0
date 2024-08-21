console.log("contact.js");
const baseURL ="http://localhost:8080";

//take code help from flowbite: modals javascript behaviour

// set the modal menu element

//The data of contact_modals.html of modal section is fetch by this function.
// view_contact_modal is a id which exist in contact_modals.html in Main modal section.

const viewContactModal = document.getElementById("view_contact_modal");

// options with default values
const options = {
    placement: 'bottom-right',
    backdrop: 'dynamic',
    backdropClasses:
        'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
    closable: true,
    onHide: () => {
        console.log('modal is hidden');
    },
    onShow: () => {
        console.log('modal is shown');
    },
    onToggle: () => {
        console.log('modal has been toggled');
    },
};

// instance options object
const instanceOptions = {
  id: 'view_contact_modal',
  override: true
};

const contactModal = new Modal(viewContactModal, options, instanceOptions);


function openContactModal(){
  contactModal.show();
}


// call this function from contact_modals.html
function closeContactModal(){
  contactModal.hide();
}

// This function call by button section from contacts.html
async function loadContactData(cid){

  // function call to load data

  // fetch data, call  getContact() method of ApiController.java

  console.log(cid);

  try{
    const data = await(
      await fetch(`${baseURL}/api/contacts/${cid}`)).json();

      console.log(data);

      //contact_name is id of h block in contact_modals.html

      document.querySelector('#contact_name').innerHTML=data.name;
      document.querySelector('#contact_email').innerHTML=data.email;
      document.querySelector("#contact_image").src=data.picture;
      document.querySelector("#contact_address").innerHTML=data.address;
      document.querySelector("#contact_phone").innerHTML=data.phoneNumber;
      document.querySelector("#contact_description").innerHTML=data.description;

      const contactFavorite = document.querySelector("#contact_favorite");

      if (data.favorite) {
      contactFavorite.innerHTML =
        "<i class='fas fa-star text-yellow-400'></i><i class='fas fa-star text-yellow-400'></i><i class='fas fa-star text-yellow-400'></i><i class='fas fa-star text-yellow-400'></i><i class='fas fa-star text-yellow-400'></i>";
      } 
      else {
      contactFavorite.innerHTML = "Not Favorite Contact";
      }

      // href use: when we click on website text, we can go on website.
      document.querySelector('#contact_website').href=data.websiteLink;
      document.querySelector('#contact_website').innerHTML=data.websiteLink;
      document.querySelector('#contact_linkedIn').href=data.linkedInLink;
      document.querySelector('#contact_linkedIn').innerHTML=data.linkedInLink;

      // it call itself
      openContactModal();


  }
  catch(error){
    console.log("Error: ",error);
  }

}


 //delete contact request come from contacts.html or search.html

async function deleteContact(cid){

 // This code download from sweetalert website.
  Swal.fire({
    title: "Do you want to delete the contact?",
    icon: "warning",
    showCancelButton: true,
    confirmButtonColor: "#3085d6",
    cancelButtonColor: "#d33",
    confirmButtonText: "Yes, delete it!"
    
  
  }).then((result) => {
    if (result.isConfirmed){

      const url =`${baseURL}/user/contacts/delete/`+cid;
      window.location.replace(url);
    }
    
  });

}



