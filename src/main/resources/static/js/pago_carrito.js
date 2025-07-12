const mp = new MercadoPago('TEST-36d6c7ee-efbf-4767-b016-b1d8985e2cc9', {
    locale: 'es-PE'
});

// Función principal para crear la preferencia de pago
const MP = async () =>{
    
    try {
    // Crear objeto con los datos del artículo
    const productoCart = {
        nombre : "ceviche",
        precio : 25.00
    }
    const mlArticulo = {
        producto: productoCart,
        cantidad: 1
    };

    //Enviar datos al backend para crear la preferencia
        const response = await fetch("/api/mp", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(mlArticulo)
        });
        
        if (!response.ok) {
            throw new Error('Error al crear preferencia');
        }
        
        const preference = await response.text(); // Cambiado de text() a json()
        createCheckoutButton(preference); // Pasar solo el ID de preferencia
        
    } catch(e) {
        console.error("Error:", e);
        alert("Ocurrió un error al procesar el pago. Por favor intenta nuevamente.");
    }
}

// Función para crear el botón de pago
const createCheckoutButton = (preferenceId) => {
    const bricksBuilder = mp.bricks();
    
    // Eliminar botón existente si hay uno
    if (window.checkoutButton) {
        window.checkoutButton.unmount();
    }
    
    // Crear nuevo botón de pago
    bricksBuilder.create("wallet", "wallet_container", {
        initialization: {
            preferenceId: preferenceId, // ID de la preferencia creada
        },
        callbacks: {
            onReady: () => {
                console.log("Botón de pago listo");
            },
            onError: (error) => {
                console.error("Error en el botón de pago:", error);
                alert("Error al cargar el método de pago");
            }
        }
    });
}

// Llamar a la función cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', () => {
    MP();
});