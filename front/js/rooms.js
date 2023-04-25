$(document).ready(function () {
  var table = $('#rooms').DataTable({
    ajax: {
      url: "http://localhost:8888/rooms",
      dataSrc: ""
    },
    columns: [
      { data: "id" },
      { data: "name" },
      { data: "status" },
      { data: "reward" },
      {
        data: null,
        render: function (data, type, row) {
          return "<form class='join-form' action='http://localhost:8888/rooms/join/" + row.id + "/users/1' method='put'>" +
            "<input type='submit' value='Rejoindre'>" +
            "</form>";
        }
      }
    ]
  });

  // Ajouter un gestionnaire d'événements pour les formulaires d'achat
  $("#rooms").on("submit", ".join-form", function (event) {
    event.preventDefault();
    var form = $(this);
    $.ajax({
      url: form.attr("action"),
      type: form.attr("method"),
      error: function (xhr, status, error) {
        if (xhr.status == 403) {
          alert("Vous ne pouvez pas rejoindre cette room.");
        } else {
          alert("Une erreur est survenue. Veuillez réessayer plus tard.");
        }
      }
    });
  });
});
