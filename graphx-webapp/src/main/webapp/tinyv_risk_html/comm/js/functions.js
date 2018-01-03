function show(obj,id) {    
    var objDiv = $("#"+id+"");
    $(objDiv).css("display","table");
    $(objDiv).css("left", obj.offset().left - objDiv.width());
    $(objDiv).css("top", obj.offset().top - objDiv.height());
};
function hide(obj,id) {
    var objDiv = $("#"+id+"");
    $(objDiv).css("display", "none");
};