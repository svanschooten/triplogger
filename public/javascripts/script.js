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

	setupTripCanvas();

});

function addBuddy() {
    jsRoutes.controllers.PostHandler.requestBuddy(selectedBuddy).ajax({
    	success : function(response) {
			$("#alertPlaceholder").replaceWith('<div id="alertPlaceholder" class="alert alert-success">' +
				'<a class="close" data-dismiss="alert" href="#">&times;</a>' +
				'<strong>Success!</strong> ' + response + ' </div>');
			$('<div class="span4 btn-group" id="pending-' + selectedBuddy + '">' +
									  	'<button class="btn btn-large btn-info" href="' +
									  	jsRoutes.controllers.PostHandler.profile(selectedBuddy) +
									  	'">' +
									     	'<i class="icon-comment icon-white"></i>' + selectedBuddy +
									    '</button>' +
                                        '<button class="btn btn-large btn-info" onclick="' + jsRoutes.controllers.PostHandler.cancelRequest(selectedBuddy)+ '" name="' + selectedBuddy  + '">' +
                                             '<i class="icon-remove icon-white"></i> Cancel' +
                                        '</button>' +
                                             '</div>').appendTo("#userPending");
			$(".alert").alert();
		},
		error : function(response) {
			$("#alertPlaceholder").replaceWith('<div id="alertPlaceholder" class="alert">' +
            				'<a class="close" data-dismiss="alert" href="#">&times;</a>' +
            				'<strong>Warning!</strong> ' + response.responseText + ' </div>');
			$(".alert").alert();
		}
    });
}

function acceptBuddy(acceptedBuddy) {
	jsRoutes.controllers.PostHandler.acceptBuddy(acceptedBuddy).ajax({
		success : function(response) {
			$("#alertPlaceholder").replaceWith('<div id="alertPlaceholder" class="alert alert-success">' +
				'<a class="close" data-dismiss="alert" href="#">&times;</a>' +
				'<strong>Success!</strong> ' + response + ' </div>');
			$("#request-" + acceptedBuddy).fadeOut(300, function(){$(this).remove();});
            $('<div class="span4">' +
				  '<a class="btn btn-large" href="' + jsRoutes.controllers.PostHandler.profile(acceptedBuddy) + '"><i class="icon-user"></i>' +
					  acceptedBuddy +
				  '</a>' +
			 '</div>').appendTo("#userBuddies").fadeIn();
			$(".alert").alert();
		},
		error : function(response) {
			$("#alertPlaceholder").replaceWith('<div id="alertPlaceholder" class="alert">' +
							'<a class="close" data-dismiss="alert" href="#">&times;</a>' +
							'<strong>Warning!</strong> ' + response.responseText + ' </div>');
			$(".alert").alert();
		}
	});

}

function cancelRequest(cancelBuddy) {
	jsRoutes.controllers.PostHandler.cancelRequest(cancelBuddy).ajax({
		success : function(response) {
			$("#alertPlaceholder").replaceWith('<div id="alertPlaceholder" class="alert alert-success">' +
				'<a class="close" data-dismiss="alert" href="#">&times;</a>' +
				'<strong>Success!</strong> ' + response + ' </div>');
			$("#pending-" + cancelBuddy).fadeOut(300, function(){$(this).remove();});
			$(".alert").alert();
		},
		error : function(response) {
			$("#alertPlaceholder").replaceWith('<div id="alertPlaceholder" class="alert">' +
							'<a class="close" data-dismiss="alert" href="#">&times;</a>' +
							'<strong>Warning!</strong> ' + response.responseText + ' </div>');
			$(".alert").alert();
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

function setupTripCanvas() {
	var counter = 0;
	var canvasses = $(".tripCanvas");
	var stepSize = 360 / (canvasses.length + 1);
	for (var i=0; i<canvasses.length; i++) {
		var canvas = canvasses[i];
		var label = canvas.getAttribute("name"); // name of drug
		var ctx = canvas.getContext("2d");
        var height = canvas.height;
        var width = canvas.width;
        var color = toRGB(counter, 1, 0.3);
		var fractals = 2;
		var randomFractals = 2;
		var randomColorsS = 0.4;
		var randomColorsI = 0.2;
		var compareY = height / 2; // 300
		var compareX = width / 2; // 300
        ctx.fillStyle = rgbToHex(color[0], color[1], color[2]);
        ctx.lineTo(0, 0);
        ctx.lineTo(width, 0);
        ctx.lineTo(width, height);
        ctx.lineTo(0, height);
        ctx.closePath();
		ctx.fill();
        for (var i2=0; i2<fractals; i2++) {
            ctx.beginPath();
			var randY = Math.floor(Math.random() * height * randomFractals) - (height * randomFractals * 0.5);
			var randX = Math.floor(Math.random() * width * randomFractals) - (width * randomFractals * 0.5);
			ctx.moveTo((compareX) + randX, (compareY) + randY);
			color = toRGB(counter, 1 - (Math.random() * randomColorsS), 0.3 + ((Math.random() * randomColorsI) - (randomColorsI * 0.5)));
			ctx.fillStyle = rgbToHex(color[0], color[1], color[2]);
			var x = Math.floor(Math.random() * width);
			var y = Math.floor(Math.random() * height);
            if(y < compareY){
            	ctx.lineTo(x, 0);
                if(x < compareX){
                  ctx.lineTo(0,0);
                  ctx.lineTo(0,y);
                } else {
                  ctx.lineTo(width,0);
                  ctx.lineTo(width,y);
                }
              } else {
				ctx.lineTo(x, height);
                if(x < compareX){
                  ctx.lineTo(0, height);
                  ctx.lineTo(0,y);
                } else {
                  ctx.lineTo(width, height);
                  ctx.lineTo(width,y);
                }
              }
            ctx.lineTo(compareY,compareX);
			ctx.closePath();
			ctx.fill();
		}
		ctx.fillStyle = "#A9A9A9";
		var fontSize = 24;
		ctx.font = fontSize + "pt Helvetica";
		var maxWidth = width - 40;
		var maxHeight = height - 40;
		var lineHeight = fontSize * 1.5;
		var textX = 20;
		var textY = 40;
		wrapText(ctx, label, textX, textY, maxWidth, maxHeight, lineHeight)
        counter = counter + stepSize;
        if (counter >= 360) {
        	counter = counter - 360;
        }
	};
};

function wrapText(context, text, x, y, maxWidth, maxHeight, lineHeight) {
	var words = text.split(' ');
	var line = '';
	for(var n = 0; n < words.length; n++) {
		if(y > maxHeight) {
			context.fillText("....", x, y);
			return;
		}
		var word = words[n];
		while(context.measureText(word + "..").width > maxWidth) {
			word = word.substr(0, word.length - 3) + "..";
		}
		var testLine = line + word + ' ';
		var metrics = context.measureText(testLine);
		var testWidth = metrics.width;
		if(testWidth > maxWidth) {
			context.fillText(line, x, y);
			line = word + ' ';
			y += lineHeight;
		} else {
			line = testLine;
		}
	}
	context.fillText(line, x, y);
}
