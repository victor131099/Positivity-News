function validateFirstName() {
    var firstname = document.getElementById("firstname");

    if (firstname.value === null || firstname.value === "") {
        updateInvalid(firstname);
    }

    else {
        updateValid(firstname);
    }
}

function validateLastName() {
    var lastname = document.getElementById("lastname");

    if (lastname.value === null || lastname.value === "") {
        updateInvalid(lastname);
    }

    else {
        updateValid(lastname);
    }
}

function validateEmail() {
    var email = document.getElementById("email");
    var pattern = /^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,253}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,253}[a-zA-Z0-9])?)*$/;

    if (!pattern.test(email.value)) {
        updateInvalid(email);
    }

    else {
        updateValid(email);
    }
}

function validatePasswords() {
    var password = document.getElementById("password");
    var confirm = document.getElementById("confirm");

    if (password.value === null || password.value.length < 8) {
        updateInvalid(password);
    }

    else {
        updateValid(password);
    }

    if (password.value != confirm.value || confirm.value == null || confirm.value.length < 8) {
        updateInvalid(confirm)
    }

    else {
        updateValid(confirm)
    }
}

function updateInvalid(element) {
    if (element.classList.contains("is-valid")) {
        element.classList.remove("is-valid");
    }

    if (!element.classList.contains("is-invalid)")) {
        element.classList.add("is-invalid");
    }
}

function updateValid(element) {
    if (element.classList.contains("is-invalid")) {
        element.classList.remove("is-invalid");
    }

    if (!element.classList.contains("is-valid)")) {
        element.classList.add("is-valid");
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