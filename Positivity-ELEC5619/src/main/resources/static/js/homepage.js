function displayErrorRetrievingStories(message) {
    var div = document.getElementById('stories');
    div.innerHTML = `
                   <div class = "row m-0 pt-3 pb-2" >
                       <div class = "card bg-wh shadow-sm" >
                           <br>
                           <p class="text-subtitle text-center px-10">${message}</p>
                           <br><br>
                       </div>
                   </div>`;
}

// Front-end external API call
function getSentiment(story) {

    const POSITIVE = true
    if (POSITIVE) {
        var data = {
            "document": {
                "type": "PLAIN_TEXT",
                "content": story
            },
            "encodingType": "UTF8"
        };

        var url = 'https://language.googleapis.com/v1beta2/documents:analyzeSentiment?key=' + sentimentAPIKey;
        var xmlHttp = new XMLHttpRequest();

        return new Promise(function (resolve, reject) {
            xmlHttp.onload = function () {
                if (this.status >= 200 && this.status < 300) {
                    if (JSON.parse(xmlHttp.responseText).sentences[0].sentiment.score > 0) {
                        resolve(true);
                    } else {
                        resolve(false);
                    }
                } else {
                    reject({
                        status: this.status,
                        statusText: this.statusText
                    });
                }
            }

            xmlHttp.open("POST", url, true); // true for asynchronous
            xmlHttp.send(JSON.stringify(data));
        });
    } else {
        return new Promise(function (resolve, reject) {
            resolve(true);
        });
    }
}

// Backend API call to retrieve list of stories
function getStories() {
    return new Promise(function (resolve, reject) {
        var url = '/api/my-stories';
        var xmlHttp = new XMLHttpRequest();
        xmlHttp.open("GET", url, true);
        xmlHttp.onload = function () {
            if (this.status >= 200 && this.status < 300) {
                resolve(JSON.parse(xmlHttp.responseText));
            } else {
                reject({
                    status: this.status,
                    statusText: this.statusText
                });
            }
        }
        xmlHttp.onerror = function () {
            reject({
                status: this.status,
                statusText: this.statusText
            });
        };
        xmlHttp.send(null);
    });

}


async function filterSentiment(stories) {
    var div = document.getElementById('stories');
    html = ''
    const numStoriesToCheck = 15;
    const promises = stories.slice(0, Math.min(stories.length, numStoriesToCheck)).map(x => getSentiment(x.title));
    var titles = []
    await Promise.all(promises).then(function (isPositive) {

        for (var i = 0; i < isPositive.length; i++) {
            if (isPositive[i]) {
                if (titles.includes(stories[i].title)) {
                    break
                }
                titles.push(stories[i].title)
                var date = new Date(stories[i].publishedDate);
                var dateString = date.toDateString();

                html += `
                   <div class = "row m-0 pt-3 pb-2">
                       <div id="storyCard" class="card bg-wh shadow-sm" onclick="view('story${i}')">
                        <input id='story${i}' type="hidden" value='${JSON.stringify(stories[i])}'>
                           <div class = "row m-0 p-0">
                               <div class = "col-8 pt-3">
                                   <p class = "text " style = "font-size: 36px; font-weight: 600">${stories[i].title}</p>
                                   <p class="text-muted" style="font-size: 20px; font-weight: 600"><i class="bi bi-calendar-week"></i> ${dateString}</p>
                                   <p class="text" style="font-size: 20px">${stories[i].excerpt}</p>
                                   <p class="text-muted "><i class="bi bi-easel2"></i> Presented by <a href="${stories[i].link}">${stories[i].source}</a>, by ${stories[i].authors}</p>
                               </div>
                               <div class="col-4 my-auto mx-0">
                                   <img src="${stories[i].media}" class="img-thumbnail" alt="Story Image">
                               </div>
                           </div>
                       </div>
                   </div>`;
            }
        }
        html += `<div class = "row m-0 pt-3 pb-2" >
                       <button class="btn btn-bg mx-0 pt-3 pb-3" style="font-size: 24px !important; color: #DFE8F5 !important;" onClick="window.location.reload();" >More Stories</button>
                     </div>
                     <br>`

        if (titles.length == 0) {
            throw new Error("Could not find any positive news... Try again later.")
        }

    }).catch(function (error) {
        if (error == "Could not find any positive news... Try again later.") {
            displayErrorRetrievingStories(error);
        } else {
            displayErrorRetrievingStories("An error occurred while retrieving stories. Please try again later.");
        }
        console.log(error);
    });

    if (html == '') {
        displayErrorRetrievingStories("No stories were returned. Please update your preferences or try again later.");
    }
    div.innerHTML = html;
}

function customReset() {
    document.getElementById("keywords").value = "";
    document.getElementById("topic").value = null;
    document.getElementById("outlet").value = null;
    document.getElementById("region").value = null;
    document.getElementById("language").value = null;
    document.getElementById("order_by").value = "relevance";
}

$(function () {
    $('form#searchForm').on('submit', function (e) {
        e.preventDefault();
        document.getElementById("stories").innerHTML = `
        <br>
        <div class="text-subtitle">Searching for positive news stories...</div>
        <br>
        <div class="loader"></divclass>
        `
        $.post('/api/search', $(this).serialize(), function (data) {
            if (data.length == 0) {
                displayErrorRetrievingStories("Your search returned no results. Try using less specific filters and/or keywords.");
                return;
            }
            filterSentiment(data);
        });
    });
});

async function preferenceStories() {
    document.getElementById("stories").innerHTML = `
        <br>
        <div class="text-subtitle">Searching for positive news stories...</div>
        <br>
        <div class="loader"></divclass>
        `

    getStories().then(async function (storiesJSON) {

        if (storiesJSON.length == 0) {
            displayErrorRetrievingStories("Could not find any positive news... Try again later.");
            return;
        }

        filterSentiment(storiesJSON)

    }).catch(function (error) {
        displayErrorRetrievingStories("An error occurred while retrieving stories. Please try again later.");
        console.log(error.status);
    });
}

function view(id) {
    // Taken from https://stackoverflow.com/questions/14432165/uncaught-syntaxerror-unexpected-token-with-json-parse
    var story = document.getElementById(id).value
    story = story.replace(/\\n/g, "\\n")
        .replace(/\\'/g, "\\'")
        .replace(/\\"/g, '\\"')
        .replace(/\\&/g, "\\&")
        .replace(/\\r/g, "\\r")
        .replace(/\\t/g, "\\t")
        .replace(/\\b/g, "\\b")
        .replace(/\\f/g, "\\f");
    // remove non-printable and other non-valid JSON chars
    story = story.replace(/[\u0000-\u0019]+/g, "");
    var story = JSON.parse(story)

    fetch('/story', {
        method: "POST",
        headers: {
            Accept: "application/json",
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            id: story.id,
            country: story.country,
            language: story.language,
            title: story.title,
            authors: story.authors,
            publishedDate: story.publishedDate,
            link: story.link,
            excerpt: story.excerpt,
            source: story.source,
            summary: story.summary,
            media: story.media,
            likes: 0,
            views: 0
        })
    }).then((response) => {
            window.location.href = "/story?storyID=" + story.id;
        }
    ).catch(err => console.error(err));
}
