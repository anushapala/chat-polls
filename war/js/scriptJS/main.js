var app = null;


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
	    domElement += '         <input class="input_default  option-holder" type="text" placeholder="Enter an option..">';
	    domElement += '	 		<span class="delete"></span>';
	    domElement += '		</div>';
	    domElement += '	  </div>';
	    
	    $('#poll-option-container').append(domElement);
	});
	
		
	//creating a new poll
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

$(document.body).on('click','#create-new-poll, #create_a_poll',function(){
	
	var source = "";
	if($(this).attr('id') == 'create_a_poll'){
		source = "showEmptyState";
	}else{
		source = "showPollsList";
	}
	PollOperations.showCreateNewPollView(source);
});

$(document.body).on('click','#back-button',function(){
	var moveTo = $(this).attr('data-moveTo');
	if('showEmptyState' == moveTo){
		showPollEmptyState();
	}else if('showPollsList' == moveTo){
		PollOperations.showPollsListView();
	}
});


$(document.body).on('click','#polls-list li>h5, #polls-list li>p',function(e){
	e.stopImmediatePropagation();
	
	if( $(this).parent('li').hasClass('open') ){//manual slide up
		$(this).parent('li').removeClass('open').find('div.contract').slideUp('3000');
	}else{
		$('#polls-list li.open').removeClass('open').find('div.contract').slideUp('3000');//auto slide up
		$(this).parent('li').addClass('open').find('div.contract').slideDown('3000');
	}
//	if( $(this).find('div.contract').css('display') == 'block' ){
//		$(this).css('opacity', '1');
//	}else{
//		$(this).css('opacity', '0.5');
//	}
});

$(document.body).on('click','#polls-list-container div.poll-option',function(e){
	e.stopImmediatePropagation();
	
	var pollOptionID = $(this).parent('div.poll-opt-div').attr('id').split('_')[0];
	var pollID = $(this).parents('li.question').attr('id').split('_')[0];
	var contactID = Poll.getAppUser().id;
	
	PollOperations.updatePoll(pollID,pollOptionID,contactID);
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

var showLoader = function(){
	$('#overlay, #loader').show();
}

var hideLoader = function(){
	$('#overlay, #loader').hide();
}

var showPollEmptyState = function(){
	$('#create-poll-container').hide();
	$('#polls-list-container').hide();
	$('#empty_poll_container').show();
}

var Poll = (function($,window,document,undefined){
	
	
	var _appuser, _context = {};
	
	var _init = function(){
		app = AAFClient.init();
		app.on('registered', function(data) {
			   console.error("On app.registered event.",data);
			   _appuser = data.user;
			 });
		
		app.on('activated',function(data){
			   console.error("On app activation.",data);
			  _context = data.context;
			  
			  $('#create-poll-container').hide();
			  $('#polls-list-container').hide();
			  $('#empty_poll_container').hide();
			  
			  var streamID = "";
				if(Poll.getContext().public != undefined && Poll.getContext().public ){
					streamID = Poll.getContext().id;
				}else{
					//poll for individual chat
					var idList = [Poll.getContext().id,Poll.getAppUser().id].sort();
					streamID = idList+"";
				}
				
			  PollOperations.fetchPollsForTheStream(streamID);	
			});

		app.on('context-change',function(data){
				console.error("On context change.",data);
				_context = data.context;
				
				$('#create-poll-container').hide();
				$('#polls-list-container').hide();
				$('#empty_poll_container').hide();
				
				var streamID = "";
				if(Poll.getContext().public != undefined && Poll.getContext().public ){
					streamID = Poll.getContext().id;
				}else{
					//poll for individual chat
					var idList = [Poll.getContext().id,Poll.getAppUser().id].sort();
					streamID = idList+"";
				}
				
				PollOperations.fetchPollsForTheStream(streamID);
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
		
		var streamID = "";
		if(Poll.getContext().public != undefined && Poll.getContext().public ){
			streamID = Poll.getContext().id;
		}else{
			//poll for individual chat
			var idList = [Poll.getContext().id,Poll.getAppUser().id].sort();
			streamID = idList+"";
		}
		
		var requestMap = {
				'pollQuestionDetails' : {
					'streamID' 		  : streamID,
//					'streamID' 		  :'3aab167e-fcb5-4b6a-a962-17c48551c204',
					'createdBy' 	  : Poll.getAppUser().id,
//					'createdBy' 	  : '5f3e80ff-e730-470f-a708-bb4639a55a6c',
					'createdUserName' 	  : Poll.getAppUser().firstName,
//					'createdUserName' : 'anusha',
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
					var pollObj = {
							'pollQuestionDetails' : response.pollQuestionDetails,
							'pollOptionsList' : response.pollOptionsList
					}
					
					pollArray.push(pollObj);
					PollOperations.diplayPollsList(pollArray,'prependNewPoll');
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
						PollOperations.diplayPollsList(response.PollsDetailsList,'listAllPoll');
					}else{
						showPollEmptyState();
					}
				}else{
					showPollEmptyState();
				}
				hideLoader();
			},
			error :function(errResp){
				console.error(errResp);
				hideLoader();
			}
		});
	};
	
	var diplayPollsList = function(pollsList, requestionFrom){
		
		for(var index in pollsList){
			
			var pollQuestionDetails = pollsList[index].pollQuestionDetails;
			var pollOptionsList = pollsList[index].pollOptionsList;
			
			var pollItemsList = "";
			var count = 0;
			for(var ind in pollOptionsList){
				count = count+1;
				var pollItem = '<div class="poll-opt-div" id="'+pollOptionsList[ind].pollOptionID+'_optionID">';
				pollItem += '		<label class="input_label">option '+count+':</label>';
				pollItem += '		<div class="poll-option">';
				pollItem += '			<img class="icon" src="'+pollOptionsList[ind].pollOptionImageURL+'">';
				
				//default selecting the poll option 
				var likedList = pollOptionsList[ind].optionLikedList;
				if( likedList.indexOf(Poll.getAppUser().id) > -1 ){
					pollItem += ' 			<input class="input_default  option-holder selected" type="text" readonly value="'+pollOptionsList[ind].pollOptionText+'"/>';	
				}else{
					pollItem += ' 			<input class="input_default  option-holder" type="text" readonly value="'+pollOptionsList[ind].pollOptionText+'"/>';	
				}
				pollItem += '		</div>';
				
				//showing the count of the votes
				var votesCount = 0;
				if(likedList.length != 0 ){
					if(likedList.indexOf(Poll.getAppUser().id) > -1 ){
						if(likedList.length == 1){
							votesCount = likedList.length+ " vote - You voted";
						}else{
							votesCount = likedList.length+ " votes - You voted"
						}
					}else{
						if(likedList.length == 1){
							votesCount = likedList.length + " vote";
						}else{
							votesCount = likedList.length + " votes";
						}
					}
				}else{
					votesCount = likedList.length + " votes";
				}
								
				pollItem += '		<code>'+votesCount+'</code>';
				pollItem += '	</div>';
				pollItemsList += pollItem;
			}
			
			var pollDomItem = '<li class="question" id="'+pollQuestionDetails.pollID+'_pollID">';
//			pollDomItem += '		<span class="notification"></span>';
			pollDomItem += '		<h5 class="name" id="'+pollQuestionDetails.createdBy+'_pollOnwer">'+pollQuestionDetails.createdUserName+'</h5>';
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
			
			if('prependNewPoll' == requestionFrom){
				$('#polls-list').prepend(pollDomItem);
				$('#polls-list').find('div.contract').slideUp('3000');

			}else{
				$('#polls-list').append(pollDomItem);
			}
	
		}
		
		PollOperations.showPollsListView();
	};	
	
	var updatePoll = function(pollID,pollOptionID,contactID){
		
		if(pollID == "" ||pollOptionID == "" || contactID == "" ){
			console.error('no required parameters');
			return;
		}
		showLoader();
		var requestMap = {
				'pollID' : pollID,
				'pollOptionID' : pollOptionID,
				'contactID' : contactID
		}
		
		var responseJSON = JSON.stringify(requestMap);
		
		$.ajax({
			type : 'POST',
			url : '/poll/updatePollOption',
			data : responseJSON,
			dataType : 'json',
			contentType:'application/json',
			success : function(response){
				if(response.success){
					hideLoader();
					
					var pollOptionsList = response.pollOptionsList;
					
					$('#'+pollID+'_pollID').find('input.option-holder').removeClass('selected');
					for(var index in pollOptionsList){
						var likedList = pollOptionsList[index].optionLikedList;
						var pollOptionID = pollOptionsList[index].pollOptionID +"_optionID";
						
						var count = 0;
						if(likedList.length != 0 ){
							if(likedList.indexOf(Poll.getAppUser().id) > -1 ){
								if(likedList.length == 1){
									count = likedList.length+ " vote - You voted";
								}else{
									count = likedList.length+ " votes - You voted"
								}
								$('#'+pollID+'_pollID').find('#'+pollOptionID).find('input.option-holder').addClass('selected');
							}else{
								if(likedList.length == 1){
									count = likedList.length + " vote";
								}else{
									count = likedList.length + " votes";
								}
							}
						}else{
							count = likedList.length + " votes";
						}
						
						$('#'+pollID+'_pollID').find('#'+pollOptionID).find('code').html(count);
					}	
				}
				
			},
			error : function(errResp){
				console.error(errResp);
			}
		});
	
	};
	
	var showCreateNewPollView = function(source){
		$('#back-button').attr('data-moveTo',source);
		$('#empty_poll_container').hide();
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
	