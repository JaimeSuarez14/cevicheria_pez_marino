document.addEventListener("DOMContentLoaded", () => {
    const precioTotal = document.querySelector(".precioTotal");
    const pt = document.querySelector(".pt");
    let carrito = []; 

    function actualizarprecioTotal() {
        let total = 0;

        const preciostotales = carrito.map( (p, index) => {
            return parseFloat(p.producto.precio) * parseInt(p.cantidad);
        } )

        preciostotales.forEach(p => {
            total += parseFloat(p);
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
            
            if(Array.isArray(data) && data.length > 0){
                carrito = data.map(e => e);
                showNotification('Carrito Cargado!', 'success');
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

    async function inicializarPagina() {
        const carritoCargado = await recogerDatosCarrito();
        console.log(carrito)
        if (carritoCargado) {
            actualizarprecioTotal();
            showNotification('Página cargada y carrito inicializado.', 'info');
        } else {
            showNotification('No se pudo cargar el carrito o está vacío.', 'warning');
        }
    }

    inicializarPagina()
})