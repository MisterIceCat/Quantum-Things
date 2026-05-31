// Mobile menu toggle functionality
const menuToggle = document.getElementById('mobile-menu-toggle');
const navigation = document.getElementById('container_navigation');
const overlay = document.getElementById('mobile-overlay');

if (menuToggle && navigation && overlay) {
    function toggleMenu() {
        const isOpen = navigation.classList.toggle('mobile-open');
        overlay.classList.toggle('mobile-open');
        document.body.classList.toggle('menu-open', isOpen);
    }

    function closeMenu() {
        navigation.classList.remove('mobile-open');
        overlay.classList.remove('mobile-open');
        document.body.classList.remove('menu-open');
    }

    menuToggle.addEventListener('click', function (e) {
        e.stopPropagation();
        toggleMenu();
    });

    overlay.addEventListener('click', closeMenu);

    navigation.querySelectorAll('a').forEach(function (link) {
        link.addEventListener('click', closeMenu);
    });

    document.addEventListener('keydown', function (e) {
        if (e.key === 'Escape' && navigation.classList.contains('mobile-open')) {
            closeMenu();
        }
    });
}

