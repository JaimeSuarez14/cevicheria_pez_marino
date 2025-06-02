document.addEventListener('DOMContentLoaded', function() {
    console.log('Iniciando carga de componentes...');
    cargarEstilosComunes();

    initializeTheme();
    console.log('Componentes cargados');
});

function incrementarPersonas() {
    const input = document.getElementById('numPersonas');
    if (input.value < 10) {
        input.value = parseInt(input.value) + 1;
    }
}

function decrementarPersonas() {
    const input = document.getElementById('numPersonas');
    if (input.value > 1) {
        input.value = parseInt(input.value) - 1;
    }
}

// Función para cargar la barra de progreso
function cargarBarraProgreso(pasoActual) {
    console.log('Cargando barra de progreso. Paso actual:', pasoActual);
    const contenedor = document.getElementById('progreso-container');
    if (!contenedor) {
        console.error('No se encontró el contenedor de la barra de progreso');
        return;
    }

    const pasos = ['Degustación', 'Personas', 'Fecha', 'Hora', 'Datos'];
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
                <span class="fw-bold">${paso}</span>
            </div>`;
    });
    
    barraProgreso += `
        </div>
    </div>`;
    
    contenedor.innerHTML = barraProgreso;
    console.log('Barra de progreso cargada');
}

// Función para cargar los estilos comunes
function cargarEstilosComunes() {
    const estilos = `
    <style>
        :root {
            --bg-color: #ffffff;
            --text-color: #212529;
            --card-bg: #f8f9fa;
            --header-bg: #f8f9fa;
            --hover-color: #e9ecef;
            --primary-color: #0d6efd;
            --border-color: #dee2e6;
        }

        .dark-mode {
            --bg-color: #212529;
            --text-color: #f8f9fa;
            --card-bg: #343a40;
            --header-bg: #343a40;
            --hover-color: #495057;
            --primary-color: #1e88e5;
            --border-color: #495057;
        }

        body {
            background-color: var(--bg-color);
            color: var(--text-color);
            transition: all 0.3s ease;
        }

        .navbar, .card {
            background-color: var(--card-bg) !important;
            transition: all 0.3s ease;
        }
    </style>`;
    
    document.getElementById('estilos-comunes').innerHTML = estilos;
} 