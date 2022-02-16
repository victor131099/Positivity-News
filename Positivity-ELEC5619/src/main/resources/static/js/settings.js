function validateFirstName() {
    var firstname = document.getElementById("firstname");

    if (firstname.value === null || firstname.value === "") {
        if (firstname.classList.contains("is-valid")) {
            firstname.classList.remove("is-valid");
        }

        if (!firstname.classList.contains("is-invalid)")) {
            firstname.classList.add("is-invalid");
        }
    }

    else {
        if (firstname.classList.contains("is-invalid")) {
            firstname.classList.remove("is-invalid");
        }

        if (!firstname.classList.contains("is-valid)")) {
            firstname.classList.add("is-valid");
        }
    }
}

function validateLastName() {
    var lastname = document.getElementById("lastname");

    if (lastname.value === null || lastname.value === "") {
        if (lastname.classList.contains("is-valid")) {
            lastname.classList.remove("is-valid");
        }

        if (!lastname.classList.contains("is-invalid)")) {
            lastname.classList.add("is-invalid");
        }
    }

    else {
        if (lastname.classList.contains("is-invalid")) {
            lastname.classList.remove("is-invalid");
        }

        if (!lastname.classList.contains("is-valid)")) {
            lastname.classList.add("is-valid");
        }
    }
}

function validateEmail() {
    var email = document.getElementById("email");
    var pattern = /^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,253}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,253}[a-zA-Z0-9])?)*$/;

    if (!pattern.test(email.value)) {
        if (email.classList.contains("is-valid")) {
            email.classList.remove("is-valid");
        }

        if (!email.classList.contains("is-invalid)")) {
            email.classList.add("is-invalid");
        }
    }

    else {
        if (email.classList.contains("is-invalid")) {
            email.classList.remove("is-invalid");
        }

        if (!email.classList.contains("is-valid)")) {
            email.classList.add("is-valid");
        }
    }
}

function validatePasswords() {
    var password = document.getElementById("password");
    var confirm = document.getElementById("confirm");

    if (password.value === null || password.value.length < 8) {
        if (password.classList.contains("is-valid")) {
            password.classList.remove("is-valid");
        }

        if (!password.classList.contains("is-invalid)")) {
            password.classList.add("is-invalid");
        }
    }

    else {
        if (password.classList.contains("is-invalid")) {
            password.classList.remove("is-invalid");
        }

        if (!password.classList.contains("is-valid)")) {
            password.classList.add("is-valid");
        }
    }

    if (password.value != confirm.value || confirm.value == null || confirm.value.length < 8) {
        if (confirm.classList.contains("is-valid")) {
            confirm.classList.remove("is-valid");
        }

        if (!confirm.classList.contains("is-invalid)")) {
            confirm.classList.add("is-invalid");
        }
    }

    else {
        if (confirm.classList.contains("is-invalid")) {
            confirm.classList.remove("is-invalid");
        }

        if (!confirm.classList.contains("is-valid)")) {
            confirm.classList.add("is-valid");
        }
    }
}

// Example starter JavaScript for disabling form submissions if there are invalid fields
// Taken from https://getbootstrap.com/docs/5.1/forms/validation/
(function () {
    'use strict'

    // Fetch all the forms we want to apply custom Bootstrap validation styles to
    var forms = document.querySelectorAll('.needs-validation')

    // Loop over them and prevent submission
    Array.prototype.slice.call(forms)
        .forEach(function (form) {
            form.addEventListener('submit', function (event) {
                validateFirstName();
                validateLastName();
                validateEmail();
                validatePasswords();

                var pass = document.getElementById("firstname");
                if (pass.classList.contains("is-invalid")) {
                    event.preventDefault();
                    event.stopPropagation();
                }
                var pass = document.getElementById("lastname");
                if (pass.classList.contains("is-invalid")) {
                    event.preventDefault();
                    event.stopPropagation();
                }
                var pass = document.getElementById("email");
                if (pass.classList.contains("is-invalid")) {
                    event.preventDefault();
                    event.stopPropagation();
                }
                var pass = document.getElementById("password");
                if (pass.classList.contains("is-invalid")) {
                    event.preventDefault();
                    event.stopPropagation();
                }
                var pass = document.getElementById("confirm");
                if (pass.classList.contains("is-invalid")) {
                    event.preventDefault();
                    event.stopPropagation();
                }
            }, false)
        })
})()