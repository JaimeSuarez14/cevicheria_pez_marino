document.addEventListener('DOMContentLoaded', function () {
    initializeTheme();
    cargarBarraProgreso(4);

    // Si hay una hora previamente seleccionada en el modelo, márcala
    const selectedHoraInput = document.getElementById('selectedHora');
    if (selectedHoraInput && selectedHoraInput.value) {
        document.querySelectorAll('.hora-btn').forEach(button => {
            if (button.dataset.hora === selectedHoraInput.value) {
                button.classList.add('selected');
            }
        });
    }
});

function seleccionarHora(btn) {
    // Remover la clase selected de todos los botones
    document.querySelectorAll('.hora-btn').forEach(button => {
        button.classList.remove('selected');
    });

    // Agregar la clase selected al botón clickeado
    btn.classList.add('selected');
    document.getElementById('selectedHora').value = btn.dataset.hora;

}