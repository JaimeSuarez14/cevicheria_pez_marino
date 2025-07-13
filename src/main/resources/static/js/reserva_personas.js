
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