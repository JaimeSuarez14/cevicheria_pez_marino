// Función para alternar entre tema oscuro y claro
function toggleTheme() {
    const body = document.body;
    const themeIcon = document.getElementById('themeIcon');
    
    // Cambiar el tema
    if (body.classList.contains('dark-mode')) {
        body.classList.remove('dark-mode');
        themeIcon.classList.replace('fa-sun', 'fa-moon');
        localStorage.setItem('theme', 'light');
    } else {
        body.classList.add('dark-mode');
        themeIcon.classList.replace('fa-moon', 'fa-sun');
        localStorage.setItem('theme', 'dark');
    }
}

// Función para cargar el tema guardado
function loadTheme() {
    const savedTheme = localStorage.getItem('theme');
    const themeIcon = document.getElementById('themeIcon');
    
    if (savedTheme === 'dark') {
        document.body.classList.add('dark-mode');
        themeIcon.classList.replace('fa-moon', 'fa-sun');
    }
}

// Estilos para el botón de tema
const themeStyles = `
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

    .dark-mode {
        --bg-color: #1a1a1a;
        --text-color: #ffffff;
        --card-bg: #2d2d2d;
        --header-bg: #2d2d2d;
        --hover-color: #3d3d3d;
        --primary-color: #1e88e5;
        --border-color: #404040;
    }

    .dark-mode .navbar {
        background-color: var(--card-bg) !important;
    }

    .dark-mode .card {
        background-color: var(--card-bg) !important;
        border-color: var(--border-color);
    }

    .dark-mode .input-field {
        background-color: #333333;
        color: var(--text-color);
    }

    .dark-mode .btn-outline-secondary {
        border-color: var(--border-color);
        color: var(--text-color);
    }

    .dark-mode .progress {
        background-color: #333333;
    }

    .dark-mode .social-login-btn {
        background-color: var(--card-bg);
        border-color: var(--border-color);
        color: var(--text-color);
    }

    .dark-mode .google-btn {
        background-color: var(--card-bg);
        color: var(--text-color);
    }
`;

// Función para agregar el botón de tema y los estilos
function initializeTheme() {
    // Agregar estilos
    const styleElement = document.createElement('style');
    styleElement.textContent = themeStyles;
    document.head.appendChild(styleElement);

    // Agregar botón de tema
    const themeButton = document.createElement('button');
    themeButton.className = 'theme-toggle';
    themeButton.innerHTML = '<i id="themeIcon" class="fas fa-moon"></i>';
    themeButton.onclick = toggleTheme;
    document.body.appendChild(themeButton);

    // Cargar tema guardado
    loadTheme();
} 