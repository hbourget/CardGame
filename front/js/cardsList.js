$(document).ready(function () {
  var table = $('#cardsList').DataTable({
    ajax: {
      url: "http://localhost:8888/inventories/availablecards",
      dataSrc: ""
    },
    columns: [
      { data: "id" },
      { data: "name" },
      { data: "description" },
      { data: "power" },
      { data: "health" },
      { data: "type" },
      { data: "energy" },
      { data: "price" },
      { data: "image" },
      {
        data: null,
        render: function (data, type, row) {
          return "<form class='buy-form' action='http://localhost:8888/inventories/buy/users/1/cards/" + row.id + "' method='post'>" +
            "<input type='submit' value='Acheter'>" +
            "</form>";
        }
      },
      {
        data: null,
        render: function (data, type, row) {
          return "<form class='sell-form' action='http://localhost:8888/inventories/sell/users/1/cards/" + row.id + "' method='post'>" +
            "<input type='submit' value='Vendre'>" +
            "</form>";
        }
      }
    ]
  });

  // Ajouter un gestionnaire d'événements pour les formulaires d'achat
  $("#cardsList").on("submit", ".buy-form", function (event) {
    event.preventDefault();
    var form = $(this);
    $.ajax({
      url: form.attr("action"),
      type: form.attr("method"),
      success: function (data) {
        alert("La carte a été achetée avec succès !");
      },
      error: function (xhr, status, error) {
        if (xhr.status == 409) {
          alert("Vous n'avez pas assez d'argent pour acheter cette carte.");
        } else {
          alert("Une erreur est survenue. Veuillez réessayer plus tard.");
        }
      }
    });
  });

  // Ajouter un gestionnaire d'événements pour les formulaires de vente
  $("#cardsList").on("submit", ".sell-form", function (event) {
    event.preventDefault();
    var form = $(this);
    $.ajax({
      url: form.attr("action"),
      type: form.attr("method"),
      success: function (data) {
        alert("La carte a été vendue avec succès !");
      },
      error: function (xhr, status, error) {
        if (xhr.status == 409) {
          alert("Vous ne possédez pas la carte ou la carte n'existe pas.");
        } else {
          alert("Une erreur est survenue. Veuillez réessayer plus tard.");
        }
      }
    });
  });
});
