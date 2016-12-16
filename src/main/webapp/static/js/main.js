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

function displayOpenMachine(counts) {
    $(".vending-machine > .closed").addClass("hidden");
    $(".vending-machine > .open").removeClass("hidden");
    var cans = $(".vending-machine .open .can-holder-spacer");
    for(var i = 1; i < cans.length; i++) {
        $(cans[i]).empty();
        for(var x = 0; x < counts[i-1].count; x++) {
            // they don't stack!
            console.log("Appending");
            $(cans[i]).append("<div class='can-top row'></div>");
        }
    }
}

function displayClosedMachine() {
    $(".vending-machine > .open").addClass("hidden");
    $(".vending-machine > .closed").removeClass("hidden");
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
    });
    $(".open-machine-key").click(function() {
        openMachine();
    });
    $("#closeMachine").click(function() {
        closeMachine();
    });
    $(".vending-machine .open .refill-can").each(function(i, sel) {
        var type = sel.getAttribute("data-type");
        $(sel).click(function() {
            refill(type);
        })
    });
});