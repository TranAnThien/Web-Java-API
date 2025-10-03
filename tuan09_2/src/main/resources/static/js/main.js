$(document).ready(function() {

    // --- Hàm tiện ích cho nút loading ---
    function setButtonLoading(button, isLoading) {
        if (isLoading) {
            button.prop('disabled', true);
            button.html('<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Loading...');
        } else {
            button.prop('disabled', false);
            button.html(button.data('original-text'));
        }
    }

    // --- Logic cho trang Profile (giữ nguyên) ---
    if (window.location.pathname.endsWith("/profile")) {
        const token = localStorage.getItem('token');
        if (!token) {
            window.location.href = "/login";
        } else {
            $.ajax({
                type: 'GET',
                url: '/users/me',
                beforeSend: function(xhr) {
                    xhr.setRequestHeader('Authorization', 'Bearer ' + token);
                },
                success: function(data) {
                    $('#profile').html('<h4 class="card-title">Welcome, ' + data.fullName + '</h4><p class="card-text text-muted">' + data.email + '</p>');
                },
                error: function() {
                    alert("Your session has expired. Please log in again.");
                    localStorage.removeItem('token');
                    window.location.href = "/login";
                }
            });
        }
    }

    // --- Logic cho nút Login ---
    $('#loginButton').click(function() {
        const button = $(this);
        button.data('original-text', button.html()); // Lưu lại text gốc
        setButtonLoading(button, true);

        const email = $('#email').val();
        const password = $('#password').val();
        const loginData = JSON.stringify({ email: email, password: password });

        $.ajax({
            type: "POST",
            url: "/auth/login",
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            data: loginData,
            success: function(data) {
                localStorage.setItem('token', data.token);
                window.location.href = "/user/profile";
            },
            error: function(xhr) {
                const errorMsg = "Incorrect email or password.";
                $('#login-error').text(errorMsg).fadeIn();
            },
            complete: function() {
                setButtonLoading(button, false);
            }
        });
    });

    // --- Logic cho nút Logout ---
    $('#logoutButton').click(function() {
        localStorage.removeItem('token');
        window.location.href = "/login";
    });

    // --- Logic cho nút Signup ---
    $('#signupButton').click(function() {
        const button = $(this);
        button.data('original-text', button.html());
        setButtonLoading(button, true);

        const fullName = $('#fullName').val();
        const email = $('#email-signup').val();
        const password = $('#password-signup').val();

        const signupData = JSON.stringify({ fullName, email, password });

        $.ajax({
            type: "POST",
            url: "/auth/signup",
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            data: signupData,
            success: function(data) {
                $('#signup-success').text('Account created successfully! You can now log in.').fadeIn();
                $('#signup-error').hide();
                $('#signup-form')[0].reset(); // Xóa trống form
            },
            error: function(xhr) {
                const errorMsg = xhr.responseText || "An error occurred.";
                $('#signup-error').text(errorMsg).fadeIn();
                $('#signup-success').hide();
            },
            complete: function() {
                setButtonLoading(button, false);
            }
        });
    });

    // --- Logic cho nút Hiện/Ẩn mật khẩu (Login) ---
    $('#togglePassword').click(function() {
        const passwordInput = $('#password');
        const icon = $(this).find('i');
        if (passwordInput.attr('type') === 'password') {
            passwordInput.attr('type', 'text');
            icon.removeClass('bi-eye-slash').addClass('bi-eye');
        } else {
            passwordInput.attr('type', 'password');
            icon.removeClass('bi-eye').addClass('bi-eye-slash');
        }
    });

    // --- Logic cho nút Hiện/Ẩn mật khẩu (Signup) ---
    $('#togglePasswordSignup').click(function() {
        const passwordInput = $('#password-signup');
        const icon = $(this).find('i');
        if (passwordInput.attr('type') === 'password') {
            passwordInput.attr('type', 'text');
            icon.removeClass('bi-eye-slash').addClass('bi-eye');
        } else {
            passwordInput.attr('type', 'password');
            icon.removeClass('bi-eye').addClass('bi-eye-slash');
        }
    });
});