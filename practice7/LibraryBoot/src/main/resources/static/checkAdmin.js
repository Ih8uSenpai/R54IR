role = sessionStorage.getItem('role')
if (role !== 'ADMIN'){
    location.href='/auth/login';
}