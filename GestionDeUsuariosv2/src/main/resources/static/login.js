document.getElementById("login-form").addEventListener("submit", async function(event) {
    event.preventDefault(); // Evita que el formulario se envíe de la forma tradicional

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;
    const errorMessage = document.getElementById("error-message");  //Asegurar que el ID es correcto

    try {
        const response = await fetch("/auth/login", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({username, password})
        });

        if(response.ok) {
            const data = await response.json();
            localStorage.setItem("jwtToken", data.token);   // Guarda el token en localStorage
            window.location.href = "/admin/prueba"; // Redirige al área privada
        } else {
            errorMessage.style.display = "block";
            errorMessage.textContent = "Usuario o contraseña incorrectos"; // Asegurar mensaje claro
        }
    } catch (error){
        console.error("Error en el login:", error);
        errorMessage.style.display = "block";
        errorMessage.textContent = "Error en el servidor. Inténtalo más tarde.";
    }
});