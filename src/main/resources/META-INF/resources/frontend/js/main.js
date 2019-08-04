/*$(document).ready(function () {
 console.log("jquery document ready function");
 });*/

function scrollTop() {
    $("html, body").animate({scrollTop: 0}, "slow");
}

function removeVaadinLicense() {
    console.log("remove vaadin license");
    var fun = function () {
        var license = $('vaadin-license-box');
        var iron = $('iron-a11y-announcer');
        console.log(license);
        console.log(iron);
        if (license !== undefined)
            license.remove();
        //if (iron !== undefined)
        // iron.remove();
    }
    fun();
    setTimeout(fun, 1500);
}

function viewReceipt(src) {
    console.log(src);
    window.location.href = src;
}

function imageView(src) {
    $('.img-view').attr('src', src).attr('alt', src);
}

function galleryClick(image) {
    image.click(function () {
        var scr = $(this).attr('src');
        var alt = $(this).attr('alt');
        $('.img-view').attr('src', scr).attr('alt', alt);
    });
}

function removeImage(name) {
    var alt = $('.img-view').attr('alt');
    var files = $('.jarvis-upload').prop('files');
    var div = $('#gallery').find("div[id='" + name + "']");
    console.log("=======>>>> remove image");
    console.log(alt);
    console.log(files);
    console.log(div);
    if (div !== undefined) {
        div.remove();
        //enableUpload(count)
        for (var i = 0; i < files.length; i++) {
            if (files[i].name === name) {
                files.splice(i, 1);
                break;
            }
        }
        if (alt === name) {
            var img = $('#gallery img');
            $('.img-view').attr('src', img.attr('src')).attr('alt', img.attr('alt'));
        }
    }
    if ($('#gallery div') === undefined || $('#gallery div').length === 0) {
        $('.img-view').attr('src', '').attr('alt', '');
        console.log('no image in gallery anymore');
    }
    console.log(name);
    console.log($('.jarvis-upload').prop('maxFilesReached'));
}

function enableUpload(count) {
    var btnUpload = $('#btnUpload');
    var length = $('#gallery div').length;
    console.log("enable upload image");
    console.log("count " + count);
    if (btnUpload !== undefined && length < count) {
        btnUpload.prop("disabled", false);
        btnUpload.removeAttr('disabled').removeAttr('aria-disabled');
    }
}
