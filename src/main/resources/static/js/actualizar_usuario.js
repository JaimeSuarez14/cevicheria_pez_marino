document.addEventListener('DOMContentLoaded', function () {
    let dataUsuarios = [];
    let username = "";

    async function cargarDatosVentas() {
        try {
            const response = await fetch("/administracion/dataUsuarios");
            dataUsuarios = await response.json();
            console.log(dataUsuarios) // Simplificado
        } catch (error) {
            console.error("Error al cargar datos:", error);
        }
    }

    //datos del formulario
    const nombreUpdate = document.getElementById("nombreEditar");
    const emailUpdate = document.getElementById("emailUpdate");
    const direccionUpdate = document.getElementById("direccionUpdate");
    const telefonoUpdate = document.getElementById("telefonoUpdate");
    const botonesEditar = document.querySelectorAll(".botonEditar")
    const btnGuardar = document.getElementById("btnGuardar");

    //al guardar el modal
    btnGuardar.addEventListener('click', guardarCambios);

    // Función para manejar el clic
    function manejarClickEditar(event) {
        const boton = event.currentTarget;
        username = boton.getAttribute('data-username');
        console.log("ID usuario:", username);

        // Aquí puedes cargar los datos del usuario en el modal
        const  usuario = dataUsuarios.find(u => u.username == username);
        console.log("Usuario encontrado:", usuario);
        if (usuario!=null) {
            nombreUpdate.value = usuario.nombre || '';
            emailUpdate.value = usuario.email || '';
            direccionUpdate.value = usuario.direccion || '';
            telefonoUpdate.value = usuario.telefono || '';

        }
    }

    // Cargar datos y configurar eventos
    cargarDatosVentas().then(() => {
        console.log("Datos cargados:", dataUsuarios);

        // Configurar event listeners para TODOS los botones de editar
        botonesEditar.forEach(boton => {
            boton.addEventListener('click', manejarClickEditar);
        });
    });

    function guardarCambios() {
        const nombre = nombreUpdate.value.trim();
        const email = emailUpdate.value.trim();
        const direccion = direccionUpdate.value.trim();
        const telefono = telefonoUpdate.value.trim();

        if (!username || !nombre || !email || !direccion || !telefono ) return;

        const usernameUpdate = username;

        const datos = { 
            username: usernameUpdate,
            nombre: nombre,
            email: email,
            direccion: direccion,
            telefono: telefono
        };

        fetch(`/administracion/actualizarUsuario/${usernameUpdate}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(datos)
        })
            .then(response => {
                if (response.ok) {
                    alert('Cambios guardados correctamente');
                    location.reload(); // Recargar la página
                }
            });
    }

});


