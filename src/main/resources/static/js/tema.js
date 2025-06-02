// Función para alternar entre tema oscuro y claro
function toggleTheme() {
    console.log('🔄 [toggleTheme] Iniciando cambio de tema...');
    const body = document.body;
    const themeIcon = document.getElementById('themeIcon');
    
    if (!themeIcon) {
        console.error('❌ [toggleTheme] Error: No se encontró el icono del tema');
        return;
    }
    
    try {
        const temaActual = body.classList.contains('dark-mode') ? 'oscuro' : 'claro';
        console.log(`ℹ️ [toggleTheme] Tema actual: ${temaActual}`);

        // Cambiar el tema
        if (body.classList.contains('dark-mode')) {
            console.log('🌞 [toggleTheme] Cambiando a tema claro...');
            body.classList.remove('dark-mode');
            if (themeIcon.classList.contains('fa-sun')) {
                console.log('🔄 [toggleTheme] Cambiando icono de sol a luna');
                themeIcon.classList.remove('fa-sun');
                themeIcon.classList.add('fa-moon');
            }
            localStorage.setItem('theme', 'light');
            console.log('✅ [toggleTheme] Tema claro aplicado y guardado');
        } else {
            console.log('🌚 [toggleTheme] Cambiando a tema oscuro...');
            body.classList.add('dark-mode');
            if (themeIcon.classList.contains('fa-moon')) {
                console.log('🔄 [toggleTheme] Cambiando icono de luna a sol');
                themeIcon.classList.remove('fa-moon');
                themeIcon.classList.add('fa-sun');
            }
            localStorage.setItem('theme', 'dark');
            console.log('✅ [toggleTheme] Tema oscuro aplicado y guardado');
        }
        
        // Verificar el cambio
        console.log('📊 [toggleTheme] Estado final:', {
            'Modo oscuro activo': body.classList.contains('dark-mode'),
            'Clase del icono': themeIcon.className,
            'Tema en localStorage': localStorage.getItem('theme')
        });
    } catch (error) {
        console.error('❌ [toggleTheme] Error al cambiar el tema:', error);
    }
}

// Función para cargar el tema guardado
function loadTheme() {
    console.log('🔄 [loadTheme] Iniciando carga del tema guardado...');
    const savedTheme = localStorage.getItem('theme');
    console.log(`ℹ️ [loadTheme] Tema guardado en localStorage: ${savedTheme}`);

    const themeIcon = document.getElementById('themeIcon');
    const body = document.body;
    
    if (!themeIcon) {
        console.error('❌ [loadTheme] Error: No se encontró el icono del tema');
        return;
    }
    
    try {
        if (savedTheme === 'dark') {
            console.log('🌚 [loadTheme] Aplicando tema oscuro guardado...');
            body.classList.add('dark-mode');
            themeIcon.classList.remove('fa-moon');
            themeIcon.classList.add('fa-sun');
            console.log('✅ [loadTheme] Tema oscuro aplicado');
        } else {
            console.log('🌞 [loadTheme] Aplicando tema claro...');
            body.classList.remove('dark-mode');
            themeIcon.classList.remove('fa-sun');
            themeIcon.classList.add('fa-moon');
            console.log('✅ [loadTheme] Tema claro aplicado');
        }
        
        console.log('📊 [loadTheme] Estado inicial:', {
            'Modo oscuro activo': body.classList.contains('dark-mode'),
            'Clase del icono': themeIcon.className,
            'Tema en localStorage': localStorage.getItem('theme')
        });
    } catch (error) {
        console.error('❌ [loadTheme] Error al cargar el tema:', error);
    }
}

// Estilos para el botón de tema y modo oscuro
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

// Función para agregar el botón de tema y los estilos
function initializeTheme() {
    console.log('🚀 [initializeTheme] Iniciando inicialización del tema...');
    
    try {
        // Primero, eliminar cualquier botón de tema existente
        const existingButton = document.querySelector('.theme-toggle');
        if (existingButton) {
            console.log('🧹 [initializeTheme] Eliminando botón de tema existente...');
            existingButton.remove();
        }

        // Eliminar estilos existentes si los hay
        const existingStyles = document.querySelector('style#theme-styles');
        if (existingStyles) {
            console.log('🧹 [initializeTheme] Eliminando estilos existentes...');
            existingStyles.remove();
        }

        // Agregar estilos nuevos
        console.log('🎨 [initializeTheme] Agregando estilos del tema...');
        const styleElement = document.createElement('style');
        styleElement.id = 'theme-styles';
        styleElement.textContent = themeStyles;
        document.head.appendChild(styleElement);
        console.log('✅ [initializeTheme] Estilos del tema agregados');

        // Agregar botón de tema
        console.log('🔘 [initializeTheme] Creando botón de tema...');
        const themeButton = document.createElement('button');
        themeButton.className = 'theme-toggle';
        themeButton.innerHTML = '<i id="themeIcon" class="fas fa-moon"></i>';
        themeButton.onclick = toggleTheme;
        document.body.appendChild(themeButton);
        console.log('✅ [initializeTheme] Botón de tema agregado');

        // Cargar tema guardado
        console.log('🔄 [initializeTheme] Cargando tema guardado...');
        loadTheme();
        console.log('✅ [initializeTheme] Inicialización completada');
    } catch (error) {
        console.error('❌ [initializeTheme] Error al inicializar el tema:', error);
    }
} 