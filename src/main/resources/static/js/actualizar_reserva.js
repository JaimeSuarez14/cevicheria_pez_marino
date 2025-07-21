document.addEventListener('DOMContentLoaded', function () {
    let dataReservas = [];
    let id = "";

    async function cargarDatosVentas() {
        try {
            const response = await fetch("/administracion/dataReservas");
            dataReservas = await response.json();
            console.log(dataReservas) // Simplificado
        } catch (error) {
            console.error("Error al cargar datos:", error);
        }
    }

    //datos del formulario
    const celularUpdate = document.getElementById("celularUpdate");
    const numPersonasUpdate = document.getElementById("numPersonasUpdate");
    const fechaUpdate = document.getElementById("fechaUpdate");
    const horaUpdate = document.getElementById("horaUpdate");
    const botonesEditar = document.querySelectorAll(".botonEditar")
    const btnGuardar = document.getElementById("btnGuardar");

    //al guardar el modal
    btnGuardar.addEventListener('click', guardarCambios);

    // Función para manejar el clic
    function manejarClickEditar(event) {
        const boton = event.currentTarget;
        id = boton.getAttribute('data-id');
        console.log("ID reserva:", id);

        // Aquí puedes cargar los datos del usuario en el modal
        const  reserva = dataReservas.find(reserva => reserva.id == id);

        if (reserva!=null) {
            celularUpdate.value = reserva.celular || '';
            numPersonasUpdate.value = reserva.numPersonas || '';
            fechaUpdate.value = reserva.fecha || '';
            horaUpdate.value = reserva.hora || '';

        }
    }

    // Cargar datos y configurar eventos
    cargarDatosVentas().then(() => {
        console.log("Datos cargados:", dataReservas);

        // Configurar event listeners para TODOS los botones de editar
        botonesEditar.forEach(boton => {
            boton.addEventListener('click', manejarClickEditar);
        });
    });

    function guardarCambios() {
        const celular = celularUpdate.value.trim();
        const numPersonas = numPersonasUpdate.value.trim();
        const fecha = fechaUpdate.value.trim();
        const hora = horaUpdate.value.trim();

        if (!id || !celular || !numPersonas || !fecha || !hora ) return;

        const idUpdate = id;

        const datos = { 
            id: idUpdate,
            celular: celular,
            numPersonas: numPersonas,
            fecha: fecha,
            hora: hora
        };

        fetch(`/adminReserva/actualizarReserva/${idUpdate}`, {
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


