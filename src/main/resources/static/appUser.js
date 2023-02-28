async function thisUser() {
    fetch("http://localhost:8080/api/user")
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
                <td>${data.roles.map(role => " " + role.name.substring(5))}</td>)`;
            $('#userPanelBody').append(user);
        })
}

$(async function () {
    await thisUser();
});

