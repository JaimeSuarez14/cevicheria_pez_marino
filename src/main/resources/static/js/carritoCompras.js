document.addEventListener('DOMContentLoaded', function() {
    cargarBarraProgreso(1);

    const cantidadProducto = document.querySelectorAll('.seleccionCantidad');  
    const precioTotalProducto = document.querySelectorAll('.precioTotalProducto');
    const precioTotal = document.querySelector(".precioTotal");
    const pt = document.querySelector(".pt");
    let carrito = []; 

    function actualizarCantidad(){
        cantidadProducto.forEach((inputElement, index) => {
            inputElement.addEventListener('change', async (e) => {
                const nuevaCantidad = parseInt(e.target.value);
                const idProducto = carrito[index].producto.id;
                if(nuevaCantidad==0){
                    await quitarProducto(idProducto);
                    return;
                }
                carrito[index].cantidad = nuevaCantidad;
                const precioF = parseFloat(carrito[index].producto.precio) * nuevaCantidad;
                precioTotalProducto[index].textContent = precioF.toFixed(2);
                actualizarprecioTotal();
                actualizarCarrito();
                showNotification('Carrito Actualizado..!!', 'success');
            });
        });  
    }  

    function actualizarprecioTotal() {
        let total = 0;

        precioTotalProducto.forEach(p => {
            total += parseFloat(p.textContent);
        });
        precioTotal.textContent = total.toFixed(2);
        pt.textContent = total.toFixed(2);
    }

    async function recogerDatosCarrito(){
        try{
            const response = await fetch('/carrito/obtenerCarrito');

            if (!response.ok) {
                throw new Error(`Error HTTP! Estado: ${response.status}`);
            }

            const data = await response.json();
            
            if(data && data.length > 0){
                carrito = data;
                return true;
            }else{
                return false;
            }
        }catch(error){
            console.error(error);
            return false;
        }

    }

    function showNotification(message, type) {
        // Crear elemento de notificación
        const notification = document.createElement('div');
        notification.className = `alert alert-${type} alert-dismissible fade show position-fixed`;
        notification.style.top = '20px';
        notification.style.right = '20px';
        notification.style.zIndex = '9999';
        notification.style.minWidth = '300px';
        notification.style.fontWeight="bold";
        notification.style.fontStyle="italic";
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

    async function actualizarCarrito(){
        
        try{
            const response = await fetch('/carrito/productos', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(carrito)
            });
            if (!response.ok) {
                // Manejar errores si el POST no fue exitoso
                const errorData = await response.json();
                throw new Error(errorData.mensaje || 'Error al procesar el carrito');
            }
    
            const data = await response.json(); // Recibe el JSON de éxito del POST
            console.log(data.mensaje); // "Carrito procesado con éxito!"

            
        }catch(error){
            console.error("Error al enviar el carrito:", error);
            let mensajeError = "Error al enviar el carrito";
            
            if (error.message) {
                mensajeError += ": " + error.message;
            }
            
            alert(mensajeError);
        }
    }

    async function quitarProducto (id) {
        
        try{
            const response = await fetch(`/carrito/elimarPCarrito/${id}`, {
                method: 'POST',
                
                redirect: 'follow'
            });


            if(response.redirected) {
                // Forzamos al navegador a ir a la URL final después de la redirección
                window.location.href = response.url;
            } else if (!response.ok) {
                // Si la respuesta no fue una redirección y no fue exitosa (ej. 400, 500)
                const errorText = await response.text();
                console.error("Error al eliminar el producto (sin redirección esperada):", errorText);
                showNotification('Error al eliminar el producto.', 'error');
                alert('Error al eliminar el producto: ' + errorText.substring(0, 100));
            }

            
        }catch(error){
            console.error("Error al enviar el carrito:", error);
            let mensajeError = "Error al enviar el carrito";
            
            if (error.message) {
                mensajeError += ": " + error.message;
            }
            
            alert(mensajeError);
        }
    }

    // ORDEN DE EJECUCIÓN CRÍTICO AL INICIO
    async function inicializarPagina() {
        const carritoCargado = await recogerDatosCarrito();
        if (carritoCargado) {
            actualizarCantidad(); // Ahora el 'carrito' ya está lleno cuando se adjuntan los listeners
            actualizarprecioTotal(); // Calcula los totales iniciales
            // showNotification('Página cargada y carrito inicializado.', 'info'); // Opcional
        } else {
            showNotification('No se pudo cargar el carrito o está vacío.', 'warning');
        }
    }
    
    inicializarPagina();

});