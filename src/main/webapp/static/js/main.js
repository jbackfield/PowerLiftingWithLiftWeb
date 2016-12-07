function setVended(drink) {
    var v = $("#vended");
    v.removeClass("crud-colors");
    v.removeClass("diet-crud-colors");
    v.removeClass("spite-colors");
    v.removeClass("hidden");
    v.text(drink.name);
    v.addClass(drink.name.replace(" ", "-").toLowerCase() + "-colors");
}

function setMessage(message) {
    var d = $(".vending-machine .closed .machine-operations .message-panel");
    d.text(message);
    setTimeout(function() {
        if(d.text() == message) { d.text(""); }
    }, 2000);
}

$(function() {
    $(".operation-selection").each(function(i, sel) {
        var id = parseInt(sel.getAttribute("data-index"), 10);
        $(sel).click(function() {
            purchase(id);
        });
    });
    $("#vended").click(function() {
        grabVended();
    })
});