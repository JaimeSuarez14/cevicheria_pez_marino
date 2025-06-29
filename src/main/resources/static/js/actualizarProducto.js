document.addEventListener("DOMContentLoaded",()=>{

    const model = document.getElementById("createUserModal");
    const id = document.getElementById("newId");
    const nombre = document.getElementById("newNombre");
    const descripcion = document.getElementById("newDescripcion");
    const categoria = document.getElementById("newCategoria");
    const precio = document.getElementById("newPrecio");
    const stock = document.getElementById("newStock");

    model.addEventListener('shown.bs.modal',llenarDatos)

    function llenarDatos(event){
        let button = event.relatedTarget;; // Botón que activó el modal
        let idBase = button.dataset.id;
        let nombreBase = button.dataset.nombre;
        let descripcionBase = button.dataset.descripcion;
        let categoriaBase = button.dataset.categoria;
        let precioBase = button.dataset.precio;
        let stockBase = button.dataset.stock;

        id.value = idBase;
        nombre.value = nombreBase;
        descripcion.value = descripcionBase;
        categoria.value = categoriaBase;
        precio.value = precioBase;
        stock.value = stockBase;
    }

    const formulario = document.getElementById("actualizarProductForm");
    formulario.addEventListener('submit',function(event){
        event.preventDefault();
        const idActualizado = id.value;
        const nombreActualizado = nombre.value;
        const descripcionActualizado = descripcion.value;
        const categoriaActualizado = categoria.value;
        const precioActualizado = precio.value;
        const stockActualizado = stock.value;

        //validamos que no esten vacias
        if (!idActualizado || !nombreActualizado || !descripcionActualizado || !categoriaActualizado || !precioActualizado || !stockActualizado) {
            alert("Por favor, complete todos los campos.");
            return;
        }

        const formData = new FormData(formulario);

        fetch(`/administracion/actualizarProducto/${idActualizado}`, {
            method: "POST",
            body: formData
        })
        .then(response => response.json())
        .then(data => {
            // Cerrar el modal
            var modal = bootstrap.Modal.getInstance(model);
            modal.hide();
            
            // Mostrar mensaje de éxito
            showNotification(data.success || 'Operación exitosa', 'success');
            
            // Recargar la página para ver los cambios
            setTimeout(() => {
                window.location.reload();
            }, 1100);

            
        })
        .catch(error => console.error(error));
    })

    function showNotification(message, type) {
        // Crear elemento de notificación
        const notification = document.createElement('div');
        notification.className = `alert alert-${type} alert-dismissible fade show position-fixed`;
        notification.style.top = '20px';
        notification.style.right = '20px';
        notification.style.zIndex = '9999';
        notification.style.minWidth = '300px';
        notification.role = 'alert';
        notification.style.transition = 'all 0.5s ease-in-out';

        notification.innerHTML = `
                    ${message}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                `;

        document.body.appendChild(notification);

        // Eliminar después de 3 segundos
        setTimeout(() => {
            notification.remove();
        }, 3000);
    }
})