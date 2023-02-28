$(async function () {
    await thisUser();
    await allUsers();
    await newUser();
    editUser();
    deleteUser();
});
const table = $('#tbodyAllUserTable');

async function thisUser() {
    fetch("/api/user")
        .then(res => res.json())
        .then(data => {
            // Добавляем информацию в шапку
            $('#headerUsername').append(data.email);
            let roles = data.roles.map(role => " " + role.name.substring(5));
            $('#headerRoles').append(roles);

            //Добавляем информацию в таблицу
            let user = `$(
            <tr>
                <td>${data.id}</td>
                <td>${data.username}</td>
                <td>${data.name}</td>
                <td>${data.lastname}</td>
                <td>${data.age}</td>
                <td>${data.email}</td>
                <td>${roles}</td>)`;
            $('#userPanelBody').append(user);
        })
}
//заполняем tbody id="tbodyAllUserTable" + кнопки модалок
async function allUsers() {
    table.empty()
    fetch("/api/users")
        .then(res => res.json())
        .then(data => {
            data.forEach(user => {
                let tableWithUsers = `$(
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.username}</td>
                            <td>${user.name}</td>
                            <td>${user.lastname}</td>
                            <td>${user.age}</td>
                            <td>${user.email}</td>
                            <td>${user.roles.map(role => " " + role.name.substring(5))}</td>
                            <td>
                                <button type="button" class="btn btn-info" data-toggle="modal" id="buttonEdit"
                                data-action="edit" data-id="${user.id}" data-target="#edit">Edit</button>
                            </td>
                            <td>
                                <button type="button" class="btn btn-danger" data-toggle="modal" id="buttonDelete"
                                data-action="delete" data-id="${user.id}" data-target="#delete">Delete</button>
                            </td>
                        </tr>)`;
                table.append(tableWithUsers);
            })
        })
}

async function newUser() {
    await fetch("api/roles")
        .then(res => res.json())
        .then(roles => {
            roles.forEach(role => {
                let el = document.createElement("option");
                el.text = role.name.substring(5);
                el.value = role.id;
                $('#newUserRoles')[0].appendChild(el);
            })
        })

    const form = document.forms["formNewUser"];

    form.addEventListener('submit', addNewUser)

    function addNewUser(e) {
        e.preventDefault();
        let newUserRoles = [];
        for (let i = 0; i < form.roles.options.length; i++) {
            if (form.roles.options[i].selected) newUserRoles.push({
                id: form.roles.options[i].value,
                name: form.roles.options[i].name
            })
        }
        fetch("/api/users", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                username: form.username.value,
                name: form.name.value,
                lastname: form.lastname.value,
                age: form.age.value,
                email: form.email.value,
                password: form.password.value,
                roles: newUserRoles
            })
        }).then(() => {
            form.reset();
            allUsers();
            $('#allUsersTable').click();
        })
    }
}

$('#edit').on('show.bs.modal', ev => {
    let button = $(ev.relatedTarget);
    let id = button.data('id');
    showModal(id, "Edit");
})

async function showModal(id, action) {
    let user = await getUser(id);
    let form = document.forms[`form${action}User`];
    form.id.value = user.id;
    form.username.value = user.username;
    form.name.value = user.name;
    form.lastname.value = user.lastname;
    form.age.value = user.age;
    form.email.value = user.email;
    if (action === "Edit") form.password.value = "";

    $(`#roles${action}User`).empty();

    await fetch("/api/roles")
        .then(res => res.json())
        .then(roles => {
            roles.forEach(role => {
                let selectedRole = false;
                for (let i = 0; i < user.roles.length; i++) {
                    if (user.roles[i].name === role.name) {
                        selectedRole = true;
                        break;
                    }
                }
                let el = document.createElement("option");
                el.text = role.name.substring(5);
                el.value = role.id;
                if (selectedRole) el.selected = true;
                $(`#roles${action}User`)[0].appendChild(el);
            })
        });
}

function editUser() {
    const editForm = document.forms["formEditUser"];
    editForm.addEventListener("submit", ev => {
        ev.preventDefault();
        let editUserRoles = [];
        for (let i = 0; i < editForm.roles.options.length; i++) {
            if (editForm.roles.options[i].selected) editUserRoles.push({
                id: editForm.roles.options[i].value,
                name: editForm.roles.options[i].name
            })
        }

        fetch("/api/users/" + editForm.id.value, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                id: editForm.id.value,
                username: editForm.username.value,
                name: editForm.name.value,
                lastname: editForm.lastname.value,
                age: editForm.age.value,
                email: editForm.email.value,
                password: editForm.password.value,
                roles: editUserRoles
            })
        }).then(() => {
            $('#editFormCloseButton').click();
            allUsers();
        })
    })
}


$('#delete').on('show.bs.modal', ev => {
    let button = $(ev.relatedTarget);
    let id = button.data('id');
    showModal(id, "Delete");
})

function deleteUser() {
    const deleteForm = document.forms["formDeleteUser"];
    deleteForm.addEventListener("submit", ev => {
        ev.preventDefault();
        fetch("/api/users/" + deleteForm.id.value, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(() => {
                $('#deleteFormCloseButton').click();
                allUsers();
            })
    })
}

async function getUser(id) {
    let url = "/api/users/" + id;
    let response = await fetch(url);
    return await response.json();
}