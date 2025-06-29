document.addEventListener('DOMContentLoaded', function() {
    const badge = document.getElementById('badge');
    const addToCartButtons = document.querySelectorAll('.btn-add-to-cart');
    let cartCount = 0;

    addToCartButtons.forEach(button => {
        button.addEventListener('click', function() {
            cartCount++;
            badge.textContent = cartCount;
        });
    });
});