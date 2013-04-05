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

function componentToHex(c) {
    var hex = c.toString(16);
    return hex.length == 1 ? "0" + hex : hex;
}

function rgbToHex(r, g, b) {
    return "#" + componentToHex(r) + componentToHex(g) + componentToHex(b);
}

function setupHomeScreen() {
	var counter = 0;
	var canvasses = $(".tripCanvas");
	var stepSize = Math.floor(360 / canvasses.length + 1);
	for (var i=0; i<canvasses.length; i++) {
		var canvas = canvasses[i];
		var ctx = canvas.getContext("2d");
        var height = canvas.height;
        var width = canvas.width;
        var color = toRGB(counter, 1, 0.333);
        ctx.fillStyle = rgbToHex(color[0], color[1], color[2]);
        ctx.lineTo(0, 0);
        ctx.lineTo(width, 0);
        ctx.lineTo(width, height);
        ctx.lineTo(0, height);
        ctx.closePath();
		ctx.fill();
        for (var i2=0; i2<3; i2++) {
			var x = Math.floor(Math.random() * width);
			var y = Math.floor(Math.random() * height);
            ctx.beginPath();
			ctx.moveTo(width / 2,height / 2);
			color = toRGB(counter, 1 - (Math.random() * 0.3), 0.333 + ((Math.random() * 0.2) - 0.1));
			alert(rgbToHex(color[0], color[1], color[2]));
			ctx.strokeStyle = rgbToHex(color[0], color[1], color[2]);
            if(y < 150){
            	ctx.lineTo(x, 0);
                if(x < 150){
                  ctx.lineTo(0,0);
                  ctx.lineTo(0,y);
                } else {
                  ctx.lineTo(300,0);
                  ctx.lineTo(300,y);
                }
              } else {
				ctx.lineTo(x, 300);
                if(x < 150){
                  ctx.lineTo(0,300);
                  ctx.lineTo(0,y);
                } else {
                  ctx.lineTo(300,300);
                  ctx.lineTo(300,y);
                }
              }
            ctx.lineTo(150,150);
			ctx.closePath();
			ctx.fill();
        }
        counter = counter + stepSize;
	};
};
