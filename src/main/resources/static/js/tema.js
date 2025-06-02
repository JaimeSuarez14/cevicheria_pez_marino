// Función para alternar entre tema oscuro y claro
function toggleTheme() {
    // Obtener elementos necesarios
    const body = document.body;
    const themeIcon = document.getElementById('themeIcon');
    
    // Verificar que el icono existe
    if (!themeIcon) {
        return;
    }
    
    // Cambiar el tema basado en el estado actual
    if (body.classList.contains('dark-mode')) {
        // Cambiar a tema claro
        body.classList.remove('dark-mode');
        if (themeIcon.classList.contains('fa-sun')) {
            themeIcon.classList.remove('fa-sun');
            themeIcon.classList.add('fa-moon');
        }
        localStorage.setItem('theme', 'light');
    } else {
        // Cambiar a tema oscuro
        body.classList.add('dark-mode');
        if (themeIcon.classList.contains('fa-moon')) {
            themeIcon.classList.remove('fa-moon');
            themeIcon.classList.add('fa-sun');
        }
        localStorage.setItem('theme', 'dark');
    }
}

// Función para cargar el tema guardado en localStorage
function loadTheme() {
    // Obtener el tema guardado y elementos necesarios
    const savedTheme = localStorage.getItem('theme');
    const themeIcon = document.getElementById('themeIcon');
    const body = document.body;
    
    // Verificar que el icono existe
    if (!themeIcon) {
        return;
    }
    
    // Aplicar el tema guardado
    if (savedTheme === 'dark') {
        // Aplicar tema oscuro
        body.classList.add('dark-mode');
        themeIcon.classList.remove('fa-moon');
        themeIcon.classList.add('fa-sun');
    } else {
        // Aplicar tema claro (por defecto)
        body.classList.remove('dark-mode');
        themeIcon.classList.remove('fa-sun');
        themeIcon.classList.add('fa-moon');
    }
}

// Función para inicializar el sistema de temas
function initializeTheme() {
    // Limpiar cualquier instancia previa
    const existingButton = document.querySelector('.theme-toggle');
    if (existingButton) {
        existingButton.remove();
    }

    // Limpiar estilos previos
    const existingStyles = document.querySelector('style#theme-styles');
    if (existingStyles) {
        existingStyles.remove();
    }

    // Agregar los estilos del tema
    const styleElement = document.createElement('style');
    styleElement.id = 'theme-styles';
    styleElement.textContent = themeStyles;
    document.head.appendChild(styleElement);

    // Crear y agregar el botón de cambio de tema
    const themeButton = document.createElement('button');
    themeButton.className = 'theme-toggle';
    themeButton.innerHTML = '<i id="themeIcon" class="fas fa-moon"></i>';
    themeButton.onclick = toggleTheme;
    document.body.appendChild(themeButton);

    // Cargar las preferencias guardadas del tema
    loadTheme();
}

// Definición de estilos para los temas claro y oscuro
const themeStyles = `
    /* Variables para tema claro (por defecto) */
    :root {
        --bg-color: #ffffff;
        --text-color: #212529;
        --card-bg: #ffffff;
        --header-bg: #f8f9fa;
        --hover-color: #e9ecef;
        --primary-color: #0d6efd;
        --border-color: #dee2e6;
    }

    /* Variables para tema oscuro */
    body.dark-mode {
        --bg-color: #1a1a1a;
        --text-color: #ffffff;
        --card-bg: #2d2d2d;
        --header-bg: #2d2d2d;
        --hover-color: #3d3d3d;
        --primary-color: #1e88e5;
        --border-color: #404040;
    }

    /* Estilos base */
    body {
        background-color: var(--bg-color);
        color: var(--text-color);
        transition: background-color 0.3s ease, color 0.3s ease;
    }

    /* Botón de tema */
    .theme-toggle {
        position: fixed;
        bottom: 20px;
        right: 20px;
        width: 50px;
        height: 50px;
        border-radius: 50%;
        background: var(--primary-color);
        color: white;
        border: none;
        cursor: pointer;
        box-shadow: 0 2px 10px rgba(0,0,0,0.2);
        transition: all 0.3s ease;
        z-index: 1000;
        display: flex;
        align-items: center;
        justify-content: center;
    }

    .theme-toggle:hover {
        transform: scale(1.1);
        box-shadow: 0 4px 15px rgba(0,0,0,0.3);
    }

    /* Estilos para tema claro */
    .navbar {
        background-color: var(--header-bg);
        color: var(--text-color);
        transition: background-color 0.3s ease, color 0.3s ease;
    }

    .card {
        background-color: var(--card-bg);
        border-color: var(--border-color);
        color: var(--text-color);
        transition: background-color 0.3s ease, color 0.3s ease;
    }

    .form-label,
    .text-muted,
    .form-control {
        color: var(--text-color);
        transition: color 0.3s ease;
    }

    .form-control {
        background-color: var(--bg-color);
        border-color: var(--border-color);
        transition: background-color 0.3s ease;
    }

    .btn-outline-secondary {
        border-color: var(--border-color);
        color: var(--text-color);
        transition: all 0.3s ease;
    }

    .progress {
        background-color: var(--hover-color);
        transition: background-color 0.3s ease;
    }

    /* Estilos específicos para modo oscuro */
    body.dark-mode .navbar,
    body.dark-mode .card {
        background-color: var(--card-bg) !important;
        border-color: var(--border-color);
        color: var(--text-color) !important;
    }

    body.dark-mode .form-control {
        background-color: var(--card-bg);
        border-color: var(--border-color);
        color: var(--text-color);
    }

    body.dark-mode .btn-outline-secondary {
        border-color: var(--border-color);
        color: var(--text-color);
    }

    body.dark-mode .btn-outline-secondary:hover {
        background-color: var(--hover-color);
    }

    body.dark-mode .progress {
        background-color: #333333;
    }

    body.dark-mode .text-muted {
        color: #a0a0a0 !important;
    }
`; 