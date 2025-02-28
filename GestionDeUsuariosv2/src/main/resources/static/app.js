//Comprobamos si la respuesta de una solicitud es 403 o 401 y redirigirnos al login

async function fetchWithAuth(url, options = {}) {
    const token = localStorage.getItem("jwtToken"); //Obtenemos el token

    //Agregamos el token al header si existe
    if (token) {
        options.headers = {
            ...options.headers,
            "Authorization": `Bearer ${token}`
        };
    }

    const response = await fetch(url, options);

    //Si no estamos autorizados, redirigimos al login
    if (response.status === 403 || response.status === 401) {
        window.location.href = "/public/login"; //Nos redirigimos al login
        return null;
    }

    return response;
}

//Funcion para verificar si el usuario esta autenticado
function checkAuth() {
    const token = localStorage.getItem("jwtToken");

    // Si no hay token y estamos en una ruta privada, redirigir al login
    if (!token && window.location.pathname.startsWith("/admin")) {
            window.location.href = "/public/login";
    }
}

//Llamamos al chechAuth() en las páginas privadas
document.addEventListener("DOMContentLoaded", () => {
    checkAuth();
});