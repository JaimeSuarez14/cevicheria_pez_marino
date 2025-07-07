document.addEventListener('DOMContentLoaded', function() {

    const badge = document.getElementById('badge');
    const addToCartButtons = document.querySelectorAll('.btn-add-to-cart');
    let carrito = [];
    
    
    addToCartButtons.forEach(button => {
        button.addEventListener('click', async function(e) {

            const idProducto = e.target.dataset.id;
            const idNumber = parseInt(idProducto);

            try{
                const responseProducto = await fetch(`/administracion/verProducto/${idNumber}`);

                //esta  opcion es para validar que la respuesta es correcta
                //si la respuesta no es correcta, se lanza un error
                if (!responseProducto.ok) {
                    throw new Error(`Error HTTP! Estado: ${responseProducto.status}`);
                }

                const productoData = await responseProducto.json();

                if(carrito.some(item => item.producto.id === productoData.id)){
                    const index = carrito.findIndex(item => item.producto.id === productoData.id);
                    carrito[index].cantidad++;
                }else{
                    carrito.push({producto: productoData, cantidad: 1});
                }

                // Actualizar el contador del badge (UI inmediata)
                asignarCantidad()

                // Paso 4: Enviar el carrito ACTUALIZADO al backend
                // Esta es la llamada asíncrona que sincroniza el carrito local con el servidor
                await enviarCarritoAlBackend(); // Nueva función para claridad

                showNotification('Producto Agregado al Carrito!', 'success');
            }catch(error){
                console.error(error);
            }
        });
    });

    async function enviarCarritoAlBackend(){

        if(validaSiAgregaronAlCarrito()){
            return;
        }
        
        try{
            const response = await fetch('/carrito/productos', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(carrito)
            });

            // if(response.ok){
            //     console.log("Carrito enviado correctamente");
            //     alert("Bienvenido al carrito");
            // }else{
            //     console.log("Error al enviar el carrito");
            //     alert("Error al enviar el carrito");
            // }

            if (!response.ok) {
                // Manejar errores si el POST no fue exitoso
                const errorData = await response.json();
                throw new Error(errorData.mensaje || 'Error al procesar el carrito');
            }
    
            const data = await response.json(); // Recibe el JSON de éxito del POST
            console.log(data.mensaje); // "Carrito procesado con éxito!"|
            
        }catch(error){
            console.error("Error al enviar el carrito:", error);
            let mensajeError = "Error al enviar el carrito";
            
            if (error.message) {
                mensajeError += ": " + error.message;
            }
            
            alert(mensajeError);
        }
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

    function asignarCantidad(){
        let cantidad = carrito.reduce((item, index) => {
            return item + index.cantidad;
        }, 0);

        badge.textContent = cantidad;
    }

    recogerDatosCarrito().then((hayCarrito) => {
        if(hayCarrito){
            asignarCantidad();
        }
    }).catch(error => {
        console.error("Error en la inicialización de recogerDatosCarrito:", error);
    });;

    
    function validaSiAgregaronAlCarrito(){
        if(!(parseInt(badge.textContent) > 0)){
            alert("No has agregado ningun producto al carrito");
            return true;
        }
        return false;
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
        notification.role = 'alert';
        notification.style.transition = 'all 0.5s ease-in-out';
        notification.style.border = "1px solid green";

        notification.innerHTML = `
                    ${message}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                `;

        document.body.appendChild(notification);

        // Eliminar después de 3 segundos
        setTimeout(() => {
            notification.remove();
        }, 1000);
    }

    
});


