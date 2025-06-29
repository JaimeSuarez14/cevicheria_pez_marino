document.addEventListener("DOMContentLoaded", () => {
    const productDetailModal = document.getElementById('staticBackdrop');

    // Referencias a los elementos dentro del modal que mostraremos la info
    const modalImage = document.getElementById('modalProductImage');
    const modalName = document.getElementById('modalProductName');
    const modalPrice = document.getElementById('modalProductPrice');
    const modalDescription = document.getElementById('modalProductDescription');
    const modalStock = document.getElementById('modalProductStock');
    const modalCategoria = document.getElementById('modalProductCategoria');
    const modalTitle = document.getElementById('staticBackdropLabel'); // El título del modal

    if (productDetailModal) {
        productDetailModal.addEventListener('show.bs.modal', (event) => {
            const button = event.relatedTarget; // Botón que abrió el modal
            const id = button.getAttribute('data-id');
            const nombre = button.getAttribute('data-nombre');
            const descripcion = button.getAttribute('data-descripcion');
            const categoria = button.getAttribute('data-categoria');
            const precio = button.getAttribute('data-precio');
            const stock = button.getAttribute('data-stock');
            const imagen = button.getAttribute('data-imagen');

            modalTitle.textContent = nombre;
            modalImage.src = `/imagenes/${imagen}`;
            modalName.textContent = nombre;
            modalPrice.textContent = precio;
            modalDescription.textContent = descripcion;
            modalStock.textContent = stock;
            modalCategoria.textContent = categoria;
        });
    }
});