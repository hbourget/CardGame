$(document).ready(function () {
  $.ajax({
    url: "http://localhost:8888/rooms",
    type: "GET",
    dataType: "json",
    success: function (data) {
      // Pour chaque room, récupération des informations de la room
      for (var i = 0; i < data.length; i++) {
        // Création d'une ligne de tableau avec les informations de la room
        var html = "<tr>";
        html += "<td>" + data[i].id + "</td>";
        html += "<td>" + data[i].name + "</td>";
        html += "<td>" + data[i].idUser_1 + "</td>";
        html += "<td>" + data[i].idUser_2 + "</td>";
        html += "<td>" + data[i].idCardUser_1 + "</td>";
        html += "<td>" + data[i].idCardUser_2 + "</td>";
        html += "<td>" + data[i].cooldownUser_1 + "</td>";
        html += "<td>" + data[i].cooldownUser_2 + "</td>";
        html += "<td>" + data[i].status + "</td>";
        html += "<td>" + data[i].idUserWinner + "</td>";
        html += "<td>" + data[i].reward + "</td>";
        html += "</tr>";
        // Ajout de la ligne de tableau au tableau d'affichage
        $("#rooms tbody").append(html);
      }
    },
    error: function () {
      alert("Erreur lors de la récupération des informations de la room.");
    },
  });
});
