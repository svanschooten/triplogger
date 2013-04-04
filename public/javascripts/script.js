/* Author: 

*/
var selectedBuddy;


$(document).ready(function() {
	$(".alert").alert();

	$("#searchBuddy").autocomplete({
		minLength: 2,
		delay: 500,
		source: $("#searchBuddy").data("url"),
		select: function(event, ui) {
			selectedBuddy = ui.item.value;
		}
	});

	setupHomeScreen();

});

function addBuddy() {
    jsRoutes.controllers.PostHandler.requestBuddy(selectedBuddy).ajax({
    	success : function(response) {
			$("#alertPlaceholder").replaceWith('<div id="alertPlaceholder" class="alert alert-success">' +
				'<a class="close" data-dismiss="alert" href="#">&times;</a>' +
				'<strong>Success!</strong> ' + response.responseText + ' </div>');
            $("#userPending").append('<div class="span4 btn-group" id="pending-' + selectedBuddy + '">' +
									  	'<button class="btn btn-large btn-info" href="' +
									  	jsRoutes.controllers.PostHandler.profile(selectedBuddy) +
									  	'">' +
									     	'<i class="icon-comment icon-white"></i>' + selectedBuddy +
									    '</button>' +
                                        '<button class="btn btn-large btn-info" onclick="' + jsRoutes.controllers.PostHandler.cancelRequest(selectedBuddy)+ '" name="' + selectedBuddy  + '">' +
                                             '<i class="icon-remove icon-white"></i> Cancel' +
                                        '</button>' +
                                             '</div>');
		},
		error : function(response) {
			$("#alertPlaceholder").replaceWith('<div id="alertPlaceholder" class="alert">' +
            				'<a class="close" data-dismiss="alert" href="#">&times;</a>' +
            				'<strong>Warning!</strong> ' + response.responseText + ' </div>');
		}
    });
}

function acceptBuddy(acceptedBuddy) {
	jsRoutes.controllers.PostHandler.acceptBuddy(acceptedBuddy).ajax({
		success : function(response) {
			$("#alertPlaceholder").replaceWith('<div id="alertPlaceholder" class="alert alert-success">' +
				'<a class="close" data-dismiss="alert" href="#">&times;</a>' +
				'<strong>Success!</strong> ' + response.responseText + ' </div>');
			$("#request-" + acceptedBuddy).fadeOut('fast', $("#request-" + acceptedBuddy).remove());
            $("#userBuddies").append('<div class="span4">' +
										  '<a class="btn btn-large" href="' + jsRoutes.controllers.PostHandler.profile(acceptedBuddy) + '"><i class="icon-user"></i>' +
											  acceptedBuddy +
										  '</a>' +
                                     '</div>');
		},
		error : function(response) {
			$("#alertPlaceholder").replaceWith('<div id="alertPlaceholder" class="alert">' +
							'<a class="close" data-dismiss="alert" href="#">&times;</a>' +
							'<strong>Warning!</strong> ' + response.responseText + ' </div>');
		}
	});

}

function cancelRequest(cancelBuddy) {
	jsRoutes.controllers.PostHandler.cancelRequest(cancelBuddy).ajax({
		success : function(response) {
			$("#alertPlaceholder").replaceWith('<div id="alertPlaceholder" class="alert alert-success">' +
				'<a class="close" data-dismiss="alert" href="#">&times;</a>' +
				'<strong>Success!</strong> ' + response.responseText + ' </div>');
			$("#pending-" + cancelBuddy).fadeOut('fast', $("#pending-" + cancelBuddy).remove());
		},
		error : function(response) {
			$("#alertPlaceholder").replaceWith('<div id="alertPlaceholder" class="alert">' +
							'<a class="close" data-dismiss="alert" href="#">&times;</a>' +
							'<strong>Warning!</strong> ' + response.responseText + ' </div>');
		}
	});
}

function setupHomeScreen() {
	var counter = 0;
	var canvasses = $(".tripCanvas");
	var stepSize = canvasses.length;
	alert(stepSize);
}





















