var app = AAFClient.init();


$(document).ready(function(){
	
	
	
	
	
	
	
	
	
});


//default events

//To show the count on app icon at chat header.
app.postMessage('showCount',{  
'count': 10,
'id'   : context-id // userId / streamId
}); 

// To trigger desktop notification w.r.t user/stream
app.postMessage('showNotification',{  
    'id'     : context-id
    'icon'   : null,          // App icon url to be shown in desktop notification.
    'title'  : title          // Title for the desktop notification. Ex :"Todo app remainder",
    'message': message        // body content for desktop notification. Ex : "Enabled desktop notification for todo app."
  });

// To show the app update indicator on recent contact/stream view context.
app.postMessage('showIndicator',{
    'id' : context-id // Context reference to show update indicator.
});


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
	
	var createPoll = function(pollQuestion,pollDescription,pollOptionList){
		
		var requestMap = {
				'pollQuestion' = {
					'streamId' : Poll().getContext().id,
					'pollQuestion' : pollQuestion,
					'pollDescription' : pollDescription
				},
				'pollOptions' = pollOptionList
		}
		
		var requestJSON = JSON.stringify(requestMap);
		
		$.ajax({
			type :'POST',
			url  :'/poll/createAPoll',
			data : requestJSON,
			success : function(responseJSON){
				var data = JSON.parse(responseJSON);
				
			},failure : function(){
				
			};
		});
		
	};
	
	var fetchPoll = function(){
		
	};
	
	var updatePoll
	
	return{
		createPoll : createPoll,
		fetchPoll : fetchPoll,
	}
	
})(jQuery,window,document);
	