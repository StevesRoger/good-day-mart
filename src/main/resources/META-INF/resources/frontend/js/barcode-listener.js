/*var code = "";
document.addEventListener('keypress', function (e) {
    //usually scanners throw an 'Enter' key at the end of read
    if (e.keyCode === 13) {
        var tag = document.getElementById('barcode');
        if (tag != undefined && code.length > 0) {
            tag.setAttribute('value', code);
            console.log(tag);
            console.log(code);
            code = "";
        }
    } else {
        code += e.key;//while this is not an 'enter' it stores the every key
    }
})*/

$(document).scannerDetection({
    timeBeforeScanTest: 200, // wait for the next character for upto 200ms
    startChar: [120], // Prefix character for the cabled scanner (OPL6845R)
    endChar: [13], // be sure the scan is complete if key 13 (enter) is detected
    avgTimeByChar: 40, // it's not a barcode if a character takes longer than 40ms
    onComplete: function (barcode, qty) {
        $('#barcode').attr('value', barcode);
        //console.log(qty);
        //console.log(barcode);
        alert(barcode);
    }, // main callback function
});