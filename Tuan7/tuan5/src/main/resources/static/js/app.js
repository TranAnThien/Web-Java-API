document.addEventListener("DOMContentLoaded", () => {
  // Tự động ẩn alert
  document.querySelectorAll(".alert[data-auto-dismiss]").forEach(el => {
    const ms = parseInt(el.getAttribute("data-auto-dismiss"), 10) || 2500;
    setTimeout(() => {
      el.style.transition = "opacity 0.3s";
      el.style.opacity = "0";
    }, ms - 300);
    setTimeout(() => el.remove(), ms);
  });

  // Xác nhận trước khi xoá
  document.querySelectorAll("[data-confirm]").forEach(link => {
    link.addEventListener("click", e => {
      if (!confirm(link.getAttribute("data-confirm"))) {
        e.preventDefault();
      }
    });
  });
});
