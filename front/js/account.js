$(document).ready(function () {
  // Récupération de l'inventaire de l'utilisateur
  $.ajax({
    url: "http://localhost:8888/inventories/users/1",
    type: "GET",
    dataType: "json",
    success: function (data) {
      // Pour chaque carte de l'inventaire, récupération des informations de la carte
      for (var i = 0; i < data.inventory.cards.length; i++) {
        $.ajax({
          url: "http://localhost:8888/cards/27",
          type: "GET",
          dataType: "json",
          success: function (cardData) {
            // Création d'une ligne de tableau avec les informations de la carte
            var html = "<tr>";
            html += "<td>" + cardData.id + "</td>";
            html += "<td>" + cardData.name + "</td>";
            html += "<td>" + cardData.description + "</td>";
            html += "<td>" + cardData.power + "</td>";
            html += "<td>" + cardData.health + "</td>";
            html += "<td>" + cardData.type + "</td>";
            html += "<td>" + cardData.energy + "</td>";
            html += "<td>" + cardData.price + "</td>";
            html += "<td>" + cardData.image + "</td>";
            html += "<td> <button>Acheter</button> </td>";
            html += "<td> <button>Vendre</button> </td>";
            html += "</tr>";
            // Ajout de la ligne de tableau au tableau d'affichage
            $("#account tbody").append(html);
          },
        });
      }
    },
  });
});
