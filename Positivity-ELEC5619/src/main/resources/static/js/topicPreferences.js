let myTopics = document.getElementById('mytopics');
new Sortable(myTopics, {
    group: 'shared',
    sort: false,
    animation: 150,
    onAdd: function(evt) {
        $.ajax({
            type: "PUT",
            url: "/api/topicPreference",
            data: {
                "topic_name": evt.item.childNodes[1].innerText
            },
        });
    },
    onRemove: function(evt) {
        $.ajax({
            type: "DELETE",
            url: "/api/topicPreference",
            data: {
                "topic_name": evt.item.childNodes[1].innerText
            },
        });
    },
});

let allTopics = document.getElementById('alltopics');
new Sortable(allTopics, {
    group: 'shared',
    sort: false,
    animation: 150
});

var topicForm = document.getElementById("topic-form")
topicForm.addEventListener("submit", function (event) {
    event.preventDefault();
    event.stopPropagation();

//    topics = []
//    for (var i = 0; i < topicForm.children.length; i++) {
//        topics.push(topicForm.children[i].firstElementChild.innerHTML);
//    }
//    console.log(topics)

}, false)
