var app = AAFClient.init();


$(document).ready(function(){

	
	//adding extra option on creating the poll
	$('#add-option').on('click',function(e){
		var optionsCount = $('#poll-option-container').find('label').length;
		optionsCount =  optionsCount+1;
		var label = "option "+optionsCount;
		
	    var domElement = '<div>';
	    domElement += '		<label class="input_label">'+label+':</label>';
	    domElement += '		<div class="poll-option">';
	    domElement += '			<img class="icon picture-upload" id= img'+optionsCount+'>';
	    domElement += '			<input type="file" style="visibility: hidden;display:none" id=imgupload'+optionsCount+'>';
	    domElement += '         <input class="input_default input_small option-holder" type="text" placeholder="Enter option here...">';
	    domElement += '	 		<span class="delete"></span>';
	    domElement += '		</div>';
	    domElement += '	  </div>';		
	    
	    $('#poll-option-container').append(domElement);
	});
	
	
	$('#create-poll').on('click',function(e){
		var pollQuestion = $('#poll-question').find('textarea').val();
		var pollDescription = $('#poll-description').find('textarea').val();
		
		var pollOptionsArrayList = [];
		
		$('#poll-option-container').find('div.poll-option').each(function(){
			var pollOptionMap = {};
			var pollImg = $(this).find('img').attr('src');
			var pollOption =  $('#poll-option-container').find('div.poll-option:eq(0)').find('input.option-holder').val();
			pollOptionMap = {
					'pollOptionImageURL' : pollImg,
					'pollOptionContent' : pollOption
			}
			pollOptionsArrayList.push(pollOptionMap);
			
		});
		
		console.log(pollQuestion+"\n"+pollDescription+"\n"+pollOptionsArrayList);
		
		PollOperations.createPoll(pollQuestion,pollDescription,pollOptionsArrayList);
		
	});
	
});


$(document.body).on('click','.picture-upload', function () {
    var id = $(this).attr('id');
    var number = id.substring(3);
    $("#imgupload" + number)[0].click();
});

$(document.body).on('change','input:file', function () {
    var id = $(this).attr('id');
    var number = id.substring(9);
    showImage(number, this);
});

$(document.body).on('click','.delete',function(){
	$(this).parent('div.poll-option').parent('div').remove();
});

function showImage(id, fileInput) {
    var file = fileInput.files[0];
    var imageType = /image.*/;
    if (file.type.match(imageType)) {
        var reader = new FileReader();
        reader.onload = function (e) {
            $("#img" + id).attr("src", reader.result);
        }
        reader.onloadend = function (e) {
        }
        reader.onerror = function(e)
        {
        }
        reader.readAsDataURL(file);
    } else {
        console.log('File not supported' + imageType);
    }
}

////default events
//
////To show the count on app icon at chat header.
//app.postMessage('showCount',{  
//'count': 10,
//'id'   : context-id // userId / streamId
//}); 
//
//// To trigger desktop notification w.r.t user/stream
//app.postMessage('showNotification',{  
//    'id'     : context-id
//    'icon'   : null,          // App icon url to be shown in desktop notification.
//    'title'  : title          // Title for the desktop notification. Ex :"Todo app remainder",
//    'message': message        // body content for desktop notification. Ex : "Enabled desktop notification for todo app."
//  });
//
//// To show the app update indicator on recent contact/stream view context.
//app.postMessage('showIndicator',{
//    'id' : context-id // Context reference to show update indicator.
//});


var Poll = (function($,window,document,undefined){
	var _appuser, _context = {};
	
	var getAppUser = function(){
		return _appuser;
	};
	var getContext = function(){
		return _context;
	};
	var _init = function(){
		
		app.on('registered', function(data) {
			   console.error("On app.registered event.",data);
			   _appUser = data.user;
			 });
		
		app.on('activated',function(data){
			   console.error("On app activation.",data);
			  _context = data.context;
			});

		app.on('context-change',function(data){
				console.error("On context change.",data);
				_context = data.context;
			});

		app.on('deactivated',function(data){
				console.error("On app deactivation.",data);
			});
			
	};
		
	return{ 
		_init : _init,
		getAppUser : getAppUser,
		getContext : getContext
	}
	
})(jQuery,window,document);

var PollOperations = (function($,window,document,undefined){
	
	var createPoll = function(pollQuestion,pollDescription,pollOptionsList){
		
		var requestMap = {
				'pollQuestionDetails' : {
					//'streamID' : Poll().getContext().id,
					'streamID' :'3aab167e-fcb5-4b6a-a962-17c48551c204',
					'pollQuestion' : pollQuestion,
					'pollDescription' : pollDescription
				},
				'pollOptionsDetails' : pollOptionsList
		}
		
		var requestJSON = JSON.stringify(requestMap);
		
		$.ajax({
			type :'POST',
			url  :'/poll/createPoll',
			data : requestJSON,
			dataType : 'json',
			contentType:'application/json',
			success : function(responseJSON){
				console.log(responseJSON);
				var data = JSON.parse(responseJSON);
				console.log(data);
				
			},failure : function(){
				
			}
		});
		
	};
	
	var fillPollInfo = function(){
		
	};
	var fetchPoll = function(){
		
	};
	
	var updatePoll
	
	return{
		createPoll : createPoll,
		fetchPoll : fetchPoll,
		fillPollInfo : fillPollInfo,
	}
	
})(jQuery,window,document);
	