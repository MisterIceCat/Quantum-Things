// Save and restore navigation scroll position
const nav = document.getElementById("container_navigation");
if (nav) {
    const savedScroll = sessionStorage.getItem("scroll");
    if (savedScroll) {
        nav.scrollTop = savedScroll;
        sessionStorage.removeItem("scroll");
    }
}

document.body.addEventListener('click', function (e) {
    if (e.target.tagName === 'A') {
        const nav = document.getElementById("container_navigation");
        if (nav) {
            sessionStorage.setItem("scroll", nav.scrollTop);
        }
    }
});
