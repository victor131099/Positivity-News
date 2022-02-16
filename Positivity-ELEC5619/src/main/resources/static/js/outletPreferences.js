let myOutlets = document.getElementById('myoutlets');
new Sortable(myOutlets, {
    group: 'shared',
    sort: false,
    animation: 150,
    onAdd: function(evt) {
        $.ajax({
            type: "PUT",
            url: "/api/outletPreference",
            data: {
                "outlet_name": evt.item.childNodes[1].innerText
            },
        });
    },
    onRemove: function(evt) {
        $.ajax({
            type: "DELETE",
            url: "/api/outletPreference",
            data: {
                "outlet_name": evt.item.childNodes[1].innerText
            },
        });
    },
});

let allOutlets = document.getElementById('alloutlets');
new Sortable(allOutlets, {
    group: 'shared',
    sort: false,
    animation: 150,
});

var outletForm = document.getElementById("outlet-form")
outletForm.addEventListener("submit", function (event) {
    event.preventDefault();
    event.stopPropagation();
}, false)



