document.addEventListener('DOMContentLoaded', function () {
    function generarCodigoQR() {
        const inputText = document.getElementById('inputText').value;
        const qr = new QRious({
            element: document.getElementById('qrCanvas'),
            value: inputText,
            size: 200
        });
    }
    function calcularTotales() {

        const cantidades = document.querySelectorAll('.cantidad');
        const precios = document.querySelectorAll('.precio');
        let total = 0;

        for (let i = 0; i < cantidades.length; i++) {
            const cantidad = parseInt(cantidades[i].textContent);
            const precio = parseFloat(precios[i].textContent);
            total += cantidad * precio;
        }

        const subtotal = total / 1.18; // 18% IGV
        const igv = subtotal * 0.18;

        // Actualizar los totales en el DOM
        document.getElementById('subtotal').textContent = 'S/. ' + subtotal.toFixed(2);
        document.getElementById('igv').textContent = 'S/. ' + igv.toFixed(2);
        document.getElementById('total').textContent = 'S/. ' + total.toFixed(2);
    }


    generarCodigoQR();
    calcularTotales()
});