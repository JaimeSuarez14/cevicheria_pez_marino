function cargarBarraProgreso(pasoActual) {

    console.log('Cargando barra de progreso. Paso actual:', pasoActual);
    const contenedor = document.getElementById('progreso-container');
    if (!contenedor) {
        console.error('No se encontró el contenedor de la barra de progreso');
        return;
    }

    const pasos = ['Productos', 'Cliente', 'Envio', 'Pago', 'Finalizar'];
    let barraProgreso = `
    <div class="container mt-3">
        <div class="progress" style="height: 30px;">`;
    
    pasos.forEach((paso, index) => {
        const esCompletado = index < pasoActual;
        const esActual = index === pasoActual - 1;
        const clase = esCompletado || esActual ? 'bg-success' : 'bg-secondary';
        
        barraProgreso += `
            <div class="progress-bar ${clase}" role="progressbar" style="width: 20%;" 
                 aria-valuenow="20" aria-valuemin="0" aria-valuemax="100">
                <a class="fw-bold text-white text-decoration-none" th:href="@{/carrito/${paso.toLowerCase()}}">${paso}</a>
            </div>`;
    });
    
    barraProgreso += `
        </div>
    </div>`;
    
    contenedor.innerHTML = barraProgreso;
    console.log('Barra de progreso cargada');
}