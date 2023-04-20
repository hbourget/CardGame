$(document).ready(function () {
  $.ajax({
    url: "http://localhost:8888/cards",
    type: "GET",
    dataType: "json",
    success: function (data) {
      var html = "";
      for (var i = 0; i < data.length; i++) {
        html += "<tr>";
        html += "<td>" + data[i].id + "</td>";
        html += "<td>" + data[i].name + "</td>";
        html += "<td>" + data[i].description + "</td>";
        html += "<td>" + data[i].power + "</td>";
        html += "<td>" + data[i].health + "</td>";
        html += "<td>" + data[i].type + "</td>";
        html += "<td>" + data[i].energy + "</td>";
        html += "<td>" + data[i].price + "</td>";
        html += "<td>" + data[i].image + "</td>";
        html += "<td>";
        html += "<form action='http://localhost:8888/inventories/buy/users/1/cards/" + data[i].id + "' method='post'>";
        html += "<input type='submit' value='Acheter'>";
        html += "</form>";
        html += "</td>";
        
        html += "<td>";
        html += "<form action='http://localhost:8888/inventories/sell/users/1/cards/" + data[i].id + "' method='post'>";
        html += "<input type='submit' value='Vendre'>";
        html += "</form>";
        html += "</td>";
        html += "</tr>";
      }
      $("#cardsList tbody").html(html);
    },
    error: function () {
      alert("error");
    },
  });
});
