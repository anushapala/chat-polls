var app = null;


$(document).ready(function(){

	$('#create-poll-container').hide();
	$('#polls-list-container').hide();
	
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
	    domElement += '         <input class="input_default option-holder" type="text" placeholder="Enter option here...">';
	    domElement += '	 		<span class="delete"></span>';
	    domElement += '		</div>';
	    domElement += '	  </div>';
	    
	    $('#poll-option-container').append(domElement);
	});
	
	
	$('#create-poll').on('click',function(e){
		var pollQuestion = $('#poll-question').find('textarea').val();
		var pollDescription = $('#poll-description').find('textarea').val();
			
		var empty = $('#poll-option-container').find('input.option-holder').filter(function() {
			return this.value === "";
		});
		
		if(empty.length) {
			$(".Polling").animate({marginLeft: "+=25px"},100);
			$(".Polling").animate({marginLeft: "-=50px"},100);
			$(".Polling").animate({marginLeft: "+=25px"},100);
		}else{
			var pollOptionsArrayList = [];
			
			$('#poll-option-container').find('div.poll-option').each(function(){
				var pollOptionMap = {};
				var pollImg = $(this).find('img').attr('src');
				var pollOption = $(this).find('input.option-holder').val();
				pollOptionMap = {
				'pollOptionImageURL' : pollImg,
				'pollOptionContent' : pollOption
				}
				pollOptionsArrayList.push(pollOptionMap);
				
			});	
			
			console.log(pollQuestion+"\n"+pollDescription+"\n"+pollOptionsArrayList);

			PollOperations.createPoll(pollQuestion,pollDescription,pollOptionsArrayList);
		}
			
	});
		
	  //PollOperations.fetchPollsForTheStream(Poll.getContext().id);	
	  PollOperations.fetchPollsForTheStream('3aab167e-fcb5-4b6a-a962-17c48551c204');	

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
	var optionCount = 1;
	$('#poll-option-container>div').each(function(){
		$(this).find('label.input_label').html('option ' + optionCount);
		optionCount++;
	});
});

$(document.body).on('click','#create-new-poll',function(){
	PollOperations.showCreateNewPollView();
});

$(document.body).on('click','#polls-list li',function(){
	$(this).find('div.contract').slideToggle('3000')
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

var showLoader = function(){
	$('#overlay, #loader').show();
}

var hideLoader = function(){
	$('#overlay, #loader').hide();
}

var Poll = (function($,window,document,undefined){
	
	
	var _appuser, _context = {};
	
	var _init = function(){
		app = AAFClient.init();
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
	
	var getAppUser = function(){
		return _appuser;
	};
	var getContext = function(){
		return _context;
	};
	
	_init();
	
	return{ 
		_init : _init,
		getAppUser : getAppUser,
		getContext : getContext
	}
	
})(jQuery,window,document);

var PollOperations = (function($,window,document,undefined){
	
	var createPoll = function(pollQuestion,pollDescription,pollOptionsList){
		
		if(pollQuestion == "" || pollOptionsList.length == 0 ){
			console.error('no proper parameters to create the poll');
			return;
			
		}
		showLoader();
		var requestMap = {
				'pollQuestionDetails' : {
					//'streamID' : Poll.getContext().id,
					'streamID' 		  :'3aab167e-fcb5-4b6a-a962-17c48551c204',
					'createdBy' 	  : '5f3e80ff-e730-470f-a708-bb4639a55a6c',
					'createdUserName' : 'anusha',
					'createdUserImg'  : 'http://lh3.googleusercontent.com/gklzH6FsFJ0t8pyBDoiwUKMW34SDixebyqfdVt7BVvetxL-jksSsgQN69R0bf5gS0YCu038ziYCK28rt2vxDBQ-s8JbBfuOPl8D67YQ',
 					'pollQuestion' 	  : pollQuestion,
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
			success : function(response){
				console.log(response);
				if(response.success){
					var pollArray = [];
					pollArray.push(response);
					PollOperations.diplayPollsList(pollArray);
					hideLoader();
				}else{
					console.log(response);
					hideLoader();
				}
				
			},
			error : function(errResp){
				console.error(errResp);
				hideLoader();
			}
		});
		
	};
	
	var fetchPollsForTheStream = function(streamID){
		$('#create-poll-container').hide();
		$('#polls-list').html("");
		$('#polls-list-container').show();
		if(streamID == ""){
			console.error("streamID is empty so cant proceed further");
			return;
		}
		showLoader();
		var requestMap ={
				'streamID' : streamID
		}
		var requestJSON = JSON.stringify(requestMap);
		$.ajax({
			type :'POST',
			url  :'/poll/fetchPoll',
			data : requestJSON,
			dataType : 'json',
			contentType:'application/json',
			success: function(response){
				console.log(response);
				if(response.success){
					if( response.PollsDetailsList != 'undefined' && response.PollsDetailsList != null ){
						PollOperations.diplayPollsList(response.PollsDetailsList);
					}else{
						$('#polls-list').html("<p>No Polls Yet!</p>");
					}
				}else{
					$('#polls-list').html("<p>No Polls Yet!</p>");
				}
				hideLoader();
			},
			error :function(errResp){
				console.error(errResp);
				hideLoader();
			}
		});
	};
	
	var diplayPollsList = function(pollsList){
		//show the loader goes here
		
		for(var index in pollsList){
			
			var pollQuestionDetails = pollsList[index].pollQuestionDetails;
			var pollOptionsList = pollsList[index].pollOptionsList;
			
			var pollItemsList = "";
			var count = 0;
			for(var ind in pollOptionsList){
				count = count+1;
				var pollItem = '<div id="'+pollOptionsList[ind].pollOptionID+'">';
				pollItem += '		<label class="input_label">option '+count+':</label>';
				pollItem += '		<div class="poll-option">';
				pollItem += '			<img class="icon" src="'+pollOptionsList[ind].pollOptionImageURL+'">';
				pollItem += ' 			<input class="input_default option-holder" type="text" readonly value="'+pollOptionsList[ind].pollOptionText+'"/>';
				pollItem += '		</div>';
				if(pollOptionsList[ind].optionLikedList != null ){
					pollItem += '		<code>'+pollOptionsList[ind].optionLikedList.length+' votes</code>';
				}else{
					pollItem += '		<code> 0 votes</code>';
				}
				pollItem += '	</div>';
				pollItemsList += pollItem;
			}
			
			var pollDomItem = '<li class="question" id="'+pollQuestionDetails.pollID+'">';
			pollDomItem += '		<span class="notification"></span>';
			pollDomItem += '		<h5 class="name" id="'+pollQuestionDetails.createdBy+'">' + pollQuestionDetails.createdUserName + '</h5>';
			pollDomItem += '		<p>'+pollQuestionDetails.pollQuestion+'</p>';
			pollDomItem += '		<div class="contract">';
			if(pollQuestionDetails.pollDescription != null && pollQuestionDetails.pollDescription != ""){
				pollDomItem += '			<cite>'+pollQuestionDetails.pollDescription+'</cite>';				
			}else{
				pollDomItem += '			<cite style="display:none;"></cite>';
			}
			pollDomItem += '			<div calss="option-container">';
			pollDomItem += 					pollItemsList;		
			pollDomItem += '			</div>';
			pollDomItem += '		</div>';
			pollDomItem += '	</li>';
			
			$('#polls-list').append(pollDomItem);
		}
		
		PollOperations.showPollsListView();
	};	
	
	var updatePoll = function(pollID,pollOptionID,contactID){
		
		if(pollID == "" ||pollOptionID == "" || contactID == "" ){
			console.error('no required parameters');
			return;
		}
		
		var requestMap = {
				'pollID' : pollID,
				'pollOptionID' : pollOptionID,
				'contactID' : contactID
		}
		
		var responseJSON = JSON.strinfigy(requestMap);
		
		
		$.ajax({
			type : 'POST',
			url : '/poll/updatePollOption',
			data : responseJSON,
			dataType : 'json',
			contentType:'application/json',
			success : function(response){
				if(resposne.success){
					
					//do the update front end action. 
					
				}
				
			},error : function(errResp){
				console.error(errResp);
			}
		});
	
	};
	
	var showCreateNewPollView = function(){
		$('#polls-list-container').hide();
		$('#create-poll-container').find('textarea').val("");
		$('#create-poll-container').find('input').val("");
		$('#create-poll-container').find('img').attr('src','')
		$('#create-poll-container').show();
	};
	
	var showPollsListView = function(){
		$('#create-poll-container').hide();
		$('#polls-list-container').show();
	}
	
	return{
		createPoll : createPoll,
		fetchPollsForTheStream : fetchPollsForTheStream,
		diplayPollsList : diplayPollsList,
		showCreateNewPollView : showCreateNewPollView,
		showPollsListView : showPollsListView,
		updatePoll : updatePoll,
	}
	
})(jQuery,window,document);
	