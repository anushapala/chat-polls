/**
 * 
 */
$(document).ready(function(){
	$(".picture-upload").on('click', function () {
        var id = $(this).attr('id');
        var number = id.substring(3);
        $("#imgupload" + number)[0].click();
    });
	$("input:file").on('change', function () {
        var id = $(this).attr('id');
        var number = id.substring(9);
        showImage(number, this);
    });
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