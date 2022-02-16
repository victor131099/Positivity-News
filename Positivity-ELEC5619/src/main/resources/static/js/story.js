var likeForm = document.getElementById("likeForm")
likeForm.addEventListener("submit", function (event) {
    event.preventDefault();
    event.stopPropagation();
    var id = document.getElementById("id").value;
    var button = document.getElementById("likeButton");
    if (button.innerText.startsWith("Like")) {
        likeStory(id);
    }
    else {
        unlikeStory(id);
    }
}, false)

function likeStory(storyID) {
    let xhr = new XMLHttpRequest();
    xhr.open("GET", "/likeStory?storyID=" + storyID);
    xhr.send();

    xhr.onload = function() {
        if (xhr.status != 200) {
            alert("Error");
        }

        else {
            var button = document.getElementById("likeButton");
            button.innerText = "Unlike (" + xhr.response + ")"
        }
    }
}

function unlikeStory(storyID) {
    let xhr = new XMLHttpRequest();
    xhr.open("GET", "/unlikeStory?storyID=" + storyID);
    xhr.send();

    xhr.onload = function() {
        if (xhr.status != 200) {
            alert("Error");
        }

        else {
            var button = document.getElementById("likeButton");
            button.innerText = "Like (" + xhr.response + ")";
        }
    }
}