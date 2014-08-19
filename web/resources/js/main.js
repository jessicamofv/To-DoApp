/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function allowDrop(ev)
{
    ev.preventDefault();
}

function drag(ev)
{
    var draggedElement = ev.target;
    var draggedClass = ev.target.getAttribute("class");

    while (draggedClass !== "listItem")
    {
        draggedElement = draggedElement.parentNode;
        draggedClass = draggedElement.getAttribute("class");
    }

    ev.dataTransfer.setData("Text", draggedElement.getAttribute("id"));
}

function drop(ev)
{
    ev.preventDefault();
    var draggedItemId = ev.dataTransfer.getData("Text");
    var draggedItem = document.getElementById(draggedItemId);
    var droppedOnElement = ev.target;
    var droppedOnClass = ev.target.getAttribute("class");

    while (droppedOnClass !== "listItem")
    {
        droppedOnElement = droppedOnElement.parentNode;
        droppedOnClass = droppedOnElement.getAttribute("class");
    }

    var droppedOnItemId = droppedOnElement.getAttribute("id");
                
    var draggedItemIdString = "#" + draggedItemId;
    var droppedOnItemIdString = "#" + droppedOnItemId;
    var inputTagString = " input:eq(1)";
    var draggedHiddenSelector = draggedItemIdString + inputTagString;
    var droppedOnHiddenSelector = droppedOnItemIdString + inputTagString;
    /*var draggedHiddenId = "hidden" + draggedItemId.substring(4);
    var draggedHidden = document.getElementById(draggedHiddenId);
    var draggedHiddenIdString = "#" + draggedHiddenId;
    var droppedOnHiddenId = "hidden" + droppedOnItemId.substring(4);
    var droppedOnHidden = document.getElementById(droppedOnHiddenId);
    var droppedOnHiddenIdString = "#" + droppedOnHiddenId;*/
    var aTagString = " a";
    var draggedItemTextSelector = draggedItemIdString + aTagString;
    var droppedOnItemTextSelector = droppedOnItemIdString + aTagString;
    var draggedItemText = $(draggedItemTextSelector).text();
    var draggedItemAndNum = draggedItemText.trim() + 
            "./." + droppedOnItemId.substring(4);
    var droppedOnItemText = $(droppedOnItemTextSelector).text();
    var droppedOnItemAndNum = droppedOnItemText.trim() + 
            "./." + draggedItemId.substring(4);;
                
    /*$(draggedHiddenIdString).val(droppedOnItemAndNum);
    $(droppedOnHiddenIdString).val(draggedItemAndNum);
    draggedHidden.setAttribute("id", droppedOnHiddenId);
    droppedOnHidden.setAttribute("id", draggedHiddenId);*/
    $(draggedHiddenSelector).val(draggedItemAndNum);
    $(droppedOnHiddenSelector).val(droppedOnItemAndNum);
    
    draggedItem.setAttribute("id", droppedOnItemId);
    droppedOnElement.setAttribute("id", draggedItemId);
                
    var draggedNumId = "num" + draggedItemId.substring(4);
    var draggedNum = document.getElementById(draggedNumId);
    var droppedOnNumId = "num" + droppedOnItemId.substring(4);
    var droppedOnNum = document.getElementById(droppedOnNumId);

    draggedNum.removeChild(draggedItem);
    droppedOnNum.removeChild(droppedOnElement);
    draggedNum.appendChild(droppedOnElement);
    droppedOnNum.appendChild(draggedItem);
   
    $("#itemsListForm").submit();
}

$(function(){
    $("#home").on("mouseenter", ".listItem", function() {
        // get the item link as opposed to the remove icon link
        var listItem = $(this).children("a.easy, a.medium, a.hard");
        var listItemText = listItem.text().trim();
        var timeIntervalString = $(this).children("input:eq(0)");
        timeIntervalString.val(listItemText.toLowerCase());
        $("#itemsListForm\\:removeHelper").click();
        var timeInterval = timeIntervalString.val().parseInt();
        if (timeInterval <= 15)
        {
            var removeIcon = $(this).find(".remove");
            removeIcon.addClass("glyphicon glyphicon-remove");
        }
    });
    $("#home").on("mouseleave", ".listItem", function() {
        var removeIcon = $(this).find(".remove");
        removeIcon.removeClass("glyphicon glyphicon-remove");
    });
});