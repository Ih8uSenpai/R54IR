role = sessionStorage.getItem('role');
if (role !== 'USER'){
    var button = document.querySelector(".button");
    button.remove();
}