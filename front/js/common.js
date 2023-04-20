$(document).ready(function () {
  $.ajax({
    url: "http://localhost:8888/users/1",
    type: "GET",
    dataType: "json",
    success: function (data) {
      var html = "";
      for (var i = 0; i < data.length; i++) {
        html += "<tr>";
        html += "<td>" + data[i].id + "</td>";
        html += "<td>" + data[i + 1].balance + "$" + "</td>";
        html += "</tr>";
      }
      $("#user-info").html(data.username);
      $("#money").html(data.balance);
    },
  });
});
