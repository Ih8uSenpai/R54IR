role = sessionStorage.getItem('role')
if (role !== 'READER' && role !== 'ADMIN'){
    location.href='/auth/login';
}