$(document).ready(function () {
  $.ajax({
    url: "http://localhost:8888/users",
    type: "GET",
    dataType: "json",
    success: function (data) {
      // Pour chaque utilisateur, récupération des informations
      for (var i = 0; i < data.length; i++) {
        $.ajax({
          url: "http://localhost:8888/users/" + data[i].id,
          type: "GET",
          dataType: "json",
          success: function (userData) {
            // Création d'une ligne de tableau avec les informations de l'utilisateur
            var html = "<tr>";
            html += "<td>" + userData.id + "</td>";
            html += "<td>" + userData.username + "</td>";
            html += "<td>" + userData.balance + "</td>";
            html += "<td>" + userData.idInventory + "</td>";
            html += "</tr>";
            // Ajout de la ligne de tableau au tableau d'affichage
            $("#allUsers tbody").append(html);
          },
          error: function () {
            alert(
              "Erreur lors de la récupération des informations de l'utilisateur."
            );
          },
        });
      }
    },
    error: function () {
      alert("Erreur lors de la récupération de la liste des utilisateurs.");
    },
  });
});
